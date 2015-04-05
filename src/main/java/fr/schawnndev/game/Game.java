package fr.schawnndev.game;

import fr.schawnndev.Main;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    private int manches;

    @Getter (value = AccessLevel.PUBLIC)
    private Location playerSpawnLocation;

    @Getter (value = AccessLevel.PUBLIC)
    private int poulesParManche;

    private List<Location> poulesSpawnLocations;

    private List<Integer> taskIdList;

    @Getter (value = AccessLevel.PUBLIC)
    private boolean isCurrentlyRunning;

    @Getter (value = AccessLevel.PUBLIC)
    private List<UUID> playersPlaying;

    @Getter (value = AccessLevel.PUBLIC)
    private int maxPlayers;

    public Game(int id, int manches, int poulesParManche, Location playerSpawnLocation, List<Location> poulesSpawnLocations, int maxPlayers){
        this.id = id;
        this.manches = manches;
        this.playerSpawnLocation = playerSpawnLocation;
        this.poulesParManche = poulesParManche;
        this.poulesSpawnLocations = poulesSpawnLocations;
        this.taskIdList = new ArrayList<>();
        this.isCurrentlyRunning = false;
        this.playersPlaying = new ArrayList<>();
        this.maxPlayers = maxPlayers;
    }

    /**
     * <ul>Start the game !</ul>
     */

    public void start(){
        final Random r = new Random();
        isCurrentlyRunning = true;

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
