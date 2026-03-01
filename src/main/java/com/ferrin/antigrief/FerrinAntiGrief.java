package com.ferrin.antigrief;

import com.ferrin.antigrief.alias.AliasManager;
import com.ferrin.antigrief.audit.AuditLogger;
import com.ferrin.antigrief.command.ReloadCommand;
import com.ferrin.antigrief.confirm.ConfirmManager;
import com.ferrin.antigrief.listener.CommandListener;
import com.ferrin.antigrief.listener.InventoryListener;
import com.ferrin.antigrief.listener.TabCompleteListener;
import com.ferrin.antigrief.protection.ExecuteProtector;
import com.ferrin.antigrief.webhook.DiscordWebhook;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class FerrinAntiGrief extends JavaPlugin {

    private AliasManager aliasManager;
    private AuditLogger auditLogger;
    private ConfirmManager confirmManager;
    private DiscordWebhook discordWebhook;
    private ExecuteProtector executeProtector;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.auditLogger = new AuditLogger(this);
        this.aliasManager = new AliasManager(this);
        this.confirmManager = new ConfirmManager(this);
        this.discordWebhook = new DiscordWebhook(this);
        this.executeProtector = new ExecuteProtector(this);

        aliasManager.loadAndSync();

        Objects.requireNonNull(getCommand("ferrinreload")).setExecutor(new ReloadCommand(this));

        getServer().getPluginManager().registerEvents(new CommandListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        getServer().getPluginManager().registerEvents(new TabCompleteListener(this), this);

        getLogger().info("FerrinAntiGrief đã sẵn sàng.");
    }

    @Override
    public void onDisable() {
        if (auditLogger != null) auditLogger.close();
    }

    public void reloadSystems() {
        reloadConfig();
        aliasManager.loadAndSync();
    }

    public AliasManager getAliasManager() { return aliasManager; }
    public AuditLogger getAuditLogger() { return auditLogger; }
    public ConfirmManager getConfirmManager() { return confirmManager; }
    public DiscordWebhook getDiscordWebhook() { return discordWebhook; }
    public ExecuteProtector getExecuteProtector() { return executeProtector; }
}