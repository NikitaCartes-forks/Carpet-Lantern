package ru.nern.carpetlantern;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class BotCapStorage {
    public static Object2IntMap<String> BOT_MAP = new Object2IntOpenHashMap<>();
    public static int BOT_NUMBER = 0;

    public static boolean isCapReachedFor(String playerName){
        return BOT_MAP.containsKey(playerName) && BOT_MAP.getInt(playerName) >= CarpetLanternSettings.maxPlayerBotCap;
    }

    public static boolean isCapReached(){
        return BOT_NUMBER >= CarpetLanternSettings.maxPlayerBotGlobalCap;
    }

    public static void increment(String playerName){
        BOT_NUMBER++;
        BOT_MAP.put(playerName, BOT_MAP.getInt(playerName)+1);
    }

    public static void decrement(String playerName){
        BOT_NUMBER--;
        int value = BOT_MAP.getInt(playerName)-1;
        if(value == 0){
            BOT_MAP.removeInt(playerName);
        }else if (value > 0){
            BOT_MAP.put(playerName, value);
        }
    }

    public static int get(String playerName){
        return BOT_MAP.getInt(playerName);
    }
}

