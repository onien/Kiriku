package me.m69.core.player;

import me.m69.core.utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Messages.WELCOME_MSG);
        event.setJoinMessage(null);
    }
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
