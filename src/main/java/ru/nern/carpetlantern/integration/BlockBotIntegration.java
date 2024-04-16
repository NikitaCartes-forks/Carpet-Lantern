package ru.nern.carpetlantern.integration;

import com.mojang.authlib.GameProfile;
import io.github.quiltservertools.blockbotdiscord.extensions.linking.LinkingExtensionKt;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import ru.nern.carpetlantern.CarpetLanternSettings;

public class BlockBotIntegration {
    public static boolean isPlayerWhitelisted(GameProfile profile, MinecraftServer server) {
        if(!FabricLoader.getInstance().isModLoaded("blockbot-discord")) {
            return true;
        }

        if(CarpetLanternSettings.playerCommandBlockBotVerification) {
            Text message = LinkingExtensionKt.canJoin(profile, server);
            return message == null;
        }

        return true;
    }
}
