package com.ferrin.antigrief.listener;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEditBookEvent;

public class InventoryListener implements Listener {

    private final FerrinAntiGrief plugin;

    public InventoryListener(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        //null - removed
    }

    @EventHandler
    public void onBookEdit(PlayerEditBookEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("ferrin.bypass.nbt")) return;

        int maxPages = plugin.getConfig().getInt("protection.nbt.max-book-pages", 50);
        if (event.getNewBookMeta().getPageCount() > maxPages) {
            event.setCancelled(true);
            player.sendMessage(ColorUtil.colorize("&c[Ferrin] Cuốn sách này quá dài (&7>" + maxPages + " trang&c)!"));
        }
    }
}