package me.m69.core.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {

    public static ItemStack buildItem(Material material, int amount, int setShort, String itemName, List<String> setLore) {
        ItemStack itemStack = new ItemStack(material, amount, (short)setShort);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        itemMeta.setLore(setLore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
