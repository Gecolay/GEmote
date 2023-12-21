package dev.geco.gemote;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.*;

import dev.geco.gemote.api.event.*;
import dev.geco.gemote.cmd.*;
import dev.geco.gemote.cmd.tab.*;
import dev.geco.gemote.events.*;
import dev.geco.gemote.link.*;
import dev.geco.gemote.manager.*;
import dev.geco.gemote.manager.mm.*;
import dev.geco.gemote.util.*;

public class GEmoteMain extends JavaPlugin {

    private SVManager svManager;
    public SVManager getSVManager() { return svManager; }

    private CManager cManager;
    public CManager getCManager() { return cManager; }

    private DManager dManager;
    public DManager getDManager() { return dManager; }

    private EmoteManager emoteManager;
    public EmoteManager getEmoteManager() { return emoteManager; }

    private UManager uManager;
    public UManager getUManager() { return uManager; }

    private PManager pManager;
    public PManager getPManager() { return pManager; }

    private TManager tManager;
    public TManager getTManager() { return tManager; }

    private MManager mManager;
    public MManager getMManager() { return mManager; }

    private EmoteUtil emoteUtil;
    public EmoteUtil getEmoteUtil() { return emoteUtil; }

    private EnvironmentUtil environmentUtil;
    public EnvironmentUtil getEnvironmentUtil() { return environmentUtil; }

    private PlaceholderAPILink placeholderAPILink;
    public PlaceholderAPILink getPlaceholderAPILink() { return placeholderAPILink; }

    private WorldGuardLink worldGuardLink;
    public WorldGuardLink getWorldGuardLink() { return worldGuardLink; }

    private boolean spigotBased = false;
    public boolean isSpigotBased() { return spigotBased; }

    private boolean basicPaperBased = false;
    public boolean isBasicPaperBased() { return basicPaperBased; }

    private boolean paperBased = false;
    public boolean isPaperBased() { return paperBased; }

    public final String NAME = "GEmote";

    public final String RESOURCE = "114073";

    private static GEmoteMain GPM;

    public static GEmoteMain getInstance() { return GPM; }

    private void loadSettings(CommandSender Sender) {

        if(!connectDatabase(Sender)) return;

        getEmoteManager().createTable();
        getEmoteManager().reloadEmotes();
    }

    private void linkBStats() {

        BStatsLink bstats = new BStatsLink(getInstance(), 20531);

        bstats.addCustomChart(new BStatsLink.SimplePie("plugin_language", () -> getCManager().L_LANG));
        bstats.addCustomChart(new BStatsLink.SingleLineChart("use_emote_feature", () -> getEmoteManager().getEmoteUsedCount()));
        bstats.addCustomChart(new BStatsLink.SingleLineChart("seconds_emote_feature", () -> (int) getEmoteManager().getEmoteUsedSeconds()));

        getEmoteManager().resetFeatureUsedCount();
    }

    public void onLoad() {

        GPM = this;

        svManager = new SVManager(getInstance());
        cManager = new CManager(getInstance());
        dManager = new DManager(getInstance());
        uManager = new UManager(getInstance());
        pManager = new PManager(getInstance());
        tManager = new TManager(getInstance());
        emoteManager = new EmoteManager(getInstance());

        emoteUtil = new EmoteUtil();
        environmentUtil = new EnvironmentUtil(getInstance());

        preloadPluginDependencies();

        mManager = isBasicPaperBased() && getSVManager().isNewerOrVersion(18, 2) ? new MPaperManager(getInstance()) : new MSpigotManager(getInstance());
    }

    public void onEnable() {

        if(!versionCheck()) return;

        loadSettings(Bukkit.getConsoleSender());

        setupCommands();
        setupEvents();
        linkBStats();

        getMManager().sendMessage(Bukkit.getConsoleSender(), "Plugin.plugin-enabled");

        loadPluginDependencies(Bukkit.getConsoleSender());
        getUManager().checkForUpdates();
    }

    public void onDisable() {

        unload();
        getMManager().sendMessage(Bukkit.getConsoleSender(), "Plugin.plugin-disabled");
    }

    private void unload() {

        getEmoteManager().clearEmotes();
        getDManager().close();

        if(getPlaceholderAPILink() != null) getPlaceholderAPILink().unregister();
    }

    private void setupCommands() {

        getCommand("gemote").setExecutor(new GEmoteCommand(getInstance()));
        getCommand("gemote").setTabCompleter(new GEmoteTabComplete(getInstance()));
        getCommand("gemote").setPermissionMessage(getMManager().getMessage("Messages.command-permission-error"));
        getCommand("gemotereload").setExecutor(new GEmoteReloadCommand(getInstance()));
        getCommand("gemotereload").setTabCompleter(new EmptyTabComplete());
        getCommand("gemotereload").setPermissionMessage(getMManager().getMessage("Messages.command-permission-error"));
    }

    private void setupEvents() {

        getServer().getPluginManager().registerEvents(new PlayerEvents(getInstance()), getInstance());
    }

    private void preloadPluginDependencies() {

        try {
            Class.forName("org.spigotmc.event.entity.EntityDismountEvent");
            spigotBased = true;
        } catch (ClassNotFoundException ignored) { }

        try {
            Class.forName("io.papermc.paper.event.entity.EntityMoveEvent");
            basicPaperBased = true;
        } catch (ClassNotFoundException ignored) { }

        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler");
            paperBased = true;
        } catch (ClassNotFoundException ignored) { }

        if(Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            worldGuardLink = new WorldGuardLink(getInstance());
            getWorldGuardLink().registerFlags();
        }
    }

    private void loadPluginDependencies(CommandSender Sender) {

        Plugin plugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");

        if(plugin != null && plugin.isEnabled()) {
            placeholderAPILink = new PlaceholderAPILink(getInstance());
            getMManager().sendMessage(Sender, "Plugin.plugin-link", "%Link%", plugin.getName());
            getPlaceholderAPILink().register();
        } else placeholderAPILink = null;

        plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");

        if(plugin != null && plugin.isEnabled()) {
            if(worldGuardLink == null) {
                worldGuardLink = new WorldGuardLink(getInstance());
                getWorldGuardLink().registerFlags();
            }
            getMManager().sendMessage(Sender, "Plugin.plugin-link", "%Link%", plugin.getName());
        } else worldGuardLink = null;
    }

    public void reload(CommandSender Sender) {

        Bukkit.getPluginManager().callEvent(new GEmoteReloadEvent(getInstance()));

        getCManager().reload();
        getMManager().loadMessages();

        unload();

        loadSettings(Sender);
        loadPluginDependencies(Sender);
        getUManager().checkForUpdates();
    }

    private boolean connectDatabase(CommandSender Sender) {

        boolean connect = getDManager().connect();

        if(connect) return true;

        getMManager().sendMessage(Sender, "Plugin.plugin-data");

        Bukkit.getPluginManager().disablePlugin(getInstance());

        return false;
    }

    private boolean versionCheck() {

        if(!getSVManager().isNewerOrVersion(13, 0)) {

            String version = Bukkit.getServer().getClass().getPackage().getName();

            getMManager().sendMessage(Bukkit.getConsoleSender(), "Plugin.plugin-version", "%Version%", version.substring(version.lastIndexOf('.') + 1));

            getUManager().checkForUpdates();

            Bukkit.getPluginManager().disablePlugin(getInstance());

            return false;
        }

        return true;
    }

}