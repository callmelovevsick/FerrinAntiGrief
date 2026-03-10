package com.ferrin.antigrief.command;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class LockdownCommand implements CommandExecutor {

    private final FerrinAntiGrief plugin;

    public LockdownCommand(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("ferrin.admin.lockdown")) {
            sender.sendMessage(ColorUtil.colorize("&cBạn không có quyền quản lý Lockdown."));
            return true;
        }

        boolean newState = !plugin.getLockdownManager().isLockdownEnabled();
        plugin.getLockdownManager().setLockdown(newState);

        if (newState) {
            Bukkit.broadcast(ColorUtil.colorize("&4&l[!] SERVER ĐÃ BẬT CHẾ ĐỘ PHONG TỎA (LOCKDOWN)."));
            plugin.getDiscordWebhook().sendSimpleAlert("Emergency Lockdown", "Lockdown ENABLED by " + sender.getName());
        } else {
            Bukkit.broadcast(ColorUtil.colorize("&a&l[!] Chế độ phong tỏa đã được gỡ bỏ."));
            plugin.getDiscordWebhook().sendSimpleAlert("Lockdown Lifted", "Lockdown DISABLED by " + sender.getName());
        }

        return true;
    }
}