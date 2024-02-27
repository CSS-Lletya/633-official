package com.rs.game.player;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Statistics {

    private final Map<String, Integer> statistics;
    private final Map<String, Integer> NPCKillStatistics;

    public Statistics() {
        statistics = new ConcurrentHashMap<>();
        NPCKillStatistics = new ConcurrentHashMap<>();
    }

    public Statistics addStatistic(String key) {
        Map<String, Integer> targetMap = key.contains("_Kills") ? NPCKillStatistics : statistics;
        targetMap.merge(key, 1, Integer::sum);
        return this;
    }

    public Statistics subtractStatistic(String key) {
        Map<String, Integer> targetMap = key.contains("_Kills") ? NPCKillStatistics : statistics;
        targetMap.computeIfPresent(key, (k, v) -> v > 0 ? v - 1 : 0);
        return this;
    }

    public void clearMapEntry(String key) {
        if (key.contains("_Kills")) {
            NPCKillStatistics.remove(key);
        } else {
            statistics.remove(key);
        }
    }

    public int getStatisticsMapValue(String entry){
        return statistics.getOrDefault(entry, 0);
    }

    public int getKillsMapValue(String entry){
        return NPCKillStatistics.getOrDefault(entry + "_Kills", 0);
    }

    public Map<String, Integer> getStatistics() {
        return Collections.unmodifiableMap(statistics);
    }

    public Map<String, Integer> getNPCKillStatistics() {
        return Collections.unmodifiableMap(NPCKillStatistics);
    }
}
