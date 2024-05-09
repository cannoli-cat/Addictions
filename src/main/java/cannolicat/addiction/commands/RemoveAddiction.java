package cannolicat.addiction.commands;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.Addictions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class RemoveAddiction implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("removeaddiction") && strings.length == 2) {
            Player player = Bukkit.getServer().getPlayer(strings[0]);
            Addictions addictionToRemove = Addictions.valueOf(strings[1]);

            assert player != null;
            if (Addiction.getPlugin().addicts.containsKey(player.getUniqueId())) {
                if(Addiction.getPlugin().addicts.get(player.getUniqueId()).containsKey(addictionToRemove)) {
                    if(Addiction.getPlugin().addicts.get(player.getUniqueId()).size() > 1)
                        Addiction.getPlugin().addicts.get(player.getUniqueId()).remove(addictionToRemove);
                    else
                        Addiction.getPlugin().addicts.remove(player.getUniqueId());

                    if(commandSender instanceof Player)
                        commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + " has had their " + addictionToRemove + " addiction removed!");
                    else
                        Bukkit.getLogger().info("[Addiction] " + player.getName() + " has had their " + addictionToRemove + " addiction removed!");

                    Bukkit.getScheduler().cancelTask(Addiction.getPlugin().ids.get(player).get(addictionToRemove));
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC +
                            "You are no longer addicted to " + addictionToRemove + "!");
                } else {
                    if(commandSender instanceof Player)
                        commandSender.sendMessage(player.getDisplayName() + ChatColor.RED + " does not have a " + addictionToRemove + " addiction!");
                    else
                        Bukkit.getLogger().warning("[Addiction] " + player.getName() + " does not have a " + addictionToRemove + " addiction!");
                }
            } else {
                if (commandSender instanceof Player)
                    commandSender.sendMessage(ChatColor.RED + "This player is not an addict!");
                else
                    Bukkit.getLogger().warning("[Addiction] This player is not an addict!");
            }
        } else {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.RED + "You must specify a player and addiction to remove!");
            }
            else {
                Bukkit.getLogger().warning("[Addiction] You must specify a player and addiction to remove!");
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
