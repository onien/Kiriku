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

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kiriku.kick")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(Messages.PLAYER_OFFLINE);
                return true;
            }

            Rank targetrank = PlayerProfile.getRank(target.getUniqueId());
            String kickmsg = Messages.KICK;
            String kicktitle = Messages.KICK_MESSAGE;

            kickmsg = kickmsg.replace("$player", targetrank.getMaincolor() + target.getName());
            kicktitle = kicktitle.replace("$reason", "No reason");

            if (sender instanceof ConsoleCommandSender) {
                kickmsg = kickmsg.replace("$staff", "&b&oConsole");
                kicktitle = kicktitle.replace("$staff", "&b&oConsole");
            }else {
                Player staff = (Player) sender;
                Rank staffrank = staff.isOnline() ? PlayerProfile.getRank(staff.getUniqueId()) : Main.getRankManager().getByName(Main.getSqlManager().getRank(staff.getUniqueId()));
                kickmsg = kickmsg.replace("$staff", staffrank.getMaincolor() + staff.getName());
                kicktitle = kicktitle.replace("$staff", staffrank.getMaincolor() + staff.getName());
            }

            kickPlayer(target, Bukkit.getOfflinePlayer(sender.getName()), "No reason");
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
            kickPlayer(target, Bukkit.getOfflinePlayer(sender.getName()), reason.toString());
            return true;
        }

        sender.sendMessage(Messages.CC("&cUsage: /kick <player> <reason>"));
        return true;
    }

    public void kickPlayer(Player target, OfflinePlayer staff, String reason) {

        Rank targetrank = PlayerProfile.getRank(target.getUniqueId());
        String kickmsg = Messages.KICK;
        String kicktitle = Messages.KICK_MESSAGE;

        kickmsg = kickmsg.replace("$player", targetrank.getMaincolor() + target.getName());
        kickmsg = kickmsg.replace("$reason", reason);
        kicktitle = kicktitle.replace("$reason", reason);

        Rank staffrank = staff.isOnline() ? PlayerProfile.getRank(staff.getUniqueId()) : Main.getRankManager().getByName(Main.getSqlManager().getRank(staff.getUniqueId()));
        kickmsg = staff.getName().equals("CONSOLE") ? kickmsg.replace("$staff", "&b&oConsole") : kickmsg.replace("$staff", staffrank.getMaincolor() + staff.getName()) ;
        kicktitle = staff.getName().equals("CONSOLE") ? kicktitle.replace("$staff", "&b&oConsole") : kicktitle.replace("$staff", staffrank.getMaincolor() + staff.getName()) ;

        Main.getInstance().getServer().broadcastMessage(Messages.CC(kickmsg));
        target.kickPlayer(Messages.CC(kicktitle));
        Main.getPunishmentManager().createHistory(target.getUniqueId(), staff.getName(),"Kick",System.currentTimeMillis(),0,reason,false);
    }
}
