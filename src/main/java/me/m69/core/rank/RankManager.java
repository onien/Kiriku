package me.m69.core.rank;

import me.m69.core.Main;

import java.util.ArrayList;
import java.util.List;

public class RankManager {

    private List<Rank> ranks;

    public RankManager() {
        this.ranks = new ArrayList<>();
    }

    public void loadRanks() {
        for(String r : Main.rankConfig.getConfigurationSection("Ranks").getKeys(false)) {
            String prefix = Main.rankConfig.getString("Ranks." + r + ".prefix");
            String suffix = Main.rankConfig.getString("Ranks." + r + ".suffix");
            String maincolor = Main.rankConfig.getString("Ranks." + r + ".maincolor");
            int priority = Main.rankConfig.getInt("Ranks." + r + ".priority");
            boolean isDefault = Main.rankConfig.getBoolean("Ranks." + r + ".default");
            ArrayList<String> perms = (ArrayList<String>) Main.getRankConfig().getStringList("Ranks." + r + ".permissions");

            Rank rank = new Rank(r, prefix, suffix, maincolor, priority, isDefault, perms);

            if (rank == null) {
                Main.getInstance().getServer().getConsoleSender().sendMessage("§cFailed to load rank " + rank.getName() + "...");
                return;
            }
            ranks.add(rank);
            Main.getInstance().getServer().getConsoleSender().sendMessage("§aSuccessfully loaded rank " + rank.getName() + "!");
        }
    }

    public void addRank(Rank rank) {
        if (!ranks.contains(rank)) {
            ranks.add(rank);
        }
    }

    public void removeRank(Rank rank) {
        if (ranks.contains(rank)) {
            ranks.remove(rank);
        }
    }

    public List<Rank> getRanks() {
        return ranks;
    }

    public Rank getByName(String name) {
        for (Rank rank : Main.getRankManager().getRanks()) {
            if (rank.getName().equals(name)) {
                return rank;
            }
        }
        return null;
    }

    public String getDefaultRank() {
        for (Rank rank : Main.getRankManager().getRanks()) {
            if (rank.isDefaultRank()) {
                return rank.getName();
            }
        }
        return "Default rank not found!";
    }
}
