package ru.nern.carpetlantern.mixin.carpet;

import carpet.commands.PlayerCommand;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//Checks if player can spawn anymore carpet bots.
@Mixin(value = PlayerCommand.class, remap = false)
public interface PlayerCommandSpawnInvoker {

    @Invoker("spawn")
    static int spawn(CommandContext<ServerCommandSource> context) {
        throw new AssertionError();
    }
}
