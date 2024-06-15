package cannolicat.addictions.conditions;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.Addiction;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class HasAddiction implements IEntityCondition {
    protected final Addiction addiction;

    public HasAddiction(MythicLineConfig config) {
        this.addiction = Addictions.getAddiction(config.getString(new String[] {"addiction","a"})).orElse(null);
    }

    @Override
    public boolean check(AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player && addiction != null) {
            Addict addict = Addictions.inst().getAddict(livingEntity.getUniqueId()).orElse(null);
            return addict != null && addict.hasAddiction(addiction);
        }
        return false;
    }
}
