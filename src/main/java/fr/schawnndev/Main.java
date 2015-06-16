package fr.schawnndev;

import fr.schawnndev.files.FileManager;
import fr.schawnndev.game.Game;
import fr.schawnndev.game.GameManager;
import fr.schawnndev.listeners.GameListener;
import fr.schawnndev.particles.ParticleCube;
import fr.schawnndev.utils.LocationSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
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

        for(int i = 0; i < 10; i+=3){
            birdsLocations.add(middleBirdsSpawn.clone().add(i, 0.0, 0.0));
            birdsLocations.add(middleBirdsSpawn.clone().add(-i, 0.0, 0.0));
        }


        defaultGame = new Game(1, 5, 35, playerSpawn, birdsLocations, 10);

        // > Commands < //

        getCommand("poulet").setExecutor(this);


    }

    public void onDisable() {

    }

    private List<ItemStack> getRandomItemStacks(Random random, boolean fullRandom){
        List<ItemStack> itemStacks = new ArrayList<>();

        if(fullRandom) {

            int fullR = random.nextInt(60);

            if(fullR == 23)
                itemStacks.add(new ItemStack(Material.DIAMOND, (random.nextInt(500) == 45 ? 2 : 1)));

            int r = random.nextInt(20);

            for(int i = 0; i < r; i++){
                int material = i + random.nextInt(250);
                Material m = Material.getMaterial(material);

                if(m != null && m != Material.DIAMOND && material != 60 && material != 138  && m != Material.DIAMOND_BLOCK  && m != Material.GOLD_BLOCK  && m != Material.REDSTONE_BLOCK && m != Material.COAL_BLOCK)
                    itemStacks.add(new ItemStack(m, random.nextInt(20)+1));

            }

        } else {

        }

        return itemStacks;
    }

    /**
     * <ul>Commands..</ul>
     */

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(cmd.getName().equalsIgnoreCase("cube")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("§cThe command sender must be a player !");
                return true;
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage("§cCorrect usage: §f/cube <xLength> <yLength> <zLength>");
                return true;
            } else if (args.length == 3) {
                double xLength, yLength, zLength;

                try {
                    xLength = Double.parseDouble(args[0]);
                    yLength = Double.parseDouble(args[1]);
                    zLength = Double.parseDouble(args[2]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cCorrect usage: §f/cube <double> <double> <double>");
                    return true;
                }

                if(xLength != 0 && yLength != 0 && zLength != 0){

                    final ParticleCube cube = new ParticleCube(player.getLocation().add(0d, 2d,0d), xLength, yLength, zLength);
                    cube.start();

                    new BukkitRunnable(){

                        @Override
                        public void run() {
                            if(cube != null && !cube.isStopped())
                                cube.stop();
                        }

                    }.runTaskLater(this, 20 * 60 * 30);

                } else {
                    player.sendMessage("§cCorrect usage: §f/cube <double> <double> <double> §c| §fNot 0 !!");
                    return true;
                }

            }
        }

        if(cmd.getName().equalsIgnoreCase("chest")){

            if(!(sender instanceof Player)){
                sender.sendMessage("§cThe command sender must be a player !");
                return true;
            }

            Player player = (Player)sender;

            if(args.length == 0){
                player.sendMessage("§cCorrect usage: §f/chest <number> <x max random> <y max random>");
                return true;
            } else if (args.length == 3){
                int chests;
                int xmaxrandom;
                int ymaxrandom;

                try {
                    chests = Integer.parseInt(args[0]);
                    xmaxrandom = Integer.parseInt(args[1]);
                    ymaxrandom = Integer.parseInt(args[2]);
                } catch (NumberFormatException e){
                    player.sendMessage("§cCorrect usage: §f/chest <int> <int> <int>");
                    return true;
                }

                Random r = new Random();

                for(int i = 0; i < chests; i++){

                    int x = player.getLocation().getBlockX()+r.nextInt(xmaxrandom) * (r.nextBoolean() ? 1 : -1),
                    z = player.getLocation().getBlockZ()+r.nextInt(ymaxrandom) * (r.nextBoolean() ? 1 : -1),
                    y = player.getWorld().getHighestBlockYAt(x, z);
                    player.getWorld().getBlockAt(x, y, z).setType(Material.CHEST);
                    Chest chest = (Chest)player.getWorld().getBlockAt(x, y, z).getState();

                    if(chest != null){

                        for(ItemStack itemStack : getRandomItemStacks(r, true))
                            chest.getInventory().addItem(itemStack);

                        chest.update(true);

                        DecimalFormat df = new DecimalFormat("##.#");

                        Bukkit.broadcastMessage("§2Nouveau coffre en X: " + df.format(chest.getLocation().getX())
                        + " | Y: " + df.format(chest.getLocation().getY()) + " | Z: " + df.format(chest.getLocation().getZ()));

                    }


                }


            }



        }


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
                            getDefaultGame().stop(true);
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
                        if(getDefaultGame().getPlayersPlaying().contains(player.getUniqueId())){
                            player.sendMessage("§cYou're already in the game !");
                            return true;
                        }
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
                        if(!getDefaultGame().getPlayersPlaying().contains(player.getUniqueId())){
                            player.sendMessage("§cYou're not in the game !");
                            return true;
                        }
                        getDefaultGame().removePlayer(player.getUniqueId());
                        player.sendMessage("§aYou left the game !");
                        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0F, 1.0F);
                        return true;
                    }

                }
            } else {
                for(Entity e : player.getWorld().getEntities()) {
                    if(e instanceof Chicken) {
                        ((Chicken) e).damage(10000000);
                    }
                }
                player.sendMessage("§cToo many args !");
                return true;
            }
        }

        return false;

    }
}
