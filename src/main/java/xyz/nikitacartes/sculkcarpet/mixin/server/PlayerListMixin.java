package xyz.nikitacartes.sculkcarpet.mixin.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.nikitacartes.sculkcarpet.BotCapStorage;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;

import java.net.SocketAddress;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "canPlayerLogin", at = @At("HEAD"), cancellable = true)
    public void sculkcarpet$checkBotLogin(SocketAddress socketAddress, NameAndId nameAndId, CallbackInfoReturnable<Component> cir) {
        if (SculkCarpetSettings.disablePlayerJoinAsBot && BotCapStorage.checkBotName(nameAndId.name())) {
            cir.setReturnValue(Component.literal("You cannot join as a bot"));
        }
    }
}
