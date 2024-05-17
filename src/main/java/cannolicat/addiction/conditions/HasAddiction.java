package cannolicat.addiction.conditions;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.addict.Addict;
import cannolicat.addiction.addict.Addictions;
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
            Addict addict = Addiction.inst().getAddict(livingEntity.getUniqueId());
            return addict != null && addict.hasAddiction(addiction);
        }
        return false;
    }
}
