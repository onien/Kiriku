package me.m69.core.player;

import me.m69.core.Main;
import me.m69.core.rank.Rank;
import me.m69.core.tag.Tag;
import me.m69.core.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import sun.plugin2.message.Message;

import java.util.UUID;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Main.getSqlManager().savePlayer(uuid);
        Player player = event.getPlayer();
        //This is the rest of the code
        Rank rank = Main.getRankManager().getByName(Main.getSqlManager().getRank(uuid));
        Tag tag = Main.getTagManager().getByName(Main.getSqlManager().getTag(uuid));
        player.setPlayerListName(Messages.CC(rank.getMaincolor()) + player.getName());
        player.setDisplayName(Messages.CC(rank.getMaincolor()) + player.getName());
        if (rank == null) {
            Main.getSqlManager().setRank(player.getUniqueId(), Main.getRankManager().getDefaultRank());
            rank = Main.getRankManager().getByName(Main.getRankManager().getDefaultRank());
        }

        if (Main.getSqlManager().isExpired(uuid)) {
            Main.getSqlManager().removeExpires(uuid);
            PlayerProfile.rankdata.put(event.getPlayer(), Main.getRankManager().getByName(Main.getRankManager().getDefaultRank()));
        }else {
            PlayerProfile.rankdata.put(event.getPlayer(), rank);
        }
        PlayerProfile.setTag(player, tag);


        if (rank.getPerms().contains("*")) {
            player.setOp(true);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (Main.getInstance().getConfig().getBoolean("Settings.chat-format.enabled")) {
            Rank rank = PlayerProfile.getRank(player);
            Tag tag = PlayerProfile.getTag(player);

            if (!Main.getRankManager().getRanks().contains(rank)) {
                Rank def = Main.getRankManager().getByName(Main.getRankManager().getDefaultRank());
                if (tag == null) {
                    event.setFormat(Messages.CC(def.getMaincolor() + player.getName() + "&f: " + "%2$s"));
                }else {
                    String t = tag.getName().equalsIgnoreCase("None") ? "" : tag.getDisplay() + " ";
                    event.setFormat(Messages.CC(t + def.getMaincolor() + player.getName() + ": " + "%2$s"));
                }
                return;
            }

            String format = rank.getMaincolor() + player.getName();

            if (!rank.getPrefix().equalsIgnoreCase("None")) {
                format = rank.getPrefix() + " " + format;
            }

            if (tag != null && !tag.getName().equalsIgnoreCase("None")) {
                format = tag.getDisplay() + " " + format;
            }

            if (!rank.getSuffix().equalsIgnoreCase("None")) {
                format = format + " " + rank.getSuffix();
            }

            format = format + "&f: ";

            event.setFormat(Messages.CC(format + "%2$s"));
        }
    }
}
