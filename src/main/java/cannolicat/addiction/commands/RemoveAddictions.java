package cannolicat.addiction.commands;

import cannolicat.addiction.Addiction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveAddictions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("removeaddictions") && strings.length == 1) {
            Player player = Bukkit.getServer().getPlayer(strings[0]);

            assert player != null;
            if (Addiction.getPlugin().addicts.containsKey(player.getUniqueId())) {

                Addiction.getPlugin().addicts.remove(player.getUniqueId());

                if(commandSender instanceof Player)
                    commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + "'s addictions have been cleared!");
                else
                    Bukkit.getLogger().warning(player.getDisplayName() + "'s addictions have been cleared!");

                for(int id : Addiction.getPlugin().ids.get(player).values()) {
                    Bukkit.getScheduler().cancelTask(id);
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Congratulations! You are no longer an addict!");
                }

                Addiction.getPlugin().ids.remove(player);
            } else {
                if (commandSender instanceof Player)
                    commandSender.sendMessage(ChatColor.RED + "This player is not an addict!");
                else
                    Bukkit.getLogger().warning("This player is not an addict!");
            }
        } else {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.RED + "You must specify a player to clear addictions for!");
            }
            else {
                Bukkit.getLogger().warning("You must specify a player to clear addictions for!");
            }
        }
        return true;
    }
}
