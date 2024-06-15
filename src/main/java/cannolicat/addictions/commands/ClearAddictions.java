package cannolicat.addictions.commands;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearAddictions implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("clearaddictions") && strings.length == 1) {
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

            assert player != null;
            Addict addict = Addictions.inst().getAddict(player.getUniqueId()).orElse(null);
            if (addict != null) {
                addict.remove();

                if(commandSender instanceof Player)
                    commandSender.sendMessage(player.getDisplayName() + ChatColor.GREEN + "'s addictions have been cleared!");
                else
                    Bukkit.getLogger().info("[Addiction] " + player.getName() + "'s addictions have been cleared!");

                player.sendMessage(ChatColor.GOLD + "" + ChatColor.ITALIC + "Congratulations! You are no longer an addict!");
            } else {
                if (commandSender instanceof Player)
                    commandSender.sendMessage(ChatColor.RED + "This player is not an addict!");
                else
                    Bukkit.getLogger().warning("[Addiction] This player is not an addict!");
            }
        } else {
            if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.sendMessage(ChatColor.RED + "You must specify a player to clear addictions for!");
            }
            else {
                Bukkit.getLogger().warning("[Addiction] You must specify a player to clear addictions for!");
            }
        }
        return true;
    }
}
