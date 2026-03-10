package com.ferrin.antigrief.lockdown;

import com.ferrin.antigrief.FerrinAntiGrief;
import java.util.HashSet;
import java.util.Set;

public class LockdownManager {

    private final FerrinAntiGrief plugin;
    private boolean enabled;
    private final Set<String> blockedCommands = new HashSet<>();

    public LockdownManager(FerrinAntiGrief plugin) {
        this.plugin = plugin;
        this.enabled = plugin.getConfig().getBoolean("lockdown.enabled", false);
        
        // Các lệnh bị chặn khi ở chế độ Lockdown
        blockedCommands.add("tnt");
        blockedCommands.add("summon");
        blockedCommands.add("give");
        blockedCommands.add("execute");
        blockedCommands.add("data");
    }

    public boolean isLockdownEnabled() {
        return enabled;
    }

    public void setLockdown(boolean state) {
        this.enabled = state;
        plugin.getConfig().set("lockdown.enabled", state);
        plugin.saveConfig();
    }

    public boolean shouldBlockCommand(String command) {
        if (!enabled) return false;
        String root = command.toLowerCase().split(" ")[0].replace("/", "");
        return blockedCommands.contains(root);
    }

    public boolean isPlayerFrozen() {
        return enabled && plugin.getConfig().getBoolean("lockdown.freeze-players", true);
    }
}