package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Random;

public class cRandom {

    public static void runRandomCard(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.give.random") || !( sender instanceof Player ) )
            if (args.length >= 2 && args.length <= 3) {

                Player recPlayer = mInit.getPlugin().getServer().getPlayer( sQuery.getExistingPlayerUUID( args[1] ) );
                Random rand = new Random();
                Integer recRarity = rand.nextInt(5) + 1;

                Integer recAmount = 1;
                Boolean recValue = true;

                if (recPlayer != null) {

                    if (args.length == 3) if (Integer.valueOf(args[2]) >= 1 && Integer.valueOf(args[2]) <= 6)
                        recRarity = Integer.valueOf(args[2]);
                    else recValue = false;

                    if (recValue) {

                        sQuery.runRefreshCards();

                        String recCardName = null;

                        ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT name, id FROM cards WHERE category = ? AND active = 1 ORDER BY RAND() LIMIT 1", new String[]{recRarity.toString()});

                        try {
                            if (res.next()) {

                                String recCard = res.getString("id");
                                recCardName = res.getString("name");

                                res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE card = ? AND user = ?", new String[]{recCard, recPlayer.getPlayerListName()});

                                try {

                                    if (res.next()) {

                                        mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{recAmount.toString(), recCard, recPlayer.getPlayerListName()});

                                    } else {

                                        mInit.getSqlConnection().sqlUpdate("INSERT INTO stash VALUES ( null, ?, ?, '1' )", new String[]{recPlayer.getPlayerListName(), recCard});

                                    }

                                } catch (SQLException e) {

                                    e.printStackTrace();

                                }

                            }

                        } catch (SQLException e) {

                            e.printStackTrace();

                        }

                        sender.sendMessage(mInit.getMsgPrefix() + "You have given " + ChatColor.GREEN + recPlayer.getPlayerListName() + ChatColor.WHITE + ", " + ChatColor.GOLD + recAmount + " " + ChatColor.AQUA + recCardName + ChatColor.WHITE + " card.");

                        recPlayer.sendMessage(mInit.getMsgPrefix() + "You have received a " + ChatColor.AQUA + recCardName + ChatColor.WHITE + " card.");

                    } else
                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Rarity has to be between 1 and 6.");

                } else
                    sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[1] + ChatColor.RED + " is not online.");

            } else
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards random <player> [rarity]");

        else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
