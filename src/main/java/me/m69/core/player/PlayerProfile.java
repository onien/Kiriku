package me.m69.core.player;

import me.m69.core.Main;
import me.m69.core.rank.Rank;
import me.m69.core.tag.Tag;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerProfile {
    public static HashMap<Player, Rank> rankdata = new HashMap<>();
    public static HashMap<Player, Tag> tagdata = new HashMap<>();

    public static Rank getRank(Player player) {
        return rankdata.get(player);
    }
    public static Rank getRank(UUID uuid) {
        Player player = Main.getInstance().getServer().getPlayer(uuid);
        return rankdata.get(player);
    }
    public static void setRank(Player player, Rank rank) {
        rankdata.put(player, rank);
    }

    public static Tag getTag(Player player) {
        return tagdata.get(player);
    }
    public static Tag getTag(UUID uuid) {
        Player player = Main.getInstance().getServer().getPlayer(uuid);
        return tagdata.get(player);
    }
    public static void setTag(Player player, Tag tag) {
        tagdata.put(player, tag);
    }

    public static List<String> getPermissons(Player player) {
        return Main.getRankConfig().getStringList("player-permissions." + player.getUniqueId().toString());
    }
}
