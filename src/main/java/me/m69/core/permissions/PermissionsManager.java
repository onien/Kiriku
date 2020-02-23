package me.m69.core.permissions;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/*
以下のコードを使用しました
https://www.youtube.com/watch?v=Geo8xG0vri4
 */

public class PermissionsManager implements Listener {

    public static HashMap<UUID, PermissionAttachment> playerperms = new HashMap<>();

    @EventHandler
    public void SetPerm(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                setupPerms(player);

            }
        };
        task.runTaskLater(Main.getInstance(), 5);
    }

    @EventHandler
    public void ClearPerm(PlayerQuitEvent event) {
        playerperms.remove(event.getPlayer().getUniqueId());
    }

    public void setupPerms(Player player) {
        PermissionAttachment attachment = player.addAttachment(Main.getInstance());
        playerperms.put(player.getUniqueId(), attachment);
        setPlayerPerms(player.getUniqueId());
    }

    public void setPlayerPerms(UUID uuid) {
        PermissionAttachment attachment = playerperms.get(uuid);
        Rank rank = PlayerProfile.getRank(uuid);

        if (rank == null || rank.getPerms() == null) return;
        List<String> perms = new ArrayList<>(rank.getPerms());

        for (String perm : Main.getRankConfig().getStringList("player-permissions." + uuid.toString())) {
            perms.add(perm);
        }

        if (perms.contains("*")) {
            Player player = Bukkit.getPlayer(uuid);
            player.setOp(true);
        }

        for (String perm : perms) {
            attachment.setPermission(perm, true);
        }
    }
}
