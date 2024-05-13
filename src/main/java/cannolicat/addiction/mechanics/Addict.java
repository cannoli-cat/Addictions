package cannolicat.addiction.mechanics;

import cannolicat.addiction.Addiction;
import cannolicat.addiction.AddictionData;
import cannolicat.addiction.Addictions;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.HashMap;

public class Addict implements ITargetedEntitySkill {
    protected final Addictions addiction;

    public Addict(MythicLineConfig config) {
        this.addiction = Addictions.valueOf(config.getString(new String[] {"addiction","a"}, String.valueOf(Addictions.MARIJUANA)));
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        LivingEntity livingEntity = (LivingEntity) BukkitAdapter.adapt(abstractEntity);
        if (livingEntity instanceof Player) {
            Player player = (Player) livingEntity;
            if (Addiction.getPlugin().addicts.containsKey(player.getUniqueId())) {
                AddictionData data = new AddictionData(addiction);
                Addiction.getPlugin().addicts.get(player.getUniqueId()).put(addiction, data);
                Addiction.addicted.triggerAddiction(player, data);
            } else {
                AddictionData data = new AddictionData(addiction);
                HashMap<Addictions, AddictionData> addictionData = new HashMap<>();
                addictionData.put(addiction, data);

                Addiction.getPlugin().addicts.put(player.getUniqueId(), addictionData);
                Addiction.addicted.triggerAddiction(player, data);
            }
            return SkillResult.SUCCESS;
        }
        return SkillResult.INVALID_TARGET;
    }
}
