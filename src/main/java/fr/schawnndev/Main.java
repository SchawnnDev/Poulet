package fr.schawnndev;

import fr.schawnndev.files.FileManager;
import fr.schawnndev.game.Game;
import fr.schawnndev.game.GameManager;
import fr.schawnndev.listeners.GameListener;
import fr.schawnndev.utils.LocationSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by SchawnnDev on 04/04/2015.
 *  <b>Infos</b>
 *  <ul>Lombok is a add-on (plugin) to simplify the code and and to improve the aesthetics "esthetic"</ul>
 *  <ul>Lombok disappears during the compilation (build)</ul>
 *  <ul>The plugin is normaly a <b>multi-game plugin</b>, but i didn't have time to finish..</ul>
 *  <ul>You can add games with create a <b>new</b> Game (example in commands)</ul>
 *  <ul>Why english ? But i'm french (:</ul>
 */
public class Main extends JavaPlugin {

    public static Plugin instance;
    @Getter @Setter
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
        getServer().getPluginManager().registerEvents(new GameListener(), this);

        // > Game < //

        Location playerSpawn = LocationSerializer.stringToLocation((String) FileManager.getCoordsConfig().get("playerSpawn"), true);

        Location middleBirdsSpawn =  LocationSerializer.stringToLocation((String) FileManager.getCoordsConfig().get("birdsSpawn"), true);

        List<Location> birdsLocations = new ArrayList<>();

        birdsLocations.add(middleBirdsSpawn);

        for(int i = 0; i < 10; i+=2){
            birdsLocations.add(middleBirdsSpawn.clone().add(0.0, 0.0, i));
            birdsLocations.add(middleBirdsSpawn.clone().add(0.0, 0.0, -i));
        }


        defaultGame = new Game(1, 5, 35, playerSpawn, birdsLocations, 5);

        // > Commands < //

        getCommand("poulet").setExecutor(this);


    }

    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("poulet")){

            if(!(sender instanceof Player)){
                sender.sendMessage("§cThe command sender must be a player !");
                return true;
            }

            Player player = (Player)sender;

            if(args.length == 0){

                if(player.hasPermission("poulet.start") || player.isOp())
                    player.sendMessage("§6Start a game: §7/poulet start");

                if(player.hasPermission("poulet.stop") || player.isOp())
                    player.sendMessage("§6Stopping a game: §7/poulet stop");

                if(player.hasPermission("poulet.setplayerspawn") || player.isOp())
                    player.sendMessage("§6Setting playerspawn: §7/poulet setplayerspawn");

                if(player.hasPermission("poulet.setbirdsspawn") || player.isOp())
                    player.sendMessage("§6Setting birdspawn: §7/poulet setbirdsspawn");

                player.sendMessage("§6Joining a game: §7/poulet join");
                player.sendMessage("§6Quit a game: §7/poulet quit");

                return true;
            } else if (args.length == 1){
                if(args[0].equalsIgnoreCase("start")){

                    if(player.hasPermission("poulet.start") || player.isOp()) {

                        if (getDefaultGame().isCurrentlyRunning()) {
                            player.sendMessage("§cThe game has already started !");
                            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                            return true;
                        } else {
                            player.sendMessage("§aStarting game....");
                            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                            getDefaultGame().start();
                            return true;
                        }

                    } else {
                        player.sendMessage("§cYou don't have permission to use this command !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        return true;
                    }

                } else if(args[0].equalsIgnoreCase("setplayerspawn")) {

                    if (player.hasPermission("poulet.setplayerspawn") || player.isOp()) {
                        player.sendMessage("§aSetting player spawn at " + LocationSerializer.locationToString(player.getLocation(), true));
                        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);

                        try {
                            FileManager.getCoordsConfig().set("playerSpawn", LocationSerializer.locationToString(player.getLocation(), true));
                            FileManager.save();
                        } catch (Exception e){
                            player.sendMessage("§cErreur: " + e.getMessage());
                            return true;
                        }
                    } else {
                        player.sendMessage("§cYou don't have permission to use this command !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        return true;
                    }

                } else if(args[0].equalsIgnoreCase("setbirdsspawn")) {

                    if (player.hasPermission("poulet.setplayerspawn") || player.isOp()) {
                        player.sendMessage("§aSetting player spawn at " + LocationSerializer.locationToString(player.getLocation(), true));
                        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);

                        try {
                            FileManager.getCoordsConfig().set("birdsSpawn", LocationSerializer.locationToString(player.getLocation(), true));
                            FileManager.save();
                        } catch (Exception e){
                            player.sendMessage("§cErreur: " + e.getMessage());
                            return true;
                        }
                    } else {
                        player.sendMessage("§cYou don't have permission to use this command !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        return true;
                    }

                } else if(args[0].equalsIgnoreCase("stop")){

                    if(player.hasPermission("poulet.stop") || player.isOp()) {

                        if (!getDefaultGame().isCurrentlyRunning()) {
                            player.sendMessage("§cThe game isn't running !");
                            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                            return true;
                        } else {
                            player.sendMessage("§aStopping game....");
                            getDefaultGame().stop();
                            player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                            return true;
                        }

                    } else {
                        player.sendMessage("§cYou don't have permission to use this command !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        return true;
                    }

                } else if(args[0].equalsIgnoreCase("join")){

                    if (!getDefaultGame().isCurrentlyRunning()) {
                        player.sendMessage("§cThe game isn't running !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        return true;
                    } else {
                        player.sendMessage("§aJoining game....");
                        getDefaultGame().addPlayer(player.getUniqueId());
                        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                        return true;
                    }

                } else if(args[0].equalsIgnoreCase("quit")){

                    if (!getDefaultGame().isCurrentlyRunning()) {
                        player.sendMessage("§cThe game isn't running !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
                        return true;
                    } else {
                        getDefaultGame().removePlayer(player.getUniqueId());
                        player.sendMessage("§aYou left the game !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                        return true;
                    }

                }
            } else {
                player.sendMessage("§cToo many args !");
                return true;
            }
        }

        return false;

    }
}
