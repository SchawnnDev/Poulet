package fr.schawnndev;

import fr.schawnndev.files.FileManager;
import fr.schawnndev.game.Game;
import fr.schawnndev.game.GameManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Created by SchawnnDev on 04/04/2015.
 *  <b>Infos</b>
 *  <ul>Lombok is a add-on (plugin) to simplify the code "esthetic"</ul>
 *  <ul>Lombok disappears during the compilation (build)</ul>
 */
public class Main extends JavaPlugin {

    public static Plugin instance;
    @Getter
    private static Game currentGame;
    @Getter @Setter
    private static Game defaultGame = new Game(0,0,0,null,null,0);
    @Getter
    private static String prefix = "§6[§bPoulet§6] ";
    @Getter
    private static Location spawn;


    public void onEnable() {

        // > Instances < //

        instance = this;

        // > Files < //

        try {
            FileManager.init();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }

        // > Inits. < //

        GameManager.initWeapons();


    }

    public void onDisable() {

    }

    public static void main(String[] args){
        System.out.println(new Random().nextLong() * 1000);
    }

}
