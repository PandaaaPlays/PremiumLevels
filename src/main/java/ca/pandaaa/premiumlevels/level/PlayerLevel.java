package ca.pandaaa.premiumlevels.level;

import ca.pandaaa.premiumlevels.utils.ConfigManager;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerLevel {
    private final ConfigManager configManager;
    private final Player player;
    private int level;
    private double experience;

    public PlayerLevel(ConfigManager configManager, Player player, double experience, int level) {
        this.configManager = configManager;
        this.player = player;
        this.experience = experience;
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public void addExperience(double amount) {
        this.addExperience(amount, false, false);
    }

    public void addExperience(double amount, boolean forceSilent, boolean forceReset) {
        if(forceReset)
            experience = amount;
        else
            experience += amount;

        double experienceNeededForNextLevel = configManager.getMaxExperienceForLevel(level);
        double remainingExperienceAfterLevelUp = experience - experienceNeededForNextLevel;

        if(!configManager.isExperienceNotificationSilent() && !forceSilent)
            Objects.requireNonNull(player.getPlayer()).sendMessage(configManager.getExperienceNotification(amount));

        if (remainingExperienceAfterLevelUp >= 0 && level < configManager.getMaxLevel()) {
            this.addLevel(1);
            if (!configManager.isLevelNotificationSilent())
                Objects.requireNonNull(player.getPlayer()).sendMessage(configManager.getLevelNotification(level));
            addExperience(remainingExperienceAfterLevelUp, true, true);
        }
    }

    public void setExperience(double amount) {
        this.setExperience(amount, true);
    }

    public void setExperience(double amount, boolean shouldUpdateAutomatically) {
        experience = amount;
        if(shouldUpdateAutomatically)
            this.addExperience(0, true, false);
    }

    public void addLevel(int amount) {
        level += amount;
    }

    public void setLevel(int amount) {
        level = amount;
    }
}
