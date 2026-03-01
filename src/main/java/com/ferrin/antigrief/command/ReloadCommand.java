package com.ferrin.antigrief.command;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final FerrinAntiGrief plugin;

    public ReloadCommand(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("ferrin.admin.reload")) {
            sender.sendMessage(ColorUtil.colorize("&cNo permission."));
            return true;
        }

        plugin.reloadSystems();
        sender.sendMessage(ColorUtil.colorize("&a[FerrinAntiGrief] Plugin reloaded and aliases regenerated."));
        sender.sendMessage(ColorUtil.colorize("&eCheck Discord or runtime-alias.yml for the new codes."));
        return true;
    }
}