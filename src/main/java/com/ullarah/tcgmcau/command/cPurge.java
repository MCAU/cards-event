package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class cPurge {

    public static void runStaffPurge(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.purge") || !( sender instanceof Player ) ) {

            if (args.length == 2) {

                try {

                    ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT id FROM users WHERE user = ?", new String[]{args[1]});

                    if (res.next()) {

                        mInit.getSqlConnection().sqlUpdate("DELETE FROM stash WHERE user = ?", new String[]{args[1]});

                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + "Stash has been purged successfully for " + ChatColor.AQUA + args[1]);

                    } else {

                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[1] + ChatColor.RED + " is not registered. Check the name.");

                    }

                } catch (SQLException e) {

                    e.printStackTrace();

                }

            } else
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards purge <player>");

        } else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
