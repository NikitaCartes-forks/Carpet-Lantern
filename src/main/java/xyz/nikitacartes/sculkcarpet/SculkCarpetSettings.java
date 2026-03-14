package xyz.nikitacartes.sculkcarpet;


import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import carpet.utils.Messenger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import static carpet.api.settings.RuleCategory.*;

public class SculkCarpetSettings {
    public static final String SCULK = "sculk";
    private static final String BLOCK_INTERACTION_DISABLED_LIST_SPLITTER = "[,;\\s]+";
    private static volatile String cachedFakePlayerBlockInteractionDisabledList = "";
    private static volatile Set<ResourceLocation> cachedFakePlayerBlockInteractionDisabledSet = Set.of();

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

    private static class BlockInteractionDisabledListValidator extends Validator<String> {
        @Override
        public String validate(CommandSourceStack source, CarpetRule<String> currentRule, String newValue, String string) {
            try {
                return normalizeBlockInteractionDisabledList(newValue);
            } catch (IllegalArgumentException exception) {
                if (source != null) {
                    Messenger.m(source, "r " + exception.getMessage());
                }
                return null;
            }
        }

        @Override
        public String description() {
            return "Use a comma, space, or semicolon separated list of block ids, e.g. minecraft:chest,minecraft:hopper";
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

    @Rule(
            strict = false,
            categories = {SCULK, FEATURE},
            validators = BlockInteractionDisabledListValidator.class
    )
    public static String fakePlayerBlockInteractionDisabledList = "";

    public static boolean isFakePlayerBlockInteractionDisabled(BlockState state) {
        if (state == null) {
            return false;
        }
        return getFakePlayerBlockInteractionDisabledSet().contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
    }

    private static Set<ResourceLocation> getFakePlayerBlockInteractionDisabledSet() {
        String ruleValue = fakePlayerBlockInteractionDisabledList;
        if (Objects.equals(cachedFakePlayerBlockInteractionDisabledList, ruleValue)) {
            return cachedFakePlayerBlockInteractionDisabledSet;
        }

        synchronized (SculkCarpetSettings.class) {
            if (!Objects.equals(cachedFakePlayerBlockInteractionDisabledList, ruleValue)) {
                cachedFakePlayerBlockInteractionDisabledSet = parseBlockInteractionDisabledList(ruleValue, false);
                cachedFakePlayerBlockInteractionDisabledList = ruleValue;
            }
            return cachedFakePlayerBlockInteractionDisabledSet;
        }
    }

    private static String normalizeBlockInteractionDisabledList(String ruleValue) {
        Set<ResourceLocation> disabledBlocks = parseBlockInteractionDisabledList(ruleValue, true);
        if (disabledBlocks.isEmpty()) {
            return "";
        }
        return String.join(",", disabledBlocks.stream().map(ResourceLocation::toString).toList());
    }

    private static Set<ResourceLocation> parseBlockInteractionDisabledList(String ruleValue, boolean failOnInvalid) {
        if (ruleValue == null || ruleValue.isBlank()) {
            return Set.of();
        }

        LinkedHashSet<ResourceLocation> disabledBlocks = new LinkedHashSet<>();
        for (String entry : ruleValue.split(BLOCK_INTERACTION_DISABLED_LIST_SPLITTER)) {
            if (entry.isBlank()) {
                continue;
            }

            String normalizedEntry = entry.trim().toLowerCase(Locale.ENGLISH);
            ResourceLocation blockId;
            try {
                blockId = ResourceLocation.parse(normalizedEntry);
            } catch (RuntimeException exception) {
                if (failOnInvalid) {
                    throw new IllegalArgumentException("Invalid block id: " + entry);
                }
                continue;
            }

            if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
                if (failOnInvalid) {
                    throw new IllegalArgumentException("Unknown block id: " + entry);
                }
                continue;
            }

            disabledBlocks.add(blockId);
        }

        if (disabledBlocks.isEmpty()) {
            return Set.of();
        }
        return Collections.unmodifiableSet(disabledBlocks);
    }
}
