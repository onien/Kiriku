package me.m69.core.alts;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class AltsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        if (!sender.hasPermission("kriku.alts")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            String ip = Main.getAltsConfig().getString(target.getName());
            List<String> alts = Main.getAltsConfig().getStringList(ip);
            sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
            sender.sendMessage(Messages.CC("&fCurrently checking " + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + player.getName() + "&f's &6Alts&f."));
            sender.sendMessage(Messages.CC("  &eIP: &f" + ip));
            sender.sendMessage(Messages.CC("  &eAlts: &f" + alts.toString().replace("[", "").replace("]","")));
            sender.sendMessage(Messages.CC("&7&m------------------------------------------"));
            return true;
        }
        sender.sendMessage(Messages.CC("&cUsage: /alts <player>"));
        return true;
    }
}
