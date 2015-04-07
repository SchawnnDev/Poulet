package fr.schawnndev.game;

import fr.schawnndev.Main;
import fr.schawnndev.weapon.Weapon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by SchawnnDev on 04/04/2015.
 */
public class Game {

    /**
     * <ul>Variables..</ul>
     */

    @Getter
    private int id;

    @Getter
    private int manches;

    @Getter
    private Location playerSpawnLocation;

    @Getter
    private int birdsPerManche;

    private List<Location> birdsSpawnLocations;

    private List<Integer> taskIdList;

    @Getter
    private boolean isCurrentlyRunning;

    @Getter
    private List<UUID> playersPlaying;

    @Getter
    private int maxPlayers;

    @Getter
    private List<Entity> birds;

    @Getter
    @Setter
    private int currentManche;

    @Getter
    private List<Points> playerPoints;

    private Random r;

    private boolean isMancheCurrentlyRunning;

    @Getter
    private boolean isFinished;

    @Getter
    private UUID winner;

    public Game(int id, int manches, int birdsPerManche, Location playerSpawnLocation, List<Location> birdsSpawnLocations, int maxPlayers){
        this.id = id;
        this.manches = manches;
        this.playerSpawnLocation = playerSpawnLocation;
        this.birdsPerManche = birdsPerManche;
        this.birdsSpawnLocations = birdsSpawnLocations;
        this.taskIdList = new ArrayList<>();
        this.isCurrentlyRunning = false;
        this.playersPlaying = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.birds = new ArrayList<>();
        this.currentManche = 1;
        this.playerPoints = new ArrayList<>();
        this.isMancheCurrentlyRunning = false;
        this.r = new Random();
        this.isFinished = false;
    }

    /**
     * <ul>Start the game !</ul>
     */

    public void start(){
        isCurrentlyRunning = true;

        Bukkit.broadcastMessage(Main.getPrefix() + "§7A game with §c" + getManches() + " §7will start in 20 seconds !");
        Bukkit.broadcastMessage(Main.getPrefix() + "§7Type §c/poulet join §7to join the game !");

        for(UUID uuid : getPlayersPlaying())
            playerPoints.add(new Points(uuid, 0));

        taskIdList.add(Bukkit.getScheduler().runTaskTimer(Main.instance, new Runnable() {

            @Override
            public void run() {
                if(getCurrentManche() == currentManche){
                    finish();
                }

                if (!isMancheCurrentlyRunning) {
                    startManche();
                }
            }

        }, 20 * 10, 20L).getTaskId());

    }

    /**
     * <ul>Stop the game !</ul>
     */

    public void stop(){
        for(int i : taskIdList)
            if(Bukkit.getScheduler().isCurrentlyRunning(i))
                Bukkit.getScheduler().cancelTask(i);

        for(UUID uuid : getPlayersPlaying()){
            Player p = Bukkit.getPlayer(uuid);
            p.teleport(Main.getSpawn());
        }

        this.getPlayersPlaying().clear();
    }

    /**
     * <ul>Finish the game !</ul>
     */

    public void finish(){

        Bukkit.broadcastMessage(Main.getPrefix() + "§c" + Bukkit.getPlayer(getWinner()).getName() + " §ahas won the game with §c" + getPlayerPoints().get(GameManager.getIntLocationInList(getWinner(), getPlayerPoints())).getPoints() + "§a birds killed!");

        GameManager.broadcastMessageInCurrentGame("");

        GameManager.broadcastMessageInCurrentGame("§6The game will stop in §c5 seconds §6!");

        Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {
            @Override
            public void run() {
                stop();
            }

        }, 100L);



    }

    /**
     * <ul>Add player</ul>
     */

    public void addPlayer(UUID uuid){
        if(getPlayersPlaying().size() > getMaxPlayers()){
            if(isCurrentlyRunning){
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(getPlayerSpawnLocation());

            }
        }
    }

    /**
     * <ul>Remove player</ul>
     */

    public void removePlayer(UUID uuid){
        if(getPlayersPlaying().contains(uuid)) {
            Player player = Bukkit.getPlayer(uuid);
            player.getInventory().clear();
            getPlayersPlaying().remove(uuid);
            GameManager.broadcastMessageQuit(uuid);
        }
    }

    /**
     * <ul>Start new manche</ul>
     */

    public void startManche(){

        isMancheCurrentlyRunning = true;

        final World world = getPlayerSpawnLocation().getWorld();
        final long time = 200 + r.nextInt(birdsPerManche * 14);

        currentManche++;


        GameManager.broadcastMessageInCurrentGame("§6Starting " + ((getCurrentManche() == getManches()) ? "§blatest§6" : "") + " manche §c" + getCurrentManche() + "§6....");

        GameManager.broadcastMessageInCurrentGame("§6You have §c" + time / 20 + "§6 seconds for kill all the chickens !");


       final int task = Bukkit.getScheduler().runTaskTimer(Main.instance, new BukkitRunnable() {

            int currentBirds = 0;

            @Override
            public void run() {
                if(currentBirds > getBirdsPerManche()) {

                    cancel();

                    GameManager.broadcastMessageInCurrentGame("§6Manche §c" +getCurrentManche() + " is finished !");

                    Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {

                        public void run() {
                            isMancheCurrentlyRunning = false;
                        }

                    }, 100L);

                } else {
                    Entity poulet = world.spawn(birdsSpawnLocations.get(r.nextInt(birdsSpawnLocations.size())), Chicken.class);
                    poulet.setMetadata("poulet", new FixedMetadataValue(Main.instance, "poulet"));
                    currentBirds++;
                }

            }

        }, 0L, 20L).getTaskId();

        taskIdList.add(task);

    }




}
