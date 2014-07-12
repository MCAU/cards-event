package com.ullarah.tcgmcau;

import com.ullarah.tcgmcau.command.cGive;
import com.ullarah.tcgmcau.command.cRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mEvents implements Listener{

    final String youFound(String typeCard) {
        return "You have found " + ChatColor.translateAlternateColorCodes('&', typeCard + " card!");
    }

    private static boolean isBetween(int value, int lower, int upper) {
        return value >= lower && value <= upper;
    }

    private String randomCard( Player player ) {

        List<Integer> category = new ArrayList<>();

        Integer rarePercent = new Random().nextInt( 101 );

        Integer rareIndex = 0;
        String rareName = null;

        Boolean enabledVeryCommon = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.VERY_COMMON" );
        Boolean enabledCommon = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.COMMON" );
        Boolean enabledLessCommon = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.LESS_COMMON" );
        Boolean enabledRare = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.RARE" );
        Boolean enabledVeryRare = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.VERY_RARE" );
        Boolean enabledExtraRare = mConfig.getPlayerConfig().getBoolean( player.getPlayerListName() + ".rarity.EXTRA_RARE" );

        Integer rarityVeryCommon = mConfig.getChanceConfig().getInt( "rarity.VERY_COMMON" );
        Integer rarityCommon  = mConfig.getChanceConfig().getInt( "rarity.COMMON" );
        Integer rarityLessCommon = mConfig.getChanceConfig().getInt( "rarity.LESS_COMMON" );
        Integer rarityRare  = mConfig.getChanceConfig().getInt( "rarity.RARE" );
        Integer rarityVeryRare = mConfig.getChanceConfig().getInt( "rarity.VERY_RARE" );
        Integer rarityExtraRare = mConfig.getChanceConfig().getInt( "rarity.EXTRA_RARE" );

        if( isBetween( rarePercent, rarityVeryCommon , rarityCommon ) && enabledVeryCommon ) {
            rareIndex = Rarities.VERY_COMMON.getRareNum();
            rareName = "a &7Very Common&f";
        }

        if( isBetween( rarePercent, rarityCommon, rarityLessCommon ) && enabledCommon ) {
            rareIndex = Rarities.COMMON.getRareNum();
            rareName = "a &aCommon&f";
        }

        if( isBetween( rarePercent, rarityLessCommon, rarityRare ) && enabledLessCommon ) {
            rareIndex = Rarities.LESS_COMMON.getRareNum();
            rareName = "a &3Less Common&f";
        }

        if( isBetween( rarePercent, rarityRare, rarityVeryRare ) && enabledRare ) {
            rareIndex = Rarities.RARE.getRareNum();
            rareName = "a &9Rare&f";
        }

        if( isBetween( rarePercent, rarityVeryRare, rarityExtraRare ) && enabledVeryRare ) {
            rareIndex = Rarities.VERY_RARE.getRareNum();
            rareName = "a &dVery Rare&f";
        }

        if( isBetween( rarePercent, rarityExtraRare, 101 ) && enabledExtraRare ) {
            rareIndex = Rarities.EXTRA_RARE.getRareNum();
            rareName = "an &cExtra Rare&f";
        }

        if( rareName != null){

            category.clear();

            try {

                ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM cards WHERE category = ? AND active = '1'", new String[]{rareIndex.toString()});

                while ( res.next() ) category.add( res.getInt("id") );

            } catch (SQLException e) {

                e.printStackTrace();

            }

            if( category.size() == 0 ) return null;

            Integer cardIndex = category.get( new Random().nextInt( category.size() ) );

            switch( mCheck.checkTotal( player, cardIndex ) ) {

                case 0:
                    return mInit.getMsgPrefix() + mConfig.getLanguageConfig().getString("maximumcards");

                case 1:
                    break;

                case 2:
                    randomCard( player );

            }

            try {

                ResultSet res = mInit.getSqlConnection().sqlQuery("SELECT * FROM stash WHERE card = ? AND user = ?", new String[]{cardIndex.toString(), player.getPlayerListName()});

                if ( res.next() ) {

                    Integer setAmount = res.getInt( "amount" ) + 1;

                    mInit.getSqlConnection().sqlUpdate("UPDATE stash SET amount = ? WHERE card = ? AND user = ?", new String[]{setAmount.toString(), cardIndex.toString(), player.getPlayerListName()});

                    return mInit.getMsgPrefix() + youFound( rareName );

                } else {

                    mInit.getSqlConnection().sqlUpdate("INSERT INTO stash VALUES ( null, ?, ?, '1' )", new String[]{player.getPlayerListName(), cardIndex.toString()});

                    return mInit.getMsgPrefix() + youFound( rareName );

                }

            } catch (SQLException e) {

                e.printStackTrace();

                return null;

            }

        } else {

            return null;

        }

    }

    @EventHandler
    public void onEntityDeath( final EntityDeathEvent event ){

        if( !mInit.getMaintenanceCheck()){

            Entity entity = event.getEntity();

            Integer ran = new Random().nextInt( 100 );

            Entity mob = event.getEntity();

            String gotCard = null;

            if( !( entity instanceof Player ) ) if (entity.getLastDamageCause() instanceof EntityDamageByEntityEvent) {

                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entity.getLastDamageCause();

                if (entityDamageByEntityEvent.getDamager() instanceof Player) {

                    Player player = (Player) entityDamageByEntityEvent.getDamager();

                    if ( mCheck.checkEntity( mob, ran ) && mCheck.checkRegion(player) ) {

                        if ( mCheck.checkRegister(player) ) gotCard = randomCard( player );
                        else player.sendMessage(mInit.getMsgPrefix() + mConfig.getLanguageConfig().getString("notregistered"));

                        if (gotCard != null) player.sendMessage(gotCard);

                    }

                }

            }

        }

    }

    @EventHandler
    public void onPlayerInteract( PlayerInteractEvent event ) {

        Player player = event.getPlayer();

        if( event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK )

            if (event.getClickedBlock().getState() instanceof Sign) {

                Sign signClicked = (Sign) event.getClickedBlock().getState();

                if (signClicked.getLine(0).matches(".*?(\\[Cards])")) {

                    List<String> playerEventList = new ArrayList<>();

                    if (mInit.eventFinished.get(player.getUniqueId()) == null)
                        mInit.eventFinished.put(player.getUniqueId(), playerEventList);
                    else playerEventList = mInit.eventFinished.get(player.getUniqueId());

                    String signClickedEventName = ChatColor.stripColor(signClicked.getLine(1));
                    String signClickedCardName = ChatColor.stripColor(signClicked.getLine(3));

                    if (playerEventList.contains(signClickedEventName))
                        player.sendMessage(mInit.getMsgPrefix() + ChatColor.RED + "Don't be greedy. You already got your card!");
                    else {

                        player.sendMessage(mInit.getMsgPrefix() + "Congratulations on Beating: " + ChatColor.AQUA + signClickedEventName);

                        if (signClickedCardName.matches(".*?(Random Card)")) {

                            String[] args = {"random", player.getPlayerListName()};
                            cRandom.runRandomCard(Bukkit.getConsoleSender(), args);
                            playerEventList.add(signClickedEventName);
                            mInit.eventFinished.put(player.getUniqueId(), playerEventList);

                        } else {

                            String[] args = {"give", player.getPlayerListName(), signClickedCardName.toLowerCase()};
                            cGive.runGiveCard(Bukkit.getConsoleSender(), args);
                            playerEventList.add(signClickedEventName);
                            mInit.eventFinished.put(player.getUniqueId(), playerEventList);

                        }

                    }

                    event.setCancelled(true);

                }

            }

    }

    private enum Rarities {

        VERY_COMMON(1), COMMON(2), LESS_COMMON(3),
        RARE(4), VERY_RARE(5), EXTRA_RARE(6);

        private final int rareNum;

        Rarities(int r) {
            rareNum = r;
        }

        public int getRareNum() {
            return rareNum;
        }
    }

}