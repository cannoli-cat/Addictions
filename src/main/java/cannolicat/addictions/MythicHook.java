package cannolicat.addictions;

import cannolicat.addictions.conditions.HasAddiction;
import cannolicat.addictions.conditions.IsAddict;
import cannolicat.addictions.mechanics.AddictTo;
import cannolicat.addictions.mechanics.RemoveAddictions;
import cannolicat.addictions.mechanics.UpdateAddict;
import io.lumine.mythic.bukkit.events.MythicConditionLoadEvent;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MythicHook implements Listener {
    @EventHandler
    public void onMythicMechanicLoad(MythicMechanicLoadEvent event)	{
        switch (event.getMechanicName().toUpperCase()) {
            case "ADDICTIONADD", "ADDADDICTION", "ADDADDICT", "ADDICT" ->
                    event.register(new AddictTo(event.getConfig()));
            case "UPDATEADDICTION", "USEDADDICTION", "USED", "UPDATEADDICT" ->
                    event.register(new UpdateAddict(event.getConfig()));
            case "CLEARADDICTIONS", "REMOVEADDICT", "CLEARADDICT", "REMOVEADDICTIONS" ->
                    event.register(new RemoveAddictions());
            default -> {}
        }
    }

    @EventHandler
    public void onMythicConditionLoad(MythicConditionLoadEvent event) {
        switch (event.getConditionName().toUpperCase()) {
            case "HASADDICTION", "ISADDICTED" ->
                    event.register(new HasAddiction(event.getConfig()));
            case "ISADDICT", "ADDICT" ->
                    event.register(new IsAddict());
            default -> {}
        }
    }
}
