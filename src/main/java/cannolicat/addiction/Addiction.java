package cannolicat.addiction;

import cannolicat.addiction.commands.*;
import cannolicat.addiction.events.Addicted;
import cannolicat.addiction.events.PlayerEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public final class Addiction extends JavaPlugin {
    public HashMap<UUID, HashMap<Addictions, AddictionData>> addicts = new HashMap<>();
    public HashMap<Player, HashMap<Addictions, Integer>> ids = new HashMap<>();
    private static Addiction plugin;
    public static Addicted addicted;
    private static final File file = new File("plugins" + File.separator + "Addiction" + File.separator + "addictions.ser");

    @Override
    public void onEnable() {
        plugin = this;
        AddAddiction addiction = new AddAddiction();

        getCommand("addaddiction").setExecutor(addiction);
        getCommand("showaddictions").setExecutor(new ShowAddictions());
        getCommand("removeaddiction").setExecutor(new RemoveAddiction());
        getCommand("clearAddictions").setExecutor(new ClearAddictions());

        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        addicted = new Addicted();
        addicted.addListener(addiction);

        if(file.exists()) {
            Bukkit.getLogger().info("[Addiction] Found saved data... attempting to load...");
            addicts = loadAddicts();
            Bukkit.getLogger().info("[Addiction] Successfully loaded saved data!");
        }
    }

    @Override
    public void onDisable() {
        if(!file.exists() && !addicts.isEmpty()) {
            try {
                file.getParentFile().mkdirs();
                if (file.createNewFile()) {
                    Bukkit.getLogger().info("[Addiction] Save file created: " + file.getName());
                }
            } catch (IOException e) {
                Bukkit.getLogger().severe("[Addiction] An error occurred.");
                e.printStackTrace();
            }
        }

        if(!addicts.isEmpty() && file.exists()) {
            if (saveAddicts()) Bukkit.getLogger().info("[Addiction] Successfully saved data!");
            else Bukkit.getLogger().severe("[Addiction] Could not save data!");
        } else {
            Bukkit.getLogger().info("[Addiction] Addicts list is empty, cancelling save...");
        }
    }

    public static Addiction getPlugin() {
        return plugin;
    }

    public void removeAddiction(@NonNull Player p, @NonNull Addictions addictionToRemove) {
        if (addicts.containsKey(p.getUniqueId())) {
            if(addicts.get(p.getUniqueId()).containsKey(addictionToRemove)) {
                if(addicts.get(p.getUniqueId()).size() > 1) addicts.get(p.getUniqueId()).remove(addictionToRemove);
                else addicts.remove(p.getUniqueId());
                Bukkit.getScheduler().cancelTask(ids.get(p).get(addictionToRemove));
                return;
            }
            else {
                Bukkit.getLogger().warning("[Addiction] Failed to remove addiction from " + p.getName() + ". This player does not have this addiction!");
                return;
            }
        }

        Bukkit.getLogger().warning("[Addiction] Failed to remove addiction! " + p.getName() + " is not an addict!");
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
            e.printStackTrace();
            return false;
        }
    }

    private HashMap<UUID, HashMap<Addictions, AddictionData>> loadAddicts() {
        HashMap<UUID, HashMap<Addictions, AddictionData>> list;

        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            if(file.length() != 0) list = (HashMap<UUID, HashMap<Addictions, AddictionData>>) ois.readObject();
            else list = new HashMap<>();

            ois.close();
            fis.close();

            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
