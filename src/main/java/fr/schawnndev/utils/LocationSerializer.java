package fr.schawnndev.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Paul on 03/04/2015.
 */
public class LocationSerializer {

    public static String locationToString(Location location, boolean pitchAndYaw){
       String loc = null;

       if(pitchAndYaw)
           loc = "" + location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ() + " " + location.getYaw() + " " + location.getPitch();
        else
           loc = "" + location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();

        return loc;
    }

    public static Location stringToLocation(String location, boolean pitchAndYaw){
        Location loc = null;
        String[] splitted = location.split(" ");
        World world = Bukkit.getWorld(splitted[0]);
        double x = Double.parseDouble(splitted[1]),
                y = Double.parseDouble(splitted[2]),
                z = Double.parseDouble(splitted[3]);

        if(pitchAndYaw){
            float yaw = Float.parseFloat(splitted[4]),
                  pitch = Float.parseFloat(splitted[5]);

            loc = new Location(world, x , y, z, yaw, pitch);
        } else {
            loc = new Location(world, x, y ,z);
        }

        return loc;
    }

}
