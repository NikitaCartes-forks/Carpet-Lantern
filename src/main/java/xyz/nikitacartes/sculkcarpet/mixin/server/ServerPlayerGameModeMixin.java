package xyz.nikitacartes.sculkcarpet.mixin.server;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {

    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    private void sculkcarpet$skipDisabledFakePlayerBlockInteractions(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (!(player instanceof EntityPlayerMPFake)) {
            return;
        }

        if (SculkCarpetSettings.isFakePlayerBlockInteractionDisabled(level.getBlockState(blockHitResult.getBlockPos()))) {
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}