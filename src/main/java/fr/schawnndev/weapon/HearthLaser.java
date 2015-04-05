package fr.schawnndev.weapon;

import fr.schawnndev.Main;
import fr.schawnndev.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

/**
 * Created by SchawnnDev on 04/04/2015.
 */
public class HearthLaser extends Weapon {

    public HearthLaser(ItemStack item, String itemName, int secondsBetweenUses, Permission permission){
        super(item, itemName, secondsBetweenUses, permission);
    }

    @Override
    public void shoot(Player player) {
        //TODO : Line of sight
    }

}
