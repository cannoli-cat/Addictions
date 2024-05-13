package cannolicat.addiction.mechanics;

import cannolicat.addiction.Addiction;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RemoveAddictions implements ITargetedEntitySkill {
    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) abstractEntity;
        if(livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            if (Addiction.getPlugin().addicts.containsKey(player.getUniqueId())) {
                Addiction.getPlugin().addicts.remove(player.getUniqueId());

                for (int id : Addiction.getPlugin().ids.get(player).values()) {
                    Bukkit.getScheduler().cancelTask(id);
                }

                Addiction.getPlugin().ids.remove(player);
                return SkillResult.SUCCESS;
            }
            return SkillResult.CONDITION_FAILED;
        }
        return SkillResult.INVALID_TARGET;
    }
}
