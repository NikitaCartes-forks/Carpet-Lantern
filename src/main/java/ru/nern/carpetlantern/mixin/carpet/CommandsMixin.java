package ru.nern.carpetlantern.mixin.carpet;

import carpet.api.settings.SettingsManager;
import carpet.commands.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

//Replace Carpet's perms system with Fabric Permissions API
public class CommandsMixin {
    @Mixin(value = CounterCommand.class, remap = false)
    public static class CounterCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.counter", 2));
        }
    }

    @Mixin(value = DistanceCommand.class, remap = false)
    public static class DistanceCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.distance", 0));
        }
    }

    @Mixin(value = DrawCommand.class, remap = false)
    public static class DrawCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.draw", 2));
        }
    }

    @Mixin(value = InfoCommand.class, remap = false)
    public static class InfoCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.info", 0));
        }
    }

    @Mixin(value = LogCommand.class, remap = false)
    public static class LogCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.log", 0));
        }
    }

    @Mixin(value = MobAICommand.class, remap = false)
    public static class MobAICommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.trackAI", 2));
        }
    }

    @Mixin(value = PerimeterInfoCommand.class, remap = false)
    public static class PerimeterInfoCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.perimeterinfo", 0));
        }
    }

    @Mixin(value = PlayerCommand.class, remap = false)
    public static class PlayerCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 0))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.player.root", 2));
        }
    }

    @Mixin(value = ProfileCommand.class, remap = false)
    public static class ProfileCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.profile", 0));
        }
    }

    @Mixin(value = SpawnCommand.class, remap = false)
    public static class SpawnCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.spawn", 2));
        }
    }

    @Mixin(value = TickCommand.class, remap = false)
    public static class TickCommandMixin {
        @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;"))
        private static ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.tick", 2));
        }
    }

    @Mixin(value = SettingsManager.class, remap = false)
    public static class SettingsManagerMixin {
        @Redirect(method = "registerCommand", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;requires(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;", ordinal = 0))
        private ArgumentBuilder carpetlantern$requirePerms(LiteralArgumentBuilder<ServerCommandSource> instance, Predicate<ServerCommandSource> predicate) {
            return instance.requires(Permissions.require("carpet.carpet", 2));
        }
    }
}
