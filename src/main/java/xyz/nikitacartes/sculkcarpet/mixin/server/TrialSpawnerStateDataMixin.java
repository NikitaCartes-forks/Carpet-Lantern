package xyz.nikitacartes.sculkcarpet.mixin.server;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerStateData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;

import java.util.Set;
import java.util.UUID;

@Mixin(TrialSpawnerStateData.class)
public class TrialSpawnerStateDataMixin {

    @Final
    @Shadow
    Set<UUID> detectedPlayers;

    @Inject(method = "tryDetectPlayers", at = @At("TAIL"))
    private void sculkcarpet$ignoreFakePlayersInTrialSpawner(
            ServerLevel level, BlockPos pos, TrialSpawner spawner, CallbackInfo ci) {
        if (!SculkCarpetSettings.trialChamberIgnoresFakePlayers) {
            return;
        }
        detectedPlayers.removeIf(uuid -> level.getPlayerByUUID(uuid) instanceof EntityPlayerMPFake);
    }
}
