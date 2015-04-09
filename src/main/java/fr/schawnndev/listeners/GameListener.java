package fr.schawnndev.listeners;

import fr.schawnndev.Main;
import fr.schawnndev.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by SchawnnDev on 07/04/2015.
 */
public class GameListener implements Listener {

    private List<UUID> cooldown = new ArrayList<>();

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        if(Main.getCurrentGame().isCurrentlyRunning() && Main.getCurrentGame().getPlayersPlaying().contains(e.getPlayer().getUniqueId()))
            Main.getCurrentGame().removePlayer(e.getPlayer().getUniqueId());
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setArmorContents(null);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(Main.getCurrentGame().isCurrentlyRunning() && Main.getCurrentGame().getPlayersPlaying().contains(e.getPlayer().getUniqueId()))
            if((e.getItemDrop().getItemStack().getType() == GameManager.getFireLaserWeapon().getItem().getType()) || (e.getItemDrop().getItemStack().getType() == GameManager.getHeartLaserWeapon().getItem().getType()))
                e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player && Main.getCurrentGame().isCurrentlyRunning() && Main.getCurrentGame().getPlayersPlaying().contains(e.getEntity().getUniqueId()))
            e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        if(e.getEntity() instanceof Chicken && e.getEntity().hasMetadata("poulet")){
            e.getDrops().clear();
            e.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_AIR && Main.getCurrentGame().isCurrentlyRunning() && Main.getCurrentGame().getPlayersPlaying().contains(e.getPlayer().getUniqueId())) {
            if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals(GameManager.getFireLaserWeapon().getItemName())) {
                GameManager.getFireLaserWeapon().shoot(e.getPlayer());
                final UUID uuid = e.getPlayer().getUniqueId();
                cooldown.add(uuid);

                Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {

                    @Override
                    public void run() {
                        cooldown.remove(uuid);
                    }

                }, GameManager.getFireLaserWeapon().getSecondsBetweenUses() * 20);
            } else if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equals(GameManager.getHeartLaserWeapon().getItemName())){
                GameManager.getHeartLaserWeapon().shoot(e.getPlayer());
                final UUID uuid = e.getPlayer().getUniqueId();
                cooldown.add(uuid);

                Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {

                    @Override
                    public void run() {
                        cooldown.remove(uuid);
                    }

                }, GameManager.getHeartLaserWeapon().getSecondsBetweenUses() * 20);
            }
        }
    }

}
