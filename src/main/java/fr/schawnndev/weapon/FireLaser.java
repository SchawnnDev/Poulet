package fr.schawnndev.weapon;

import fr.schawnndev.Main;
import fr.schawnndev.particles.UtilParticle;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

/**
 * Created by SchawnnDev on 04/04/2015.
 */
public class FireLaser extends Weapon {

    public FireLaser(ItemStack item, String itemName, int secondsBetweenUses, Permission permission){
        super(item, itemName, secondsBetweenUses, permission);
    }

    public void shoot(Player player) {

        int maxDistance = 15;
        double distanceEntreParticle = 1.0;

        Vector playerDirection = player.getLocation().subtract(0d, 0.5d, 0d).getDirection();
        Location lastParticleLocation = new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY(), player.getEyeLocation().getZ());
        for (int i = 0; i < maxDistance / distanceEntreParticle; i++) {
            new UtilParticle(UtilParticle.Particle.FLAME, 0.0D, 1, 0.0001D).sendToLocation(lastParticleLocation);
            lastParticleLocation.add((playerDirection.getX() * distanceEntreParticle), (playerDirection.getY() * distanceEntreParticle), (playerDirection.getZ() * distanceEntreParticle));
            for (Entity p2 : player.getWorld().getEntities()) {
                if (p2.getLocation().distance(lastParticleLocation) < 1) {
                    if(p2 instanceof Chicken && p2.hasMetadata("poulet")){
                        Main.getCurrentGame().getPoints(player.getUniqueId()).addPoint();
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);
                        Main.getCurrentGame().getBirds().remove(p2);
                        p2.remove();
                        return;
                    }
                }
            }
        }
    }

}
