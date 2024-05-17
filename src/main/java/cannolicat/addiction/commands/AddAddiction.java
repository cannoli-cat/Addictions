package cannolicat.addiction.commands;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.addict.Addict;
import cannolicat.addiction.addict.AddictionData;
import cannolicat.addiction.addict.Addictions;
import cannolicat.addiction.listeners.AddictedListener;
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

            Addictions addictionToAdd;
            try {
                addictionToAdd = Addictions.valueOf(strings[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                if(commandSender instanceof Player) {
                    commandSender.sendMessage(ChatColor.RED + "You must enter a valid addiction!");
                    return true;
                } else {
                    Bukkit.getLogger().warning("[Addiction] You must enter a valid addiction!");
                    return true;
                }
            }

            assert player != null;
            Addict addict = Addiction.inst().getAddict(player.getUniqueId());
            if (addict != null) {
                if (addict.hasAddiction(addictionToAdd)) {
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(ChatColor.RED + "This player already has this addiction!");
                    else
                        Bukkit.getLogger().warning("[Addiction] This player already has this addiction!");
                } else {
                    if (addict.addAddiction(addictionToAdd)) {
                        if (commandSender instanceof Player)
                            commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is now addicted to " + ChatColor.RED + addictionToAdd + ChatColor.RESET + "!");
                        else
                            Bukkit.getLogger().info("[Addiction] " + player.getName() + " is now addicted to " + addictionToAdd + "!");
                    } else {
                        Bukkit.getLogger().warning("[Addiction] Failed to add addiction: " + addictionToAdd + " for player: " + player.getName());
                        return false;
                    }
                }
            } else {
                new Addict(player.getUniqueId(), addictionToAdd);
                if(commandSender instanceof Player)
                    commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is now addicted to " + ChatColor.RED + addictionToAdd + ChatColor.RESET + "!");
                else
                    Bukkit.getLogger().info("[Addiction] " + player.getName() + " is now addicted to " + addictionToAdd + "!");
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
            ArrayList<String> list = new ArrayList<>();
            for(Addictions addiction : EnumSet.allOf(Addictions.class))
                list.add(addiction.toString());
            return list;
        }
        return null;
    }

    @Override
    public void onAddicted(Player p, AddictionData data) {
        Addict addict = Addiction.inst().getAddict(p.getUniqueId());
        if(addict != null) {
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are addicted to " + data.getAddiction().toString().toLowerCase() + "...");
            int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Addiction.inst(), () -> {
                long difHours = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - data.getDate().getTime());
                if(!(difHours >= 2)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC +
                            "You start to feel withdrawal effects from " + data.getAddiction().toString().toLowerCase() + "...");

                    for(PotionEffect effect : data.getAddiction().getEffects()) {
                        p.addPotionEffect(effect);
                    }
                }
                else {
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                            "You are no longer addicted to " + data.getAddiction().toString().toLowerCase() + "!");
                    if(!addict.removeAddiction(data.getAddiction()))
                        Bukkit.getLogger().severe("[Addiction] Failed to remove addiction: " + data.getAddiction().toString() + " from player: " + p.getName());
                }
            }, 36000, 36000);
            if(Addiction.inst().ids.containsKey(p)) {
                Addiction.inst().ids.get(p).put(data.getAddiction(), id);
            } else {
                HashMap<Addictions, Integer> list = new HashMap<>();
                list.put(data.getAddiction(), id);
                Addiction.inst().ids.put(p, list);
            }
        }
    }
}
