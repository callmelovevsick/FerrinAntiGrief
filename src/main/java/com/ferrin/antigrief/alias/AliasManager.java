package com.ferrin.antigrief.alias;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

public class AliasManager {

    private final FerrinAntiGrief plugin;
    private final File aliasFile;
    private final Map<String, String> originalToAlias = new HashMap<>();
    private final Map<String, String> aliasToOriginal = new HashMap<>();
    private final SecureRandom random = new SecureRandom();
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    public AliasManager(FerrinAntiGrief plugin) {
        this.plugin = plugin;
        this.aliasFile = new File(plugin.getDataFolder(), "runtime-alias.yml");
    }

    public void startMutationTask() {
        if (!plugin.getConfig().getBoolean("alias-mutation.enabled")) return;
        
        long hours = plugin.getConfig().getLong("alias-mutation.rotate-hours", 12);
        long ticks = 20L * 60 * 60 * hours;

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            plugin.getLogger().info("Rotating command aliases (Scheduled Mutation)...");
            loadAndSync();
        }, ticks, ticks);
    }

    public void triggerEmergencyMutation() {
        if (plugin.getConfig().getBoolean("alias-mutation.rotate-on-attack")) {
            loadAndSync();
        }
    }

    public void loadAndSync() {
        originalToAlias.clear();
        aliasToOriginal.clear();

        List<String> commandsToObfuscate = plugin.getConfig().getStringList("obfuscated-commands");
        YamlConfiguration config = new YamlConfiguration();

        for (String cmd : commandsToObfuscate) {
            String alias = generateRandomAlias();
            originalToAlias.put(cmd.toLowerCase(), alias);
            aliasToOriginal.put(alias, cmd.toLowerCase());
            config.set(cmd.toLowerCase(), alias);
        }

        try {
            config.save(aliasFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save runtime-alias.yml!");
        }

        plugin.getDiscordWebhook().sendUpdate(originalToAlias);
    }

    private String generateRandomAlias() {
        while (true) {
            int length = 8 + random.nextInt(7);
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
            }
            String alias = sb.toString();
            if (!aliasToOriginal.containsKey(alias) && plugin.getServer().getCommandMap().getCommand(alias) == null) {
                return alias;
            }
        }
    }

    public String getAlias(String original) { return originalToAlias.get(original.toLowerCase()); }
    public String getOriginal(String alias) { return aliasToOriginal.get(alias.toLowerCase()); }
    public boolean isAlias(String alias) { return aliasToOriginal.containsKey(alias.toLowerCase()); }
    public boolean isProtected(String cmd) { return originalToAlias.containsKey(cmd.toLowerCase()); }
    public Map<String, String> getMappings() { return Collections.unmodifiableMap(originalToAlias); }
}