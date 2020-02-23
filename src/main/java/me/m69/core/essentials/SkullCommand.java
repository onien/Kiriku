package me.m69.core.essentials;

//import net.minecraft.server.v1_7_R4.ItemStack;
import me.m69.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class SkullCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        if (sender.hasPermission("kiriku.skull")) {
            ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();

            meta.setOwner(args[0]);
            meta.setDisplayName(ChatColor.LIGHT_PURPLE + args[0]);
            skull.setItemMeta(meta);
            player.getPlayer().getInventory().addItem(skull);
            player.getPlayer().sendMessage(ChatColor.GOLD + "You got " + Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid)).getMaincolor() + args[0] + "'s" + ChatColor.GOLD + "head!");

            return false;
        }
        return false;
    }
}
