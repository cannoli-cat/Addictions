package cannolicat.addiction.conditions;

import cannolicat.addiction.Addiction;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class IsAddict implements IEntityCondition {
    @Override
    public boolean check(AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            return Addiction.getPlugin().addicts.containsKey(player.getUniqueId());
        }
        return false;
    }
}
