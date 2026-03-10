package com.ferrin.antigrief.webhook;

import com.ferrin.antigrief.FerrinAntiGrief;
import org.bukkit.Bukkit;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class DiscordWebhook {

    private final FerrinAntiGrief plugin;

    public DiscordWebhook(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    // Method quan trọng để fix lỗi biên dịch
    public void sendSimpleAlert(String title, String description) {
        String url = plugin.getConfig().getString("discord.webhook-url");
        if (url == null || url.isEmpty() || url.equals("YOUR_WEBHOOK_HERE")) return;

        String json = "{\"embeds\": [{\"title\": \"" + title + "\", \"description\": \"" + description + "\", \"color\": 16711680}]}";
        sendAsync(url, json);
    }

    public void sendUpdate(Map<String, String> mappings) {
        // Code gửi danh sách alias (tùy chọn)
        sendSimpleAlert("Alias Rotation", "Hệ thống vừa cập nhật danh sách lệnh giả mới.");
    }

    private void sendAsync(String webhookUrl, String jsonPayload) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(webhookUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                con.getResponseCode();
                con.disconnect();
            } catch (Exception e) {
                plugin.getLogger().warning("Không thể gửi thông báo đến Discord: " + e.getMessage());
            }
        });
    }
}