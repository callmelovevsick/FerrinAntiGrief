package com.ferrin.antigrief.lag;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.concurrent.ConcurrentHashMap;

public class RedstoneDetector implements Listener {

    private final FerrinAntiGrief plugin;
    private final ConcurrentHashMap<Location, Integer> trackMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Location, Long> lastReset = new ConcurrentHashMap<>();

    public RedstoneDetector(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRedstoneChange(BlockRedstoneEvent event) {
        if (!plugin.getConfig().getBoolean("redstone-protection.enabled", true)) return;

        Location loc = event.getBlock().getLocation();
        long now = System.currentTimeMillis();
        
        if (now - lastReset.getOrDefault(loc, 0L) > 1000) {
            trackMap.put(loc, 0);
            lastReset.put(loc, now);
        }

        int count = trackMap.getOrDefault(loc, 0) + 1;
        trackMap.put(loc, count);

        int maxToggles = plugin.getConfig().getInt("redstone-protection.max-toggles-per-second", 40);

        if (count > maxToggles) {
            event.setNewCurrent(event.getOldCurrent());
            
            if (count == maxToggles + 1) {
                plugin.getAuditLogger().log(null, "REDSTONE_LAG", 
                    "Machine detected at: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                
                plugin.getDiscordWebhook().sendSimpleAlert("Redstone Lag Machine", 
                    "Detected at: " + loc.getWorld().getName() + " " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
            }
        }
    }
}