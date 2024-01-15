package ru.nern.carpetlantern.mixin;

import carpet.commands.PlayerCommand;
import carpet.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.ServerCommandSource;
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

//Checks if player can spawn anymore carpet bots.
@Mixin(value = PlayerCommand.class, remap = false)
public class PlayerCommandMixin {
    @Inject(method = "cantSpawn", at = @At("RETURN"), cancellable = true)
    private static void carpetlantern$botCapCheck(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Boolean> cir) {
        //Bot cap
        if(context.getSource().isExecutedByPlayer() && !Permissions.check(context.getSource(), "carpet.unlimitedBots", 2) && BotCapStorage.isCapReachedFor(context.getSource().getPlayer().getGameProfile().getName())){
            Messenger.m(context.getSource(), "You can't spawn more than " + CarpetLanternSettings.maxPlayerBotCap + " players");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "spawn", at = @At("TAIL"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void carpetlantern$spawnIncrement(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir, ServerCommandSource source, Vec3d pos, Vec2f facing, RegistryKey dimType, GameMode mode, boolean flying, String playerName, PlayerEntity player) {
        //Bot cap
        if(player != null) {
            String summonerName = context.getSource().isExecutedByPlayer() ? context.getSource().getPlayer().getGameProfile().getName() : null;
            ((IPlayerAccessor)player).setSummonerName(summonerName);
            BotCapStorage.increment(summonerName);
        }
    }
}
