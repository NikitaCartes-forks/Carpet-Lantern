package xyz.nikitacartes.sculkcarpet.mixin.carpet;

import carpet.patches.EntityPlayerMPFake;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nikitacartes.sculkcarpet.BotCapStorage;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;
import xyz.nikitacartes.sculkcarpet.integration.LuckPermsHelper;

//Handle decrement when a fake player is killed and LuckPerms group management.
@Mixin(EntityPlayerMPFake.class)
public abstract class EntityPlayerMPFakeMixin extends ServerPlayer {

    public EntityPlayerMPFakeMixin(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile, ClientInformation clientInformation) {
        super(minecraftServer, serverLevel, gameProfile, clientInformation);
    }

    @Inject(method = "<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/server/level/ServerLevel;Lcom/mojang/authlib/GameProfile;Lnet/minecraft/server/level/ClientInformation;Z)V", at = @At("RETURN"))
    private static void sculkcarpet$addToLuckPermsGroup(MinecraftServer server, ServerLevel worldIn, GameProfile profile, ClientInformation cli, boolean shadow, CallbackInfo ci) {
        LuckPermsHelper.addToGroup(profile.id(), profile.name());
    }

    @Inject(method = "kill(Lnet/minecraft/network/chat/Component;)V", at = @At("TAIL"))
    private void sculkcarpet$decrementOnKill(Component reason, CallbackInfo ci) {
        if (SculkCarpetSettings.fakePlayerXpDropFix) {
            this.experienceLevel = 0;
            this.totalExperience = 0;
            this.experienceProgress = 0.0F;
        }
        LuckPermsHelper.removeFromGroup(this.getUUID(), this.nameAndId().name());
        BotCapStorage.decrement(this.nameAndId().name());
        ServerPlayConnectionEvents.DISCONNECT.invoker().onPlayDisconnect(this.connection, this.level().getServer());
    }
}
