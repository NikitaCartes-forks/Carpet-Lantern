package xyz.nikitacartes.sculkcarpet;


import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

import static carpet.api.settings.RuleCategory.*;

public class SculkCarpetSettings {
    public static final String SCULK = "sculk";
    private static final String LIST_SPLITTER = "[,;\\s]+";

    private static volatile Set<ResourceLocation> cachedFakePlayerBlockInteractionDisabledSet = Set.of();
    private static volatile Set<ResourceLocation> cachedFakePlayerEntityInteractionDisabledSet = Set.of();

    private static class MaxBotCapValidator extends Validator<Integer> {
        @Override
        public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string) {
            return newValue >= -1 ? newValue : null;
        }

        @Override
        public String description() {
            return "You must choose a value from -1 (disabled) to 20M";
        }
    }

    @Rule(
            options = {"-1", "2", "4"},
            strict = false,
            categories = {SCULK, FEATURE},
            validators = MaxBotCapValidator.class
    )
    public static int maxPlayerBotCap = -1;

    @Rule(
            options = {"-1", "2", "4", "8"},
            strict = false,
            categories = {SCULK, FEATURE},
            validators = MaxBotCapValidator.class
    )
    public static int maxPlayerBotGlobalCap = -1;

    @Rule(
            options = {"default", "bot", "player"},
            strict = false,
            categories = {SCULK, FEATURE}
    )
    public static String fakePlayerLuckPermsGroup = "default";

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static boolean fakePlayerRemoveDefaultGroup = false;

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static boolean fakePlayerDefaultPrivate = false;

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static boolean playerCommandBlockBotVerification = false;

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static boolean fakePlayerXpDropFix = false;

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static boolean disablePlayerJoinAsBot = false;

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static String botNamePrefix = "";

    @Rule(
            categories = {SCULK, FEATURE}
    )
    public static String botNameSuffix = "";

    private static class BlockInteractionDisabledListValidator extends Validator<String> {
        @Override
        public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
            try {
                cachedFakePlayerBlockInteractionDisabledSet = parseIdList(newValue, BuiltInRegistries.BLOCK::containsKey);
                return newValue;
            } catch (IllegalArgumentException exception) {
                if (source != null) Messenger.m(source, "r " + exception.getMessage());
                return null;
            }
        }

        @Override
        public String description() {
            return "Use a comma, space, or semicolon separated list of block ids, e.g. minecraft:chest,minecraft:hopper";
        }
    }

    @Rule(
            strict = false,
            categories = {SCULK, FEATURE},
            validators = BlockInteractionDisabledListValidator.class
    )
    public static String fakePlayerBlockInteractionDisabledList = "";

    private static class EntityInteractionDisabledListValidator extends Validator<String> {
        @Override
        public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
            try {
                cachedFakePlayerEntityInteractionDisabledSet = parseIdList(newValue, BuiltInRegistries.ENTITY_TYPE::containsKey);
                return newValue;
            } catch (IllegalArgumentException exception) {
                if (source != null) Messenger.m(source, "r " + exception.getMessage());
                return null;
            }
        }

        @Override
        public String description() {
            return "Use a comma, space, or semicolon separated list of entity type ids, e.g. minecraft:villager,minecraft:cow";
        }
    }

    @Rule(
            strict = false,
            categories = {SCULK, FEATURE},
            validators = EntityInteractionDisabledListValidator.class
    )
    public static String fakePlayerEntityInteractionDisabledList = "";

    public static boolean isFakePlayerBlockInteractionDisabled(BlockState state) {
        if (state == null) return false;
        return cachedFakePlayerBlockInteractionDisabledSet.contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
    }

    public static boolean isFakePlayerEntityInteractionDisabled(Entity entity) {
        if (entity == null) return false;
        return cachedFakePlayerEntityInteractionDisabledSet.contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()));
    }

    private static Set<ResourceLocation> parseIdList(String ruleValue, Predicate<ResourceLocation> registryCheck) {
        if (ruleValue == null || ruleValue.isBlank()) return Set.of();

        LinkedHashSet<ResourceLocation> ids = new LinkedHashSet<>();
        for (String entry : ruleValue.split(LIST_SPLITTER)) {
            if (entry.isBlank()) continue;

            String normalized = entry.trim().toLowerCase(Locale.ENGLISH);
            ResourceLocation id;
            try {
                id = ResourceLocation.parse(normalized);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Invalid id: " + entry);
            }

            if (!registryCheck.test(id)) {
                throw new IllegalArgumentException("Unknown id: " + entry);
            }

            ids.add(id);
        }

        return ids.isEmpty() ? Set.of() : Collections.unmodifiableSet(ids);
    }
}
