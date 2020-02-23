package me.m69.core.punishments;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.rank.Rank;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kiriku.unmute")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("-s")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                if (!Main.getPunishmentManager().isMuted(target.getUniqueId())) {
                    sender.sendMessage(Messages.CC("&c" + target.getName() + " is not muted."));
                    return true;
                }

                Main.getPunishmentManager().unMute(target.getUniqueId());
                String msg = Messages.UNMUTE;
                Rank targetrank = Main.getRankManager().getByName(Main.getSqlManager().getRank(target.getUniqueId()));
                msg = msg.replace("$player", targetrank.getMaincolor() + target.getName());

                if (sender instanceof ConsoleCommandSender) {
                    msg = msg.replace("$staff", "&b&oConsole");
                }else {
                    Player staff = (Player) sender;
                    Rank staffrank = PlayerProfile.getRank(staff.getUniqueId());
                    msg = msg.replace("$staff", staffrank.getMaincolor() + staff.getName());
                }

                for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
                    if (player.hasPermission("kiriku.show-silent")) {
                        player.sendMessage(Messages.CC(Messages.SILENT + msg));
                        Main.getInstance().getServer().broadcastMessage(Messages.CC(Messages.SILENT + msg));
                    }
                }

                return true;
            }
            sender.sendMessage(Messages.CC("&cInvalid args."));
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

            if (!Main.getPunishmentManager().isMuted(target.getUniqueId())) {
                sender.sendMessage(Messages.CC("&c" + target.getName() + " is not muted."));
                return true;
            }

            Main.getPunishmentManager().unMute(target.getUniqueId());
            String msg = Messages.UNMUTE;
            Rank targetrank = Main.getRankManager().getByName(Main.getSqlManager().getRank(target.getUniqueId()));
            msg = msg.replace("$player", targetrank.getMaincolor() + target.getName());

            if (sender instanceof ConsoleCommandSender) {
                msg = msg.replace("$staff", "&b&oConsole");
            }else {
                Player staff = (Player) sender;
                Rank staffrank = PlayerProfile.getRank(staff.getUniqueId());
                msg = msg.replace("$staff", staffrank.getMaincolor() + staff.getName());
            }
            Main.getInstance().getServer().broadcastMessage(Messages.CC(msg));
            return true;
        }
        sender.sendMessage(Messages.CC("&cUsage: /unmute [-s] <player>"));
        return true;
    }

}
