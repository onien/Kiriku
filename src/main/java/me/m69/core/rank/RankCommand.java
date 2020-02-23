package me.m69.core.rank;

import me.m69.core.Main;
import me.m69.core.permissions.PermissionsManager;
import me.m69.core.player.PlayerProfile;
import me.m69.core.utils.Messages;
import me.m69.core.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kiriku.rank")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove")) {
                Rank rank = Main.getRankManager().getByName(args[1]);
                if (rank == null) {
                    sender.sendMessage(Messages.RANK_NOT_EXIST);
                    return true;
                }
                Main.getRankManager().removeRank(rank);
                Main.getRankConfig().set("Ranks." + rank.getName(), null);
                try {
                    Main.getRankConfig().save(Main.rank);
                    Main.getRankConfig().load(Main.rank);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                }
                String msg = Messages.RANK_REMOVE;
                msg = msg.replace("$rank", rank.getName());
                sender.sendMessage(Messages.CC(msg));
                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                Rank rank = Main.getRankManager().getByName(args[1]);
                if (rank == null) {
                    sender.sendMessage(Messages.RANK_NOT_EXIST);
                    return true;
                }

                String a = rank.getPrefix().equalsIgnoreCase("None") ? "None":rank.getPrefix();
                String b = rank.getSuffix().equalsIgnoreCase("None") ? "None":rank.getSuffix();
                sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                sender.sendMessage(Messages.CC("&6Rank Information &7(" + rank.getMaincolor() + rank.getName() + "&7)"));
                sender.sendMessage(Messages.CC("&7Prefix: &f" + a + rank.getMaincolor() + " Example"));
                sender.sendMessage(Messages.CC("&7Suffix: &f" + b));
                sender.sendMessage(Messages.CC(""));
                sender.sendMessage(Messages.CC("&7Permissions: &f(" + rank.getPerms().size() + ")"));
                sender.sendMessage(Messages.CC(rank.getPerms().toString().replace("[","").replace("]","")));
                sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                return true;
            }

            if (args[0].equalsIgnoreCase("create")) {
                for (Rank rank : Main.getRankManager().getRanks()) {
                    String name = rank.getName();
                    if (name.equals(args[1])) {
                        sender.sendMessage(Messages.RANK_ALREADY_EXIST);
                        return true;
                    }
                }
                createRank(args[1]);
                String msg = Messages.RANK_CREATED;
                msg = msg.replace("$rank", args[1]);
                sender.sendMessage(Messages.CC(msg));
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("setprefix") || args[0].equalsIgnoreCase("setsuffix")
                    || args[0].equalsIgnoreCase("addperm") || args[0].equalsIgnoreCase("removeperm")) {
                Rank rank = Main.getRankManager().getByName(args[1]);
                if (rank == null) {
                    sender.sendMessage(Messages.RANK_NOT_EXIST);
                    return true;
                }
                String msg = "";
                switch (args[0].toLowerCase()) {
                    case "setprefix":
                        rank.setPrefix(args[2]);
                        update(UpdateEnum.PREFIX, rank.getName(), args[2], false);
                        msg = Messages.RANK_SET_PREFIX;
                        msg = msg.replace("$prefix", args[2]);
                        break;
                    case "setsuffix":
                        rank.setSuffix(args[2]);
                        update(UpdateEnum.SUFFIX, rank.getName(), args[2], false);
                        msg = Messages.RANK_SET_SUFFIX;
                        msg = msg.replace("$suffix", args[2]);
                        break;
                    case "addperm":
                        update(UpdateEnum.PERMISSIONS, rank.getName(), args[2], false);
                        msg = Messages.RANK_ADD_PERM;
                        msg = msg.replace("$perm", args[2]);
                        msg = msg.replace("$rank", rank.getMaincolor() + rank.getName());
                        break;
                    case "removeperm":
                        update(UpdateEnum.PERMISSIONS, rank.getName(), args[2], true);
                        msg = Messages.RANK_REMOVE_PERM;
                        msg = msg.replace("$suffix", args[2]);
                        msg = msg.replace("$rank", rank.getMaincolor() + rank.getName());
                        break;
                    default:
                        break;
                }

                sender.sendMessage(Messages.CC(msg));
                return true;
            }

        }

        if (args.length == 3 || args.length == 4) {
            if (args[0].equalsIgnoreCase("set")) {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_OFFLINE);
                    return true;
                }
                Rank rank = Main.getRankManager().getByName(args[2]);
                if (rank == null) {
                    sender.sendMessage(Messages.RANK_NOT_EXIST);
                    return true;
                }

                boolean isTemp = args.length == 4;
                List<String> msgs;
                long expires = 0;

                if (isTemp) {
                    msgs = Messages.GRANT_PROMOTED_TEMP;
                    TimeUtil unit = TimeUtil.getFromShortcut((args[3].substring(args[3].length() - 1)));
                    int duration = 0;
                    duration = Integer.parseInt(args[3].substring(0, args[3].length() - 1));
                    expires = unit.getToSecond() * duration;
                    Main.getSqlManager().createHistory(target.getUniqueId(), rank.getName(), sender.getName(), System.currentTimeMillis(),expires,"No Reason",true);
                    Main.getSqlManager().setRank(target.getUniqueId(), rank.getName(), expires);
                } else {
                    msgs = Messages.GRANT_PROMOTED;
                    Main.getSqlManager().createHistory(target.getUniqueId(), rank.getName(), sender.getName(), System.currentTimeMillis(),0,"No Reason",false);
                    Main.getSqlManager().setRank(target.getUniqueId(), rank.getName());
                }

                if (target.isOnline()) {
                    Player t = (Player) target;
                    PlayerProfile.setRank(t, rank);
                    new PermissionsManager().setupPerms(t);

                    for (String msg : msgs) {
                        if (isTemp) {
                            msg = msg.replace("$expire", getTimeLeft(expires * 1000 + System.currentTimeMillis()));
                        }
                        msg = msg.replace("$rank", rank.getMaincolor() + rank.getName());
                        msg = msg.replace("$rank_color", rank.getMaincolor() + rank.getName());
                        msg = msg.replace("$player", sender instanceof ConsoleCommandSender ? "Console":sender.getName());
                        t.sendMessage(Messages.CC(msg));
                    }
                }

                for (String msg : Messages.GRANT_PROMOTED_STAFF) {
                    msg = msg.replace("$player", target.getName());
                    msg = msg.replace("$rank", rank.getMaincolor() + rank.getName());
                    sender.sendMessage(Messages.CC(msg));
                }

                return true;

            }
        }

        printHelp(sender);
        return true;
    }

    public void printHelp(CommandSender player) {
        for (String msg : Messages.RANK_INFO) {
            player.sendMessage(Messages.CC(msg));
        }
    }

    public void createRank(String name) {
        Main.getRankConfig().set("Ranks." + name + ".prefix", "None");
        Main.getRankConfig().set("Ranks." + name + ".suffix", "None");
        Main.getRankConfig().set("Ranks." + name + ".maincolor", "&a");
        Main.getRankConfig().set("Ranks." + name + ".permissions", Arrays.asList("perm.here"));
        try {
            Main.getRankConfig().save(Main.rank);
            Main.getRankConfig().load(Main.rank);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Rank rank = new Rank(name,"None","None","&a", 100,false, Arrays.asList("perm.here"));
        Main.getRankManager().addRank(rank);
    }

    public String getTimeLeft(Long time){

        if (time == 0) {
            return "Never";
        }
        long tempsRestant = (time - System.currentTimeMillis()) / 1000;
        int month = 0;
        int day = 0;

        while(tempsRestant >= TimeUtil.MONTH.getToSecond()){
            month++;
            tempsRestant -= TimeUtil.MONTH.getToSecond();
        }

        while(tempsRestant >= TimeUtil.DAY.getToSecond()){
            day++;
            tempsRestant -= TimeUtil.DAY.getToSecond();
        }

        String result1;
        String result2;

        result1 = (month == 0) ? "" : month + " " + TimeUtil.MONTH.getName();
        result2 = (day == 0) ? "" : day  + " " + TimeUtil.DAY.getName();


        return result1 + result2;
    }

    public void update(UpdateEnum type, String name, String str, boolean remove) {
        switch (type) {
            case PREFIX:
                Main.getRankConfig().set("Ranks." + name + ".prefix", str);
                break;
            case SUFFIX:
                Main.getRankConfig().set("Ranks." + name + ".suffix", str);
                break;
            case MAINCOLOR:
                Main.getRankConfig().set("Ranks." + name + ".maincolor", str);
                break;
            case PERMISSIONS:
                Rank rank = Main.getRankManager().getByName(name);
                List<String> perms = new ArrayList<>(rank.getPerms());
                if (remove) {
                    perms.remove(str);
                }else {
                    perms.add(str);
                }
                Main.getRankConfig().set("Ranks." + name + ".permissions", perms);
                rank.setPerms(perms);
                break;
        }

        try {
            Main.getRankConfig().save(Main.rank);
            Main.getRankConfig().load(Main.rank);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
