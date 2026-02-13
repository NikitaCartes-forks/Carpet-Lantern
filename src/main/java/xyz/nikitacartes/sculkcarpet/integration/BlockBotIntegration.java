package xyz.nikitacartes.sculkcarpet.integration;

import io.github.quiltservertools.blockbotdiscord.config.LinkingSpec;
import io.github.quiltservertools.blockbotdiscord.config.ConfigKt;
import io.github.quiltservertools.blockbotdiscord.extensions.linking.LinkingExtensionKt;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;

/**
 * Integration with BlockBot Discord mod for player whitelist verification.
 * Used to prevent spawning carpet bots with names of players who are
 * whitelisted/linked via BlockBot.
 */
public class BlockBotIntegration {
    
    // Cache the mod loaded check to avoid repeated FabricLoader calls
    private static final boolean BLOCKBOT_LOADED = FabricLoader.getInstance().isModLoaded("blockbot-discord");

    /**
     * Checks if a player with the given GameProfile is whitelisted via BlockBot.
     * 
     * @param profile The GameProfile to check
     * @param server The Minecraft server instance
     * @return true if the player IS whitelisted (meaning bot spawning should be BLOCKED),
     *         false if the player is NOT whitelisted (bot spawning is allowed)
     */
    public static boolean isPlayerWhitelisted(NameAndId profile, MinecraftServer server) {
        // If BlockBot mod is not loaded, allow bot spawning
        if (!BLOCKBOT_LOADED) {
            return false;
        }

        // If the verification setting is disabled, allow bot spawning
        if (!SculkCarpetSettings.playerCommandBlockBotVerification) {
            return false;
        }

        try {
            // If BlockBot's linking is not enabled or not required, allow bot spawning
            if (!shouldCheckLinking()) {
                return false;
            }

            Component message = LinkingExtensionKt.canJoin(profile, server);
            return message == null;
        } catch (Exception e) {
            System.out.println("Failed to check BlockBot linking status for player " + profile.name() + ": " + e.getMessage());
            return false;
        }
    }

    private static boolean shouldCheckLinking() {
        return ConfigKt.getConfig().get(LinkingSpec.INSTANCE.getEnabled()) && ConfigKt.getConfig().get(LinkingSpec.INSTANCE.getRequireLinking());
    }
}
