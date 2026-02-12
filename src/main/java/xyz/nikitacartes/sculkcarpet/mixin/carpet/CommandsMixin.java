package xyz.nikitacartes.sculkcarpet.mixin.carpet;

import carpet.api.settings.SettingsManager;
import carpet.commands.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

//Replace Carpet's perms system with Fabric Permissions API
public class CommandsMixin {
    @Mixin(value = CounterCommand.class, remap = false)
    public static class CounterCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.counter", 2).test(source));
        }
    }

    @Mixin(value = DistanceCommand.class, remap = false)
    public static class DistanceCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.distance", 0).test(source));
        }
    }

    @Mixin(value = DrawCommand.class, remap = false)
    public static class DrawCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.draw", 2).test(source));
        }
    }

    @Mixin(value = InfoCommand.class, remap = false)
    public static class InfoCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.info", 0).test(source));
        }
    }

    @Mixin(value = LogCommand.class, remap = false)
    public static class LogCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.log", 0).test(source));
        }
    }

    @Mixin(value = MobAICommand.class, remap = false)
    public static class MobAICommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.trackAI", 2).test(source));
        }
    }

    @Mixin(value = PerimeterInfoCommand.class, remap = false)
    public static class PerimeterInfoCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.perimeterinfo", 0).test(source));
        }
    }

    @Mixin(value = PlayerCommand.class, remap = false)
    public static class PlayerCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 0))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.player.root", 2).test(source));
        }
    }

    @Mixin(value = ProfileCommand.class, remap = false)
    public static class ProfileCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.profile", 0).test(source));
        }
    }

    @Mixin(value = SpawnCommand.class, remap = false)
    public static class SpawnCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.spawn", 2).test(source));
        }
    }

    @Mixin(value = SettingsManager.class, remap = false)
    public static class SettingsManagerMixin {
        @Redirect(method = "registerCommand", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 0))
        private ArgumentBuilder sculkcarpet$requirePerms(LiteralArgumentBuilder<CommandSourceStack> instance, Predicate<CommandSourceStack> predicate) {
            return instance.requires(source -> predicate.test(source) && Permissions.require("carpet.carpet", 2).test(source));
        }
    }
}
