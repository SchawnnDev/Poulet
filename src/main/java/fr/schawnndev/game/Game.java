package fr.schawnndev.game;

import fr.schawnndev.Main;
import fr.schawnndev.weapon.Weapon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

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

    @Getter
    private boolean canJoinGame;

    @Getter
    private boolean finalManchIsFinished;

    private int currentTaskInt = 0;
    private int currentGeneralTaskInt = 0;

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
        this.currentManche = 0;
        this.playerPoints = new ArrayList<>();
        this.isMancheCurrentlyRunning = false;
        this.r = new Random();
        this.isFinished = false;
        Main.setCurrentGame(this);
        this.canJoinGame = false;
        this.finalManchIsFinished = false;
    }

    /**
     * <ul>Start the game !</ul>
     */

    public void start(){
        isCurrentlyRunning = true;
        canJoinGame = true;

        Bukkit.broadcastMessage(Main.getPrefix() + "§7A game with §c" + getManches() + " manches §7will start in 20 seconds !");
        Bukkit.broadcastMessage(Main.getPrefix() + "§7Type §c/poulet join §7to join the game !");


        currentGeneralTaskInt = Bukkit.getScheduler().runTaskTimer(Main.instance, new Runnable() {

            @Override
            public void run() {

                if (canJoinGame) {

                    GameManager.broadcastMessageInCurrentGame("§aGiving stuff...");

                    for (UUID uuid : getPlayersPlaying()) {
                        Player player = Bukkit.getPlayer(uuid);

                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);

                        if (r.nextInt(2) == 1)
                            GameManager.getHeartLaserWeapon().giveItem(player);
                        else
                            GameManager.getFireLaserWeapon().giveItem(player);

                        playerPoints.add(new Points(uuid, 0));
                    }
                    canJoinGame = false;
                }

                if (getCurrentManche() == getManches()) {
                    if(finalManchIsFinished) {
                        finish();
                    }
                }

                if (!isMancheCurrentlyRunning && !finalManchIsFinished) {
                    startManche();
                }

                for (Entity e : ((Player)Bukkit.getOnlinePlayers().toArray()[0]).getWorld().getEntities()) {
                    if (e instanceof Chicken && e.hasMetadata("poulet") && !e.isDead() && e.isOnGround()) {
                        getBirds().remove(e);
                        ((Chicken)e).damage(100000);
                    }
                }

            }

        }, 20 * 10, 20L).getTaskId();

    }

    /**
     * <ul>Get points</ul>
     */

    public Points getPoints(UUID uuid){
        for(Points points : getPlayerPoints())
            if(points.getUuid() == uuid) return points;
        return null;
    }

    /**
     * <ul>Stop the game !</ul>
     */

    public void stop(boolean force){
        for(int i : taskIdList)
            if(Bukkit.getScheduler().isCurrentlyRunning(i))
                Bukkit.getScheduler().cancelTask(i);
        Bukkit.getScheduler().cancelTask(currentGeneralTaskInt);

        if(force)
            GameManager.broadcastMessageInCurrentGame("§cThe game was stopped !");

        for(UUID uuid : getPlayersPlaying())
        {
            Player player = Bukkit.getPlayer(uuid);
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }

        this.getPlayersPlaying().clear();
    }

    /**
     * <ul>Finish the game !</ul>
     */

    public void finish(){

        Collections.sort(playerPoints, new PointsComparator());

        winner = ((Points)playerPoints.toArray()[0]).getUuid();

        for(UUID uuid : getPlayersPlaying())
        {
            Player player = Bukkit.getPlayer(uuid);
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1.0F, 1.0F);
        }

        Bukkit.broadcastMessage(Main.getPrefix() + "§c" + Bukkit.getPlayer(getWinner()).getName() + " §ahas won the game with §c" + getPlayerPoints().get(GameManager.getIntLocationInList(getWinner(), getPlayerPoints())).getPoints() + "§a birds killed!");

        GameManager.broadcastMessageInCurrentGame("§3Thanks for playing game Poulet !");

        stop(false);

    }

    /**
     * <ul>Add player</ul>
     */

    public void addPlayer(UUID uuid){
        if(!canJoinGame) return;
        if(getPlayersPlaying().size() < getMaxPlayers()){
            if(isCurrentlyRunning){
                Player player = Bukkit.getPlayer(uuid);
                player.teleport(getPlayerSpawnLocation());
                getPlayersPlaying().add(uuid);
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


        GameManager.broadcastMessageInCurrentGame("§6Starting " + (((getCurrentManche() == getManches()) ? "§blatest§6" : "")) + " manche §c(" + getCurrentManche() + ")§6....");

        GameManager.broadcastMessageInCurrentGame("§6You have §c" + time / 20 + "§6 seconds for kill all the chickens !");

        for(UUID uuid : getPlayersPlaying())
        {
            Player player = Bukkit.getPlayer(uuid);
            player.playSound(player.getLocation(), Sound.CAT_MEOW, 1.0F, 1.0F);
        }


       currentTaskInt = Bukkit.getScheduler().runTaskTimer(Main.instance, new Runnable() {

           int currentBirds = 0;

           @Override
           public void run() {
               if (currentBirds > getBirdsPerManche()) {

                   GameManager.broadcastMessageInCurrentGame("§6Manche §c" + getCurrentManche() + " §6is finished !");

                   for(UUID uuid : getPlayersPlaying())
                   {
                       Player player = Bukkit.getPlayer(uuid);
                       player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 1.0F, 1.0F);
                   }

                   Collections.sort(playerPoints, new PointsComparator());

                    GameManager.broadcastMessageInCurrentGame("§6Ranking: §31 = §c" + Bukkit.getPlayer(((Points)playerPoints.toArray()[0]).getUuid()).getName()
                            + "§3 | 2 = §c" +( getPlayersPlaying().size() >= 2 ? Bukkit.getPlayer(((Points)playerPoints.toArray()[1]).getUuid()).getName() : "nobody")
                            + "§3 | 3 = §c" +( getPlayersPlaying().size() >= 3 ? Bukkit.getPlayer(((Points)playerPoints.toArray()[2]).getUuid()).getName() : "nobody"));

                   Bukkit.getScheduler().runTaskLater(Main.instance, new Runnable() {

                       public void run() {
                           if(getCurrentManche() == getManches())
                               finalManchIsFinished = true;

                           isMancheCurrentlyRunning = false;

                       }

                   }, 100L);
                    Bukkit.getScheduler().cancelTask(currentTaskInt);
               } else {
                   Entity poulet = world.spawn(birdsSpawnLocations.get(r.nextInt(birdsSpawnLocations.size())), Chicken.class);
                   poulet.setMetadata("poulet", new FixedMetadataValue(Main.instance, "poulet"));
                   currentBirds++;
               }

           }

       }, 0L, 20L).getTaskId();

        taskIdList.add(currentTaskInt);

    }




}
