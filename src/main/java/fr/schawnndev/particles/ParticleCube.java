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

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParticleCube {

    @Getter @Setter
    private int xLength, yLength, zLength;

    @Getter
    private List<Location> locations;

    @Getter
    private Location location;

    public ParticleCube(Location location, int xLength, int yLength, int zLength){
        this.locations = new ArrayList<>();
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
        this.location = location;
    }

    private void calculate(){
        for(double x = 0d; x <= zLength; x+=0.1){
            locations.add(location.clone().add(0d, 0d, 0d));
            locations.add(location.clone().add(0d, 0d, 0d));
            locations.add(location.clone().add(0d, 0d, 0d));
            locations.add(location.clone().add(0d, 0d, 0d));
        }

        for(double y = 0d; y <= zLength; y+=0.1){
            locations.add(location.clone().add(0d, 0d, 0d));
            locations.add(location.clone().add(0d, 0d, 0d));
            locations.add(location.clone().add(0d, 0d, 0d));
            locations.add(location.clone().add(0d, 0d, 0d));
        }

        for(double z = 0d; z <= zLength; z+=0.1){
            locations.add(location.clone().add(0, 0d, z));
            locations.add(location.clone().add(0d, 5d, z));
            locations.add(location.clone().add(4d, 0d, z));
            locations.add(location.clone().add(4d, 5d, z));
        }

    }

    public void start(){

    }

    public void stop(){}


}
