package cannolicat.addiction;

import cannolicat.addiction.addict.Addict;
import cannolicat.addiction.addict.Addictions;
import cannolicat.addiction.commands.*;
import cannolicat.addiction.conditions.HasAddiction;
import cannolicat.addiction.conditions.IsAddict;
import cannolicat.addiction.listeners.Addicted;
import cannolicat.addiction.listeners.PlayerEventHandler;
import cannolicat.addiction.mechanics.AddictTo;
import cannolicat.addiction.mechanics.RemoveAddictions;
import cannolicat.addiction.mechanics.UpdateAddict;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class Addiction extends JavaPlugin implements Listener {
    public ArrayList<Addict> addicts = new ArrayList<>();
    public HashMap<Player, HashMap<Addictions, Integer>> ids = new HashMap<>();
    private static Addiction inst;
    public static Addicted addicted;
    private static final File file = new File("plugins" + File.separator + "Addiction" + File.separator + "addictions.ser");

    @Override
    public void onEnable() {
        inst = this;
        AddAddiction addAddiction = new AddAddiction();

        getCommand("addaddiction").setExecutor(addAddiction);
        getCommand("showaddictions").setExecutor(new ShowAddictions());
        getCommand("removeaddiction").setExecutor(new RemoveAddiction());
        getCommand("clearAddictions").setExecutor(new ClearAddictions());
        getCommand("updatedate").setExecutor(new UpdateDate());

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);

        addicted = new Addicted();
        addicted.addListener(addAddiction);

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
            if(file.exists()) file.delete();
        }
    }

    public static Addiction inst() {
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
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
            return null;
        }
    }

    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event)	{
        switch (event.getMechanicName().toUpperCase()) {
            case "ADDICTIONADD", "ADDADDICTION", "ADDADDICT", "ADDICT" ->
                    event.register(new AddictTo(event.getConfig()));
            case "UPDATEADDICTION", "USEDADDICTION", "USED", "UPDATEADDICT" ->
                    event.register(new UpdateAddict(event.getConfig()));
            case "CLEARADDICTIONS", "REMOVEADDICT", "CLEARADDICT", "REMOVEADDICTIONS" ->
                    event.register(new RemoveAddictions());
            default -> {}
        }
    }

    @EventHandler
    public void onMythicConditionLoad(MythicConditionLoadEvent event) {
        switch (event.getConditionName().toUpperCase()) {
            case "HASADDICTION", "ISADDICTED" ->
                    event.register(new HasAddiction(event.getConfig()));
            case "ISADDICT", "ADDICT" ->
                    event.register(new IsAddict());
            default -> {}
        }
    }

    public Addict getAddict(UUID uuid) {
        for(Addict a : addicts) {
            if(a.getUniqueId().equals(uuid)) return a;
        }
        return null;
    }
}
