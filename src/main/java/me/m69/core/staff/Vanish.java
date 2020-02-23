package me.m69.core.staff;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Vanish implements CommandExecutor {

    public static ArrayList<Player> vanished = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if (p.hasPermission("kiriku.vanish"))
                if(vanished.contains(p)){
                    for(Player player : Bukkit.getOnlinePlayers()){
                        player.showPlayer(p);
                    }
                    vanished.remove(p);
                    p.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.RED + "disabled" + ChatColor.YELLOW + " your vanish");
                } else {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.hidePlayer(p);
                    }
                    vanished.add(p);
                    p.sendMessage(ChatColor.YELLOW + "You have " + ChatColor.GREEN + "enabled" + ChatColor.YELLOW + " your vanish");
                }else {
                p.sendMessage(ChatColor.RED + "No Permissions");

                return true;
            }
            return false;
        }
        return false;
    }
}