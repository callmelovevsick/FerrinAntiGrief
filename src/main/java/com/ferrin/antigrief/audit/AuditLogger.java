package com.ferrin.antigrief.audit;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuditLogger {

    private final FerrinAntiGrief plugin;
    private final File logFile;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AuditLogger(FerrinAntiGrief plugin) {
        this.plugin = plugin;
        this.logFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("audit.file-name", "admin-audit.log"));
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void log(Player player, String command, String result) {
        String timestamp = LocalDateTime.now().format(formatter);
        String name = player.getName();
        String uuid = player.getUniqueId().toString();
        String ip = player.getAddress() != null ? player.getAddress().getAddress().getHostAddress() : "N/A";

        String entry = String.format("[%s] %s (%s) IP: %s | CMD: %s | RESULT: %s", 
                timestamp, name, uuid, ip, command, result);

        executor.submit(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                writer.write(entry);
                writer.newLine();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to write to audit log!");
            }
        });

        if (plugin.getConfig().getBoolean("audit.log-to-console")) {
            plugin.getLogger().info("[AUDIT] " + entry);
        }
    }

    public void close() {
        executor.shutdown();
    }
}