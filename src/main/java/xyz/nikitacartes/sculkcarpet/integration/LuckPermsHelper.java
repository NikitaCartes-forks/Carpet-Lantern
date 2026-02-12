package xyz.nikitacartes.sculkcarpet.integration;

import net.fabricmc.loader.api.FabricLoader;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.InheritanceNode;
import xyz.nikitacartes.sculkcarpet.SculkCarpetSettings;

import java.util.UUID;

/**
 * Helper class for LuckPerms integration.
 * Handles adding/removing fake players to/from LuckPerms groups.
 */
public class LuckPermsHelper {
    
    private static final boolean LUCKPERMS_LOADED = FabricLoader.getInstance().isModLoaded("luckperms");
    private static LuckPerms luckPerms = null;
    
    /**
     * Gets the LuckPerms API instance, caching it for future calls.
     * @return LuckPerms API instance, or null if LuckPerms is not loaded
     */
    private static LuckPerms getLuckPerms() {
        if (!LUCKPERMS_LOADED) {
            return null;
        }
        if (luckPerms == null) {
            try {
                luckPerms = LuckPermsProvider.get();
            } catch (IllegalStateException e) {
                // LuckPerms is not loaded
                return null;
            }
        }
        return luckPerms;
    }
    
    /**
     * Adds a player to the configured LuckPerms group.
     * @param playerUuid The UUID of the player to add
     * @param playerName The name of the player
     */
    public static void addToGroup(UUID playerUuid, String playerName) {
        if (SculkCarpetSettings.fakePlayerLuckPermsGroup.isEmpty()) {
            return;
        }
        
        LuckPerms api = getLuckPerms();
        if (api == null) {
            return;
        }
        
        api.getUserManager().loadUser(playerUuid, playerName).thenAcceptAsync(user -> {
            if (user == null) {
                return;
            }

            String groupName = SculkCarpetSettings.fakePlayerLuckPermsGroup;
            InheritanceNode node = InheritanceNode.builder(groupName).build();

            user.data().add(node);

            if (SculkCarpetSettings.fakePlayerRemoveDefaultGroup && !"default".equalsIgnoreCase(groupName)) {
                InheritanceNode defaultNode = InheritanceNode.builder("default").build();
                user.data().remove(defaultNode);
            }

            api.getUserManager().saveUser(user);
        });
    }
    
    /**
     * Removes a player from the configured LuckPerms group.
     * @param playerUuid The UUID of the player to remove
     * @param playerName The name of the player
     */
    public static void removeFromGroup(UUID playerUuid, String playerName) {
        if (SculkCarpetSettings.fakePlayerLuckPermsGroup.isEmpty()) {
            return;
        }
        
        LuckPerms api = getLuckPerms();
        if (api == null) {
            return;
        }
        
        api.getUserManager().loadUser(playerUuid, playerName).thenAcceptAsync(user -> {
            if (user == null) {
                return;
            }

            String groupName = SculkCarpetSettings.fakePlayerLuckPermsGroup;
            InheritanceNode node = InheritanceNode.builder(groupName).build();
            
            user.data().remove(node);

            // if (SculkCarpetSettings.fakePlayerRemoveDefaultGroup && !"default".equalsIgnoreCase(groupName)) {
            //     InheritanceNode defaultNode = InheritanceNode.builder("default").build();
            //     user.data().add(defaultNode);
            // }

            api.getUserManager().saveUser(user);
        });
    }
}
