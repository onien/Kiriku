package me.m69.core.staff;

import me.m69.core.Main;
import me.m69.core.player.PlayerProfile;
import me.m69.core.rank.Rank;
import me.m69.core.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class StaffJoinListener implements Listener {

    @EventHandler
    public void insertData(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("kriku.staff")) {
            if (!isContain(player.getName())) {
                insert(player.getUniqueId(), player.getName());
            }
        }else if (isContain(player.getName())) {
            removePlayer(player.getUniqueId());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("kriku.staff")) {
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (player == null)return;
                    Rank rank = PlayerProfile.getRank(player);
                    try (Connection con = Main.getInstance().getHikari().getConnection();
                         PreparedStatement statement = con.prepareStatement("SELECT * FROM kriku_staff")) {
                        ResultSet result = statement.executeQuery();
                        while (result.next()) {
                            try {
                                String msg = Messages.STAFF_JOIN;
                                msg = msg.replace("$staff", rank.getMaincolor() + player.getName());
                                msg = msg.replace("$server", Main.getInstance().getServer().getName());
                                ByteArrayOutputStream b = new ByteArrayOutputStream();
                                DataOutputStream out = new DataOutputStream(b);
                                String name = result.getString("Name");
                                out.writeUTF("Message");
                                out.writeUTF(name);
                                out.writeUTF(Messages.CC(msg));
                                player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
                            } catch (IOException e) {
                                Main.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "Failed to send join message... (1)");
                            }
                        }
                    } catch (SQLException e) {
                        Main.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.RED + "Failed to send Staff join message... (2)");
                    }
                }
            };
            task.runTaskLater(Main.getInstance(), 20);

        }
    }

    public void insert(UUID uuid, String name) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO kriku_staff (uuid, name) VALUES (?, ?);")) {
            statement.setString(1, uuid.toString());
            statement.setString(2, name);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isContain(String name){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM kriku_staff WHERE NAME=?")) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void removePlayer(UUID uuid) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM kriku_staff WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
