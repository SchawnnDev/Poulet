package fr.schawnndev.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * Created by SchawnnDev on 07/04/2015.
 */
public class Points {

    @Getter
    private UUID uuid;

    @Getter @Setter
    private int points;

    public Points(UUID player, int defaultPoints){
        this.uuid = player;
        this.points = defaultPoints;
    }

    public void addPoint(){
        points++;

        if(Bukkit.getPlayer(uuid) != null)
            Bukkit.getPlayer(uuid).setLevel(points);

    }

    public void removePoint(){
        if(points > 1)
            points--;
    }

}
