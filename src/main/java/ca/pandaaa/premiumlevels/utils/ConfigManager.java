package ca.pandaaa.premiumlevels.utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class ConfigManager {
    private final FileConfiguration configuration;
    private final FileConfiguration levels;
    private final FileConfiguration playerData;

    public ConfigManager(FileConfiguration configuration, FileConfiguration levels, FileConfiguration playerData) {
        this.configuration = configuration;
        this.levels = levels;
        this.playerData = playerData;
    }

    public boolean isExperienceNotificationSilent() {
        return !configuration.getBoolean("display-experience-notifications");
    }

    public boolean isLevelNotificationSilent() {
        return !configuration.getBoolean("display-level-notifications");
    }

    public String getUnknownCommandMessage() {
        return Utils.applyFormat(configuration.getString("unknown-command"));
    }

    public String getNoPermissionMessage() {
        return Utils.applyFormat(configuration.getString("no-permission"));
    }

    public String getPluginReloadMessage() {
        return Utils.applyFormat(configuration.getString("plugin-reload"));
    }

    public double getMaxExperienceForLevel(int level) {
        return levels.getDouble("levels." + level);
    }

    public String getExperienceNotification(double experience) {
        return Utils.applyFormat(configuration.getString("experience-notification")
                .replaceAll("%experience%", String.valueOf(experience)));
    }

    public String getLevelNotification(int level) {
        return Utils.applyFormat(configuration.getString("level-notification")
                .replaceAll("%level%", String.valueOf(level)));
    }

    public int getMaxLevel() {
        return configuration.getInt("maximum-level");
    }

    public double getPlayerExperience(UUID playerUUID) {
        return playerData.getDouble(playerUUID + ".experience");
    }

    public int getPlayerLevel(UUID playerUUID) {
        return playerData.getInt(playerUUID + ".level");
    }

    public void setPlayerExperience(UUID playerUUID, double amount) {
        playerData.set(playerUUID + ".experience", amount);
    }

    public void setPlayerLevel(UUID playerUUID, int amount) {
        playerData.set(playerUUID + ".level", amount);
    }

}