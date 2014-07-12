package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;
import com.ullarah.tcgmcau.sql.sQuery;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class cGive {

    public static void runGiveCard(CommandSender sender, String[] args){

        if ( sender.hasPermission("cards.staff.give") || !( sender instanceof Player ) ){

            if (args.length >= 3 && args.length <= 4) {

                Player recPlayer = mInit.getPlugin().getServer().getPlayer( sQuery.getExistingPlayerUUID( args[1] ) );
                String recCard = args[2].toLowerCase();
                Integer recAmount = 1;

                if ( args.length == 4 && !( Integer.valueOf( args[3] ) <= 0 ) )
                    if (args[3].matches("[0-9]+")) recAmount = Integer.valueOf(args[3]);
                    else
                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Quantity has to be a number greater than 0.");

                if (recPlayer != null) {

                    sQuery.runRefreshCards();
                    String cardName = null;
                    String cardTag;

                    if (recCard.matches("[0-9]+") || mInit.cardCode.get(recCard) != null) try {

                        if (recCard.matches("[0-9]+")) {
                            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT name FROM cards WHERE id = ?", new String[]{recCard});
                            if(res.next()) cardName = res.getString("name");
                            cardTag = recCard;
                        } else {
                            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT name FROM cards WHERE code = ?", new String[]{recCard});
                            if(res.next()) cardName = res.getString("name");
                            cardTag = mInit.cardCode.get(recCard);
                        }

                        ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE card = ? AND user = ?", new String[]{cardTag, recPlayer.getPlayerListName()});

                        if (res.next()) {

                            Integer setAmount = Integer.valueOf(res.getString("amount")) + recAmount;

                            if (recCard.matches("[0-9]+"))
                                mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{setAmount.toString(), recCard, recPlayer.getPlayerListName()});
                            else
                                mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{setAmount.toString(), mInit.cardCode.get(recCard), recPlayer.getPlayerListName()});

                        } else {

                            if (recCard.matches("[0-9]+"))
                                mInit.getSqlConnection().sqlUpdate("INSERT INTO stash VALUES ( null, ?, ?, ? )", new String[]{recPlayer.getPlayerListName(), recCard, recAmount.toString()});
                            else
                                mInit.getSqlConnection().sqlUpdate("INSERT INTO stash VALUES ( null, ?, ?, ? )", new String[]{recPlayer.getPlayerListName(), mInit.cardCode.get(recCard), recAmount.toString()});

                        }

                        sender.sendMessage(mInit.getMsgPrefix() + "You gave " + ChatColor.GREEN + recPlayer.getPlayerListName() + ChatColor.WHITE + ", " + ChatColor.GOLD + recAmount + " " + ChatColor.AQUA + cardName + ChatColor.WHITE + " card.");

                        recPlayer.sendMessage(mInit.getMsgPrefix() + "You received a " + ChatColor.AQUA + cardName + ChatColor.WHITE + " card.");

                    } catch (SQLException e) {

                        e.printStackTrace();

                    }
                    else
                        sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "The card " + ChatColor.BLUE + args[2] + ChatColor.RED + " does not exist.");

                } else
                    sender.sendMessage(mInit.getMsgPrefix() + ChatColor.GREEN + args[1] + ChatColor.RED + " is not online or not registered.");

            } else
                sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards give <player> <id|code> [quantity]");

        } else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
