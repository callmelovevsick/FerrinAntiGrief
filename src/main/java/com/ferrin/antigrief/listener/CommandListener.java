package com.ferrin.antigrief.listener;

import com.ferrin.antigrief.FerrinAntiGrief;
import com.ferrin.antigrief.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {

    private final FerrinAntiGrief plugin;

    public CommandListener(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().substring(1);
        String[] args = message.split(" ");
        String root = args[0].toLowerCase();

        // 1. Rate Limit & Spam
        if (plugin.getCommandMonitor().isSpamming(player)) {
            event.setCancelled(true);
            player.sendMessage(ColorUtil.colorize("&cPlease slow down!"));
            return;
        }

        // 2. Honeytrap check
        if (plugin.getCommandMonitor().checkHoneytrap(player, root)) {
            event.setCancelled(true);
            return;
        }

        // 3. Lockdown check
        if (plugin.getLockdownManager().shouldBlockCommand(root) && !player.hasPermission("ferrin.admin.lockdown")) {
            event.setCancelled(true);
            player.sendMessage(ColorUtil.colorize("&4&l[!] Server is in Lockdown mode."));
            return;
        }

        // 4. Alias Handling
        if (plugin.getAliasManager().isAlias(root)) {
            String original = plugin.getAliasManager().getOriginal(root);
            if (!player.hasPermission("ferrin.use.alias." + original)) {
                event.setCancelled(true);
                player.sendMessage(ColorUtil.colorize("&cNo permission."));
                return;
            }

            // Double Confirm
            if (plugin.getConfirmManager().needsConfirmation(original)) {
                if (!plugin.getConfirmManager().isConfirmed(player, message)) {
                    event.setCancelled(true);
                    plugin.getConfirmManager().addPending(player, message);
                    player.sendMessage(ColorUtil.colorize("&6Confirm by typing: &b/" + root + " confirm"));
                    return;
                }
            }

            // Dispatch Original
            event.setCancelled(true);
            String realCmd = original + (message.contains(" ") ? message.substring(message.indexOf(" ")) : "");
            plugin.getServer().dispatchCommand(player, realCmd.replace(" confirm", ""));
        } else if (plugin.getAliasManager().isProtected(root)) {
            // Block direct original command access
            event.setCancelled(true);
            player.sendMessage(ColorUtil.colorize("&cUnknown command."));
        }
    }
}