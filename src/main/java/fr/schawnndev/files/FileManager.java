package fr.schawnndev.files;

import fr.schawnndev.Main;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

/**
 * Created by SchawnnDev on 04/04/2015.
 * <b>Yaml configurations file manager</b>
 */

public class FileManager {

    /**
     *  <ul>Vars,</ul>
     *  <ul>files, etc..</ul>
     */

    @Getter
    private static File coords;

    @Getter
    private static FileConfiguration coordsConfig;

    /**
     *  <ul>Init the file manager, check files, create and set configs</ul>
     */

    public static void init() throws FileNotFoundException {

        coords = new File(Main.instance.getDataFolder().getAbsolutePath() + File.pathSeparator + "coords.yml");

        if(!coords.exists()){
            try {
                coords.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        coordsConfig = YamlConfiguration.loadConfiguration(coords);

    }

    /**
     * <ul>Save the coords.yml file</ul>
     */

    private static void save(){
        try {
            coordsConfig.save(coords);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <ul>Reload the coords.yml file</ul>
     */

    private static void reload(){
        try {
           init();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     *  <b>Delete, copy files</b>
     */

    private static void copyDirectory(File sourceLocation, File targetLocation)
            throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]), new File(
                        targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    private static void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
    }


}
