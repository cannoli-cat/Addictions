package cannolicat.addictions;

import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.Addiction;
import cannolicat.addictions.commands.*;
import cannolicat.addictions.listeners.Addicted;
import cannolicat.addictions.listeners.PlayerEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

public final class Addictions extends JavaPlugin {
    public ArrayList<Addict> addicts = new ArrayList<>();
    private static final ArrayList<Addiction> addictions = new ArrayList<>();
    public HashMap<Player, HashMap<Addiction, Integer>> ids = new HashMap<>();
    private static Addictions inst;
    public static Addicted addicted;
    private static final File file = new File("plugins" + File.separator + "Addictions" + File.separator + "addictions.ser");

    @Override
    public void onEnable() {
        inst = this;
        AddAddiction addAddiction = new AddAddiction();

        getCommand("addaddiction").setExecutor(addAddiction);
        getCommand("showaddictions").setExecutor(new ShowAddictions());
        getCommand("removeaddiction").setExecutor(new RemoveAddiction());
        getCommand("clearAddictions").setExecutor(new ClearAddictions());
        getCommand("updatedate").setExecutor(new UpdateDate());
        getCommand("addictionReload").setExecutor(new Reload());

        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        if(Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
            getLogger().info("Hooked to MythicMobs!");
            getServer().getPluginManager().registerEvents(new MythicHook(), this);
        }

        addicted = new Addicted();
        addicted.addListener(addAddiction);

        if(file.exists()) {
            addicts = loadAddicts();
            getLogger().info("Successfully loaded saved data!");
        }

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists() || configFile.length() == 0L) setupConfig();

        getConfig().getKeys(false).forEach(key ->
                        addictions.add(new Addiction(key,
                        getConfig().getInt(key + ".timeBetweenSymptoms"),
                        getConfig().getInt(key + ".timeUntilClean"))));
    }

    private void setupConfig() {
        getConfig().addDefault("Tobacco.timeBetweenSymptoms", 36000);
        getConfig().addDefault("Tobacco.timeUntilClean", 144000);
        getConfig().addDefault("Tobacco.hunger.duration", 1000);
        getConfig().addDefault("Tobacco.hunger.level", 0);
        getConfig().addDefault("Tobacco.nausea.duration", 200);
        getConfig().addDefault("Tobacco.nausea.level", 0);

        getConfig().addDefault("Marijuana.timeBetweenSymptoms", 36000);
        getConfig().addDefault("Marijuana.timeUntilClean", 144000);
        getConfig().addDefault("Marijuana.slowness.duration", 1200);
        getConfig().addDefault("Marijuana.slowness.level", 0);
        getConfig().addDefault("Marijuana.nausea.duration", 200);
        getConfig().addDefault("Marijuana.nausea.level", 0);
        getConfig().addDefault("Marijuana.saturation.duration", 600);
        getConfig().addDefault("Marijuana.saturation.level", 0);

        getConfig().addDefault("Cocaine.timeBetweenSymptoms", 36000);
        getConfig().addDefault("Cocaine.timeUntilClean", 144000);
        getConfig().addDefault("Cocaine.hunger.duration", 2000);
        getConfig().addDefault("Cocaine.hunger.level", 1);
        getConfig().addDefault("Cocaine.weakness.duration", 1200);
        getConfig().addDefault("Cocaine.weakness.level", 0);
        getConfig().addDefault("Cocaine.slowness.duration", 2000);
        getConfig().addDefault("Cocaine.slowness.level", 1);

        getConfig().addDefault("Opium.timeBetweenSymptoms", 36000);
        getConfig().addDefault("Opium.timeUntilClean", 144000);
        getConfig().addDefault("Opium.nausea.duration", 1000);
        getConfig().addDefault("Opium.nausea.level", 0);
        getConfig().addDefault("Opium.weakness.duration", 1200);
        getConfig().addDefault("Opium.weakness.level", 1);
        getConfig().addDefault("Opium.blindness.duration", 400);
        getConfig().addDefault("Opium.blindness.level", 0);

        getConfig().addDefault("Methamphetamine.timeBetweenSymptoms", 36000);
        getConfig().addDefault("Methamphetamine.timeUntilClean", 144000);
        getConfig().addDefault("Methamphetamine.weakness.duration", 1200);
        getConfig().addDefault("Methamphetamine.weakness.level", 1);
        getConfig().addDefault("Methamphetamine.slowness.duration", 800);
        getConfig().addDefault("Methamphetamine.slowness.level", 1);
        getConfig().addDefault("Methamphetamine.hunger.duration", 1200);
        getConfig().addDefault("Methamphetamine.hunger.level", 1);

        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    @Override
    public void onDisable() {
        saveConfig();

        if(!file.exists() && !addicts.isEmpty()) {
            try {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    getLogger().info("Save file created: " + file.getName());
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file!", e);
            }
        }

        if(!addicts.isEmpty() && file.exists()) {
            if (saveAddicts()) getLogger().info("Successfully saved data! Goodbye!");
            else getLogger().severe("Could not save data!");
        } else {
            getLogger().info("Addicts list is empty, cancelling save...");
            if(file.exists()) file.delete();
        }
    }

    public static Addictions inst() {
        return inst;
    }

    private boolean saveAddicts() {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(addicts);
            oos.close();
            fos.close();

            return true;
        } catch(IOException e) {
            throw new RuntimeException("Failed to save addicts!", e);
        }
    }

    private ArrayList<Addict> loadAddicts() {
        ArrayList<Addict> list;

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            if(file.length() != 0) list = (ArrayList<Addict>) ois.readObject();
            else list = new ArrayList<>();

            ois.close();
            fis.close();

            return list;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load addicts!", e);
        }
    }

    public Optional<Addict> getAddict(UUID uuid) {
        for(Addict a : addicts) {
            if(a.getUniqueId().equals(uuid)) return Optional.of(a);
        }
        return Optional.empty();
    }

    public static ArrayList<Addiction> getAddictions() {
        return addictions;
    }

    public static Optional<Addiction> getAddiction(String name) {
        for(Addiction a : addictions) {
            if(a.getName().equalsIgnoreCase(name)) return Optional.of(a);
        }
        return Optional.empty();
    }
}
