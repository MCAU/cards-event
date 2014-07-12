package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cCheck {

    public static void runPlayerCardInfo(CommandSender sender, String[] args){

        if( sender.hasPermission( "cards.staff.check" ) || !( sender instanceof Player ) ) if ( args.length == 2 ) {

            String isRegistered = ChatColor.RED + "FALSE";

            if( sQuery.isPlayerRegistered( args[1] ) ) isRegistered = ChatColor.GREEN + "TRUE";

            Integer cardAmount = sQuery.getPlayerTotalCardAmount(args[1]);

            sender.sendMessage( mInit.getMsgPrefix() + "Cards Player Info: " + ChatColor.AQUA + args[1] );
            sender.sendMessage( ChatColor.GOLD + " ▪ Registered: " + isRegistered );
            sender.sendMessage( ChatColor.GOLD + " ▪ Card Amount: " + ChatColor.WHITE + cardAmount );

            sender.sendMessage( ChatColor.GOLD + " ▪ " + ChatColor.DARK_GRAY    + "S: "  + sQuery.getPlayerCardAmountCategory( args[1], 0 ) + " " +
                                                         ChatColor.GRAY         + "VC: " + sQuery.getPlayerCardAmountCategory( args[1], 1 ) + " " +
                                                         ChatColor.GREEN        + "C: "  + sQuery.getPlayerCardAmountCategory( args[1], 2 ) + " " +
                                                         ChatColor.DARK_AQUA    + "LC: " + sQuery.getPlayerCardAmountCategory( args[1], 3 ) + " " +
                                                         ChatColor.DARK_PURPLE  + "R: "  + sQuery.getPlayerCardAmountCategory( args[1], 4 ) + " " +
                                                         ChatColor.LIGHT_PURPLE + "VR: " + sQuery.getPlayerCardAmountCategory( args[1], 5 ) + " " +
                                                         ChatColor.RED          + "ER: " + sQuery.getPlayerCardAmountCategory( args[1], 6 ) + " " +
                                                         ChatColor.YELLOW       + "E: "  + sQuery.getPlayerCardAmountCategory( args[1], 7 ) + " " +
                                                         ChatColor.BLUE         + "C: "  + sQuery.getPlayerCardAmountCategory( args[1], 8 ) + " " +
                                                         ChatColor.DARK_RED     + "ST: " + sQuery.getPlayerCardAmountCategory( args[1], 9 ) );

        } else sender.sendMessage( mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards check <player>" );

        else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
