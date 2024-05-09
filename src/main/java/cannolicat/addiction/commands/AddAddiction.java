package cannolicat.addiction.commands;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.AddictionData;
import cannolicat.addiction.Addictions;
import cannolicat.addiction.interfaces.AddictedListener;
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
            if (Addiction.getPlugin().addicts.containsKey(player.getUniqueId())) {
                boolean contains = false;
                for (Addictions addiction : Addiction.getPlugin().addicts.get(player.getUniqueId()).keySet()) {
                    contains = addiction.equals(addictionToAdd);
                    if (contains) break;
                }
                if (contains) {
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(ChatColor.RED + "This player already has this addiction!");
                    else {
                        Addiction.getPlugin().addicts.get(player.getUniqueId()).get(addictionToAdd).setDate(new Date());
                    }
                } else {
                    AddictionData data = new AddictionData(addictionToAdd);
                    Addiction.getPlugin().addicts.get(player.getUniqueId()).put(addictionToAdd, data);
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is now addicted to " + ChatColor.RED + addictionToAdd + ChatColor.RESET + "!");
                    else
                        Bukkit.getLogger().info("[Addiction] " + player.getDisplayName() + " is now addicted to " + addictionToAdd + "!");
                    Addiction.addicted.triggerAddiction(player, data);
                }
            } else {
                AddictionData data = new AddictionData(addictionToAdd);
                HashMap<Addictions, AddictionData> addictionData = new HashMap<>();
                addictionData.put(addictionToAdd, data);

                Addiction.getPlugin().addicts.put(player.getUniqueId(), addictionData);
                if(commandSender instanceof Player)
                    commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is now addicted to " + ChatColor.RED + addictionToAdd + ChatColor.RESET + "!");
                else
                    Bukkit.getLogger().info("[Addiction] " + player.getName() + " is now addicted to " + addictionToAdd + "!");

                Addiction.addicted.triggerAddiction(player, data);
            }
        } else {
            if(commandSender instanceof Player) {
                Player sender = (Player) commandSender;
                sender.sendMessage(ChatColor.RED + "You must specify a player and addiction to add!");
            }
            else {
                Bukkit.getLogger().warning("[Addiction] You must specify a player and addiction to add!");
            }
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
        if(Addiction.getPlugin().addicts.containsKey(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are addicted to " + data.getKey().toString().toLowerCase() + "...");
            int id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Addiction.getPlugin(), () -> {
                long difHours = TimeUnit.MILLISECONDS.toHours(new Date().getTime() - data.getDate().getTime());
                if(!(difHours >= 2)) {
                    p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC +
                            "You start to feel withdrawal effects from " + data.getKey().toString().toLowerCase() + "...");

                    for(PotionEffect effect : data.getKey().getEffects()) {
                        p.addPotionEffect(effect);
                    }
                }
                else {
                    p.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                            "You are no longer addicted to " + data.getKey().toString().toLowerCase() + "!");
                    Addiction.getPlugin().removeAddiction(p, data.getKey());
                }
            }, 36000, 36000);
            if(Addiction.getPlugin().ids.containsKey(p)) {
                Addiction.getPlugin().ids.get(p).put(data.getKey(), id);
            } else {
                HashMap<Addictions, Integer> list = new HashMap<>();
                list.put(data.getKey(), id);
                Addiction.getPlugin().ids.put(p, list);
            }
        }
    }
}
