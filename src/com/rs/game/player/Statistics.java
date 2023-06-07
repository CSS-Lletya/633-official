package com.rs.game.player;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Getter;

/**
 * General statistics that a player increments over various things.
 * Thistle also be very useful for website contents such as Adventure logs, Highscores, such.
 * <p>
 * In advance, we can also do end of the month collection data where we should X amount of things been done
 * like RS did (Video link below for demo concept), I find this pretty good to capitalize on!
 * <p>
 * Video - https://youtu.be/7RNK0YBdwko?t=210
 * @author Dennis
 */
public final class Statistics {

    /**
     * A large collection of statistics achieved by the Player.
     * Skilling, Minigames, etc..
     */
    @Getter
    private Object2ObjectArrayMap<String, Integer> statistics = new Object2ObjectArrayMap<String, Integer>();

    /**
     * A large collection of NPC Killing-based statistics achieved by the Player.
     */
    @Getter
    private Object2ObjectArrayMap<String, Integer> NPCKillStatistics = new Object2ObjectArrayMap<String, Integer>();

    /**
     * Creates a new instance of the players' statistics if not present.
     */
    public Statistics() {
        statistics = new Object2ObjectArrayMap<String, Integer>();
        NPCKillStatistics = new Object2ObjectArrayMap<String, Integer>();
    }

    /**
     * Increments a statistic value. If not present we securely add a default value (prior)
     *
     * @param key
     */
    public void addStatistic(String key) {
        if (key.contains("_Kills")) {
            NPCKillStatistics.putIfAbsent(key, 0);
            NPCKillStatistics.put(key, NPCKillStatistics.get(key) + 1);
        } else {
            statistics.putIfAbsent(key, 0);
            statistics.put(key, statistics.get(key) + 1);
        }
    }

    /**
     * Subtracts a statistic value. If it's unable to it won't.
     *
     * @param key
     */
    public void subtractStatistic(String key) {
        if (key.contains("_Kills")) {
            if (!NPCKillStatistics.containsKey(key) || NPCKillStatistics.get(key) <= 0)
                return;
            NPCKillStatistics.put(key, NPCKillStatistics.get(key) - 1);
        } else {
            if (!statistics.containsKey(key) || statistics.get(key) <= 0)
                return;
            statistics.put(key, statistics.get(key) - 1);
        }
    }

    /**
     * Removes a specific statistic complete from their collection list.
     * This wouldn't really be used unless we're using it for a very specific reason.
     *
     * @param key
     */
    public void clearMapEntry(String key) {
        if (key.contains("_Kills")) {
            if (!NPCKillStatistics.containsKey(key))
                return;
            NPCKillStatistics.remove(key);
        } else {
            if (!statistics.containsKey(key))
                return;
            statistics.remove(key);
        }
    }

    /**
     * Gets the map ordinal and safely retrieves its value
     * @param entry
     * @return
     */
    public Integer getStatisticsMapValue(String entry){
        return (getStatistics().get(entry) == null ? 0 : getStatistics().get(entry));
    }

    /**
     * Gets the map ordinal and safely retrieves its value
     * @param entry
     * @return
     */
    public Integer getKillsMapValue(String entry){
        return (getNPCKillStatistics().get(entry + "_Kills") == null ? 0 : getNPCKillStatistics().get(entry + "_Kills"));
    }
}