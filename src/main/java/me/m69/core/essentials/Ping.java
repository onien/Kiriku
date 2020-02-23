package me.m69.core.essentials;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Ping implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        Player toCheck;
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Usage: /ping <player>");
                return true;
            }
            toCheck = (Player) sender;
        } else {

            toCheck = Bukkit.getPlayer(StringUtils.join(args));
        }
        if (toCheck == null) {
            sender.sendMessage(ChatColor.RED + "No player named '" + StringUtils.join(args) + "' found online.");
            return true;
        }
        sender.sendMessage(Messages.CC(Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + toCheck.getName() + (toCheck.getName().endsWith("s") ? "'" : "'s") + ChatColor.WHITE + " ping: " + ChatColor.YELLOW + getPing(toCheck) + "ms"));
        if (sender instanceof Player && !toCheck.getName().equals(sender.getName())) {
            Player senderPlayer = (Player) sender;
            sender.sendMessage(Messages.CC("&ePing difference: &f" + (Math.max(getPing(senderPlayer), getPing(toCheck)) - Math.min(getPing(senderPlayer), getPing(toCheck))) + "ms" + "."));
        }
        return true;
    }


    private int getPing(Player player) {
        return (((CraftPlayer) player).getHandle()).ping;
        }

    }



