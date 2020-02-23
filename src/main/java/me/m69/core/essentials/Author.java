package me.m69.core.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Author implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.AQUA + "Plugin was made by " + ChatColor.WHITE + "m69");
        player.sendMessage(ChatColor.AQUA + "If you have any questions about the plugin");
        player.sendMessage(ChatColor.AQUA + "Please DM m69#1567");
        return false;
    }
}
