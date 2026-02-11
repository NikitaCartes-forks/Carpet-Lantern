package xyz.nikitacartes.skulkcarpet;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.types.InheritanceNode;

import java.util.UUID;

/**
 * Helper class for LuckPerms integration.
 * Handles adding/removing fake players to/from LuckPerms groups.
 */
public class LuckPermsHelper {
    
    private static LuckPerms luckPerms = null;
    
    /**
     * Gets the LuckPerms API instance, caching it for future calls.
     * @return LuckPerms API instance, or null if LuckPerms is not loaded
     */
    private static LuckPerms getLuckPerms() {
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
            api.getUserManager().saveUser(user);
        });
    }
}
