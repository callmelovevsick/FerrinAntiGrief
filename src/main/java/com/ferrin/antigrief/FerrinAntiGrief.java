package com.ferrin.antigrief;

import com.ferrin.antigrief.alias.AliasManager;
import com.ferrin.antigrief.audit.AuditLogger;
import com.ferrin.antigrief.command.ReloadCommand;
import com.ferrin.antigrief.command.StatusCommand;
import com.ferrin.antigrief.command.LockdownCommand;
import com.ferrin.antigrief.confirm.ConfirmManager;
import com.ferrin.antigrief.risk.RiskManager;
import com.ferrin.antigrief.monitor.HealthMonitor;
import com.ferrin.antigrief.monitor.CommandMonitor;
import com.ferrin.antigrief.lockdown.LockdownManager;
import com.ferrin.antigrief.lag.EntityLagDetector;
import com.ferrin.antigrief.lag.RedstoneDetector;
import com.ferrin.antigrief.lag.DupeDetector;
import com.ferrin.antigrief.listener.*;
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

    private RiskManager riskManager;
    private HealthMonitor healthMonitor;
    private CommandMonitor commandMonitor;
    private LockdownManager lockdownManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.auditLogger = new AuditLogger(this);
        this.discordWebhook = new DiscordWebhook(this);
        this.aliasManager = new AliasManager(this);
        this.confirmManager = new ConfirmManager(this);
        this.executeProtector = new ExecuteProtector(this);
        
        this.riskManager = new RiskManager(this);
        this.healthMonitor = new HealthMonitor();
        this.commandMonitor = new CommandMonitor(this);
        this.lockdownManager = new LockdownManager(this);

        new EntityLagDetector(this); 
        aliasManager.startMutationTask(); 

        registerCommands();

        registerListeners();

        getLogger().info("========================================");
        getLogger().info("   FerrinAntiGrief v2.0 - LOADED       ");
        getLogger().info("   Status: High Protection Active      ");
        getLogger().info("========================================");
    }

    @Override
    public void onDisable() {
        if (auditLogger != null) auditLogger.close();
        getLogger().info("FerrinAntiGrief has been safely disabled.");
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("ferrinreload")).setExecutor(new ReloadCommand(this));
        Objects.requireNonNull(getCommand("ferrinstatus")).setExecutor(new StatusCommand(this));
        Objects.requireNonNull(getCommand("lockdown")).setExecutor(new LockdownCommand(this));
    }

    private void registerListeners() {
        var pm = getServer().getPluginManager();

        pm.registerEvents(new CommandListener(this), this);
        pm.registerEvents(new InventoryListener(this), this);
        pm.registerEvents(new TabCompleteListener(this), this);

        pm.registerEvents(new RedstoneDetector(this), this); 
        pm.registerEvents(new DupeDetector(this), this); 
        pm.registerEvents(new ProtectionListener(this), this); 
    }

    public void reloadSystems() {
        reloadConfig();
        aliasManager.loadAndSync();
        getLogger().info("FerrinAntiGrief configurations reloaded.");
    }

    public AliasManager getAliasManager() { return aliasManager; }
    public AuditLogger getAuditLogger() { return auditLogger; }
    public ConfirmManager getConfirmManager() { return confirmManager; }
    public DiscordWebhook getDiscordWebhook() { return discordWebhook; }
    public ExecuteProtector getExecuteProtector() { return executeProtector; }
    public RiskManager getRiskManager() { return riskManager; }
    public HealthMonitor getHealthMonitor() { return healthMonitor; }
    public CommandMonitor getCommandMonitor() { return commandMonitor; }
    public LockdownManager getLockdownManager() { return lockdownManager; }
}