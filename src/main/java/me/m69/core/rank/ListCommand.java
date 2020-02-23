package me.m69.core.rank;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        List<String> ranks = new ArrayList<>();
        List<String> players = new ArrayList<>();

        for (Rank rank : Main.getRankManager().getRanks()) {
            ranks.add(rank.getMaincolor() + rank.getName() + "&f");
        }

        for (Player player : Main.getInstance().getServer().getOnlinePlayers()) {
            Rank rank = PlayerProfile.getRank(player.getUniqueId());
            if (rank == null)continue;
            players.add(rank.getPriority() + ":" + rank.getMaincolor() + player.getName() + "&f");
        }

        players.sort(new Comparator<String>(){
            @Override
            public int compare(String s1, String s2){
                return Integer.valueOf(s2.split(":")[0]) - Integer.valueOf(s1.split(":")[0]);
            }
        });

        List<String> result = new ArrayList<>();
        for (String a : players) {
            result.add(a.split(":")[1]);
        }

        sender.sendMessage(Messages.CC(ranks.toString().replace("(", "").replace(")","")));
        sender.sendMessage(Messages.CC("&f(" + result.size() + "/" + Main.getInstance().getServer().getMaxPlayers() + ") " + result.toString().replace("(", "").replace(")","")));
   return true;
    }
}
