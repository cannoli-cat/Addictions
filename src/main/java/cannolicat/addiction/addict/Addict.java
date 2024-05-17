package cannolicat.addiction.addict;

import cannolicat.addiction.Addiction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Addict implements Serializable {
    @Serial
    private static final long serialVersionUID = 1412394623509314093L;

    private final UUID uuid;
    private final ArrayList<AddictionData> data;

    public Addict(UUID uuid, Addictions addiction) {
        this.uuid = uuid;
        data = new ArrayList<>();
        Addiction.inst().addicts.add(this);
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

    public AddictionData dataAt(Addictions addiction) {
        for (AddictionData datum : data) {
            if (datum.getAddiction().equals(addiction)) return datum;
        }
        return null;
    }

    public boolean addAddiction(Addictions addiction) {
        if(dataAt(addiction) != null) return false;

        data.add(new AddictionData(addiction));
        Addiction.addicted.triggerAddiction(getPlayer(), dataAt(addiction));
        return true;
    }

    public boolean removeAddiction(Addictions addiction) {
        if(dataAt(addiction) == null) return false;

        data.remove(dataAt(addiction));
        if(data.isEmpty())
            Addiction.inst().addicts.remove(this);

        Bukkit.getScheduler().cancelTask(Addiction.inst().ids.get(getPlayer()).get(addiction));

        return true;
    }

    public boolean hasAddiction(Addictions addiction) {
        return dataAt(addiction) != null;
    }

    public void remove() {
        Addiction.inst().addicts.remove(this);
        for (int id : Addiction.inst().ids.get(getPlayer()).values()) {
            Bukkit.getScheduler().cancelTask(id);
        }
        Addiction.inst().ids.remove(getPlayer());
    }
}
