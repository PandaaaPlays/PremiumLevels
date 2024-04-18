package ca.pandaaa.premiumlevels.level;

import ca.pandaaa.premiumlevels.PremiumLevels;
import ca.pandaaa.premiumlevels.utils.ConfigManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LevelManager implements Listener {

    PremiumLevels plugin = PremiumLevels.getPlugin();
    private final Map<UUID, PlayerLevel> playerLevelMap = new HashMap<>();
    private final ConfigManager configManager;

    public LevelManager(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.restorePlayer(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.savePlayer(player);
        this.plugin.savePlayerData();
    }

    public PlayerLevel getPlayerLevel(Player player) {
        return playerLevelMap.get(player.getUniqueId());
    }

    public void restorePlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        if(playerLevelMap.containsKey(playerUUID))
            return;

        double experience = configManager.getPlayerExperience(playerUUID);
        int level = configManager.getPlayerLevel(playerUUID);

        playerLevelMap.put(playerUUID, new PlayerLevel(
                configManager,
                player,
                experience,
                level == 0 ? 1 : level));
    }

    public void savePlayer(Player player) {
        UUID playerUUID = player.getUniqueId();
        configManager.setPlayerExperience(playerUUID, playerLevelMap.get(playerUUID).getExperience());
        configManager.setPlayerLevel(playerUUID, playerLevelMap.get(playerUUID).getLevel());

        playerLevelMap.remove(playerUUID);
    }
}
