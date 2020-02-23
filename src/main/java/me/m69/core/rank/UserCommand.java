package me.m69.core.rank;

import me.m69.core.Main;
import me.m69.core.permissions.PermissionsManager;
import me.m69.core.player.PlayerProfile;
import me.m69.core.utils.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class UserCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kiriku.user")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("listperms") || args[0].equalsIgnoreCase("listpermssions")) {
                Player target = Main.getInstance().getServer().getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage(Messages.PLAYER_OFFLINE);
                    return true;
                }

                Rank rank = PlayerProfile.getRank(target);
                if (rank == null) {
                    sender.sendMessage(Messages.RANK_NOT_EXIST);
                    return true;
                }

                sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                sender.sendMessage(Messages.CC("&6User Information &7(" + rank.getMaincolor() + target.getName() + "&7)"));
                sender.sendMessage(Messages.CC("&7Rank: &7" + rank.getMaincolor() + rank.getName()));
                if (PlayerProfile.getPermissons(target) == null) {
                    sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                }else {
                    sender.sendMessage(Messages.CC(""));
                    sender.sendMessage(Messages.CC("&7Permissions: &f(" + PlayerProfile.getPermissons(target).size() + ")"));
                    sender.sendMessage(Messages.CC(PlayerProfile.getPermissons(target).toString().replace("[","").replace("]","")));
                    sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
                }

                return true;
            }
        }

        if (args.length == 3) {
            String msg = "";
            OfflinePlayer target = Main.getInstance().getServer().getOfflinePlayer(args[1]);

            if (args[0].equalsIgnoreCase("addperm")) {
                setPerm(target, args[2], false);
                msg = Messages.USER_GIVE_PERM;
            }
            if (args[0].equalsIgnoreCase("removeperm")) {
                setPerm(target, args[2], true);
                msg = Messages.USER_REMOVE_PERM;
            }
            msg = msg.replace("$perm", args[2]);
            msg = msg.replace("$player", target.getName());
            sender.sendMessage(msg);
            return true;
        }
        printHelp(sender);
        return true;
    }

    public void setPerm(OfflinePlayer player, String perm, boolean remove) {
        List<String> perms = Main.getRankConfig().getStringList("player-permissions." + player.getUniqueId().toString());
        if (remove) {
            if (perms.contains(perm)) {
                perms.remove(perm);
            }
        }else {
            perms.add(perm);
        }
        Main.getRankConfig().set("player-permissions." + player.getUniqueId().toString(), perms);
        try {
            Main.getRankConfig().save(Main.rank);
            Main.getRankConfig().load(Main.rank);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        if (player.isOnline()) {
            new PermissionsManager().setupPerms((Player) player);
        }
    }

    public void printHelp(CommandSender player) {
        for (String msg : Messages.USER_INFO) {
            player.sendMessage(Messages.CC(msg));
        }
    }
}
