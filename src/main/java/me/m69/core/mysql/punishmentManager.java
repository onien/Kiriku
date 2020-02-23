package me.m69.core.mysql;

import me.m69.core.Main;
import me.m69.core.utils.Messages;
import me.m69.core.utils.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class punishmentManager {

    public void addBan(UUID uuid, String staff, long endInSeconds, String reason, boolean silent) {
        if (isBanned(uuid)) {
            unBanConsole(uuid, false);
        }

        if (endInSeconds == 0) {
            try(Connection con = Main.getInstance().getHikari().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO ban (UUID, staff, Time, Reason, Temp) VALUES(?, ?, ?, ? , ?)")) {
                statement.setString(1, uuid.toString());
                statement.setString(2, staff);
                statement.setLong(3, 0);
                statement.setString(4, reason);
                statement.setBoolean(5, false);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else {
            long endToMillis = endInSeconds * 1000;
            long end = endToMillis + System.currentTimeMillis();

            try(Connection con = Main.getInstance().getHikari().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO ban (UUID, staff, Time, Reason, Temp) VALUES(?, ?, ?, ?, ?)")) {
                statement.setString(1, uuid.toString());
                statement.setString(2, staff);
                statement.setLong(3, end);
                statement.setString(4, reason);
                statement.setBoolean(5, true);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void unBan(UUID uuid) {
        if(!isBanned(uuid)) return;

        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM ban WHERE UUID=?")) {
            //PreparedStatement sts = Main.getInstance().getConnection().prepareStatement("DELETE FROM ban WHERE UUID=?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unBanConsole(UUID uuid, boolean silent) {
        if(!isBanned(uuid)) return;
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM ban WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
            Main.getInstance().getServer().getConsoleSender().sendMessage(Messages.CC("&b[BackLog] &aUnbanned " + target.getName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBanned(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ban WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTemp(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ban WHERE UUID=?")) {
            //PreparedStatement statement = Main.getInstance().getConnection().prepareStatement("SELECT * FROM ban WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("Temp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getPunisher(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ban WHERE UUID=?")) {
            //PreparedStatement statement = Main.getInstance().getConnection().prepareStatement("SELECT * FROM ban WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("staff");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getReason(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ban WHERE UUID=?")) {
            //PreparedStatement statement = Main.getInstance().getConnection().prepareStatement("SELECT * FROM ban WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public long getEnd(UUID uuid){
        if(!isBanned(uuid)) return 0;

        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM ban WHERE UUID=?")) {
            //PreparedStatement sts = Main.getInstance().getConnection().prepareStatement("SELECT * FROM ban WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getLong("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void checkRemain(UUID uuid){
        if(!isBanned(uuid) || getEnd(uuid) == 0) return;
        if(getEnd(uuid) < System.currentTimeMillis()){
            unBanConsole(uuid, false);
        }
    }

    public String getTimeLeft(UUID uuid){
        if(!isBanned(uuid)) return "§cNot Banned";

        long tempsRestant = (getEnd(uuid) - System.currentTimeMillis()) / 1000;
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
        result2 = (day == 0) ? "" : day + " " + TimeUtil.DAY.getName() + ", ";

        if (month == 0 && day == 0 && hour == 0 && minute == 0 && second == 0) {
            if (!isTemp(uuid))return "Permanent";
            return "Expired";
        }

        return result1 + result2 + hour + " " + TimeUtil.HOUR.getName() + ", " + minute + " " + TimeUtil.MINUTE.getName() + ", " + second + " " + TimeUtil.SECOND.getName();
    }

    public void addMute(UUID uuid, String staff, long endInSeconds, String reason, boolean silent) {
        if (isMuted(uuid)) {
            unMuteConsole(uuid);
        }

        if (endInSeconds == 0) {


            try(Connection con = Main.getInstance().getHikari().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO mute (UUID, staff, Time, Reason, Temp) VALUES(?, ?, ?, ?, ?)")) {
                statement.setString(1, uuid.toString());
                statement.setString(2, staff);
                statement.setLong(3, 0);
                statement.setString(4, reason);
                statement.setBoolean(5, false);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else {
            long endToMillis = endInSeconds * 1000;
            long end = endToMillis + System.currentTimeMillis();

            try(Connection con = Main.getInstance().getHikari().getConnection();
                PreparedStatement statement = con.prepareStatement("INSERT INTO mute (UUID, staff, Time, Reason, Temp) VALUES(?, ?, ?, ?, ?)")) {
                //PreparedStatement statement = Main.getInstance().getConnection().prepareStatement("INSERT INTO mute (UUID, staff, Time, Reason, Temp) VALUES(?, ?, ?, ?, ?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, staff);
                statement.setLong(3, end);
                statement.setString(4, reason);
                statement.setBoolean(5, true);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void unMute(UUID uuid) {
        if(!isMuted(uuid)) return;
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("DELETE FROM mute WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void unMuteConsole(UUID uuid) {
                if(!isMuted(uuid)) return;
                try(Connection con = Main.getInstance().getHikari().getConnection();
                    PreparedStatement statement = con.prepareStatement("DELETE FROM mute WHERE UUID=?")) {
                    statement.setString(1, uuid.toString());
                    statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPunisherMute(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM mute WHERE UUID=?")) {
            //PreparedStatement statement = Main.getInstance().getConnection().prepareStatement("SELECT * FROM mute WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("staff");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getReasonMute(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM mute WHERE UUID=?")) {
            //PreparedStatement statement = Main.getInstance().getConnection().prepareStatement("SELECT * FROM mute WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("reason");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void checkDurationMute(UUID uuid){
        if(!isMuted(uuid) || getEndMute(uuid) == 0) return;
        if(getEndMute(uuid) < System.currentTimeMillis()){
            unMuteConsole(uuid);
        }
    }

    public long getEndMute(UUID uuid){
        if(!isMuted(uuid)) return 0;

        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM mute WHERE UUID=?")) {
            //PreparedStatement sts = Main.getInstance().getConnection().prepareStatement("SELECT * FROM mute WHERE UUID=?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                return rs.getLong("time");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getTimeLeftMute(UUID uuid){
        if(!isMuted(uuid)) return "§cNot Muted";

        long tempsRestant = (getEndMute(uuid) - System.currentTimeMillis()) / 1000;
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

        result1 = (month == 0) ? "" : month + TimeUtil.MONTH.getName() + ", ";
        result2 = (day == 0) ? "" : day + TimeUtil.DAY.getName() + ", ";

        if (month == 0 && day == 0 && hour == 0 && minute == 0 && second == 0) {
            return "Expired";
        }

        return result1 + result2 + hour + " " + TimeUtil.HOUR.getName() + ", " + minute + " " + TimeUtil.MINUTE.getName() + ", " + second + " " + TimeUtil.SECOND.getName();
    }


    public boolean isMuted(UUID uuid) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM mute WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isTempMute(UUID uuid){
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM mute WHERE UUID=?")) {
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("Temp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean Blacklist(UUID uuid) {
        return false;
    }





    public void createHistory(UUID uuid, String staff, String type, long TimeStamp, long expire, String reason, boolean Temp) {
        try(Connection con = Main.getInstance().getHikari().getConnection();
            PreparedStatement statement = con.prepareStatement("INSERT INTO punishment_history (UUID, staff, Type, TimeStamp, Time, Reason, Temp) VALUES(?, ?, ?, ?, ?, ?, ?)")) {
            long endToMillis = expire * 1000;
            long end = endToMillis + System.currentTimeMillis();
            statement.setString(1, uuid.toString());
            statement.setString(2, staff);
            statement.setString(3, type);
            statement.setLong(4, TimeStamp);
            statement.setLong(5, end);
            statement.setString(6, reason);
            statement.setBoolean(7, Temp);
            statement.executeUpdate();
        }catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED+ "Failed to create Punishment history.");
        }
    }



}
