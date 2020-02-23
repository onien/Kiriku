package me.m69.core.grant;

import me.m69.core.Main;
import me.m69.core.permissions.PermissionsManager;
import me.m69.core.utils.ItemUtil;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseMenu implements Listener {

    public static ItemUtil i = new ItemUtil();
    public static HashMap<ItemStack, OfflinePlayer> map = new HashMap<>();
    public static HashMap<Player, OfflinePlayer> pmap = new HashMap<>();
    public static HashMap<Player, Boolean> choosemap = new HashMap<>();

    public static void openChoose(Player player, OfflinePlayer target) {

        if (player.getOpenInventory() != null)
            player.getOpenInventory().close();

        Inventory inventory = Bukkit.createInventory(null, 27, Messages.CC("&6Grant GUI"));
        for (int x = 0; x<27 ; x++) {
            inventory.setItem(x, i.buildItem(Material.STAINED_GLASS_PANE, 1, 15, "&0.",null));
        }

        ItemStack rankitem = i.buildItem(Material.CHEST, 1, 0, "&eRank", null);
        ItemStack permitem = i.buildItem(Material.PAPER, 1, 0, "&aPermission", null);
        ItemStack tagitem = i.buildItem(Material.NAME_TAG, 1, 0, "&dTag", null);

        map.put(rankitem, target);
        map.put(permitem, target);
        map.put(tagitem, target);
        pmap.put(player,target);

        inventory.setItem(11, rankitem);
        inventory.setItem(15, permitem);
        inventory.setItem(13, tagitem);

        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getTitle().equalsIgnoreCase(Messages.CC("&6Grant GUI"))) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
            event.setCancelled(true);
            if (event.getCurrentItem().getType().equals(Material.STAINED_GLASS_PANE))
                return;

            Player player = (Player) event.getWhoClicked();

            switch (event.getCurrentItem().getType()) {
                case CHEST:
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    GrantCommand.openMenu(player, map.get(event.getCurrentItem()));
                    break;
                case PAPER:
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    choosemap.put(player, true);
                    player.sendMessage(Messages.CC("&7Please type the &epermission node &7you want to grant to " + map.get(event.getCurrentItem()).getName() + ", Type &4CANCEL&7 to cancel this grant."));
                    player.getOpenInventory().close();
                    break;
                case NAME_TAG:
                    player.playSound(player.getLocation(), Sound.NOTE_PLING, 1 ,2);
                    player.sendMessage(Messages.CC("&cThis feature is comming very soon."));
                    player.getOpenInventory().close();
                default:
                    break;
            }


        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        choosemap.put(event.getPlayer(), false);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (choosemap.get(event.getPlayer())) {
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                choosemap.put(event.getPlayer(),false);
                event.getPlayer().sendMessage(Messages.CC("&aYou have left grant settings."));
                return;
            }

            OfflinePlayer target = pmap.get(event.getPlayer());

            setPerm(target, event.getMessage());
            event.getPlayer().sendMessage(Messages.CC("&aAdded permission " + event.getMessage() + "."));
        }
    }

    public void setPerm(OfflinePlayer player, String perm) {
        List<String> perms = new ArrayList<>();
        if (Main.getRankConfig().getStringList("player-permissions." + player.getUniqueId().toString()) != null) {
            perms = Main.getRankConfig().getStringList("player-permissions." + player.getUniqueId().toString());
        }
        perms.add(perm);
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
}
