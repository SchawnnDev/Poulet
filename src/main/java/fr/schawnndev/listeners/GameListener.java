package fr.schawnndev.listeners;

import fr.schawnndev.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by SchawnnDev on 07/04/2015.
 */
public class GameListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(Main.getCurrentGame().isCurrentlyRunning() && Main.getCurrentGame().getPlayersPlaying().contains(e.getPlayer().getUniqueId()))
            Main.getCurrentGame().removePlayer(e.getPlayer().getUniqueId());
    }

}
