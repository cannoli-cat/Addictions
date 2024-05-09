package cannolicat.addiction.commands;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.Addictions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowAddictions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("showaddictions") && strings.length == 1) {
            Player search = Bukkit.getServer().getPlayer(strings[0]);
            assert search != null;

            if(Addiction.getPlugin().addicts.containsKey(search.getUniqueId())) {
                StringBuilder message = new StringBuilder(search.getDisplayName() + "'s addictions: ");
                for(Addictions addiction : Addiction.getPlugin().addicts.get(search.getUniqueId()).keySet()) {
                    message.append(ChatColor.GOLD + addiction.toString()).append(ChatColor.RESET + ", ");
                }

                message.deleteCharAt(message.length() - 1);
                message.deleteCharAt(message.length() - 1);

                if (commandSender instanceof Player)
                    commandSender.sendMessage(message + ".");
                else
                    Bukkit.getLogger().info("[Addiction] " + message + ".");
            } else {
                if (commandSender instanceof Player)
                    commandSender.sendMessage(ChatColor.RED + "This player is not an addict!");
                else
                    Bukkit.getLogger().warning("[Addiction] This player is not an addict!");
            }
        } else {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.RED + "You must specify a player to show the addictions!");
            }
            else {
                Bukkit.getLogger().warning("[Addiction] You must specify a player to show the addictions!");
            }
        }
        return true;
    }
}
