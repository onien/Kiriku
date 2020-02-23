package me.m69.core.mysql;

import me.m69.core.Main;
import me.m69.core.permissions.PermissionsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.UUID;

public class mySQLManager {

    public void savePlayer(UUID uuid) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO userdata (uuid, rank, tag) VALUES (?, ?, ?);")) {
            if (!playerExists(uuid)) {
                statement.setString(1, uuid.toString());
                statement.setString(2, Main.getRankManager().getDefaultRank());
                statement.setString(3, "None");
                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setRank(UUID uuid, String rank, long expires) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE userdata SET RANK=?, EXPIRES=? WHERE UUID=? ")) {
            long endToMillis = expires * 1000;
            long end = endToMillis + System.currentTimeMillis();
            statement.setString(1, rank);
            statement.setLong(2, end);
            statement.setString(3, uuid.toString());
            statement.executeUpdate();

            if (Main.getInstance().getServer().getOfflinePlayer(uuid).isOnline()) {
                PermissionsManager manager = new PermissionsManager();
                manager.setupPerms(Bukkit.getPlayer(uuid));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setRank(UUID uuid, String rank) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE userdata SET RANK=? WHERE UUID=? ")) {
            statement.setString(1, rank);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
            if (Main.getInstance().getServer().getOfflinePlayer(uuid).isOnline()) {
                PermissionsManager manager = new PermissionsManager();
                manager.setupPerms(Bukkit.getPlayer(uuid));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getRank(UUID uuid) {
        try (Connection con = Main.getInstance().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM userdata WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getString("rank");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Main.getRankManager().getDefaultRank();
    }

    public void setTag(UUID uuid, String tag) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("UPDATE userdata SET TAG=? WHERE UUID=? ")) {
            statement.setString(1, tag);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTag(UUID uuid) {
        try (Connection con = Main.getInstance().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM userdata WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return result.getString("tag");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Main.getRankManager().getDefaultRank();
    }

    public boolean isExpired(UUID uuid) {

        if(getExpires(uuid) == 0) return false;

        if(getExpires(uuid) < System.currentTimeMillis()) {
            return true;
        }
        return false;

    }

    public long getExpires(UUID uuid) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM userdata WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getLong("expires");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void removeExpires(UUID uuid) {
        try (Connection con = Main.getInstance().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("UPDATE userdata SET RANK=?, EXPIRES=?  WHERE UUID=?")) {
            statement.setString(1, Main.getRankManager().getDefaultRank());
            statement.setLong(2, 0);
            statement.setString(3, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean playerExists(UUID uuid) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM userdata WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createHistory(UUID uuid, String rank, String staff, long TimeStamp, long expire, String reason, boolean Temp) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO grant_history (UUID, rank, staff, TimeStamp, Time, Reason, Temp) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
            long endToMillis = expire * 1000;
            long end = endToMillis + System.currentTimeMillis();
            statement.setString(1, uuid.toString());
            statement.setString(2, rank);
            statement.setString(3, staff);
            statement.setLong(4, TimeStamp);
            statement.setLong(5, end);
            statement.setString(6, reason);
            statement.setBoolean(7, Temp);
            statement.executeUpdate();
        }catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ "Failed to create Grant history.");
        }
    }

    public void createTable() {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `userdata` (`UUID` char(36) NOT NULL DEFAULT '',`rank` char(15) NOT NULL DEFAULT '',`tag` char(15) NOT NULL DEFAULT '', `Expires` BIGINT, PRIMARY KEY (`UUID`));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `grant_history` (`id` INT(11) NOT NULL AUTO_INCREMENT, `UUID` char(36) NOT NULL DEFAULT '', `rank` char(15) NOT NULL DEFAULT '', `staff` char(15) NOT NULL DEFAULT '', `TimeStamp` BIGINT, `Time` BIGINT, `Reason` char(50) NOT NULL DEFAULT '', `Temp` boolean NOT NULL DEFAULT 0, PRIMARY KEY (`id`));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `ban` (`UUID` char(36) NOT NULL DEFAULT '', `staff` char(15) NOT NULL DEFAULT '', `Time` BIGINT, `Reason` char(50) NOT NULL DEFAULT '', `Temp` boolean NOT NULL DEFAULT 0, `Blacklist` boolean NOT NULL DEFAULT 0, PRIMARY KEY (`UUID`));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `mute` (`UUID` char(36) NOT NULL DEFAULT '', `staff` char(15) NOT NULL DEFAULT '', `Time` BIGINT, `Reason` char(50) NOT NULL DEFAULT '', `Temp` boolean NOT NULL DEFAULT 0, PRIMARY KEY (`UUID`));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `punishment_history` (`id` INT(11) NOT NULL AUTO_INCREMENT, `UUID` char(36) NOT NULL DEFAULT '', `staff` char(15) NOT NULL DEFAULT '', `Type` char(15) NOT NULL DEFAULT '', `TimeStamp` BIGINT, `Time` BIGINT, `Reason` char(50) NOT NULL DEFAULT '', `Temp` boolean NOT NULL DEFAULT 0, PRIMARY KEY (`id`));");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `kriku_staff` (`UUID` char(36) NOT NULL DEFAULT '', `Name` char(16) NOT NULL DEFAULT '');");

        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW +"...");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Failed to create the Table... (1)");
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW +"...");
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Failed to create the Table... (2)");
        }
    }
}
