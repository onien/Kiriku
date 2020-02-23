package me.m69.core.essentials;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Gamemode implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        if (!sender.hasPermission("kiriku.gamemode.creative")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1")) {
                player.setGameMode(GameMode.CREATIVE);
                sender.sendMessage(Messages.CC("&fYou have set your gamemode to &cCreative"));
                Bukkit.getServer().getConsoleSender().sendMessage(Messages.CC("&7&o[" + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + player.getName() + "&7&o]" + "&cYou've set your gamemode to &cCREATIVE"));
                return true;
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("2")) {
                player.setGameMode(GameMode.SURVIVAL);
                Bukkit.getServer().getConsoleSender().sendMessage(Messages.CC("&7&o[" + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + player.getName() + "&7&o]" + "&cYou've set your gamemode to &cSURVIVAL"));
                sender.sendMessage(Messages.CC("&fYou have set your gamemode to &cSurvival"));
                return true;
            }
        }
        return false;
    }
}
