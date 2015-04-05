package fr.schawnndev.weapon;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by SchawnnDev on 04/04/2015.
 */
public class FireLaser extends Weapon {

    public FireLaser(ItemStack item, String itemName, int secondsBetweenUses, Permission permission){
        super(item, itemName, secondsBetweenUses, permission);
    }

    @Override
    public void shoot(Player player) {
        //TODO : Line of sight
    }

}
