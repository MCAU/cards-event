package com.ullarah.tcgmcau;

import org.bukkit.Bukkit;

class mTask {

    public static void resetPlayerEvents(){

        Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(mInit.getPlugin(), new Runnable() {

            public void run() {

                mInit.eventFinished.clear();

            }

        }, 21600L, 21600 * 20);

    }

}