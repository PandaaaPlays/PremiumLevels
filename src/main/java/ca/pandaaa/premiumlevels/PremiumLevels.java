package ca.pandaaa.premiumlevels;

import ca.pandaaa.premiumlevels.commands.Commands;
import ca.pandaaa.premiumlevels.commands.TabCompletion;
import ca.pandaaa.premiumlevels.level.LevelManager;
import ca.pandaaa.premiumlevels.level.PlayerLevel;
import ca.pandaaa.premiumlevels.utils.ConfigManager;
import ca.pandaaa.premiumlevels.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class PremiumLevels extends JavaPlugin {
    private File levelsFile;
    private FileConfiguration levelsConfig;
    private File playerDataFile;
    private FileConfiguration playerDataConfig;
    private static PremiumLevels plugin;
    private ConfigManager configManager;
    private LevelManager levelManager;

    public static PremiumLevels getPlugin() {
        return plugin;
    }

    public PlayerLevel getPlayerLevel(Player player) {
        return levelManager.getPlayerLevel(player);
    }

    @Override
    public void onEnable() {
        plugin = this;
        int pluginId = 21640;
        new Metrics(this, pluginId);

        saveDefaultConfigurations();
        loadConfigurations();

        configManager = new ConfigManager(getConfig(), levelsConfig, playerDataConfig);
        levelManager = new LevelManager(configManager);

        for(Player player : Bukkit.getOnlinePlayers()) {
            levelManager.restorePlayer(player);
        }

        getCommandsAndListeners();

        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "   &2_____  &a__"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|  _  |&a|  |      &2Premium&aLevels"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|   __|&a|  |__    &7Version " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &2|__|   &a|_____|     &7by &8Pa&7nd&5aaa"));
        getServer().getConsoleSender().sendMessage("");

        boolean testing = true;
        if(testing) {
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + getDescription().getName() + " : This version of the plugin is meant for testing / may have bugs."));
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease try to update to the next fully working version as soon as possible."));
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlease contact Pandaaa on discord if you believe this is an error."));
        }
    }

    @Override
    public void onDisable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            levelManager.savePlayer(player);
        }
        savePlayerData();
    }

    private void saveDefaultConfigurations() {
        this.saveDefaultConfig();
        levelsFile = new File(getDataFolder(), "levels.yml");
        if (!levelsFile.exists())
            saveResource("levels.yml", false);
        levelsConfig = new YamlConfiguration();

        playerDataFile = new File(getDataFolder(), "player-data.yml");
        if (!playerDataFile.exists())
            saveResource("player-data.yml", false);
        playerDataConfig = new YamlConfiguration();
    }

    public void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        levelsConfig = YamlConfiguration.loadConfiguration(levelsFile);
        playerDataConfig = YamlConfiguration.loadConfiguration(playerDataFile);

        configManager = new ConfigManager(getConfig(), levelsConfig, playerDataConfig);
        levelManager = new LevelManager(configManager);
        getCommandsAndListeners();

        sender.sendMessage(configManager.getPluginReloadMessage());
    }

    private void loadConfigurations() {
        try {
            levelsConfig.load(levelsFile);
        } catch (IOException | InvalidConfigurationException exception) {
            System.out.println(exception);
        }

        try {
            playerDataConfig.load(playerDataFile);
        } catch (IOException | InvalidConfigurationException exception) {
            System.out.println(exception);
        }
    }

    public void savePlayerData() {
        try {
            playerDataConfig.save(playerDataFile);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void getCommandsAndListeners() {
        PluginCommand command = getCommand("PremiumLevels");
        if(command == null)
            return;

        command.setExecutor(new Commands(configManager));
        command.setTabCompleter(new TabCompletion());
        getServer().getPluginManager().registerEvents(levelManager, this);
    }
}