package com.inferni.giftcard;

import com.inferni.giftcard.Commands.CommandHandler;
import com.inferni.giftcard.Util.ConfigManager;
import com.inferni.giftcard.Util.DataInterface;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class GiftCard extends JavaPlugin {
    public ConfigManager configFile;
    public static FileConfiguration config;
    public ConfigManager dataFile;
    public static FileConfiguration data;
    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("giftcard").setExecutor(new CommandHandler());

        configFile = new ConfigManager("config.yml",this);
        configFile.getConfig().options().copyDefaults(true);
        configFile.saveConfigOnEnable();
        config = configFile.getConfig();

        dataFile = new ConfigManager("data.yml",this);
        dataFile.getConfig().options().copyDefaults(true);
        dataFile.saveConfigOnEnable();
        data = dataFile.getConfig();


        if (!new File(getDataFolder().getPath()+"\\templates", "email-template.ftl").exists()) {
            saveResource("templates/email-template.ftl", false);
        }

    }

    private static GiftCard instance;

    public GiftCard() {
        instance = this;
    }

    public static GiftCard getInstance() {
        if (instance == null) {
            instance = new GiftCard();
        }
        return instance;
    }
}
