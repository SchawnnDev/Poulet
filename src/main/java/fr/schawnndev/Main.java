package fr.schawnndev;

import fr.schawnndev.files.FileManager;
import fr.schawnndev.game.Game;
import fr.schawnndev.game.GameManager;
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
    public static Game currentGame;
    public static Game defaultGame = new Game(0,0,0,null,null,0);
    public static String prefix = "§6[§bPoulet§6] ";


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

}
