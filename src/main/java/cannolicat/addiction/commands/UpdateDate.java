package cannolicat.addiction.commands;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.Addictions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

public class UpdateDate implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("updatedate") && strings.length == 2) {
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

            Addictions addictionToUpdate;
            try {
                addictionToUpdate = Addictions.valueOf(strings[1].toUpperCase());
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
                    contains = addiction.equals(addictionToUpdate);
                    if (contains) break;
                }
                if (contains) {
                    Addiction.getPlugin().addicts.get(player.getUniqueId()).get(addictionToUpdate).setDate(new Date());
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[Addiction]" + ChatColor.RESET +" Time since last use for " + ChatColor.GOLD + addictionToUpdate + ChatColor.RESET + " updated for " + player.getDisplayName());
                } else {
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is not addicted to " + ChatColor.RED + addictionToUpdate + ChatColor.RESET + "!");
                }
            }
            else {
                if (commandSender instanceof Player)
                    commandSender.sendMessage(ChatColor.RED + "This player is not an addict!");
                else
                    Bukkit.getLogger().warning("[Addiction] This player is not an addict!");
            }
        } else {
            if(commandSender instanceof Player) {
                Player sender = (Player) commandSender;
                sender.sendMessage(ChatColor.RED + "You must specify a player and addiction to update!");
            }
            else {
                Bukkit.getLogger().warning("[Addiction] You must specify a player and addiction to update!");
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
}
