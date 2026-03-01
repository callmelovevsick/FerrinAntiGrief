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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().substring(1); 
        String[] args = message.split(" ");
        String root = args[0].toLowerCase();

        if (plugin.getAliasManager().isProtected(root)) {
            event.setCancelled(true);
            plugin.getAuditLogger().log(player, message, "BLOCKED (ORIGINAL)");
            player.sendMessage(ColorUtil.colorize("&cUnknown command. Proteced by lovevsick"));
            return;
        }

        if (plugin.getAliasManager().isAlias(root)) {
            String original = plugin.getAliasManager().getOriginal(root);
            
            if (!player.hasPermission("ferrin.use.alias." + original)) {
                event.setCancelled(true);
                player.sendMessage(ColorUtil.colorize("&cYou do not have permission to use this command."));
                return;
            }

            if (!player.hasPermission("ferrin.bypass.execute") && plugin.getExecuteProtector().isRecursive(message)) {
                event.setCancelled(true);
                plugin.getAuditLogger().log(player, message, "BLOCKED (EXECUTE_LOOP)");
                player.sendMessage(ColorUtil.colorize("&cRecursive execute commands are forbidden."));
                return;
            }

            if (plugin.getConfirmManager().needsConfirmation(original)) {
                if (!plugin.getConfirmManager().isConfirmed(player, message)) {
                    event.setCancelled(true);
                    plugin.getConfirmManager().addPending(player, message);
                    plugin.getAuditLogger().log(player, message, "DOUBLE_CONFIRM_REQUIRED");
                    player.sendMessage(ColorUtil.colorize("&6[Warning] &eThis is a sensitive command."));
                    player.sendMessage(ColorUtil.colorize("&eType &b/" + root + " " + (message.contains(" ") ? message.substring(message.indexOf(" ") + 1) : "") + " confirm &eto proceed."));
                    return;
                }
            }

            if (!player.hasPermission("ferrin.bypass.nbt") && (original.equals("give") || original.equals("item") || original.equals("data"))) {
                if (message.length() > 500) { 
                     
                }
            }

            event.setCancelled(true);
            String realCmd = original + (message.contains(" ") ? message.substring(message.indexOf(" ")) : "");
            plugin.getServer().dispatchCommand(player, realCmd.replace(" confirm", ""));
            plugin.getAuditLogger().log(player, message, "SUCCESS");
        }
    }
}