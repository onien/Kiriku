package me.m69.core.punishments;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class BanListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Main.getPunishmentManager().checkRemain(event.getPlayer().getUniqueId());
        if (Main.getPunishmentManager().isBanned(event.getPlayer().getUniqueId())) {
            String msg = "";
            if (Main.getPunishmentManager().isTemp(event.getPlayer().getUniqueId())) {
                msg = Messages.TEMPBAN_MESSAGE;
            } else {
                msg = Messages.BAN_MESSAGE;
            }
            msg = msg.replace("$reason", Main.getPunishmentManager().getReason(event.getPlayer().getUniqueId()));
            event.getPlayer().kickPlayer(Messages.CC(msg));

        }
    }
}
