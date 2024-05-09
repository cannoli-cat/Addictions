package cannolicat.addiction.events;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.AddictionData;
import cannolicat.addiction.Addictions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PlayerEventHandler implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        if(Addiction.getPlugin().addicts.containsKey(p.getUniqueId())) {
            scheduleTasks(p);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(Addiction.getPlugin().ids.containsKey(event.getPlayer())) {
            for(int id : Addiction.getPlugin().ids.get(event.getPlayer()).values()) {
                Bukkit.getScheduler().cancelTask(id);
            }
            Addiction.getPlugin().ids.remove(event.getPlayer());
        }
    }

    private void scheduleTasks(Player p) {
        HashMap<Addictions, Integer> ids = new HashMap<>();
        int i = 0;
        for(AddictionData data : Addiction.getPlugin().addicts.get(p.getUniqueId()).values()) {
            int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Addiction.getPlugin(), () -> {
                long difHours = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - data.getDate().getTime());
                if(!(difHours >= 2)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC +
                            "You start to feel withdrawal effects from " + data.getKey().toString().toLowerCase() + "...");

                    for(PotionEffect effect : data.getKey().getEffects()) {
                        p.addPotionEffect(effect);
                    }
                } else {
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                            "You are no longer addicted to " + data.getKey().toString().toLowerCase() + "!");
                    Addiction.getPlugin().removeAddiction(p, data.getKey());
                }
            }, i * new Random().nextLong(6000), 36000);
            ids.put(data.getKey(), id);
            i++;
        }
        Addiction.getPlugin().ids.put(p, ids);
    }
}
