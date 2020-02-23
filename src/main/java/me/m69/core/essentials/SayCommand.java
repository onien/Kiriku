package me.m69.core.essentials;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SayCommand implements CommandExecutor {
    String command = "";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        Player toCheck = player;

        StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String message = sb.toString();
        Bukkit.getServer().broadcastMessage(Messages.CC("&f[" + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + toCheck.getName() + Messages.CC("&f] &f") + message));
        {

        }
            return true;
        }

}
