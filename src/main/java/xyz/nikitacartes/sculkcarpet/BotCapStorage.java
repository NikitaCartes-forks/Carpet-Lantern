package xyz.nikitacartes.sculkcarpet;

import java.util.HashSet;
import java.util.Set;

public class BotCapStorage {
    private static final Set<Bot> BOTS = new HashSet<>();

    public static boolean isCapReachedFor(String playerName, int limit) {
        if (playerName == null) return false;
        if (limit < 0) return false; // -1 means disabled
        return BOTS.stream().filter(bot -> playerName.equalsIgnoreCase(bot.getSummonerName())).count() >= limit;
    }

    public static boolean isCapReached() {
        if (SculkCarpetSettings.maxPlayerBotGlobalCap < 0) return false; // -1 means disabled
        return BOTS.size() >= SculkCarpetSettings.maxPlayerBotGlobalCap;
    }

    public static void increment(String playerName, String botName, boolean privateBot) {
        BOTS.add(new Bot(botName, playerName, privateBot));
    }

    public static boolean canManipulate(String playerName, String botName) {
        if (playerName == null) return true;
        if (playerName.equalsIgnoreCase(botName)) return true;
        // if bot is private, only the summoner can manipulate it
        return BOTS.stream().filter(bot -> botName.equalsIgnoreCase(bot.getName())).findFirst()
                .map(bot -> !bot.isPrivate() || playerName.equalsIgnoreCase(bot.getSummonerName())).orElse(false);
    }

    public static boolean checkBotName(String botName) {
        return BOTS.stream().anyMatch(bot -> bot.getName().equalsIgnoreCase(botName));
    }

    public static void decrement(String botName) {
        BOTS.removeIf(bot -> bot.getName().equalsIgnoreCase(botName));
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
