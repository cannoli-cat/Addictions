package cannolicat.addictions.commands;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.Addiction;
import cannolicat.addictions.addict.AddictionData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Date;
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

            Addiction addictionToUpdate;

            if(Addictions.getAddiction(strings[1]).isPresent()) {
                addictionToUpdate = Addictions.getAddiction(strings[1]).get();
            } else {
                if(commandSender instanceof Player) {
                    commandSender.sendMessage(ChatColor.RED + "You must enter a valid addiction!");
                } else {
                    Bukkit.getLogger().warning("[Addiction] You must enter a valid addiction!");
                }

                return true;
            }

            assert player != null;
            Addict addict = Addictions.inst().getAddict(player.getUniqueId()).orElse(null);
            if (addict != null) {
                if (addict.hasAddiction(addictionToUpdate) && addict.dataAt(addictionToUpdate).isPresent()) {
                   addict.dataAt(addictionToUpdate).get().setDate(new Date());
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[Addiction]" + ChatColor.RESET +" Time since last use for " + ChatColor.GOLD + addictionToUpdate.getName().toLowerCase() + ChatColor.RESET + " updated for " + player.getDisplayName());
                } else {
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " is not addicted to " + ChatColor.RED + addictionToUpdate.getName().toLowerCase() + ChatColor.RESET + "!");
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
            if(Addictions.inst().getAddict(Bukkit.getPlayer(strings[0]).getUniqueId()).isPresent()) {
                for (AddictionData datum : Addictions.inst().getAddict(Bukkit.getPlayer(strings[0]).getUniqueId()).get().getData()) {
                    list.add(datum.getAddiction().getName());
                }
            }
            return list.isEmpty() ? null : list;
        }
        return null;
    }
}
