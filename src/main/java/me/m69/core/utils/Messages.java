package me.m69.core.utils;

import java.util.ArrayList;
import java.util.List;
import me.m69.core.Main;
import org.bukkit.ChatColor;

public class Messages
{
    public static String NO_PERMISSION = CC(Main.getInstance().getConfig().getString("Messages.no-permission"));
    public static String PLAYER_OFFLINE = CC(Main.getInstance().getConfig().getString("Messages.player-offline"));
    public static String STAFF_JOIN = CC(Main.getInstance().getConfig().getString("Messages.staff-join"));
    public static String RANK_NOT_EXIST = CC(Main.getInstance().getConfig().getString("Messages.rank-not-exist"));
    public static String RANK_ALREADY_EXIST = CC(Main.getInstance().getConfig().getString("Messages.rank-already-exist"));
    public static String RANK_CREATED = CC(Main.getInstance().getConfig().getString("Messages.rank-created"));
    public static String RANK_REMOVE = CC(Main.getInstance().getConfig().getString("Messages.rank-removed"));
    public static String RANK_SET_PREFIX = CC(Main.getInstance().getConfig().getString("Messages.rank-set-prefix"));
    public static String RANK_SET_SUFFIX = CC(Main.getInstance().getConfig().getString("Messages.rank-set-suffix"));
    public static String RANK_ADD_PERM = CC(Main.getInstance().getConfig().getString("Messages.rank-add-perm"));
    public static String RANK_REMOVE_PERM = CC(Main.getInstance().getConfig().getString("Messages.rank-remove-perm")); public static String REMOVE_HISTORY;
    public static String USER_GIVE_PERM = CC(Main.getInstance().getConfig().getString("Messages.user-give-perm")); public static String SILENT; public static String UNBAN; public static String BAN; public static String BAN_TEMP; public static String UNMUTE; public static String MUTE;
    public static String USER_REMOVE_PERM = CC(Main.getInstance().getConfig().getString("Messages.user-remove-perm")); public static String MUTE_TEMP; public static String MUTE_CHAT; public static String TEMP_MUTE_CHAT; public static String WARN; public static String WARN_MESSAGE; public static String KICK; static  {
    REMOVE_HISTORY = CC(Main.getInstance().getConfig().getString("Messages.remove-history"));
    SILENT = CC(Main.getInstance().getConfig().getString("Settings.silent-format"));

    UNBAN = CC(Main.getInstance().getConfig().getString("Messages.unban-broadcast"));
    BAN = CC(Main.getInstance().getConfig().getString("Messages.ban-broadcast"));
    BAN_TEMP = CC(Main.getInstance().getConfig().getString("Messages.tempban-broadcast"));
    UNMUTE = CC(Main.getInstance().getConfig().getString("Messages.unmute-broadcast"));
    MUTE_CHAT = CC(Main.getInstance().getConfig().getString("Messages.mute-message"));
    TEMP_MUTE_CHAT = CC(Main.getInstance().getConfig().getString("Messages.tempmute-message"));
    MUTE = CC(Main.getInstance().getConfig().getString("Messages.mute-broadcast"));
    MUTE_TEMP = CC(Main.getInstance().getConfig().getString("Messages.tempmute-broadcast"));
    WARN = CC(Main.getInstance().getConfig().getString("Messages.warn-broadcast"));
    WARN_MESSAGE = CC(Main.getInstance().getConfig().getString("Messages.warn-message"));
    KICK = CC(Main.getInstance().getConfig().getString("Messages.kick-broadcast"));
    WELCOME_MSG = CC(Main.getInstance().getConfig().getString("Messages.welcome-msg"));

    BAN_MESSAGE = CC(Main.getInstance().getConfig().getString("Messages.ban-message"));
    TEMPBAN_MESSAGE = CC(Main.getInstance().getConfig().getString("Messages.tempban-message"));
    KICK_MESSAGE = CC(Main.getInstance().getConfig().getString("Messages.kick-message"));
    WELCOME_MSG = CC(Main.getInstance().getConfig().getString("Messages.welcome-message"));

    TAG_LORE = CC(Main.getInstance().getConfig().getStringList("Messages.tag-lore"));
    GRANT_LORE = CC(Main.getInstance().getConfig().getStringList("Messages.grant-item-lore"));
    HISTORY_LORE = CC(Main.getInstance().getConfig().getStringList("Messages.history-lore"));
    GRANTS_LORE = CC(Main.getInstance().getConfig().getStringList("Messages.grant-lore"));
    GRANT_PROMOTED = CC(Main.getInstance().getConfig().getStringList("Messages.grant-promoted"));
    GRANT_PROMOTED_TEMP = CC(Main.getInstance().getConfig().getStringList("Messages.grant-promoted-temp"));
    GRANT_PROMOTED_STAFF = CC(Main.getInstance().getConfig().getStringList("Messages.grant-promoted-staff"));
    RANK_INFO = CC(Main.getInstance().getConfig().getStringList("Messages.rank-info"));
    USER_INFO = CC(Main.getInstance().getConfig().getStringList("Messages.user-info"));
}
    public static String WELCOME_MSG; public static String BAN_MESSAGE; public static String TEMPBAN_MESSAGE; public static String KICK_MESSAGE; public static List<String> HISTORY_LORE; public static List<String> GRANTS_LORE; public static List<String> GRANT_LORE; public static List<String> TAG_LORE; public static List<String> GRANT_PROMOTED; public static List<String> GRANT_PROMOTED_TEMP; public static List<String> GRANT_PROMOTED_STAFF;
    public static List<String> RANK_INFO;
    public static List<String> USER_INFO;

    public static String CC(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }


    public static List<String> CC(List<String> msgs) {
        List<String> result = new ArrayList<String>();
        for (String msg : msgs) {
            result.add(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return result;
    }
}
