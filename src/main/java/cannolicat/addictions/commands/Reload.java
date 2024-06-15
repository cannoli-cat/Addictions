package cannolicat.addictions.commands;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.Addiction;
import cannolicat.addictions.addict.AddictionData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Reload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[Addiction] " + ChatColor.RESET + "Reloading plugin...");

        Addictions.inst().reloadConfig();

        Addictions.getAddictions().clear();
        Addictions.inst().getConfig().getKeys(false).forEach(key ->
                        Addictions.getAddictions().add(new Addiction(key,
                        Addictions.inst().getConfig().getInt(key + ".timeBetweenSymptoms"),
                        Addictions.inst().getConfig().getInt(key + ".timeUntilClean"))));

        Addictions.inst().saveConfig();

        for(HashMap<Addiction, Integer> map : Addictions.inst().ids.values()) {
            for(int id : map.values()) {
                Bukkit.getScheduler().cancelTask(id);
            }
        }
        Addictions.inst().ids.clear();

        for (Addict a : Addictions.inst().addicts) {
            for(AddictionData data : a.getData()) {
                data = new AddictionData(Addictions.getAddiction(data.getAddiction().getName()).orElse(null), data.getDate());
                Addictions.addicted.triggerAddiction(a.getPlayer(), data);
            }
        }

        commandSender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "[Addiction] " + ChatColor.RESET + "Reloaded!");
        return true;
    }
}
