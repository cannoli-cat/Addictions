package cannolicat.addictions.addict;

import cannolicat.addictions.Addictions;
import org.bukkit.potion.PotionEffect;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class Addiction implements Serializable {
    @Serial
    private static final long serialVersionUID = 4422394323768245093L;
    private final String name;
    private final ArrayList<SerializablePotionEffect> effects;
    private final int timeBetweenSymptoms, timeUntilClean;

    public Addiction(String name, int timeBetweenSymptoms, int timeUntilClean) {
        this.name = name;
        this.timeBetweenSymptoms = timeBetweenSymptoms;
        this.timeUntilClean = timeUntilClean;
        effects = deserializeEffects();
    }

    public ArrayList<PotionEffect> getEffects() {
        ArrayList<PotionEffect> list = new ArrayList<>();
        for (SerializablePotionEffect effect : effects) {
            list.add(effect.getPotionEffect());
        }
        return list;
    }

    public String getName() {
        return name;
    }

    private ArrayList<SerializablePotionEffect> deserializeEffects() {
        ArrayList<SerializablePotionEffect> list = new ArrayList<>();

        Addictions.inst().getConfig().getConfigurationSection(name).getKeys(false).forEach(key -> {
            if(key.equalsIgnoreCase("timeUntilClean") || key.equalsIgnoreCase("timeBetweenSymptoms")) return;

            String path = name + "." + key + ".";

            int dur = Addictions.inst().getConfig().getInt(path + "duration");
            int level = Addictions.inst().getConfig().getInt(path + "level");

            boolean showParticles = true, showIcon = true;

            if(Addictions.inst().getConfig().getBoolean(path + "hasParticles")) {
                showParticles = Addictions.inst().getConfig().getBoolean(path + "hasParticles");
            }

            if (Addictions.inst().getConfig().getBoolean(path + "hasIcon")) {
                showIcon = Addictions.inst().getConfig().getBoolean(path + "hasIcon");
            }

            list.add(new SerializablePotionEffect(key, dur, level, showParticles, showIcon));
        });
        return list;
    }

    public int getTimeBetweenSymptoms() {
        return timeBetweenSymptoms;
    }

    public int getTimeUntilClean() {
        return timeUntilClean;
    }
}
