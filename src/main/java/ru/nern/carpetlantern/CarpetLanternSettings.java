package ru.nern.carpetlantern;


import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import static carpet.api.settings.RuleCategory.*;

public class CarpetLanternSettings {
    public static final String LANTERN = "lantern";

    private static class MaxBotCapValidator extends Validator<Integer>
    {
        @Override
        public Integer validate(ServerCommandSource source, CarpetRule<Integer> currentRule, Integer newValue, String string)
        {
            return newValue >= 0 ? newValue : null;
        }

        @Override
        public String description() { return "You must choose a value from 1 to 20M";}
    }

    @Rule(
            options = {"2", "4"},
            strict = false,
            categories = {LANTERN, FEATURE},
            validators = MaxBotCapValidator.class
    )
    public static int maxPlayerBotCap = 2;

    @Rule(
            options = {"2", "4", "8"},
            strict = false,
            categories = {LANTERN, FEATURE},
            validators = MaxBotCapValidator.class
    )
    public static int maxPlayerBotGlobalCap = 4;

    @Rule(
            options = {"-1", "64", "128"},
            strict = false,
            categories = {LANTERN, FEATURE}
    )
    public static int updateSuppressionMessageRange = -1;

    @Rule(
            categories = {LANTERN, FEATURE}
    )
    public static boolean clUseCarpetMessageFormat = true;

    @Rule(
            categories = {LANTERN, FEATURE}
    )
    public static boolean playerCommandBlockBotVerification = false;
}
