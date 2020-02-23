package me.m69.core.essentials.message;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReplyCommand implements CommandExecutor {

    Main main = Main.getPlugin(Main.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error!");
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("r")) {
            if (args.length == 0) {
                sender.sendMessage("§cInvalid Usage!");
                return false;
            }
            else {
                Player target = main.lastmsg.get(player);
                if (target == null) {
                    sender.sendMessage("§cInvalid Usage!");
                    return false;
                }
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    str.append(args[i] + " ");
                }
                sender.sendMessage(Messages.CC("&7(From " + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + target.getName() + ChatColor.GRAY + "&7) " + str.toString()));
                target.sendMessage(Messages.CC("&7(To " + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + player.getName() +  ChatColor.GRAY + "&7) " + str.toString()));
            }
        }
        return false;
    }
}