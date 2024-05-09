package cannolicat.addiction;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Addictions {
    TOBACCO(new PotionEffect[]{
            new PotionEffect(PotionEffectType.HUNGER, 1000, 0),
            new PotionEffect(PotionEffectType.CONFUSION, 200, 0)
    }),
    MARIJUANA(new PotionEffect[]{
            new PotionEffect(PotionEffectType.SLOW, 1200, 0),
            new PotionEffect(PotionEffectType.CONFUSION, 200, 0),
            new PotionEffect(PotionEffectType.SATURATION, 600, 0)
    }),
    COCAINE(new PotionEffect[]{
            new PotionEffect(PotionEffectType.HUNGER, 2000, 1),
            new PotionEffect(PotionEffectType.WEAKNESS, 1200, 0),
            new PotionEffect(PotionEffectType.SLOW, 2000, 1)
    }),
    OPIUM(new PotionEffect[]{
            new PotionEffect(PotionEffectType.CONFUSION, 1000, 0),
            new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1),
            new PotionEffect(PotionEffectType.BLINDNESS, 400, 0)
    }),
    BUFFOUT(new PotionEffect[]{
            new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1),
            new PotionEffect(PotionEffectType.SLOW, 1200, 1),
            new PotionEffect(PotionEffectType.WITHER, 100, 0)
    }),
    MEDX(new PotionEffect[]{
            new PotionEffect(PotionEffectType.SLOW, 1200, 0),
            new PotionEffect(PotionEffectType.UNLUCK, 3000, 1),
            new PotionEffect(PotionEffectType.WEAKNESS, 300, 0),
    }),
    PSYCHO(new PotionEffect[]{
            new PotionEffect(PotionEffectType.WEAKNESS, 1200, 0),
            new PotionEffect(PotionEffectType.SLOW, 1200, 0),
    }),
    METHAMPHETAMINE(new PotionEffect[]{
            new PotionEffect(PotionEffectType.WEAKNESS, 1200, 1),
            new PotionEffect(PotionEffectType.SLOW, 800, 1),
            new PotionEffect(PotionEffectType.HUNGER, 1200, 1)
    });

    private final PotionEffect[] effects;

    Addictions(PotionEffect[] effects) {
        this.effects = effects;
    }

    public PotionEffect[] getEffects() {
        return effects;
    }
}
