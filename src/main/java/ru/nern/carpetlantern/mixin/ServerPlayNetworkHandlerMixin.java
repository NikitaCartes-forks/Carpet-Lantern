package ru.nern.carpetlantern.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.nern.carpetlantern.BotCapStorage;
import ru.nern.carpetlantern.IPlayerAccessor;

//Decrements player's bot cap when a bot leaves.
@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    private void carpetlantern$decrementBotCap(CallbackInfo ci) {
        if(player instanceof EntityPlayerMPFake) {
            String name = ((IPlayerAccessor)player).carpetlantern$getSummonerName();
            if(name != null) {
                BotCapStorage.decrement(player.getGameProfile().getName());
            }

        }
    }
}
