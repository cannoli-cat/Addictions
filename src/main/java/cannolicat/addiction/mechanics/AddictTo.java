package cannolicat.addiction.mechanics;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.addict.Addict;
import cannolicat.addiction.addict.Addictions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AddictTo implements ITargetedEntitySkill {
    protected final Addictions addiction;

    public AddictTo(MythicLineConfig config) {
        this.addiction = Addictions.valueOf(config.getString(new String[] {"addiction","a"}, String.valueOf(Addictions.MARIJUANA)));
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            Addict addict = Addiction.inst().getAddict(livingEntity.getUniqueId());

            if (addict != null)
                addict.addAddiction(addiction);
            else
                new Addict(livingEntity.getUniqueId(), addiction);

            return SkillResult.SUCCESS;
        }
        return SkillResult.INVALID_TARGET;
    }
}
