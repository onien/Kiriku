package me.m69.core;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool.PoolInitializationException;
import java.io.File;
import java.util.HashMap;

import me.m69.core.alts.AltsCommand;
import me.m69.core.alts.AltsListener;
import me.m69.core.essentials.Gamemode;
import me.m69.core.essentials.HatCommand;
import me.m69.core.essentials.Ping;
import me.m69.core.essentials.SayCommand;
import me.m69.core.essentials.message.MessageCommand;
import me.m69.core.essentials.message.ReplyCommand;
import me.m69.core.grant.ChooseMenu;
import me.m69.core.grant.GrantCommand;
import me.m69.core.grant.GrantMenu;
import me.m69.core.grant.GrantsCommand;
import me.m69.core.license.License;
import me.m69.core.mysql.mySQLManager;
import me.m69.core.mysql.punishmentManager;
import me.m69.core.permissions.PermissionsManager;
import me.m69.core.player.PlayerJoin;
import me.m69.core.player.PlayerListener;
import me.m69.core.punishments.BanCommand;
import me.m69.core.punishments.BanListener;
import me.m69.core.punishments.HistoryCommand;
import me.m69.core.punishments.KickCommand;
import me.m69.core.punishments.MuteCommand;
import me.m69.core.punishments.MuteListener;
import me.m69.core.punishments.UnbanCommand;
import me.m69.core.punishments.UnmuteCommand;
import me.m69.core.punishments.WarnCommand;
import me.m69.core.rank.ListCommand;
import me.m69.core.rank.RankCommand;
import me.m69.core.rank.RankManager;
import me.m69.core.rank.UserCommand;
import me.m69.core.staff.StaffJoinListener;
import me.m69.core.tag.TagCommand;
import me.m69.core.tag.TagManager;
import me.m69.core.utils.BadPagedPaneListener;
import me.m69.core.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Main instance;
    public static RankManager rankManager;
    public static TagManager tagManager;
    public static mySQLManager sqlManager;
    public static punishmentManager punishmentManager;
    public String host;
    public String database;
    public String username;
    public String password;
    public String table;
    public int port;
    private HikariDataSource hikari;
    public static File rank;
    public static FileConfiguration rankConfig;
    public static File tag;
    public static FileConfiguration tagConfig;
    public static File alts;
    public static FileConfiguration altsConfig;
    public HashMap<Player, Player> lastmsg = new HashMap<Player, Player>();

    public Main() {
    }

    public void onEnable() {
        instance = this;
        this.registerConfigs();
        this.hikariSetup();
        rankManager = new RankManager();
        tagManager = new TagManager();
        sqlManager = new mySQLManager();
        punishmentManager = new punishmentManager();
        this.getServer().getConsoleSender().sendMessage(Messages.CC("&7&m-------------------------------------------"));
        this.registerListeners();
        this.registerCommands();
        this.registerStuffs();
        this.getServer().getConsoleSender().sendMessage(Messages.CC("&7&m-------------------------------------------"));
        String license = this.getConfig().getString("UUID");
        if ((new License(license, "https://rebornpvp.000webhostapp.com/verify.php", this)).register()) {
            ;
        }
    }

    public void onDisable() {
        if (this.hikari != null) {
            this.hikari.close();
        }

    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new StaffJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new AltsListener(), this);
        Bukkit.getPluginManager().registerEvents(new MuteListener(), this);
        Bukkit.getPluginManager().registerEvents(new BanListener(), this);
        Bukkit.getPluginManager().registerEvents(new GrantMenu(), this);
        Bukkit.getPluginManager().registerEvents(new PermissionsManager(), this);
        Bukkit.getPluginManager().registerEvents(new ChooseMenu(), this);
        Bukkit.getPluginManager().registerEvents(new BadPagedPaneListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        this.getServer().getConsoleSender().sendMessage(Messages.CC("&eListeners Registered Successfully"));
    }

    public void registerCommands() {
        this.getCommand("user").setExecutor(new UserCommand());
        this.getCommand("rank").setExecutor(new RankCommand());
        this.getCommand("grant").setExecutor(new GrantCommand());
        this.getCommand("grants").setExecutor(new GrantsCommand());
        this.getCommand("list").setExecutor(new ListCommand());
        this.getCommand("tag").setExecutor(new TagCommand());
        this.getCommand("ban").setExecutor(new BanCommand());
        this.getCommand("unban").setExecutor(new UnbanCommand());
        this.getCommand("mute").setExecutor(new MuteCommand());
        this.getCommand("unmute").setExecutor(new UnmuteCommand());
        this.getCommand("warn").setExecutor(new WarnCommand());
        this.getCommand("kick").setExecutor(new KickCommand());
        this.getCommand("history").setExecutor(new HistoryCommand());
        this.getCommand("alts").setExecutor(new AltsCommand());
        this.getCommand("ping").setExecutor(new Ping());
        this.getCommand("gamemode").setExecutor(new Gamemode());
        this.getCommand("say").setExecutor(new SayCommand());
        this.getCommand("hat").setExecutor(new HatCommand());
        this.getCommand("msg").setExecutor(new MessageCommand());
        this.getCommand("r").setExecutor(new ReplyCommand());
        this.getServer().getConsoleSender().sendMessage(Messages.CC("&eCommands Registered Successfully"));
    }

    public void registerConfigs() {
        this.saveDefaultConfig();
        this.saveResource("permissions.yml", false);
        this.saveResource("tags.yml", false);
        this.saveResource("alts.yml", false);
        rank = new File(this.getDataFolder() + "/permissions.yml");
        rankConfig = YamlConfiguration.loadConfiguration(rank);
        tag = new File(this.getDataFolder() + "/tags.yml");
        tagConfig = YamlConfiguration.loadConfiguration(tag);
        alts = new File(this.getDataFolder() + "/alts.yml");
        altsConfig = YamlConfiguration.loadConfiguration(alts);
        this.getServer().getConsoleSender().sendMessage(Messages.CC("&eConfigs Registered Successfully"));
    }

    public void registerStuffs() {
        rankManager.loadRanks();
        tagManager.loadTags();
        sqlManager.createTable();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getConsoleSender().sendMessage(Messages.CC("&eStuffs Registered Successfully"));
    }

    public void hikariSetup() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Opening " + ChatColor.YELLOW + "Hikari" + ChatColor.WHITE + "CP" + ChatColor.GREEN + " Connection...");

        try {
            HikariConfig config = new HikariConfig();
            this.host = this.getConfig().getString("MySQL.address");
            this.port = this.getConfig().getInt("MySQL.port");
            this.database = this.getConfig().getString("MySQL.database");
            this.username = this.getConfig().getString("MySQL.username");
            this.password = this.getConfig().getString("MySQL.password");
            this.table = "userdata";
            config.setDriverClassName("com.mysql.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database);
            config.addDataSourceProperty("user", this.username);
            config.addDataSourceProperty("password", this.password);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            config.addDataSourceProperty("useServerPrepStmts", "true");
            this.hikari = new HikariDataSource(config);
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Successfully Connected to MySQL! (" + ChatColor.YELLOW + "Hikari" + ChatColor.WHITE + "CP" + ChatColor.GREEN + ")");
        } catch (PoolInitializationException var2) {
            Bukkit.getConsoleSender().sendMessage(Messages.CC("&7&m--------------------------------"));
            Bukkit.getConsoleSender().sendMessage(Messages.CC("&cFailed to connect MySQL!"));
            Bukkit.getConsoleSender().sendMessage(Messages.CC("&cYour username/password/host might be wrong"));
            Bukkit.getConsoleSender().sendMessage(Messages.CC("&cPlease try again with a different conecction string"));
            Bukkit.getConsoleSender().sendMessage(Messages.CC("&7&m--------------------------------"));
            this.setEnabled(false);
        }

    }

    public static Main getInstance() {
        return instance;
    }

    public static RankManager getRankManager() {
        return rankManager;
    }

    public static TagManager getTagManager() {
        return tagManager;
    }

    public static punishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public HikariDataSource getHikari() {
        return this.hikari;
    }

    public static mySQLManager getSqlManager() {
        return sqlManager;
    }

    public static FileConfiguration getRankConfig() {
        return rankConfig;
    }

    public static FileConfiguration getAltsConfig() {
        return altsConfig;
    }
}
