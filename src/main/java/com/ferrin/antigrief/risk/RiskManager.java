package com.ferrin.antigrief.risk;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RiskManager {
    private final FerrinAntiGrief plugin;
    private final ConcurrentHashMap<UUID, Integer> riskScores = new ConcurrentHashMap<>();

    public RiskManager(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    public void addScore(Player player, int points, String reason) {
        if (!plugin.getConfig().getBoolean("risk-system.enabled")) return;

        UUID uuid = player.getUniqueId();
        int newScore = riskScores.merge(uuid, points, Integer::sum);
        
        plugin.getAuditLogger().log(player, "RISK_INCREASE: " + reason, "SCORE: " + newScore);

        checkThresholds(player, newScore);
    }

    private void checkThresholds(Player player, int score) {
        int alert = plugin.getConfig().getInt("risk-system.alert-threshold");
        int restrict = plugin.getConfig().getInt("risk-system.restrict-threshold");
        int ban = plugin.getConfig().getInt("risk-system.auto-ban-threshold");

        if (score >= ban) {
            Bukkit.getScheduler().runTask(plugin, () -> 
                player.banPlayerIP("Automated Anti-Grief Ban: Risk Score " + score));
        } else if (score >= restrict) {
            player.sendMessage(ColorUtil.colorize("&c[Ferrin] Your actions are restricted due to high risk behavior."));
        } else if (score >= alert) {
            plugin.getDiscordWebhook().sendSimpleAlert("High Risk Detected", 
                "Player: " + player.getName() + "\nScore: " + score);
        }
    }

    public int getScore(UUID uuid) {
        return riskScores.getOrDefault(uuid, 0);
    }
    
    public void resetScore(UUID uuid) {
        riskScores.remove(uuid);
    }
}