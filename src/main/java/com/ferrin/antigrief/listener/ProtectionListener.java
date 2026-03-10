package com.ferrin.antigrief.listener;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ProtectionListener implements Listener {

    private final FerrinAntiGrief plugin;

    public ProtectionListener(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTNTPlace(BlockPlaceEvent event) {
        if (plugin.getLockdownManager().isLockdownEnabled() && event.getBlock().getType() == Material.TNT) {
            if (!event.getPlayer().hasPermission("ferrin.admin.lockdown")) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ColorUtil.colorize("&c[Lockdown] Bạn không thể đặt TNT trong lúc này!"));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (plugin.getLockdownManager().isLockdownEnabled()) {
            EntityType type = event.getEntityType();
            if (type == EntityType.WITHER || type == EntityType.ENDER_DRAGON || type == EntityType.TNT_MINECART) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (plugin.getLockdownManager().isPlayerFrozen()) {
            if (event.getPlayer().hasPermission("ferrin.admin.lockdown")) return;
            
            if (event.getFrom().getX() != event.getTo().getX() || 
                event.getFrom().getZ() != event.getTo().getZ()) {
                event.setTo(event.getFrom());
            }
        }
    }
}