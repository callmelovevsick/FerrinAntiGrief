package com.ferrin.antigrief.protection;

import com.ferrin.antigrief.FerrinAntiGrief;

public class ExecuteProtector {

    private final FerrinAntiGrief plugin;

    public ExecuteProtector(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    public boolean isRecursive(String command) {
        String lower = command.toLowerCase();
        int maxDepth = plugin.getConfig().getInt("protection.execute.max-depth");
        
        String[] parts = lower.split("\\s+");
        int count = 0;
        for (String part : parts) {
            if (part.equals("execute") || part.equals("run")) {
                count++;
            }
        }
        
        return (count / 2) > maxDepth;
    }
}