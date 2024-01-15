package ru.nern.carpetlantern;


import carpet.api.settings.Rule;

import static carpet.api.settings.RuleCategory.CREATIVE;
import static carpet.api.settings.RuleCategory.FEATURE;
import static carpettisaddition.CarpetTISAdditionSettings.TIS;

public class CarpetLanternSettings {
    @Rule(
            options = {"2", "4"},
            categories = CREATIVE
    )
    public static int maxPlayerBotCap = 2;

    @Rule(
            options = {"-1", "64", "128"},
            strict = false,
            categories = {TIS, FEATURE}
    )
    public static int updateSuppressionMessageRange = -1;
}
