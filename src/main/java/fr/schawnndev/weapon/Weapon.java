package fr.schawnndev.weapon;

import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;

/**
 * Created by SchawnnDev on 04/04/2015.
 * <ul>Weapon basic class</ul>
 */
public abstract class Weapon {

    /**
     * <ul>Weapon type enum</ul>
     */

    public enum WeaponType {
        HEART_LASER, FIRE_LASER;
    }

    @Getter (value = AccessLevel.PUBLIC)
    private ItemStack item;
    @Getter(value = AccessLevel.PUBLIC)
    private int secondsBetweenUses;
    @Getter(value = AccessLevel.PUBLIC)
    private Permission permission;
    @Getter(value = AccessLevel.PUBLIC)
    private String itemName;

    public Weapon(ItemStack item, String itemName, int secondsBetweenUses, Permission permission) {
        this.item = item;
        this.secondsBetweenUses = secondsBetweenUses;
        this.permission = permission;
        this.itemName = itemName;
    }

    /**
     * <ul>Give the player the weapon</ul>
     * @param player Player
     */

    public void giveItem(Player player) {
        player.getInventory().addItem(this.item);
    }

    /**
     * <ul>Kabooooooom !</ul>
     * @param player Shooter
     */

    public abstract void shoot(Player player);

}
