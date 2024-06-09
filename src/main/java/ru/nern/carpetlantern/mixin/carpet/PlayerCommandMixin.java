package ru.nern.carpetlantern.mixin.carpet;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.lucko.fabric.api.permissions.v0.Options;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.nern.carpetlantern.BotCapStorage;
import ru.nern.carpetlantern.CarpetLanternSettings;
import ru.nern.carpetlantern.IPlayerAccessor;
import ru.nern.carpetlantern.integration.BlockBotIntegration;

import static net.minecraft.server.command.CommandManager.argument;

//Checks if player can spawn anymore carpet bots.
@Mixin(value = PlayerCommand.class, remap = false)
public class PlayerCommandMixin {

    @ModifyExpressionValue(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
    private static ArgumentBuilder carpetlantern$addPrivateArgumentIn(ArgumentBuilder builder) {
        if (!(builder instanceof LiteralArgumentBuilder) || !((LiteralArgumentBuilder) builder).getLiteral().equals("spawn")) return builder;
        if (builder.getArguments().size() < 2) return builder;

        for (Object arg : builder.getArguments()) {
            LiteralCommandNode node = (LiteralCommandNode) arg;

            if (node.getLiteral().equals("at")) {;
                builder.then(node.createBuilder().requires(Permissions.require("carpet.player.at", 2)).build());

                ArgumentCommandNode nodeArg  = (ArgumentCommandNode) node.getChild("position");
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());

                nodeArg = (ArgumentCommandNode) nodeArg.getChild("facing").getChild("direction");
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());

                nodeArg = (ArgumentCommandNode) nodeArg.getChild("in").getChild("dimension");
                nodeArg.addChild(nodeArg.getChild("in").createBuilder().requires(Permissions.require("carpet.player.in", 2)).build());
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());

                node = (LiteralCommandNode) nodeArg.getChild("in");
                nodeArg.addChild(node.createBuilder().requires(Permissions.require("carpet.player.in", 2)).build());

                nodeArg = (ArgumentCommandNode) node.getChild("gamemode");
                nodeArg.addChild(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn).build());
                continue;
            }

            if (node.getLiteral().equals("in")) {
                builder.then(node.createBuilder().requires(Permissions.require("carpet.player.in", 2)).build());;
                node.addChild(node.getChild("gamemode").createBuilder()
                        .then(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn)).build());
            }
        }

        builder.then(argument("private", BoolArgumentType.bool()).executes(PlayerCommandSpawnInvoker::spawn));
        return builder;
    }

    @Inject(method = "cantReMove(Lcom/mojang/brigadier/context/CommandContext;)Z", at = @At(value = "INVOKE", target = "Lcarpet/commands/PlayerCommand;getPlayer(Lcom/mojang/brigadier/context/CommandContext;)Lnet/minecraft/server/network/ServerPlayerEntity;"))
    private static void carpetlantern$decrementBotCap(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Boolean> cir) {
        String playerName = context.getSource().getPlayer().getGameProfile().getName();
        String botName = StringArgumentType.getString(context, "player");

        if (!BotCapStorage.canKill(playerName, botName) && !Permissions.check(context.getSource(), "carpet.ignorePrivateBot", 2)) {
            if (CarpetLanternSettings.clUseCarpetMessageFormat) {
                Messenger.m(context.getSource(), "r Only the summoner can kill private bots");
            } else {
                context.getSource().sendFeedback(() -> Text.literal("Only the summoner can kill private bots").formatted(Formatting.RED), false);
            }
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "cantSpawn", at = @At("RETURN"), cancellable = true)
    private static void carpetlantern$botCapCheck(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Boolean> cir, @Local MinecraftServer server, @Local(ordinal = 0) GameProfile profile) {
        ServerCommandSource source = context.getSource();
        //Bot cap

        if(source.isExecutedByPlayer()) {
            if (!Permissions.check(source, "carpet.ignoreGlobalBotCap", 2) && BotCapStorage.isCapReached()) {
                if(CarpetLanternSettings.clUseCarpetMessageFormat) {
                    Messenger.m(source, "r You can't spawn more than ", "rb " +CarpetLanternSettings.maxPlayerBotGlobalCap + " ", "r players globally");
                }else {
                    source.sendFeedback(() -> Text.literal("You can't spawn more than " + CarpetLanternSettings.maxPlayerBotGlobalCap + " players globally").formatted(Formatting.RED), false);
                }

                cir.setReturnValue(true);
                return;
            }
            if (!Permissions.check(source, "carpet.unlimitedBots", 2) && BotCapStorage.isCapReachedFor(source.getPlayer().getGameProfile().getName(), Options.get(source, "carpet.maxPlayerBotCap", CarpetLanternSettings.maxPlayerBotCap, Integer::parseInt))) {
                if(CarpetLanternSettings.clUseCarpetMessageFormat) {
                    Messenger.m(source, "r You can't spawn more than ", "rb " +CarpetLanternSettings.maxPlayerBotCap + " ", "r players");
                }else {
                    source.sendFeedback(() -> Text.literal("You can't spawn more than " + CarpetLanternSettings.maxPlayerBotCap + " players").formatted(Formatting.RED), false);
                }

                cir.setReturnValue(true);
                return;
            }
        }
        if(BlockBotIntegration.isPlayerWhitelisted(profile, server) && !source.hasPermissionLevel(2)) {
            if(CarpetLanternSettings.clUseCarpetMessageFormat) {
                Messenger.m(source, "r BlockBot whitelisted players can only be spawned by operators");
            }else {
                source.sendFeedback(() -> Text.literal("BlockBot whitelisted players can only be spawned by operators").formatted(Formatting.RED), false);
            }
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "spawn", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void carpetlantern$spawnIncrement(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir, ServerCommandSource source, Vec3d pos, Vec2f facing, RegistryKey dimType, GameMode mode, boolean flying, String playerName, PlayerEntity player) {
        //Bot cap
        if(player != null) {
            String summonerName = context.getSource().isExecutedByPlayer() ? context.getSource().getPlayer().getGameProfile().getName() : null;
            ((IPlayerAccessor)player).carpetlantern$setSummonerName(summonerName);
            boolean privateBot = false;
            try {
                privateBot = BoolArgumentType.getBool(context, "private");
            } catch (IllegalArgumentException ignored) {}
            BotCapStorage.increment(summonerName, playerName, privateBot);
        }
    }
}
