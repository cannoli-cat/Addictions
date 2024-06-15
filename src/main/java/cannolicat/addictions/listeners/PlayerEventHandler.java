package cannolicat.addictions.listeners;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.AddictionData;
import cannolicat.addictions.addict.Addiction;
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
        Addictions.inst().getAddict(event.getPlayer().getUniqueId()).ifPresent(a -> scheduleTasks(a.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if(Addictions.inst().ids.containsKey(event.getPlayer())) {
            for(int id : Addictions.inst().ids.get(event.getPlayer()).values()) {
                Bukkit.getScheduler().cancelTask(id);
            }
            Addictions.inst().ids.remove(event.getPlayer());
        }
    }

    private void scheduleTasks(Player p) {
        HashMap<Addiction, Integer> ids = new HashMap<>();
        Addict addict = Addictions.inst().getAddict(p.getUniqueId()).orElse(null);
        if (addict != null) {
            for (AddictionData data : addict.getData()) {
                int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Addictions.inst(), () -> {
                    long timeDif = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - data.getDate().getTime());
                    if (!(timeDif >= data.getAddiction().getTimeUntilClean() * 20L)) {
                        p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC +
                                "You crave " + data.getAddiction().getName().toLowerCase() + "...");

                        for (PotionEffect effect : data.getAddiction().getEffects()) {
                            p.addPotionEffect(effect);
                        }
                    } else {
                        p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                                "You are no longer addicted to " + data.getAddiction().getName().toLowerCase() + "!");
                        if(!addict.removeAddiction(data.getAddiction()))
                            Bukkit.getLogger().severe("[Addiction] Failed to remove addiction: " + data.getAddiction().getName() + " from player: " + p.getName());
                    }
                }, (long) data.getAddiction().getTimeBetweenSymptoms() + new Random().nextInt(6000), data.getAddiction().getTimeBetweenSymptoms());
                ids.put(data.getAddiction(), id);
            }
            Addictions.inst().ids.put(p, ids);
        }
    }
}
