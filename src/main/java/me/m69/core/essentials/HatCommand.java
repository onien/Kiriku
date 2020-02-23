package me.m69.core.essentials;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HatCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args) {
        Player player = (Player) sender;
        if (sender.hasPermission("Kiriku.hat")) {
            player.getInventory().setHelmet(player.getItemInHand());
            player.sendMessage("You now have a new hat!");
        }
        return false;
    }

}
