package cannolicat.addictions.addict;

import cannolicat.addictions.Addictions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class Addict implements Serializable {
    @Serial
    private static final long serialVersionUID = 1412394623509314093L;

    private final UUID uuid;
    private final ArrayList<AddictionData> data;

    public Addict(UUID uuid, Addiction addiction) {
        this.uuid = uuid;
        data = new ArrayList<>();
        Addictions.inst().addicts.add(this);
        addAddiction(addiction);
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public ArrayList<AddictionData> getData() {
        return data;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public Optional<AddictionData> dataAt(Addiction addiction) {
        for (AddictionData datum : data) {
            if (datum.getAddiction().equals(addiction) || datum.getAddiction().getName().equals(addiction.getName()))
                return Optional.of(datum);
        }
        return Optional.empty();
    }

    public boolean addAddiction(Addiction addiction) {
        if(dataAt(addiction).isPresent()) return false;

        data.add(new AddictionData(addiction));
        Addictions.addicted.triggerAddiction(getPlayer(), dataAt(addiction).get());
        return true;
    }

    public boolean removeAddiction(Addiction addiction) {
        if(dataAt(addiction).isEmpty()) return false;

        data.remove(dataAt(addiction).get());
        if(data.isEmpty())
            Addictions.inst().addicts.remove(this);

        Bukkit.getScheduler().cancelTask(Addictions.inst().ids.get(getPlayer()).get(addiction));

        return true;
    }

    public boolean hasAddiction(Addiction addiction) {
        return dataAt(addiction).isPresent();
    }

    public void remove() {
        Addictions.inst().addicts.remove(this);
        for (int id : Addictions.inst().ids.get(getPlayer()).values()) {
            Bukkit.getScheduler().cancelTask(id);
        }
        Addictions.inst().ids.remove(getPlayer());
    }
}
