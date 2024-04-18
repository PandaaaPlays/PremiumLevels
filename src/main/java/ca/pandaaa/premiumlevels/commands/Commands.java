package ca.pandaaa.premiumlevels.commands;

import ca.pandaaa.premiumlevels.PremiumLevels;
import ca.pandaaa.premiumlevels.level.PlayerLevel;
import ca.pandaaa.premiumlevels.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final ConfigManager configManager;

    public Commands(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
        if (!(sender instanceof Player)
                && !(sender instanceof ConsoleCommandSender))
            return false;

        if (command.getName().equalsIgnoreCase("premiumlevels")) {
            if (args.length == 0) {
                sendUnknownCommandMessage(sender);
                return false;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                reloadPlugin(sender);
            } else {
                sendUnknownCommandMessage(sender);
            }
        }
        return false;
    }

    private void reloadPlugin(CommandSender sender) {
        if (!sender.hasPermission("premiumlevels.config")) {
            sendNoPermissionMessage(sender);
            return;
        }

        PremiumLevels.getPlugin().reloadConfig(sender);
    }

    private void sendUnknownCommandMessage(CommandSender sender) {
        sender.sendMessage(configManager.getUnknownCommandMessage());
    }

    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(configManager.getNoPermissionMessage());
    }
}