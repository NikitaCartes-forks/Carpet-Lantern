package ru.nern.carpetlantern.mixin.carpet;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ru.nern.carpetlantern.BotCapStorage;
import ru.nern.carpetlantern.CarpetLanternSettings;
import ru.nern.carpetlantern.IPlayerAccessor;
import ru.nern.carpetlantern.integration.BlockBotIntegration;

import java.util.function.Predicate;

//Checks if player can spawn anymore carpet bots.
@Mixin(value = PlayerCommand.class, remap = false)
public class PlayerCommandMixin {
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 1))
    private static ArgumentBuilder carpetlantern$requirePermsIn(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
        return instance.requires(Permissions.require("carpet.player.in", 2));
    }

    //We're using illegal tricks here...
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/command/CommandManager;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;",
    ordinal = 31))
    private static LiteralArgumentBuilder<ServerCommandSource> carpetlantern$requirePermsAt(String literal) {
        return CommandManager.literal(literal).requires(Permissions.require("carpet.player.at", 2));
    }

    @Inject(method = "cantSpawn", at = @At("RETURN"), cancellable = true)
    private static void carpetlantern$botCapCheck(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Boolean> cir, @Local MinecraftServer server, @Local(ordinal = 0) GameProfile profile) {
        ServerCommandSource source = context.getSource();
        //Bot cap
        if(source.isExecutedByPlayer() && !Permissions.check(source, "carpet.unlimitedBots", 2) && BotCapStorage.isCapReachedFor(source.getPlayer().getGameProfile().getName())){
            if(CarpetLanternSettings.clUseCarpetMessageFormat) {
                Messenger.m(source, "r You can't spawn more than ", "rb " +CarpetLanternSettings.maxPlayerBotCap + " ", "r players");
            }else {
                source.sendFeedback(() -> Text.literal("You can't spawn more than " + CarpetLanternSettings.maxPlayerBotCap + " players").formatted(Formatting.RED), false);
            }

            cir.setReturnValue(true);
            return;
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
            BotCapStorage.increment(summonerName);
        }
    }
}
