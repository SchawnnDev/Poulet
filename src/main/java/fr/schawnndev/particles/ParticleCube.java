/*
 * ******************************************************
 *  * Copyright (C) 2015 SchawnnDev <contact@schawnndev.fr>
 *  *
 *  * This file (fr.schawnndev.particles.ParticleCube) is part of Poulet.
 *  *
 *  * Created by SchawnnDev on 16/06/15 13:12.
 *  *
 *  * Poulet can not be copied and/or distributed without the express
 *  * permission of SchawnnDev.
 *  ******************************************************
 */

package fr.schawnndev.particles;

import fr.schawnndev.Main;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ParticleCube {

    @Getter @Setter
    private double xLength, yLength, zLength;

    @Getter
    private List<Location> locations;

    @Getter
    private Location startLocation;

    @Getter
    private boolean stopped;

    public ParticleCube(Location startLocation, double xLength, double yLength, double zLength){
        this.locations = new ArrayList<>();
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        this.startLocation = startLocation;
        this.stopped = false;

        calculate();
    }

    private void calculate(){

        System.out.println("Starting cube calculating with x: " + xLength + " y: " + yLength + " z: " + zLength);
        long current = System.currentTimeMillis();

        for(double x = 0d; x <= xLength; x+=0.1){
            locations.add(startLocation.clone().add(x, 0d, 0d));
            locations.add(startLocation.clone().add(x, yLength, 0d));
            locations.add(startLocation.clone().add(x, 0d, zLength));
            locations.add(startLocation.clone().add(x, yLength, zLength));
        }

        for(double y = 0d; y <= yLength; y+=0.1){
            locations.add(startLocation.clone().add(0d, y, 0d));
            locations.add(startLocation.clone().add(xLength, y, 0d));
            locations.add(startLocation.clone().add(0d, y, zLength));
            locations.add(startLocation.clone().add(xLength, y, zLength));
        }

        for(double z = 0d; z <= zLength; z+=0.1){
            locations.add(startLocation.clone().add(0, 0d, z));
            locations.add(startLocation.clone().add(0d, yLength, z));
            locations.add(startLocation.clone().add(xLength, 0d, z));
            locations.add(startLocation.clone().add(xLength, yLength, z));
        }

        System.out.println("Cube calculating finished ! Time: " + (System.currentTimeMillis()-current) + " ms");
        System.out.println("A total of " + locations.size() + " locations were calculated !");

    }

    public void start(){
        new BukkitRunnable(){

            @Override
            public void run() {
                if(stopped){
                    cancel();
                    return;
                }

                for(Location location : getLocations())
                    new UtilParticle(UtilParticle.Particle.FLAME, 0.0D, 1, 0.0001D).sendToLocation(location);


            }

        }.runTaskTimer(Main.instance, 0L, 5L);
    }

    public void stop(){
        this.stopped = true;
    }


}
