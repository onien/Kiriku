package me.m69.core.tag;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.rank.Rank;
import me.m69.core.utils.BadButton;
import me.m69.core.utils.BadPagedPane;
import me.m69.core.utils.Messages;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("kiriku.tag")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (!(sender instanceof Player))return true;

        Player player = (Player) sender;

        if (args.length == 0) {
            BadPagedPane pagedPane = new BadPagedPane(3 - 2, 3,"&6Tags");

            for (Tag tag : Main.getTagManager().getTags()) {
                String name = tag.getName();
                String display = tag.getDisplay();
                Rank rank = PlayerProfile.getRank(player);
                ItemStack item = new ItemStack(Material.NAME_TAG);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(Messages.CC(display));
                List<String> lores = new ArrayList<>();
                for (String lore : Messages.TAG_LORE) {
                    String prefix = rank.getPrefix().equalsIgnoreCase("None") ? "":rank.getPrefix() + " ";
                    lore = lore.replace("$tag", display);
                    lore = lore.replace("$player", prefix +  display + " " + rank.getMaincolor() + player.getName());
                    lores.add(Messages.CC(lore));
                }
                meta.setLore(lores);
                item.setItemMeta(meta);
                pagedPane.addButton(new BadButton(item, event -> new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.hasPermission("tag." + name) || player.hasPermission("tag.*")) {
                            Main.getSqlManager().setTag(player.getUniqueId(), name);
                            PlayerProfile.setTag(player, Main.getTagManager().getByName(name));
                            player.sendMessage(Messages.CC("&aYou have enabled tag " + display + " !"));
                        } else {
                            player.sendMessage("&cYou don't have that tag.".replace("&", "§"));
                        }
                    }
                }.runTaskLater(Main.getInstance(), 1)));

                /*
                pagedPane.addButton(new BadButton(
                        item,
                        event -> GrantMenu.openExpires(player, target, rank)));

                 */
            }

            pagedPane.open(player);
            return true;
        }
        return true;
    }
}
