package com.ullarah.tcgmcau.sql;

import com.ullarah.tcgmcau.mInit;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class sQuery {

    public static UUID getExistingPlayerUUID( String player ) {

        String uuid;

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT uuid FROM users WHERE user = ?", new String[]{player});

            if( res.next() ){

                uuid = res.getString("uuid");

                return UUID.fromString( uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20, 32) );

            }

        } catch (SQLException e) {

            e.printStackTrace();

            return null;

        }

        return null;

    }

    public static Boolean isPlayerRegistered( String player ){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT uuid FROM users WHERE user = ?", new String[]{player});

            if( res.next() ) return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

        return false;

    }

    public static Integer getPlayerTotalCardAmount(String player){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT SUM(amount) AS total FROM stash INNER JOIN cards ON cards.id = stash.card WHERE cards.category BETWEEN '0' AND '9' AND cards.active = '1' AND stash.user = ?", new String[]{player});

            if( res.next() ) return res.getInt("total");

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;

        }

        return 0;

    }

    public static Integer getPlayerCardAmountCategory( String player, Integer category ){

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT SUM(amount) AS total FROM stash INNER JOIN cards ON cards.id = stash.card WHERE cards.category = ? AND cards.active = '1' AND stash.user = ?", new String[]{category.toString(), player});

            if( res.next() ) return res.getInt("total");

        } catch (SQLException e) {

            e.printStackTrace();

            mInit.getSqlConnection().sqlCloseConnection();

            return 0;

        }

        return 0;

    }

    public static void runRefreshCards() {

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT id, code FROM cards", new String[]{});

            while( res.next() ){

                String cardCode = res.getString("code").toLowerCase();
                String cardID = res.getString("id");

                mInit.cardCode.put(cardCode, cardID);

            }

            Bukkit.getLogger().info( "[" + mInit.getPlugin().getName() + "] Refreshing Enabled Cards" );

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

}
