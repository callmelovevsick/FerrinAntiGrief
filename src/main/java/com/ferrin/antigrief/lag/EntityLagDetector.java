package com.ferrin.antigrief.lag;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

public class EntityLagDetector {
    private final FerrinAntiGrief plugin;

    public EntityLagDetector(FerrinAntiGrief plugin) {
        this.plugin = plugin;
        startTask();
    }

    private void startTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            int limit = plugin.getConfig().getInt("entity-lag-detector.max-entities-per-chunk");
            for (World world : Bukkit.getWorlds()) {
                for (Chunk chunk : world.getLoadedChunks()) {
                    Entity[] entities = chunk.getEntities();
                    if (entities.length > limit) {
                        handleExcess(chunk, entities, limit);
                    }
                }
            }
        }, 20L * 60, 20L * plugin.getConfig().getInt("entity-lag-detector.scan-interval-seconds"));
    }

    private void handleExcess(Chunk chunk, Entity[] entities, int limit) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            int toRemove = entities.length - limit;
            int removed = 0;
            for (Entity e : entities) {
                if (removed >= toRemove) break;
                if (!(e instanceof org.bukkit.entity.Player)) {
                    e.remove();
                    removed++;
                }
            }
            plugin.getLogger().warning("Cleared " + removed + " entities at Chunk " + chunk.getX() + "," + chunk.getZ());
        });
    }
}