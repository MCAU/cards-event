package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;

public class cInfo {

    public static void runCardInfo(CommandSender sender, String[] args) {

        if ( sender.hasPermission("cards.staff.info") ) if (args.length == 2) try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM cards WHERE code = ?", new String[]{args[1]});

            if (res.next()) {

                Integer cardID = res.getInt("id");
                String cardCode = res.getString("code").toLowerCase();
                String cardName = res.getString("name");
                String cardBiome = res.getString("biome");

                Integer cardCategory = res.getInt("category");
                String cardCategoryString = null;

                String cardDesc = res.getString("desc");
                String cardAbility = res.getString("ability");
                String cardActive = res.getString("active");

                switch (cardCategory) {

                    case 0:
                        cardCategoryString = ChatColor.DARK_GRAY + "Secret";
                        break;

                    case 1:
                        cardCategoryString = ChatColor.GRAY + "Very Common";
                        break;

                    case 2:
                        cardCategoryString = ChatColor.GREEN + "Common";
                        break;

                    case 3:
                        cardCategoryString = ChatColor.DARK_AQUA + "Less Common";
                        break;

                    case 4:
                        cardCategoryString = ChatColor.BLUE + "Rare";
                        break;

                    case 5:
                        cardCategoryString = ChatColor.LIGHT_PURPLE + "Very Rare";
                        break;

                    case 6:
                        cardCategoryString = ChatColor.RED + "Extra Rare";
                        break;

                    case 7:
                        cardCategoryString = ChatColor.GOLD + "Event";
                        break;

                    case 8:
                        cardCategoryString = ChatColor.DARK_PURPLE + "Collectable";
                        break;

                    case 9:
                        cardCategoryString = ChatColor.DARK_RED + "Staff";
                        break;

                }

                if (cardActive.equals("1")) cardActive = ChatColor.GREEN + "True";
                else cardActive = ChatColor.RED + "False";

                sender.sendMessage(ChatColor.YELLOW + "Card: " + ChatColor.AQUA + cardName);
                sender.sendMessage(ChatColor.YELLOW + " ▪ ID: " + ChatColor.AQUA + cardID);
                sender.sendMessage(ChatColor.YELLOW + " ▪ Code: " + ChatColor.AQUA + cardCode);
                sender.sendMessage(ChatColor.YELLOW + " ▪ Biome: " + ChatColor.AQUA + cardBiome);
                sender.sendMessage(ChatColor.YELLOW + " ▪ Category: " + cardCategoryString);
                sender.sendMessage(ChatColor.YELLOW + " ▪ Ability: " + ChatColor.AQUA + cardAbility);
                sender.sendMessage(ChatColor.YELLOW + " ▪ Active: " + cardActive);

                sender.sendMessage(ChatColor.WHITE + cardDesc);

            } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Card not found.");

        } catch (SQLException e) {

            e.printStackTrace();

        }

        else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards info <code>");
        else sender.sendMessage(mInit.getMsgPermDeny());

    }

}
