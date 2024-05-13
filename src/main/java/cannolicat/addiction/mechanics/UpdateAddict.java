package cannolicat.addiction.mechanics;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.Addictions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
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
            Player player = (Player) livingEntity;
            if (Addiction.getPlugin().addicts.containsKey(player.getUniqueId())) {
                Addiction.getPlugin().addicts.get(player.getUniqueId()).get(addiction).setDate(new Date());
                return SkillResult.SUCCESS;
            } else {
                Bukkit.getLogger().warning("[Addiction] This player does not have this addiction!");
                return SkillResult.ERROR;
            }
        }
        return SkillResult.INVALID_TARGET;
    }
}
