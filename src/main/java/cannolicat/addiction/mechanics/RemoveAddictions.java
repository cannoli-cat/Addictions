package cannolicat.addiction.mechanics;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.addict.Addict;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RemoveAddictions implements ITargetedEntitySkill {
    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if(livingEntity instanceof Player) {
            Addict addict = Addiction.inst().getAddict(livingEntity.getUniqueId());
            if (addict != null) {
                addict.remove();
                return SkillResult.SUCCESS;
            }
            return SkillResult.CONDITION_FAILED;
        }
        return SkillResult.INVALID_TARGET;
    }
}
