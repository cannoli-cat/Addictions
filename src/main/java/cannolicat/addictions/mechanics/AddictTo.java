package cannolicat.addictions.mechanics;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addict;
import cannolicat.addictions.addict.Addiction;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AddictTo implements ITargetedEntitySkill {
    protected final Addiction addiction;

    public AddictTo(MythicLineConfig config) {
        this.addiction = Addictions.getAddiction(config.getString(new String[] {"addiction","a"})).orElse(null);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        if (addiction == null) return SkillResult.ERROR;

        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            Addict addict = Addictions.inst().getAddict(livingEntity.getUniqueId()).orElse(null);

            if (addict != null)
                addict.addAddiction(addiction);
            else
                new Addict(livingEntity.getUniqueId(), addiction);

            return SkillResult.SUCCESS;
        }
        return SkillResult.INVALID_TARGET;
    }
}
