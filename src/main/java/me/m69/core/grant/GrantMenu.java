package me.m69.core.grant;

import me.m69.core.rank.Rank;
import me.m69.core.utils.ItemUtil;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GrantMenu implements Listener {

    public static ItemUtil i = new ItemUtil();
    public static String duration_title = Messages.CC("&6Choose duration");
    public static HashMap<ItemStack, String> map = new HashMap<>();

    public static void openExpires(Player player, OfflinePlayer target, Rank rank) {

        if (player.getOpenInventory() != null)
            player.getOpenInventory().close();

        Inventory inventory = Bukkit.createInventory(null, 27, duration_title);
        for (int x = 0; x<27 ; x++) {
            inventory.setItem(x, i.buildItem(Material.STAINED_GLASS_PANE, 1, 15, "&0.",null));
        }

        List<String> l1 = Arrays.asList("§7If you select this","§7the rank will not expire.");
        List<String> l2 = Arrays.asList("§7If you select this","§7the rank will expire in 7 days.");
        List<String> l3 = Arrays.asList("§7If you select this","§7the rank will expire in 1 month.");
        List<String> l4 = Arrays.asList("§7If you select this","§7the rank will expire in 3 months.");


        ItemStack perm = i.buildItem(Material.REDSTONE_BLOCK, 1, 0, "&4Permanent", l1);
        ItemStack day7 = i.buildItem(Material.WOOL, 1, 5, "&a7 days", l2);
        ItemStack month1 = i.buildItem(Material.WOOL, 1, 4, "&e1 month", l3);
        ItemStack month3 = i.buildItem(Material.WOOL, 1, 14, "&c3 months", l4);

        map.put(perm, target.getName() + ":" + rank.getName());
        map.put(day7, target.getName() + ":" + rank.getName());
        map.put(month1, target.getName() + ":" + rank.getName());
        map.put(month3, target.getName() + ":" + rank.getName());

        inventory.setItem(10, perm);
        inventory.setItem(12, day7);
        inventory.setItem(14, month1);
        inventory.setItem(16, month3);

        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(Messages.CC(duration_title))) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
            event.setCancelled(true);
            if (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))
                return;

            Player player = (Player) event.getWhoClicked();
            String[] data =  map.get(event.getCurrentItem()).split(":");
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "§4Permanent":
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    player.performCommand("rank set " + data[0] + " "  + data[1]);
                    break;
                case "§a7 days":
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    player.performCommand("rank set " + data[0] + " " +  data[1] + " 7d");
                    break;
                case "§e1 month":
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    player.performCommand("rank set " + data[0] + " "  + data[1] + " 1m");
                    break;
                case "§c3 months":
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    player.performCommand("rank set " + data[0] + " "  + data[1] + " 3m");
                    break;
                    default:
                        break;
            }


            player.getOpenInventory().close();
        }
    }
}
