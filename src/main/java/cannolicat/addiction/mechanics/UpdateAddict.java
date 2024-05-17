package cannolicat.addiction.mechanics;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.addict.Addictions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Date;

public class UpdateAddict implements ITargetedEntitySkill {
    protected final Addictions addiction;

    public UpdateAddict(MythicLineConfig config) {
        this.addiction = Addictions.valueOf(config.getString(new String[] {"addiction","a"}, String.valueOf(Addictions.MARIJUANA)));
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            if (Addiction.inst().getAddict(livingEntity.getUniqueId()) != null) {
                if(Addiction.inst().getAddict(livingEntity.getUniqueId()).dataAt(addiction) != null) {
                    Addiction.inst().getAddict(livingEntity.getUniqueId()).dataAt(addiction).setDate(new Date());
                    return SkillResult.SUCCESS;
                }
                else return SkillResult.CONDITION_FAILED;
            }
            else return SkillResult.CONDITION_FAILED;
        }
        return SkillResult.INVALID_TARGET;
    }
}
