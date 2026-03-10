package com.ferrin.antigrief.lag;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

import java.util.Collection;

public class DupeDetector implements Listener {

    private final FerrinAntiGrief plugin;

    public DupeDetector(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (!plugin.getConfig().getBoolean("dupe-detector.enabled", true)) return;

        Item item = event.getEntity();
        int threshold = plugin.getConfig().getInt("dupe-detector.item-spike-threshold", 150);

        Collection<Entity> nearby = item.getWorld().getNearbyEntities(item.getLocation(), 5, 5, 5);
        long itemCount = nearby.stream().filter(e -> e instanceof Item).count();

        if (itemCount > threshold) {
            Player closestPlayer = null;
            double minDist = Double.MAX_VALUE;

            for (Entity e : nearby) {
                if (e instanceof Player p) {
                    double dist = p.getLocation().distanceSquared(item.getLocation());
                    if (dist < minDist) {
                        minDist = dist;
                        closestPlayer = p;
                    }
                }
            }

            if (closestPlayer != null) {
                plugin.getRiskManager().addScore(closestPlayer, 20, "POSSIBLE_DUPE_SPIKE");
                plugin.getAuditLogger().log(closestPlayer, "DUPE_DETECTOR", "Item spike detected (" + itemCount + " items)");
            }

            plugin.getDiscordWebhook().sendSimpleAlert("Item Dupe Warning", 
                "Large item spike (" + itemCount + " items) at " + 
                item.getLocation().getBlockX() + ", " + item.getLocation().getBlockZ());
        }
    }
}