package com.ferrin.antigrief.listener;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class TabCompleteListener implements Listener {

    private final FerrinAntiGrief plugin;

    public TabCompleteListener(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTabComplete(AsyncTabCompleteEvent event) {
        String buffer = event.getBuffer();
        if (!buffer.startsWith("/")) return;

        List<String> completions = new ArrayList<>(event.getCompletions());
        
        completions.removeIf(completion -> plugin.getAliasManager().isProtected(completion));

        if (event.getSender() instanceof org.bukkit.entity.Player player) {
            completions.removeIf(completion -> {
                if (plugin.getAliasManager().isAlias(completion)) {
                    String original = plugin.getAliasManager().getOriginal(completion);
                    return !player.hasPermission("ferrin.use.alias." + original);
                }
                return false;
            });
        }

        event.setCompletions(completions);
    }
}