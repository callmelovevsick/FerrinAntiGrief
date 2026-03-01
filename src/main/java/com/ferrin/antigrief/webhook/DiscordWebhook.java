package com.ferrin.antigrief.webhook;

import com.ferrin.antigrief.FerrinAntiGrief;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class DiscordWebhook {

    private final FerrinAntiGrief plugin;
    private final HttpClient client = HttpClient.newHttpClient();

    public DiscordWebhook(FerrinAntiGrief plugin) {
        this.plugin = plugin;
    }

    public void sendUpdate(Map<String, String> mappings) {
        if (!plugin.getConfig().getBoolean("webhook.enabled")) return;

        String url = plugin.getConfig().getString("webhook.url");
        if (url == null || url.isEmpty()) return;

        StringBuilder content = new StringBuilder("**FerrinAntiGrief Aliases Updated**\\n\\n");
        mappings.forEach((k, v) -> content.append("`").append(k).append("` -> `").append(v).append("`\\n"));

        String json = "{\"content\": \"" + content.toString() + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .exceptionally(ex -> {
                    plugin.getLogger().warning("Webhook failed: " + ex.getMessage());
                    return null;
                });
    }
}