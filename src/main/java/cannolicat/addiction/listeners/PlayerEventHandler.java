package cannolicat.addiction.listeners;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.addict.Addict;
import cannolicat.addiction.addict.AddictionData;
import cannolicat.addiction.addict.Addictions;
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
        Addict a = Addiction.inst().getAddict(event.getPlayer().getUniqueId());
        if (a != null) scheduleTasks(a.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(Addiction.inst().ids.containsKey(event.getPlayer())) {
            for(int id : Addiction.inst().ids.get(event.getPlayer()).values()) {
                Bukkit.getScheduler().cancelTask(id);
            }
            Addiction.inst().ids.remove(event.getPlayer());
        }
    }

    private void scheduleTasks(Player p) {
        HashMap<Addictions, Integer> ids = new HashMap<>();
        int i = 0;
        Addict addict = Addiction.inst().getAddict(p.getUniqueId());
        if (addict != null) {
            for (AddictionData data : addict.getData()) {
                int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Addiction.inst(), () -> {
                    long difHours = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - data.getDate().getTime());
                    if (!(difHours >= 2)) {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC +
                                "You start to feel withdrawal effects from " + data.getAddiction().toString().toLowerCase() + "...");

                        for (PotionEffect effect : data.getAddiction().getEffects()) {
                            p.addPotionEffect(effect);
                        }
                    } else {
                        p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                                "You are no longer addicted to " + data.getAddiction().toString().toLowerCase() + "!");
                        if(!addict.removeAddiction(data.getAddiction()))
                            Bukkit.getLogger().severe("[Addiction] Failed to remove addiction: " + data.getAddiction().toString() + " from player: " + p.getName());
                    }
                }, i * (long) new Random().nextInt(6000), 36000); //maybe save time since last use?
                ids.put(data.getAddiction(), id);
                i++;
            }
            Addiction.inst().ids.put(p, ids);
        }
    }
}
