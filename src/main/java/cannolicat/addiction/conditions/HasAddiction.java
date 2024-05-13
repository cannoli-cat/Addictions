package cannolicat.addiction.conditions;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.Addictions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class HasAddiction implements IEntityCondition {
    protected final Addictions addiction;

    public HasAddiction(MythicLineConfig config) {
        this.addiction = Addictions.valueOf(config.getString(new String[] {"addiction","a"}, String.valueOf(Addictions.MARIJUANA)));
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            return Addiction.getPlugin().addicts.containsKey(player.getUniqueId()) && Addiction.getPlugin().addicts.get(player.getUniqueId()).containsKey(addiction);
        }
        return false;
    }
}
