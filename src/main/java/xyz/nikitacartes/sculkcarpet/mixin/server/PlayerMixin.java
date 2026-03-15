package xyz.nikitacartes.sculkcarpet.mixin.server;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
    private void sculkcarpet$skipDisabledFakePlayerEntityInteractions(Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(((Object) this) instanceof EntityPlayerMPFake)) {
            return;
        }

        if (SculkCarpetSettings.isFakePlayerEntityInteractionDisabled(entity)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
