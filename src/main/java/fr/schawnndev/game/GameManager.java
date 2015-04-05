package fr.schawnndev.game;

import fr.schawnndev.Main;
import fr.schawnndev.weapon.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by SchawnnDev on 04/04/2015.
 */

public class GameManager {

    @Getter (value = AccessLevel.PUBLIC)
    @Setter (value = AccessLevel.PUBLIC)
    private static Game currentGame = Main.defaultGame;

    @Getter (value = AccessLevel.PUBLIC)
    private static Weapon heartLaserWeapon;

    @Getter (value = AccessLevel.PUBLIC)
    private static Weapon fireLaserWeapon;


    public static void initWeapons(){

        ItemStack heartLaser = new ItemStack(Material.DIAMOND_BARDING);
        ItemStack fireLaser = new ItemStack(Material.GOLD_BARDING);

        ItemMeta hearthLaserMeta =heartLaser.getItemMeta();
        ItemMeta fireLaserMeta =fireLaser.getItemMeta();

        hearthLaserMeta.setDisplayName("§dHearthLaser");
        fireLaserMeta.setDisplayName("§dFireLaser");

        fireLaser.setItemMeta(fireLaserMeta);
        heartLaser.setItemMeta(hearthLaserMeta);

        heartLaserWeapon = new HeartLaser(heartLaser, "§dHearthLaser", 1, null);
        fireLaserWeapon = new FireLaser(fireLaser, "§dFireLaser", 1, null);
    }

}
