package dev.geco.gemote.manager;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

import org.bukkit.configuration.file.*;

import dev.geco.gemote.GEmoteMain;

public class CManager {

    public String L_LANG;

    public boolean L_CLIENT_LANG;


    public boolean CHECK_FOR_UPDATE;

    public boolean RESTORE;

    public double MAX_DISTANCE;

    public List<String> WORLDBLACKLIST = new ArrayList<>();

    public List<String> WORLDWHITELIST = new ArrayList<>();


    private final GEmoteMain GPM;

    public CManager(GEmoteMain GPluginMain) {

        GPM = GPluginMain;

        if(GPM.getSVManager().isNewerOrVersion(18, 2)) {
            try {
                File configFile = new File(GPM.getDataFolder(), "config.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                InputStream configSteam = GPM.getResource("config.yml");
                if(configSteam != null) {
                    FileConfiguration configSteamConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(configSteam, StandardCharsets.UTF_8));
                    config.setDefaults(configSteamConfig);
                    YamlConfigurationOptions options = (YamlConfigurationOptions) config.options();
                    options.parseComments(true).copyDefaults(true).width(500);
                    config.loadFromString(config.saveToString());
                    for(String comments : config.getKeys(true)) {
                        config.setComments(comments, configSteamConfig.getComments(comments));
                    }
                }
                config.save(configFile);
            } catch (Throwable e) {
                GPM.saveDefaultConfig();
            }
        } else GPM.saveDefaultConfig();

        reload();
    }

    public void reload() {

        GPM.reloadConfig();

        L_LANG = GPM.getConfig().getString("Lang.lang", "en_us").toLowerCase();
        L_CLIENT_LANG = GPM.getConfig().getBoolean("Lang.client-lang", true);

        CHECK_FOR_UPDATE = GPM.getConfig().getBoolean("Options.check-for-update", true);
        RESTORE = GPM.getConfig().getBoolean("Options.restore", true);
        MAX_DISTANCE = GPM.getConfig().getDouble("Options.max-distance", 128d);
        WORLDBLACKLIST = GPM.getConfig().getStringList("Options.WorldBlacklist");
        WORLDWHITELIST = GPM.getConfig().getStringList("Options.WorldWhitelist");
    }

}
