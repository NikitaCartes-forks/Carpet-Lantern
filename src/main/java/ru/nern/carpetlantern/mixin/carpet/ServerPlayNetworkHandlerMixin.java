package ru.nern.carpetlantern.mixin.carpet;

import carpet.CarpetSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ServerPlayNetworkHandler.class, priority = 500)
public abstract class ServerPlayNetworkHandlerMixin {

    @WrapOperation(
            method = "onPlayerInteractBlock",
            at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;MAX_BREAK_SQUARED_DISTANCE:D")
    )
    private double carpetlantern$overridePlacementReach(Operation<Double> original) {
        return CarpetSettings.antiCheatDisabled ? 1024.0 : original.call();
    }
}
