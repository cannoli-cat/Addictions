package cannolicat.addictions.commands;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.AddictionData;
import cannolicat.addictions.addict.Addiction;
import cannolicat.addictions.listeners.AddictedListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AddAddiction implements CommandExecutor, TabCompleter, AddictedListener {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("addaddiction") && strings.length == 2) {
            Player player;
            try {
                player = Bukkit.getServer().getPlayer(strings[0]);
            } catch (CommandException e) {
                if(commandSender instanceof Player) {
                    commandSender.sendMessage(ChatColor.RED + "You must enter a valid player!");
                    return true;
                } else {
                    Bukkit.getLogger().warning("[Addiction] You must enter a valid player!");
                    return true;
                }
            }

            Addiction addictionToAdd;

            if (Addictions.getAddiction(strings[1]).isPresent()) {
                addictionToAdd = Addictions.getAddiction(strings[1]).get();
            } else {
                if (commandSender instanceof Player) {
                    commandSender.sendMessage(ChatColor.RED + "You must enter a valid addiction!");
                } else {
                    Bukkit.getLogger().warning("[Addiction] You must enter a valid addiction!");
                }

                return true;
            }

            assert player != null;
            Addict addict = Addictions.inst().getAddict(player.getUniqueId()).orElse(null);
            if (addict != null) {
                if (addict.hasAddiction(addictionToAdd)) {
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(ChatColor.RED + "This player already has this addiction!");
                    else
                        Bukkit.getLogger().warning("[Addiction] This player already has this addiction!");
                } else {
                    if (addict.addAddiction(addictionToAdd)) {
                        if (commandSender instanceof Player)
                            commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is now addicted to " + ChatColor.RED + addictionToAdd.getName().toLowerCase() + ChatColor.RESET + "!");
                        else
                            Bukkit.getLogger().info("[Addiction] " + player.getName() + " is now addicted to " + addictionToAdd.getName().toLowerCase() + "!");
                    } else {
                        Bukkit.getLogger().warning("[Addiction] Failed to add addiction: " + addictionToAdd.getName().toLowerCase() + " for player: " + player.getName());
                        return false;
                    }
                }
            } else {
                new Addict(player.getUniqueId(), addictionToAdd);
                if(commandSender instanceof Player)
                    commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is now addicted to " + ChatColor.RED + addictionToAdd.getName().toLowerCase() + ChatColor.RESET + "!");
                else
                    Bukkit.getLogger().info("[Addiction] " + player.getName() + " is now addicted to " + addictionToAdd.getName().toLowerCase() + "!");
            }
        } else {
            if(commandSender instanceof Player)
                commandSender.sendMessage(ChatColor.RED + "You must specify a player and addiction to add!");
            else
                Bukkit.getLogger().warning("[Addiction] You must specify a player and addiction to add!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length == 2) {
            return new ArrayList<>(Addictions.inst().getConfig().getKeys(false));
        }
        return null;
    }

    @Override
    public void onAddicted(Player p, AddictionData data) {
        Addict addict = Addictions.inst().getAddict(p.getUniqueId()).orElse(null);
        if(addict != null) {
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are addicted to " + data.getAddiction().getName().toLowerCase() + "...");
            int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Addictions.inst(), () -> {
                long timeDif = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - data.getDate().getTime());
                if(!(timeDif >= data.getAddiction().getTimeUntilClean() * 20L)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC +
                            "You crave " + data.getAddiction().getName().toLowerCase() + "...");

                    for(PotionEffect effect : data.getAddiction().getEffects()) {
                        p.addPotionEffect(effect);
                    }
                }
                else {
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                            "You are no longer addicted to " + data.getAddiction().getName().toLowerCase() + "!");
                    if(!addict.removeAddiction(data.getAddiction()))
                        Bukkit.getLogger().severe("[Addiction] Failed to remove addiction: " + data.getAddiction().getName().toLowerCase() + " from player: " + p.getName());
                }
            }, data.getAddiction().getTimeBetweenSymptoms(), data.getAddiction().getTimeBetweenSymptoms());
            if(Addictions.inst().ids.containsKey(p)) {
                Addictions.inst().ids.get(p).put(data.getAddiction(), id);
            } else {
                HashMap<Addiction, Integer> list = new HashMap<>();
                list.put(data.getAddiction(), id);
                Addictions.inst().ids.put(p, list);
            }
        }
    }
}
