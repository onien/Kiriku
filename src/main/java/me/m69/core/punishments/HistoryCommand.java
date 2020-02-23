package me.m69.core.punishments;

import me.m69.core.Main;
import me.m69.core.utils.BadButton;
import me.m69.core.utils.BadPagedPane;
import me.m69.core.utils.Messages;
import me.m69.core.utils.TimeUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player))
            return true;

        if (!sender.hasPermission("kiriku.history")) {
            sender.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("remove")) {
                if (!NumberUtils.isNumber(args[1]))
                    return true;

                int id = Integer.parseInt(args[1]);
                try (Connection con = Main.getInstance().getHikari().getConnection();
                     PreparedStatement statement = con.prepareStatement("DELETE FROM punishment_history WHERE ID=?")) {
                    statement.setInt(1, id);
                    statement.executeUpdate();
                    sender.sendMessage(Messages.REMOVE_HISTORY.replace("$id", id + ""));
                } catch (SQLException e) {
                    Main.getInstance().getServer().getConsoleSender().sendMessage(Messages.CC("&cFailed to delete history."));
                }
                OfflinePlayer target = Main.getInstance().getServer().getOfflinePlayer(args[2]);
                openMenu(target, (Player) sender);
            }
            return true;
        }

        if (args.length == 1) {
            OfflinePlayer target = Main.getInstance().getServer().getOfflinePlayer(args[0]);
            openMenu(target, (Player) sender);
            return true;
        }
        sender.sendMessage(Messages.CC("&cUsage: /history <player>"));
        return true;
    }

    public void openMenu(OfflinePlayer player, Player playes) {
        try (Connection con = Main.getInstance().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM punishment_history WHERE UUID=?")) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet result = statement.executeQuery();

            int rows = 4;
            BadPagedPane pagedPane = new BadPagedPane(rows - 2, rows, "&6History of " + player.getName());
            while (result.next()) {

                List<String> resu = new ArrayList<>();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);

                int id = result.getInt("ID");

                for (String m : Messages.HISTORY_LORE) {
                    String staff = result.getString("staff");
                    String reason = result.getString("reason");
                    Long expire = result.getLong("time");
                    Boolean isTemp = result.getBoolean("Temp");
                    m = m.replace("$staff", staff);
                    m = m.replace("$reason", reason);
                    if (isTemp) {
                        m = m.replace("$expire", getTimeLeft(expire));
                    }else {
                        m = m.replace("$expire", "Never");
                    }
                    m = m.replace("$timestamp", sdf.format(result.getLong("TimeStamp")));
                    m = m.replace("$type", result.getString("Type"));
                    resu.add(m);
                }

                ItemStack itemStack = new ItemStack(Material.BOOK, 1);
                ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName(Messages.CC("&6#" + id + " " + sdf.format(result.getLong("TimeStamp"))));
                meta.setLore(resu);
                itemStack.setItemMeta(meta);

                pagedPane.addButton(new BadButton(
                        itemStack,
                        event -> playes.performCommand("history remove " + id + " " + player.getName())));
            }
            pagedPane.open(playes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String getTimeLeft(Long time){

        if (time == 0) {
            return "Never";
        }
        long tempsRestant = (time - System.currentTimeMillis()) / 1000;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;

        while(tempsRestant >= TimeUtil.MONTH.getToSecond()){
            month++;
            tempsRestant -= TimeUtil.MONTH.getToSecond();
        }

        while(tempsRestant >= TimeUtil.DAY.getToSecond()){
            day++;
            tempsRestant -= TimeUtil.DAY.getToSecond();
        }

        while(tempsRestant >= TimeUtil.HOUR.getToSecond()){
            hour++;
            tempsRestant -= TimeUtil.HOUR.getToSecond();
        }

        while(tempsRestant >= TimeUtil.MINUTE.getToSecond()){
            minute++;
            tempsRestant -= TimeUtil.MINUTE.getToSecond();
        }

        while(tempsRestant >= TimeUtil.SECOND.getToSecond()){
            second++;
            tempsRestant -= TimeUtil.SECOND.getToSecond();
        }
        String result1;
        String result2;

        result1 = (month == 0) ? "" : month + " " + TimeUtil.MONTH.getName() + ", ";
        result2 = (day == 0) ? "" : day  + " " + TimeUtil.DAY.getName() + ", ";

        if (month == 0 && day == 0 && hour == 0 && minute == 0 && second == 0) {
            return "Expired";
        }

        return result1 + result2 + hour + " " + TimeUtil.HOUR.getName() + ", " + minute + " " + TimeUtil.MINUTE.getName() + ", " + second + " " + TimeUtil.SECOND.getName();
    }
}
