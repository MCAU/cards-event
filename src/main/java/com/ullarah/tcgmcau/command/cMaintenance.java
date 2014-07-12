package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cMaintenance {

    public static void runMaintenance(CommandSender sender, String[] args) {

        int maintenanceState = 0;

        if ( sender.hasPermission("cards.staff.maintenance") || !( sender instanceof Player ) ) if (args.length == 2) {

            if ( args[1].equals("on") ) maintenanceState = 1;

            switch ( maintenanceState ) {

                case 0:
                    mConfig.getConfig().set("maintenance", false);
                    mInit.setMaintenanceCheck(false);
                    mInit.getSqlConnection().sqlOpenConnection();
                    sender.sendMessage( mInit.getMsgPrefix() + ChatColor.GREEN + "Maintenance mode is now off." );
                    break;

                case 1:
                    mConfig.getConfig().set("maintenance", true);
                    mInit.setMaintenanceCheck(true);
                    mInit.getSqlConnection().sqlCloseConnection();
                    sender.sendMessage( mInit.getMsgPrefix() + ChatColor.RED + "Maintenance mode is now on." );
                    break;

            }

            mConfig.saveConfig();

        } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards maintenance <on|off>");
        else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
