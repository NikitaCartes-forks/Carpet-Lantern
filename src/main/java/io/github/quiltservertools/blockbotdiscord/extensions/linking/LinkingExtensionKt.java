package io.github.quiltservertools.blockbotdiscord.extensions.linking;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

//A bridge to the BlockBot API
public class LinkingExtensionKt {
    public static Text canJoin(GameProfile profile, MinecraftServer server) {
        throw new AssertionError();
    }
}
