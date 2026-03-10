package com.ferrin.antigrief.monitor;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class HealthMonitor {
    public double getTPS() {
        return Bukkit.getTPS()[0];
    }

    public int getTotalEntities() {
        return Bukkit.getWorlds().stream().mapToInt(w -> w.getEntityCount()).sum();
    }

    public int getTotalChunks() {
        return Bukkit.getWorlds().stream().mapToInt(w -> w.getLoadedChunks().length).sum();
    }

    public String getMemoryUsage() {
        Runtime r = Runtime.getRuntime();
        long used = (r.totalMemory() - r.freeMemory()) / 1048576;
        long total = r.maxMemory() / 1048576;
        return used + "MB / " + total + "MB";
    }
}