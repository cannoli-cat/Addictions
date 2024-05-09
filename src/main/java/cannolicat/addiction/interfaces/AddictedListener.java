package cannolicat.addiction.interfaces;

import cannolicat.addiction.AddictionData;
import org.bukkit.entity.Player;

public interface AddictedListener {
    void onAddicted(Player p, AddictionData data);
}
