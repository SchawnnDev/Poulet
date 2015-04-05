package fr.schawnndev.game;

import fr.schawnndev.Main;
import fr.schawnndev.weapon.Weapon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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

    @Getter (value = AccessLevel.PUBLIC)
    public int id;

    @Getter (value = AccessLevel.PUBLIC)
    public int manches;

    @Getter (value = AccessLevel.PUBLIC)
    public Location playerSpawnLocation;

    @Getter (value = AccessLevel.PUBLIC)
    public int birdsPerManche;

    private List<Location> poulesSpawnLocations;

    private List<Integer> taskIdList;

    @Getter (value = AccessLevel.PUBLIC)
    public boolean isCurrentlyRunning;

    @Getter (value = AccessLevel.PUBLIC)
    public List<UUID> playersPlaying;

    @Getter (value = AccessLevel.PUBLIC)
    public int maxPlayers;

    @Getter
    public List<Entity> birds;

    @Getter (value = AccessLevel.PUBLIC)
    @Setter (value = AccessLevel.PUBLIC)
    public int currentManche;

    public Game(int id, int manches, int birdsPerManche, Location playerSpawnLocation, List<Location> birdsSpawnLocations, int maxPlayers){
        this.id = id;
        this.manches = manches;
        this.playerSpawnLocation = playerSpawnLocation;
        this.birdsPerManche = birdsPerManche;
        this.poulesSpawnLocations = birdsSpawnLocations;
        this.taskIdList = new ArrayList<>();
        this.isCurrentlyRunning = false;
        this.playersPlaying = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.birds = new ArrayList<>();
        this.currentManche = 1;
    }

    /**
     * <ul>Start the game !</ul>
     */

    public void start(){
        final Random r = new Random();
        isCurrentlyRunning = true;
       // final int mancheDuration = r.nextLong();

        Bukkit.broadcastMessage(Main.prefix + "§7A game with §c" + manches + " §7will start in 20 seconds !");
        Bukkit.broadcastMessage(Main.prefix + "§7Type §c/poulet join §7to join the game !");

        taskIdList.add(Bukkit.getScheduler().runTaskTimer(Main.instance, new Runnable() {

            @Override
            public void run() {

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
    }

    /**
     * <ul>Add player</ul>
     */

    public void addPlayer(UUID uuid){
        if(this.playersPlaying.size() > this.maxPlayers){
            if(isCurrentlyRunning){
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(this.playerSpawnLocation);

            }
        }
    }




}
