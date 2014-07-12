package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mConfig;
import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cReload {

    public static void runReload(CommandSender sender) {

        if ( sender.hasPermission("cards.staff.reload") || !( sender instanceof Player ) ) {

            if( mConfig.loadConfig() ){
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + "Cards Config has been reloaded.");
            } else {
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Cards Config failed to reload.");
            }

        } else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
