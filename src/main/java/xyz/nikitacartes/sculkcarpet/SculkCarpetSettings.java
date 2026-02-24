package xyz.nikitacartes.sculkcarpet;


import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.commands.CommandSourceStack;

import static carpet.api.settings.RuleCategory.*;

public class SculkCarpetSettings {
    public static final String SCULK = "sculk";

    private static class MaxBotCapValidator extends Validator<Integer>
    {
        @Override
        public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string)
        {
            return newValue >= -1 ? newValue : null;
        }

        @Override
        public String description() { return "You must choose a value from -1 (disabled) to 20M";}
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
}
