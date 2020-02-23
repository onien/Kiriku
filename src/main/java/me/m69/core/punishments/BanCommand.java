package me.m69.core.punishments;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.rank.Rank;
import me.m69.core.utils.Messages;
import me.m69.core.utils.TimeUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kriku.ban")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            Main.getPunishmentManager().addBan(target.getUniqueId(), sender.getName(),0, "Unfair Advantage", false);
            banPlayer(target, Bukkit.getOfflinePlayer(sender.getName()),0,"Unfair Advantage",false);
            Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(), "Ban", System.currentTimeMillis(), 0, "Unfair Advantage",false);

            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("-s")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                Main.getPunishmentManager().addBan(target.getUniqueId(), sender.getName(),0, "Unfair Advantage", false);
                banPlayer(target, Bukkit.getOfflinePlayer(sender.getName()),0,"Unfair Advantage",true);
                Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(), "Ban", System.currentTimeMillis(), 0, "Unfair Advantage",false);

            }else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (NumberUtils.isNumber(args[1].substring(0, args[1].length() - 1))) {
                    TimeUtil unit = TimeUtil.getFromShortcut((args[1].substring(args[1].length() - 1)));
                    int duration = 0;
                    long expires = 0;
                    duration = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                    expires = unit.getToSecond() * duration;
                    Main.getPunishmentManager().addBan(target.getUniqueId(), sender.getName(),expires, "Unfair Advantage", false);
                    banPlayer(target, Bukkit.getOfflinePlayer(sender.getName()),expires,"Unfair Advantage",true);
                    Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(), "Ban", System.currentTimeMillis(), expires, "Unfair Advantage",true);

                } else {
                    Main.getPunishmentManager().addBan(target.getUniqueId(), sender.getName(),0, args[1], false);
                    banPlayer(target, Bukkit.getOfflinePlayer(sender.getName()),0,args[1],false);
                    Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(), "Ban", System.currentTimeMillis(), 0, args[1],false);

                }
            }

            return true;
        }

        if (args.length >= 3) {
            boolean silent = args[0].equalsIgnoreCase("-s");
            OfflinePlayer target = silent ? Bukkit.getOfflinePlayer(args[1]) : Bukkit.getOfflinePlayer(args[0]);

            if (!silent && args.length == 3) {
                if (NumberUtils.isNumber(args[1].substring(0, args[1].length() - 1))) {
                    TimeUtil unit = TimeUtil.getFromShortcut((args[1].substring(args[1].length() - 1)));
                    int duration = 0;
                    long expires = 0;
                    duration = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                    expires = unit.getToSecond() * duration;
                    Main.getPunishmentManager().addBan(target.getUniqueId(), sender.getName(),expires, args[2], false);
                    banPlayer(target, Bukkit.getOfflinePlayer(sender.getName()),expires,args[2],false);
                    Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(), "Ban", System.currentTimeMillis(), expires, args[2],true);
                    return true;
                }
            }

            StringBuilder reason = new StringBuilder();

            TimeUtil unit = TimeUtil.getFromShortcut((args[2].substring(args[2].length() - 1)));
            int duration = 0;
            long expires = 0;
            if (!NumberUtils.isNumber(args[2].substring(0, args[2].length() - 1))) {
                for (int i = 2; i < args.length; ++i) {
                    reason.append(args[i]).append(" ");
                }
            }else {
                duration = Integer.parseInt(args[2].substring(0, args[2].length() - 1));
                expires = unit.getToSecond() * duration;
                for (int i = 3; i < args.length; ++i) {
                    reason.append(args[i]).append(" ");
                }
            }

            Main.getPunishmentManager().addBan(target.getUniqueId(), sender.getName(),expires, reason.toString(), silent);
            banPlayer(target, Bukkit.getOfflinePlayer(sender.getName()),expires, reason.toString(),silent);
            Main.getPunishmentManager().createHistory(target.getUniqueId(), sender.getName(), "Ban", System.currentTimeMillis(), expires, reason.toString(),true);
            return true;
        }

        sender.sendMessage(Messages.CC("&cUsage: /ban [-s] <player> <duration> <reason>"));
        return true;
    }

    public void banPlayer(OfflinePlayer target, OfflinePlayer staff, long expires, String reason, boolean silent) {
        String msg = "";

        if (expires == 0) {
            msg = Messages.BAN;
        } else {
            msg = Messages.BAN_TEMP;
        }
        if (silent) {
            msg = Messages.SILENT + msg;
        }

        Rank staffrank = staff.isOnline() ? PlayerProfile.getRank(staff.getUniqueId()) : Main.getRankManager().getByName(Main.getSqlManager().getRank(staff.getUniqueId()));
        Rank targetrank = target.isOnline() ? PlayerProfile.getRank(target.getUniqueId()) : Main.getRankManager().getByName(Main.getSqlManager().getRank(target.getUniqueId()));

        msg = msg.replace("$player", targetrank.getMaincolor() + target.getName());
        msg = staff.getName().equals("CONSOLE") ? msg.replace("$staff", "&b&oConsole") : msg.replace("$staff", staffrank.getMaincolor() + staff.getName()) ;
        msg = msg.replace("$reason", reason);

        if (silent) {
            Main.getInstance().getServer().getConsoleSender().sendMessage(Messages.CC(msg));
            for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
                if (player.hasPermission("kriku.show-silent")) {
                    player.sendMessage(Messages.CC(msg));
                }
            }
        }else {
            Main.getInstance().getServer().broadcastMessage(Messages.CC(msg));
        }

        if (target.isOnline()) {
            Player ta = (Player) target;
            String banmsg = expires == 0 ? Messages.BAN_MESSAGE : Messages.TEMPBAN_MESSAGE;
            banmsg = banmsg.replace("$reason", reason);
            banmsg = banmsg.replace("$staff", staffrank.getMaincolor() + staff);
            banmsg = banmsg.replace("$player", targetrank.getMaincolor() + target.getName());
            ta.kickPlayer(Messages.CC(banmsg));
        }
    }
}
