package me.m69.core.alts;

import me.m69.core.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class AltsListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String ip = player.getAddress().getAddress().getHostAddress();
        if (Main.getAltsConfig().getString(player.getName()) == null) {
            Main.getAltsConfig().set(player.getName(), ip);
            try {
                Main.getAltsConfig().save(Main.alts);
                Main.getAltsConfig().load(Main.alts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!Main.getAltsConfig().getStringList(ip).contains(player.getName())) {
            List<String> alts = Main.getAltsConfig().getStringList(ip);
            alts.add(player.getName());
            Main.getAltsConfig().set(ip, alts);
            try {
                Main.getAltsConfig().save(Main.alts);
                Main.getAltsConfig().load(Main.alts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
