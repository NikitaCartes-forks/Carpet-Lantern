package ru.nern.carpetlantern;

import java.util.*;

public class BotCapStorage {
    private static final Set<Bot> BOTS = new HashSet<>();
    private static int BOT_NUMBER = 0;

    public static boolean isCapReachedFor(String playerName, int limit){
        return BOTS.stream().filter(bot -> bot.getSummonerName().equals(playerName)).count() >= limit;
    }

    public static boolean isCapReached(){
        return BOT_NUMBER >= CarpetLanternSettings.maxPlayerBotGlobalCap;
    }

    public static void increment(String playerName, String botName, boolean privateBot){
        BOT_NUMBER++;
        Bot bot = new Bot(botName, playerName, privateBot);
        BOTS.add(bot);
    }

    public static boolean canKill(String playerName, String botName){
        // if bot is private, only the summoner can kill it
        return BOTS.stream().filter(bot -> bot.getName().equals(botName)).findFirst()
                .map(bot -> !bot.isPrivate() || bot.getSummonerName().equals(playerName)).orElse(false);
    }

    public static void decrement(String botName){
        BOT_NUMBER--;
        BOTS.removeIf(bot -> bot.getName().equals(botName));
    }

    private static class Bot {
        private final String botName;
        private final String summonerName;
        private final boolean privateBot;

        public Bot(String botName, String summonerName, boolean privateBot) {
            this.botName = botName;
            this.summonerName = summonerName;
            this.privateBot = privateBot;
        }

        public String getName() {
            return botName;
        }

        public String getSummonerName() {
            return summonerName;
        }

        public boolean isPrivate() {
            return privateBot;
        }
    }
}

