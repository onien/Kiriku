package me.m69.core.punishments;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MuteListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {

        Main.getPunishmentManager().checkDurationMute(event.getPlayer().getUniqueId());

        if (Main.getPunishmentManager().isMuted(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            String msg = "";
            if (Main.getPunishmentManager().isTempMute(event.getPlayer().getUniqueId())) {
                msg = Messages.TEMP_MUTE_CHAT;
            } else {
                msg = Messages.MUTE_CHAT;
            }
            msg = msg.replace("$reason", Main.getPunishmentManager().getReasonMute(event.getPlayer().getUniqueId()));
            event.getPlayer().sendMessage(Messages.CC(msg));
        }
    }
}
