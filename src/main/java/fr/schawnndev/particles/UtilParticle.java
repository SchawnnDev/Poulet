package fr.schawnndev.particles;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by SchawnnDev on 04/04/2015.
 * <ul>Particle class</ul>
 */

public class UtilParticle {
    private static Class<?> packetClass = null;
    private static Constructor<?> packetConstructor = null;
    private static Field[] fields = null;
    private static boolean netty = true;
    private static Field player_connection = null;
    private static Method player_sendPacket = null;
    private static HashMap<Class<? extends Entity>, Method> handles = new HashMap();
    private static boolean newParticlePacketConstructor = false;
    private static Class<Enum> enumParticle = null;
    private static boolean compatible = true;

    /**
     * <ul>Search packet and set the packet constructor</ul>
     */

    static {
        String vString = getVersion().replace("v", "");
        double v = 0.0D;
        if (!vString.isEmpty()) {
            String[] array = vString.split("_");
            v = Double.parseDouble(array[0] + "." + array[1]);
        }
        try {
            if (v < 1.7D) {
                netty = false;
                packetClass = getNmsClass("Packet63WorldParticles");
                packetConstructor = packetClass.getConstructor(new Class[0]);
                fields = packetClass.getDeclaredFields();
            } else {
                packetClass = getNmsClass("PacketPlayOutWorldParticles");
                if (v < 1.8D) {
                    packetConstructor = packetClass.getConstructor(new Class[]{String.class, Float.TYPE, Float.TYPE, Float.TYPE,
                            Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE});
                } else {
                    newParticlePacketConstructor = true;
                    enumParticle = getNmsClass("EnumParticle");
                    packetConstructor = packetClass.getDeclaredConstructor(new Class[]{enumParticle, Boolean.TYPE, Float.TYPE,
                            Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Integer.TYPE, int[].class});
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            compatible = false;
        }
    }

    private Particle type;
    private double speed;
    private int count;
    private double radius;


    public UtilParticle(Particle type, double speed, int count, double radius) {
        this.type = type;
        this.speed = speed;
        this.count = count;
        this.radius = radius;
    }

    public static Object getParticleType(int id) {
        return ((Enum[]) enumParticle.getEnumConstants())[id];
    }

    /**
     * <ul>Send the packet to the player</ul>
     * @param p Packet receiver
     * @param packet Packet to send to player
     * @throws IllegalArgumentException Packet error or not found
     */

    private static void sendPacket(Player p, Object packet)
            throws IllegalArgumentException {
        try {
            if (player_connection == null) {
                player_connection = getHandle(p).getClass().getField("playerConnection");
                for (Method m : player_connection.get(getHandle(p)).getClass().getMethods()) {
                    if (m.getName().equalsIgnoreCase("sendPacket")) {
                        player_sendPacket = m;
                    }
                }
            }
            player_sendPacket.invoke(player_connection.get(getHandle(p)), new Object[]{packet});
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * <ul>*Reflection*, getHandle object</ul>
     * @param entity Player
     * @return getHandle object
     */

    private static Object getHandle(Entity entity) {
        try {
            if (handles.get(entity.getClass()) != null) {
                return ((Method) handles.get(entity.getClass())).invoke(entity, new Object[0]);
            }
            Method entity_getHandle = entity.getClass().getMethod("getHandle", new Class[0]);
            handles.put(entity.getClass(), entity_getHandle);
            return entity_getHandle.invoke(entity, new Object[0]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * <ul>Get a nms class with name</ul>
     * @param name The name of the nms class
     * @return Class
     */

    public static Class<Enum> getNmsClass(String name) {
        String version = getVersion();
        String className = "net.minecraft.server." + version + name;
        Class<Enum> clazz = null;
        try {
            clazz = (Class<Enum>) Class.forName(className);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return clazz;
    }

    /**
     * <ul>Get the version of bukkit/spigot</ul>
     * @return Version of bukkit/spigot
     */

    private static String getVersion() {
        String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
        if (array.length == 4) {
            return array[3] + ".";
        }
        return "";
    }

    public static boolean isCompatible() {
        return compatible;
    }

    public double getSpeed() {
        return this.speed;
    }

    public int getCount() {
        return this.count;
    }

    public double getRadius() {
        return this.radius;
    }

    public void sendCrackToLocation(Location location, int id) {
        try {
            Object packet = createPacket(location, true, id);
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendPacket(player, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <ul>Send the packet</ul>
     * @param location Where the packet must be displayed
     */

    public void sendToLocation(Location location) {
        try {
            Object packet = createPacket(location, false, 0);
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendPacket(player, packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <ul>Create the particle packet</ul>
     * @param location Where the packet must be displayed
     * @param crack Iconcrack or not
     * @param id Id of particle
     * @return The particle packet
     */

    private Object createPacket(Location location, boolean crack, int id) {
        try {
            if (this.count <= 0) {
                this.count = 1;
            }
            Object packet = null;

            if (crack) {

                String modifier = "iconcrack_" + id;

                packet = packetConstructor.newInstance(new Object[0]);
                for (Field f : fields) {
                    f.setAccessible(true);
                    if (f.getName().equals("a")) {
                        f.set(packet, modifier);
                    } else if (f.getName().equals("b")) {
                        f.set(packet, Float.valueOf((float) location.getX()));
                    } else if (f.getName().equals("c")) {
                        f.set(packet, Float.valueOf((float) location.getY()));
                    } else if (f.getName().equals("d")) {
                        f.set(packet, Float.valueOf((float) location.getZ()));
                    } else if ((f.getName().equals("e")) || (f.getName().equals("f")) || (f.getName().equals("g"))) {
                        f.set(packet, Double.valueOf(this.radius));
                    } else if (f.getName().equals("h")) {
                        f.set(packet, Double.valueOf(this.speed));
                    } else if (f.getName().equals("i")) {
                        f.set(packet, Integer.valueOf(this.count));
                    } else if (f.getName().equals("k")) {
                        f.set(packet, id);
                    }
                }

            } else {

                if (netty) {
                    if (newParticlePacketConstructor) {
                        Object particleType = ((Enum[]) enumParticle.getEnumConstants())[this.type.getId()];
                        packet = packetConstructor.newInstance(new Object[]{particleType,
                                Boolean.valueOf(true), Float.valueOf((float) location.getX()), Float.valueOf((float) location.getY()), Float.valueOf((float) location.getZ()),
                                Float.valueOf((float) this.radius), Float.valueOf((float) this.radius), Float.valueOf((float) this.radius),
                                Float.valueOf((float) this.speed), Integer.valueOf(this.count), new int[0]});
                    } else {
                        packet = packetConstructor.newInstance(new Object[]{this.type.getName(),
                                Float.valueOf((float) location.getX()), Float.valueOf((float) location.getY()), Float.valueOf((float) location.getZ()),
                                Float.valueOf((float) this.radius), Float.valueOf((float) this.radius), Float.valueOf((float) this.radius),
                                Float.valueOf((float) this.speed), Integer.valueOf(this.count)});
                    }
                } else {
                    packet = packetConstructor.newInstance(new Object[0]);
                    for (Field f : fields) {
                        f.setAccessible(true);
                        if (f.getName().equals("a")) {
                            f.set(packet, this.type.getName());
                        } else if (f.getName().equals("b")) {
                            f.set(packet, Float.valueOf((float) location.getX()));
                        } else if (f.getName().equals("c")) {
                            f.set(packet, Float.valueOf((float) location.getY()));
                        } else if (f.getName().equals("d")) {
                            f.set(packet, Float.valueOf((float) location.getZ()));
                        } else if ((f.getName().equals("e")) || (f.getName().equals("f")) || (f.getName().equals("g"))) {
                            f.set(packet, Double.valueOf(this.radius));
                        } else if (f.getName().equals("h")) {
                            f.set(packet, Double.valueOf(this.speed));
                        } else if (f.getName().equals("i")) {
                            f.set(packet, Integer.valueOf(this.count));
                        }
                    }
                }
            }
            return packet;
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * <ul>Particle type enum</ul>
     */

    public static enum Particle {
        EXPLOSION_NORMAL("explode", 0, 17),
        RIEN("rien", 0, 0),
        EXPLOSION_LARGE("largeexplode", 1, 1),
        EXPLOSION_HUGE("hugeexplosion", 2, 0),
        FIREWORKS_SPARK("fireworksSpark", 3, 2),
        WATER_BUBBLE("bubble", 4, 3),
        WATER_SPLASH("splash", 5, 21),
        WATER_WAKE("wake", 6, -1),
        SUSPENDED("suspended", 7, 4),
        SUSPENDED_DEPTH("depthsuspend", 8, 5),
        CRIT("crit", 9, 7),
        CRIT_MAGIC("magicCrit", 10, 8),
        SMOKE_NORMAL("smoke", 11, -1),
        SMOKE_LARGE("largesmoke", 12, 22),
        SPELL("spell", 13, 11),
        SPELL_INSTANT("instantSpell", 14, 12),
        SPELL_MOB("mobSpell", 15, 9),
        SPELL_MOB_AMBIENT("mobSpellAmbient", 16, 10),
        SPELL_WITCH("witchMagic", 17, 13),
        DRIP_WATER("dripWater", 18, 27),
        DRIP_LAVA("dripLava", 19, 28),
        VILLAGER_ANGRY("angryVillager", 20, 31),
        VILLAGER_HAPPY("happyVillager", 21, 32),
        TOWN_AURA("townaura", 22, 6),
        NOTE("note", 23, 24),
        PORTAL("portal", 24, 15),
        ENCHANTMENT_TABLE("enchantmenttable", 25, 16),
        FLAME("flame", 26, 18),
        LAVA("lava", 27, 19),
        FOOTSTEP("footstep", 28, 20),
        CLOUD("cloud", 29, 23),
        REDSTONE("reddust", 30, 24),
        SNOWBALL("snowballpoof", 31, 25),
        SNOW_SHOVEL("snowshovel", 32, 28),
        SLIME("slime", 33, 29),
        HEART("heart", 34, 30),
        BARRIER("barrier", 35, -1),
        ITEM_CRACK("iconcrack_", 36, 33),
        BLOCK_CRACK("tilecrack_", 37, 34),
        BLOCK_DUST("blockdust_", 38, -1),
        WATER_DROP("droplet", 39, -1),
        ITEM_TAKE("take", 40, -1),
        MOB_APPEARANCE("mobappearance", 41, -1);

        private String name;
        private int id;
        private int legacyId;

        private Particle(String name, int id, int legacyId) {
            this.name = name;
            this.id = id;
            this.legacyId = legacyId;
        }

        String getName() {
            return this.name;
        }

        public int getId() {
            return this.id;
        }

        int getLegacyId() {
            return this.legacyId;
        }
    }
}