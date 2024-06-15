package cannolicat.addictions.listeners;

import cannolicat.addictions.addict.AddictionData;
import org.bukkit.entity.Player;

public interface AddictedListener {
    void onAddicted(Player p, AddictionData data);
}
