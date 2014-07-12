package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class cHelp {

    public static void runStaffHelp(CommandSender sender){

        if ( sender.hasPermission( "cards.staff" ) ) {

            sender.sendMessage("");
            sender.sendMessage("------------- STAFF COMMANDS -------------");
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards list " + ChatColor.YELLOW + "- List all cards available." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards info " + ChatColor.YELLOW + "- View a specific cards information." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards purge " + ChatColor.YELLOW + "- Completely wipe a players stash." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards give " + ChatColor.YELLOW + "- Give a specific card to a player." );
            sender.sendMessage(ChatColor.GOLD + " ▪ /cards random " + ChatColor.YELLOW + "- Give a random card to a player." );
            sender.sendMessage("");

        } else {

            sender.sendMessage(mInit.getMsgPermDeny());

        }

    }

}
