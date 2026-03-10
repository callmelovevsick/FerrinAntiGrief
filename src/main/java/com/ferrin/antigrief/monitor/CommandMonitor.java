package com.ferrin.antigrief.monitor;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class CommandMonitor {
    private final FerrinAntiGrief plugin;
    private final HashMap<UUID, Long> lastCommandTime = new HashMap<>();
    private final HashMap<UUID, Integer> commandCount = new HashMap<>();

    public CommandMonitor(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    public boolean isSpamming(Player player) {
        UUID id = player.getUniqueId();
        long now = System.currentTimeMillis();
        
        long last = lastCommandTime.getOrDefault(id, 0L);
        if (now - last < (1000 / plugin.getConfig().getInt("command-spam.rate-limit-per-second"))) {
            return true;
        }
        lastCommandTime.put(id, now);
        return false;
    }

    public boolean checkHoneytrap(Player player, String cmd) {
        if (plugin.getConfig().getStringList("honeytrap.commands").contains(cmd.toLowerCase())) {
            plugin.getRiskManager().addScore(player, 30, "HONEYTRAP_TRIGGERED: " + cmd);
            return true;
        }
        return false;
    }
}