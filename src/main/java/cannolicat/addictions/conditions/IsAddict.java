package cannolicat.addictions.conditions;

import cannolicat.addictions.Addictions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class IsAddict implements IEntityCondition {
    @Override
    public boolean check(AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player)
            return Addictions.inst().getAddict(livingEntity.getUniqueId()).isPresent();
        return false;
    }
}
