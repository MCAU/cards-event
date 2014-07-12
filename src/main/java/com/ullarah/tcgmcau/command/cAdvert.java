package com.ullarah.tcgmcau.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class cAdvert {

    public static void runAdvert(CommandSender sender) {

        if (!(sender instanceof Player)) {

            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.WHITE + ChatColor.BOLD.toString() + "MCAU Collectable Trading Cards!");
            sender.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "XP" + ChatColor.WHITE + " cards! " + ChatColor.GREEN + ChatColor.BOLD.toString() + "MONEY" + ChatColor.WHITE + " cards! " + ChatColor.GOLD + ChatColor.BOLD.toString() + "DISGUISE" + ChatColor.WHITE + " cards! ");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "ITEM" + ChatColor.WHITE + " cards! " + ChatColor.YELLOW + ChatColor.BOLD.toString() + "FLY" + ChatColor.WHITE + " cards! " + ChatColor.RED + ChatColor.BOLD.toString() + "SUMMON" + ChatColor.WHITE + " cards! ");
            sender.sendMessage(ChatColor.WHITE + "What are you waiting for? Register now!");
            sender.sendMessage(ChatColor.WHITE + "Go to " + ChatColor.AQUA + "/survival" + ChatColor.WHITE + " and type " + ChatColor.GOLD + ChatColor.BOLD + "/cards register");
            sender.sendMessage(" ");

        }

    }

}
