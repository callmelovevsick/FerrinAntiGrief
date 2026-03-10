package com.ferrin.antigrief.command;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StatusCommand implements CommandExecutor {
    private final FerrinAntiGrief plugin;

    public StatusCommand(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        var hm = plugin.getHealthMonitor();
        sender.sendMessage(ColorUtil.colorize("&b&lFerrin Server Status"));
        sender.sendMessage(ColorUtil.colorize("&fPlayers: &a" + plugin.getServer().getOnlinePlayers().size()));
        sender.sendMessage(ColorUtil.colorize("&fEntities: &a" + hm.getTotalEntities()));
        sender.sendMessage(ColorUtil.colorize("&fChunks: &a" + hm.getTotalChunks()));
        sender.sendMessage(ColorUtil.colorize("&fTPS: &a" + String.format("%.2f", hm.getTPS())));
        sender.sendMessage(ColorUtil.colorize("&fMemory: &a" + hm.getMemoryUsage()));
        return true;
    }
}