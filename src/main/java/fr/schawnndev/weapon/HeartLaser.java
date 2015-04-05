package fr.schawnndev.weapon;

import fr.schawnndev.Main;
import fr.schawnndev.particles.UtilParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by SchawnnDev on 04/04/2015.
 */
public class HeartLaser extends Weapon {

    public HeartLaser(ItemStack item, String itemName, int secondsBetweenUses, Permission permission){
        super(item, itemName, secondsBetweenUses, permission);
    }

    @Override
    public void shoot(Player player) {
        //TODO : Line of sight
        List<Block> blocks = new ArrayList<Block>();
        BlockIterator lineOfSight = new BlockIterator(player, 15);
        int i = 0;
        while (lineOfSight.hasNext()) {
            if (i > 5)
                break;
            Block b = lineOfSight.next();
            blocks.add(b);
            i++;
        }

        for (Block b : blocks) {
            new UtilParticle(UtilParticle.Particle.HEART, 0.0D, 1, 0.0001D).sendToLocation(b.getLocation());
            for (Entity p2 : player.getWorld().getEntities()) {
                if (p2.getLocation().distanceSquared(b.getLocation()) < 0.75 && p2 != player) {
                    if(p2 instanceof Chicken && Main.currentGame.birds.contains(p2)){

                    }
                }
            }
        }

    }

}
