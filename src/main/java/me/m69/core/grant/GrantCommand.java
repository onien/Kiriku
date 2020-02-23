package me.m69.core.grant;

import me.m69.core.Main;
import me.m69.core.rank.Rank;
import me.m69.core.utils.BadButton;
import me.m69.core.utils.BadPagedPane;
import me.m69.core.utils.ItemUtil;
import me.m69.core.utils.Messages;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GrantCommand implements CommandExecutor {

    public static ItemUtil i = new ItemUtil();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player))
            return true;

        if (!sender.hasPermission("kiriku.grant")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(Messages.CC("&cUsage: /grant <player>"));
            return true;
        }

        Player player = (Player) sender;
        OfflinePlayer target = Main.getInstance().getServer().getOfflinePlayer(args[0]);
        ChooseMenu.openChoose(player,target);
        return true;
    }

    public static void openMenu(Player player, OfflinePlayer target) {
        Rank prank = Main.getRankManager().getByName(Main.getSqlManager().getRank(target.getUniqueId()));
        if (prank == null) {
            player.sendMessage(Messages.CC("&cFailed to load " + target.getName() + "'s profile."));
            return;
        }
        int rows = 4;
        BadPagedPane pagedPane = new BadPagedPane(rows - 2, rows,"&6Grant");

        for (Rank rank : Main.getRankManager().getRanks()) {
            String color = rank.getMaincolor();
            String pcolor = prank.getMaincolor();

            List<String> lore = new ArrayList<>();
            for (String l : Messages.GRANT_LORE) {
                l = l.replace("$rank", color.replace("&", "§") + rank.getName());
                l = l.replace("$player", pcolor.replace("&", "§") + target.getName());
                lore.add(l);
            }

            ItemStack item = i.buildItem(Material.WOOL, 1, getColorID(color),Messages.CC(rank.getMaincolor() + rank.getName()), lore);

            pagedPane.addButton(new BadButton(
                    item,
                    event -> GrantMenu.openExpires(player, target, rank)));
        }

        pagedPane.open(player);
    }

    public static int getColorID(String color) {

        switch (color) {
            case "&0":
                return 15;

            case "&1":
                return 11;


            case "&2":
                return 13;


            case "&3":
                return 9;


            case "&4":
                return 14;


            case "&5":
                return 10;


            case "&6":
                return 1;

            case "&7":
                return 8;


            case "&8":
                return 7;


            case "&9":
                return 9;


            case "&a":
                return 5;


            case "&b":
                return 3;


            case "&c":
                return 14;


            case "&d":
                return 6;


            case "&e":
                return 4;


            case "&f":
                return 0;

            default:
                return 0;
        }
    }
}
