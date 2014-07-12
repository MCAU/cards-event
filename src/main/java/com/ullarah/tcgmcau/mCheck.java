package com.ullarah.tcgmcau;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class mCheck {

    public static boolean checkRegister ( Player player ) {

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM users WHERE uuid = ?", new String[]{player.getUniqueId().toString().replace("-", "")});

            return res.next();

        } catch (SQLException e) {

            e.printStackTrace();

            return false;

        }

    }

    public static Integer checkTotal ( Player player, Integer card ) {

        Integer cardSetSingle = mConfig.getConfig().getInt( "cards.single.default" );
        Integer cardSetTotal = mConfig.getConfig().getInt( "cards.total.default" );

        if( player.hasPermission( "cards.staff" ) ) {
            cardSetSingle = mConfig.getConfig().getInt( "cards.single.staff" );
            cardSetTotal = mConfig.getConfig().getInt( "cards.total.staff" );
        }

        else if( player.hasPermission( "cards.dplus" ) ) {
            cardSetSingle = mConfig.getConfig().getInt( "cards.single.dplus" );
            cardSetTotal = mConfig.getConfig().getInt( "cards.total.dplus" );
        }

        else if( player.hasPermission( "cards.donator" ) ) {
            cardSetSingle = mConfig.getConfig().getInt( "cards.single.donator" );
            cardSetTotal = mConfig.getConfig().getInt( "cards.total.donator" );
        }

        else if( player.hasPermission( "cards.plus" ) ) {
            cardSetSingle = mConfig.getConfig().getInt( "cards.single.plus" );
            cardSetTotal = mConfig.getConfig().getInt( "cards.total.plus" );
        }

        try {

            ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT SUM(amount) AS total FROM stash INNER JOIN cards ON cards.id = stash.card WHERE cards.category BETWEEN '1' AND '6' AND cards.active = '1' AND stash.user = ?", new String[]{player.getPlayerListName()});

            if( res.next() ) {

                Integer cardTotal;

                if( res.getString( "total" ) == null ) cardTotal = 0;
                else cardTotal = res.getInt("total");

                if( cardTotal < cardSetTotal ) {

                    res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE user = ? AND card = ?", new String[]{player.getPlayerListName(), card.toString()});

                    if( res.next() ) {

                        Integer cardSingle;

                        if( res.getString( "amount" ) == null ) cardSingle = 0;
                        else cardSingle = res.getInt("amount");

                        if( cardSingle < cardSetSingle ) return 1;
                        else return 2;

                    } else return 1;

                } else return 0;

            } else return 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return 0;

        }

    }

    public static Boolean checkEntity( Entity mob, Integer ran ) {

        Integer val = 0;

        String mobType = mob.getType().toString();
        Integer mobEntity = mob.getEntityId();

        Boolean mobSpawner = mConfig.getConfig().getBoolean( "spawner" );

        if( mInit.entitySpawners.contains(mobEntity) && !mobSpawner ){

            mInit.entitySpawners.remove( mInit.entitySpawners.indexOf( mobEntity ) );

        } else {

            if( mobType.matches( "BAT|CHICKEN|COW|HORSE|MUSHROOM_COW|OCELOT|PIG|SHEEP|SQUID|VILLAGER" ) ) {
                val = mConfig.getChanceConfig().getInt("chance.passive." + mobType);
            }

            if( mobType.matches( "ENDERMAN|IRON_GOLEM|PIG_ZOMBIE|WOLF" ) ) {
                val = mConfig.getChanceConfig().getInt("chance.neutral." + mobType);
            }

            if( mobType.matches( "BLAZE|CAVE_SPIDER|CREEPER|ENDER_DRAGON|GHAST|GIANT|MAGMA_CUBE|SILVERFISH|SLIME|SPIDER|WITCH|WITHER" ) ) {
                val = mConfig.getChanceConfig().getInt("chance.hostile." + mobType);
            }

            if( mobType.matches("SKELETON") ) {
                Skeleton skelly = (Skeleton) mob;

                if( skelly.getSkeletonType() == Skeleton.SkeletonType.WITHER )
                    val = mConfig.getChanceConfig().getInt("chance.hostile.SKELETON.WITHER");
                else val = mConfig.getChanceConfig().getInt("chance.hostile.SKELETON.NORMAL");

            }

            if( mobType.matches( "ZOMBIE" ) ) {
                Zombie zambie = (Zombie) mob;

                if ( zambie.isBaby() ) {
                    val = mConfig.getChanceConfig().getInt("chance.hostile.ZOMBIE.BABY");
                } else if ( zambie.isVillager() ) {
                    val = mConfig.getChanceConfig().getInt("chance.hostile.ZOMBIE.VILLAGER");
                } else {
                    val = mConfig.getChanceConfig().getInt("chance.hostile.ZOMBIE.NORMAL");
                }

            }

        }

        return ran < val;

    }

    public static Boolean checkRegion( Player player ) {

        Vector playerVector = new Vector( player.getLocation().getX(), player.getLocation().getBlockY(), player.getLocation().getZ() );

        ApplicableRegionSet regionPlayer = WGBukkit.getRegionManager(player.getLocation().getWorld()).getApplicableRegions( playerVector );

        List<String> regionDenied = mConfig.getConfig().getStringList("deniedregions");

        for(ProtectedRegion currentRegion : regionPlayer )
            if (regionDenied.contains(currentRegion.getId())) {
                return false;
            }

        return true;

    }

}
