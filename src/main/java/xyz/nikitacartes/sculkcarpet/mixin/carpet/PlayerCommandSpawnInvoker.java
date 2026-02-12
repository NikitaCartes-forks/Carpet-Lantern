package xyz.nikitacartes.sculkcarpet.mixin.carpet;

import carpet.commands.PlayerCommand;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//Invoker for PlayerCommand.spawn method.
@Mixin(value = PlayerCommand.class, remap = false)
public interface PlayerCommandSpawnInvoker {

    @Invoker("spawn")
    static int spawn(CommandContext<CommandSourceStack> context) {
        throw new AssertionError();
    }
}
