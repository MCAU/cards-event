package com.ullarah.tcgmcau.command;

import com.ullarah.tcgmcau.mInit;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class cList {

    public static void runListCards(CommandSender sender, String[] args) {

        if ( sender.hasPermission("cards.staff.list") ) {

            String page;

            if( args.length == 1 ) page = "1";
            else page = args[1];

            if( page.matches("[0-9]+") ){

                SortedMap<Integer, String> map = new TreeMap<>();

                try {

                    ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT id, code, name FROM cards", new String[]{});

                    while( res.next() ){

                        Integer cardID = Integer.valueOf( res.getString("id") );
                        String cardCode = res.getString("code").toLowerCase();
                        String cardName = res.getString("name");

                        map.put(cardID, ChatColor.AQUA + cardName + ChatColor.YELLOW + " - " + ChatColor.GREEN + cardCode);

                    }

                } catch (SQLException e) {

                    e.printStackTrace();

                } finally {

                    paginate(sender, map, Integer.parseInt(page));

                }

            } else sender.sendMessage(mInit.getMsgPrefix() + ChatColor.YELLOW + "/cards list [page]");

        } else sender.sendMessage(mInit.getMsgPermDeny());

    }

    private static void paginate(CommandSender sender, SortedMap<Integer, String> map, int page) {

        sender.sendMessage( ChatColor.YELLOW + "Cards: Page [" + String.valueOf( page ) + " of " + ( ( ( map.size() % 6) == 0 ) ? map.size() / 6 : ( map.size() / 6) + 1 ) + "]" );

        int i = 0;
        int k = 0;

        page--;

        for ( final Map.Entry <Integer, String> e : map.entrySet() ) {

            k++;

            if ( ( ( ( page * 6) + i + 1 ) == k ) && ( k != ( ( page * 6) + 6 + 1) ) ) {

                i++;

                sender.sendMessage( ChatColor.YELLOW + " ▪ " + e.getValue() );

            }

        }

    }

}
