package me.m69.core.punishments;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.rank.Rank;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kiriku.warn")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_OFFLINE);
                return true;
            }

            Rank targetrank = Main.getRankManager().getByName(Main.getSqlManager().getRank(target.getUniqueId()));
            String warnbroadcast = Messages.WARN;
            String warnmsg = Messages.WARN_MESSAGE;

            warnbroadcast = warnbroadcast.replace("$player", targetrank.getMaincolor() + target.getName());
            warnmsg = warnmsg.replace("$reason", "No reason");

            if (sender instanceof ConsoleCommandSender) {
                warnbroadcast = warnbroadcast.replace("$staff", "&b&oConsole");
                warnmsg = warnmsg.replace("$staff", "&b&oConsole");
            }else {
                Player staff = (Player) sender;
                Rank staffrank = staff.isOnline() ? PlayerProfile.getRank(staff.getUniqueId()) : Main.getRankManager().getByName(Main.getSqlManager().getRank(staff.getUniqueId()));
                warnbroadcast = warnbroadcast.replace("$staff", staffrank.getMaincolor() + staff.getName());
                warnmsg = warnmsg.replace("$staff", staffrank.getMaincolor() + staff.getName());
            }

            Main.getInstance().getServer().broadcastMessage(Messages.CC(warnbroadcast));
            target.sendMessage(Messages.CC(warnmsg));
            Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(),"Warn",System.currentTimeMillis(),0,"No reason",false);

            return true;
        }

        if (args.length >= 2) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(Messages.PLAYER_OFFLINE);
                return true;
            }

            StringBuilder reason = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                reason.append(args[i]).append(" ");
            }

            Rank targetrank = Main.getRankManager().getByName(Main.getSqlManager().getRank(target.getUniqueId()));
            String warnbroadcast = Messages.WARN;
            String warnmsg = Messages.WARN_MESSAGE;

            warnbroadcast = warnbroadcast.replace("$player", targetrank.getMaincolor() + target.getName());
            warnmsg = warnmsg.replace("$reason", reason.toString());

            if (sender instanceof ConsoleCommandSender) {
                warnbroadcast = warnbroadcast.replace("$staff", "&b&oConsole");
                warnmsg = warnmsg.replace("$staff", "&b&oConsole");
            }else {
                Player staff = (Player) sender;
                Rank staffrank = staff.isOnline() ? PlayerProfile.getRank(staff.getUniqueId()) : Main.getRankManager().getByName(Main.getSqlManager().getRank(staff.getUniqueId()));
                warnbroadcast = warnbroadcast.replace("$staff", staffrank.getMaincolor() + staff.getName());
                warnmsg = warnmsg.replace("$staff", staffrank.getMaincolor() + staff.getName());
            }

            Main.getInstance().getServer().broadcastMessage(Messages.CC(warnbroadcast));
            target.sendMessage(Messages.CC(warnmsg));
            Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(),"Warn",System.currentTimeMillis(),0,reason.toString(),false);

            return true;
        }

        sender.sendMessage(Messages.CC("&cUsage: /warn <player> <reason>"));
        return true;
    }
}
