package xyz.nikitacartes.skulkcarpet;


import carpet.api.settings.CarpetRule;
import carpet.api.settings.Rule;
import carpet.api.settings.Validator;
import net.minecraft.commands.CommandSourceStack;

import static carpet.api.settings.RuleCategory.*;

public class SculkCarpetSettings {
    public static final String SKULK = "skulk";

    private static class MaxBotCapValidator extends Validator<Integer>
    {
        @Override
        public Integer validate(CommandSourceStack source, CarpetRule<Integer> currentRule, Integer newValue, String string)
        {
            return newValue >= 0 ? newValue : null;
        }

        @Override
        public String description() { return "You must choose a value from 1 to 20M";}
    }

    @Rule(
            options = {"2", "4"},
            strict = false,
            categories = {SKULK, FEATURE},
            validators = MaxBotCapValidator.class
    )
    public static int maxPlayerBotCap = 2;

    @Rule(
            options = {"2", "4", "8"},
            strict = false,
            categories = {SKULK, FEATURE},
            validators = MaxBotCapValidator.class
    )
    public static int maxPlayerBotGlobalCap = 4;
}
