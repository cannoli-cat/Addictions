package cannolicat.addiction.events;

import cannolicat.addiction.AddictionData;
import cannolicat.addiction.interfaces.AddictedListener;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Addicted {
    private List<AddictedListener> listeners = new ArrayList<AddictedListener>();

    public void addListener(AddictedListener listener) {
        listeners.add(listener);
    }

    public void triggerAddiction(Player player, AddictionData data) {
        for (AddictedListener listener : listeners) {
            listener.onAddicted(player, data);
        }
    }
}