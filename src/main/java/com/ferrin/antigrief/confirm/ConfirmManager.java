package com.ferrin.antigrief.confirm;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ConfirmManager {

    private final FerrinAntiGrief plugin;
    private final Map<UUID, PendingCommand> pending = new HashMap<>();
    private final List<String> confirmRequired;

    public ConfirmManager(FerrinAntiGrief plugin) {
        this.plugin = plugin;
        this.confirmRequired = plugin.getConfig().getStringList("confirm-required");
    }

    public boolean needsConfirmation(String originalCommand) {
        return confirmRequired.contains(originalCommand.toLowerCase());
    }

    public void addPending(Player player, String commandLine) {
        pending.put(player.getUniqueId(), new PendingCommand(commandLine, System.currentTimeMillis()));
    }

    public boolean isConfirmed(Player player, String commandLine) {
        UUID uuid = player.getUniqueId();
        if (!pending.containsKey(uuid)) return false;

        PendingCommand pc = pending.get(uuid);
        long timeout = plugin.getConfig().getLong("double-confirm.timeout-seconds", 15) * 1000;

        if (System.currentTimeMillis() - pc.timestamp > timeout) {
            pending.remove(uuid);
            return false;
        }

        boolean match = commandLine.equalsIgnoreCase(pc.commandLine + " confirm");
        if (match) {
            pending.remove(uuid);
        }
        return match;
    }

    private record PendingCommand(String commandLine, long timestamp) {}
}