package cannolicat.addiction.listeners;

import cannolicat.addiction.addict.AddictionData;
import org.bukkit.entity.Player;

public interface AddictedListener {
    void onAddicted(Player p, AddictionData data);
}
