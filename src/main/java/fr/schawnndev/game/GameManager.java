package fr.schawnndev.game;

import fr.schawnndev.Main;
import fr.schawnndev.weapon.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

/**
 * Created by SchawnnDev on 04/04/2015.
 */

public class GameManager {

    @Getter
    @Setter
    private static Game currentGame = Main.getDefaultGame();

    @Getter
    private static Weapon heartLaserWeapon;

    @Getter
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

        heartLaserWeapon = new HeartLaser(heartLaser, "§dHearthLaser", 2, null);
        fireLaserWeapon = new FireLaser(fireLaser, "§dFireLaser", 2, null);
    }

    public static void broadcastMessage(Game game, String message){
        for(UUID uuid : game.getPlayersPlaying()){
            Player p = Bukkit.getPlayer(uuid);
            p.sendMessage(message);
        }
    }

    public static void broadcastMessageInCurrentGame(String message){
        for(UUID uuid : Main.getCurrentGame().getPlayersPlaying()) {
            if (uuid != null) {
                Player p = Bukkit.getPlayer(uuid);
                p.sendMessage(message);
            }
        }
    }

    public static void broadcastMessageQuit(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        for(UUID u : Main.getCurrentGame().getPlayersPlaying()){
            Player p = Bukkit.getPlayer(uuid);
            p.sendMessage("§c" +player.getName() + " §6has quit the game !");
        }
    }

    public static int getIntLocationInList(Object object, List<?> list){
        for(int i = 0; i < list.size();i++)
            if(list.get(i).equals(object))
                return i;

        return 0;
    }



}
