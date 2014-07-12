package com.ullarah.tcgmcau;

import com.ullarah.tcgmcau.sql.sFunction;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class mInit extends JavaPlugin {

    private static Plugin plugin;

    private static sFunction sqlConnection;

    public static final HashMap<String, String> cardCode = new HashMap<>();
    public static final HashMap<UUID, List<String>> eventFinished = new HashMap<>();

    public static final List<Integer> entitySpawners = new ArrayList<>();

    private static String msgPrefix = null;
    private static String msgPermDeny = null;
    private static String msgNoConsole = null;

    private static Boolean maintenanceCheck;
    private static String maintenanceMessage;

    public static Plugin getPlugin() { return plugin; }
    private static void setPlugin( Plugin plugin ) { mInit.plugin = plugin; }

    public static sFunction getSqlConnection() {
        return sqlConnection;
    }
    private static void setSqlConnection(sFunction sqlConnection) {
        mInit.sqlConnection = sqlConnection;
    }

    public static String getMsgPrefix() {
        return msgPrefix;
    }
    private static void setMsgPrefix(String msgPrefix) {
        mInit.msgPrefix = msgPrefix;
    }

    public static String getMsgPermDeny() {
        return msgPermDeny;
    }
    private static void setMsgPermDeny(String msgPermDeny) {
        mInit.msgPermDeny = msgPermDeny;
    }

    public static String getMsgNoConsole() {
        return msgNoConsole;
    }
    private static void setMsgNoConsole(String msgNoConsole) {
        mInit.msgNoConsole = msgNoConsole;
    }

    public static Boolean getMaintenanceCheck() {
        return maintenanceCheck;
    }
    public static void setMaintenanceCheck(Boolean maintenanceCheck) {
        mInit.maintenanceCheck = maintenanceCheck;
    }

    public static String getMaintenanceMessage() {
        return maintenanceMessage;
    }
    private static void setMaintenanceMessage(String maintenanceMessage) {
        mInit.maintenanceMessage = maintenanceMessage;
    }

    public void onEnable() {

        setMsgPrefix(ChatColor.DARK_PURPLE + "[Cards] " + ChatColor.WHITE);

        setPlugin(this);

        PluginManager pluginManager = getServer().getPluginManager();

        new mConfig();
        mConfig.createAllFiles();

        setSqlConnection( new sFunction() );

        getCommand( "cards" ).setExecutor( new mCommands( ) );

        pluginManager.registerEvents(new mEvents(), getPlugin());

        setMsgPermDeny(getMsgPrefix() + ChatColor.RED + mConfig.getLanguageConfig().getString( "nopermission" ));
        setMsgNoConsole(getMsgPrefix() + ChatColor.RED + mConfig.getLanguageConfig().getString( "noconsole" ));

        setMaintenanceCheck(mInit.getPlugin().getConfig().getBoolean( "maintenance" ));
        setMaintenanceMessage(getMsgPrefix() + ChatColor.RED + mConfig.getLanguageConfig().getString( "maintenance" ));

        //Refresh all managed cards
        sQuery.runRefreshCards();

        mTask.resetPlayerEvents();

    }

    public void onDisable() {

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Saving Player Card States" );
        mConfig.saveConfig( "PLAYER" );

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Saving Chance Values" );
        mConfig.saveConfig( "CHANCE" );

        Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Closing SQL Connection Pool" );
        getSqlConnection().sqlCloseConnection();

    }

}