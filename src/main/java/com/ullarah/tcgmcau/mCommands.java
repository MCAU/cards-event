package com.ullarah.tcgmcau;

import com.ullarah.tcgmcau.command.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class mCommands implements CommandExecutor{

    @Override
    public boolean onCommand( CommandSender sender, Command command, String s, String[] args ) {

        if( command.getName().equalsIgnoreCase( "cards" ) )
            commandCards( sender, args );

        return true;

    }

    private void commandCards( CommandSender sender, String[] args ){

        String consoleTools = mInit.getMsgPrefix() + ChatColor.WHITE + "check | purge | give | random | reload | maintenance | version";

        if ( args.length == 0 ) {

            if( !( sender instanceof Player ) )
                sender.sendMessage( consoleTools );
            else
                cAdvert.runAdvert(sender);

        }

        else try {

            switch ( validCommands.valueOf( args[0].toUpperCase() ) ) {

                case CHECK:
                    if( !mInit.getMaintenanceCheck())
                        cCheck.runPlayerCardInfo(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case SHELP:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        cHelp.runStaffHelp(sender);
                    break;

                case PURGE:
                    if( !mInit.getMaintenanceCheck())
                        cPurge.runStaffPurge(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case GIVE:
                    if( !mInit.getMaintenanceCheck())
                        cGive.runGiveCard(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case RANDOM:
                    if( !mInit.getMaintenanceCheck())
                        cRandom.runRandomCard(sender, args);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case LIST:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cList.runListCards(sender, args);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case INFO:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage(mInit.getMsgNoConsole());
                    else
                        if( !mInit.getMaintenanceCheck())
                            cInfo.runCardInfo(sender, args);
                        else
                            sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case RELOAD:
                    if( !mInit.getMaintenanceCheck())
                        cReload.runReload(sender);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case MAINTENANCE:
                    cMaintenance.runMaintenance(sender, args);
                    break;

                case ADVERT:
                    if( !mInit.getMaintenanceCheck())
                        cAdvert.runAdvert(sender);
                    else
                        sender.sendMessage(mInit.getMaintenanceMessage());
                    break;

                case VERSION:
                    sender.sendMessage(new String[] {
                            mInit.getMsgPrefix() + "Version " + mInit.getPlugin().getDescription().getVersion(),
                            mInit.getMsgPrefix() + mInit.getPlugin().getDescription().getDescription().split("\n")[1]
                    });
                    break;

                default:
                    if( !( sender instanceof Player ) )
                        sender.sendMessage( consoleTools );
                    else
                    cAdvert.runAdvert( sender );

            }

        } catch ( IllegalArgumentException e ) {

            if( !( sender instanceof Player ) )
                sender.sendMessage( consoleTools );
            else
                cAdvert.runAdvert(sender);

        }

    }

    private enum validCommands {
        ADVERT, CHECK, GIVE,
        INFO, LIST, MAINTENANCE,
        PURGE, RANDOM, RELOAD,
        SHELP, VERSION
    }

}
