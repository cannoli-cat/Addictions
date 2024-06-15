package cannolicat.addictions.addict;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.Serial;
import java.io.Serializable;

public class SerializablePotionEffect implements Serializable {
    @Serial
    private static final long serialVersionUID = 375638103850091735L;
    private final String name;
    private final int duration, amplifier;
    private final boolean hasIcon, hasParticles;

    public SerializablePotionEffect(final String name, final int duration, final int amplifier, final boolean hasIcon, final boolean hasParticles) {
        this.name = name;
        this.duration = duration;
        this.amplifier = amplifier;
        this.hasIcon = hasIcon;
        this.hasParticles = hasParticles;
    }

    public PotionEffect getPotionEffect() {
        PotionEffectType type = Registry.EFFECT.get(NamespacedKey.minecraft(name));
        return type == null ? null : new PotionEffect(type, duration, amplifier, true, hasParticles, hasIcon);
    }
}
