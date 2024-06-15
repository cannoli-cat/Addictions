package cannolicat.addictions.mechanics;

import cannolicat.addictions.Addictions;
import cannolicat.addictions.addict.Addiction;
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
    protected final Addiction addiction;

    public UpdateAddict(MythicLineConfig config) {
        this.addiction = Addictions.getAddiction(config.getString(new String[] {"addiction","a"})).orElse(null);
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            if (Addictions.inst().getAddict(livingEntity.getUniqueId()).isPresent()) {
                if(Addictions.inst().getAddict(livingEntity.getUniqueId()).get().dataAt(addiction).isPresent()) {
                    Addictions.inst().getAddict(livingEntity.getUniqueId()).get().dataAt(addiction).get().setDate(new Date());
                    return SkillResult.SUCCESS;
                }
                else return SkillResult.CONDITION_FAILED;
            }
            else return SkillResult.CONDITION_FAILED;
        }
        return SkillResult.INVALID_TARGET;
    }
}
