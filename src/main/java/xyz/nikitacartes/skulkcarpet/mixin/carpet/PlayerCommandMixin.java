package xyz.nikitacartes.skulkcarpet.mixin.carpet;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Options;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nikitacartes.skulkcarpet.BotCapStorage;
import xyz.nikitacartes.skulkcarpet.SculkCarpetSettings;
import xyz.nikitacartes.skulkcarpet.integration.BlockBotIntegration;

import static net.minecraft.commands.Commands.argument;

//Checks if player can spawn/manipulate carpet bots with bot cap and private bot support.
@Mixin(value = PlayerCommand.class, remap = false)
public class PlayerCommandMixin {

    @ModifyExpressionValue(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
    private static ArgumentBuilder skulkcarpet$addPrivateArgumentIn(ArgumentBuilder builder) {
        if (!(builder instanceof LiteralArgumentBuilder) || !((LiteralArgumentBuilder) builder).getLiteral().equals("spawn")) return builder;
        if (builder.getArguments().size() < 2) return builder;

        for (Object arg : builder.getArguments()) {
            LiteralCommandNode node = (LiteralCommandNode) arg;

            if (node.getLiteral().equals("at")) {
                builder.then(node.createBuilder().requires(Permissions.require("carpet.player.at", 2)).build());

                ArgumentCommandNode nodeArg = (ArgumentCommandNode) node.getChild("position");
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());

                nodeArg = (ArgumentCommandNode) nodeArg.getChild("facing").getChild("direction");
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());

                nodeArg = (ArgumentCommandNode) nodeArg.getChild("in").getChild("dimension");
                nodeArg.addChild(nodeArg.getChild("in").createBuilder().requires(Permissions.require("carpet.player.in", 2)).build());
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());

                LiteralCommandNode litNode = (LiteralCommandNode) nodeArg.getChild("in");
                nodeArg.addChild(litNode.createBuilder().requires(Permissions.require("carpet.player.in", 2)).build());

                nodeArg = (ArgumentCommandNode) litNode.getChild("gamemode");
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());
                continue;
            }

            if (node.getLiteral().equals("in")) {
                builder.then(node.createBuilder().requires(Permissions.require("carpet.player.in", 2)).build());
                node.addChild(node.getChild("gamemode").createBuilder()
                        .then(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn)).build());
            }
        }

        builder.then(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn));
        return builder;
    }

    @Inject(method = "cantManipulate", at = @At(value = "HEAD"), cancellable = true)
    private static void skulkcarpet$checkPrivateBotManipulation(CommandContext<CommandSourceStack> context, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer sender = context.getSource().getPlayer();
        if (sender == null) return;

        String playerName = sender.nameAndId().name();
        String botName = StringArgumentType.getString(context, "player");

        if (!BotCapStorage.canManipulate(playerName, botName)) {
            if (!Permissions.check(context.getSource(), "carpet.ignorePrivateBot", 2)) {
                Messenger.m(context.getSource(), "r Only the summoner can manipulate private bots");
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "cantSpawn", at = @At("TAIL"), cancellable = true)
    private static void skulkcarpet$botCapCheck(CommandContext<CommandSourceStack> context, CallbackInfoReturnable<Boolean> cir, @Local(name = "server") MinecraftServer server, @Local(name = "profile") NameAndId profile) {
        CommandSourceStack source = context.getSource();

        if (!source.isPlayer()) {
            return;
        }

        String summonerName = source.getPlayer().nameAndId().name();

        if (!Permissions.check(source, "carpet.ignoreGlobalBotCap", 2) && BotCapStorage.isCapReached()) {
            Messenger.m(source, "r You can't spawn more than ", "rb " + SculkCarpetSettings.maxPlayerBotGlobalCap + " ", "r players globally");
            cir.setReturnValue(true);
            return;
        }
        if (!Permissions.check(source, "carpet.unlimitedBots", 2) && BotCapStorage.isCapReachedFor(summonerName, Options.get(source, "carpet.maxPlayerBotCap", SculkCarpetSettings.maxPlayerBotCap, Integer::parseInt))) {
            Messenger.m(source, "r You can't spawn more than ", "rb " + SculkCarpetSettings.maxPlayerBotCap + " ", "r players");
            cir.setReturnValue(true);
        }

        // BlockBot integration: prevent spawning bots with names of whitelisted players
        if (BlockBotIntegration.isPlayerWhitelisted(profile, server) && !Permissions.check(source, "carpet.bypassBlockBot", 2)) {
            Messenger.m(source, "r BlockBot whitelisted players can only be spawned by operators or permission holders");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "spawn", at = @At("TAIL"))
    private static void skulkcarpet$spawnIncrement(CommandContext<CommandSourceStack> context, CallbackInfoReturnable<Integer> cir, @Local(name = "source") CommandSourceStack source, @Local(name = "playerName") String playerName, @Local(name = "success") boolean success) {
        if (success) {
            String summonerName = source.isPlayer() ? source.getPlayer().nameAndId().name() : null;
            boolean privateBot = SculkCarpetSettings.fakePlayerDefaultPrivate;
            try {
                privateBot = BoolArgumentType.getBool(context, "private");
            } catch (IllegalArgumentException ignored) {}
            BotCapStorage.increment(summonerName, playerName, privateBot);
        }
    }
}
