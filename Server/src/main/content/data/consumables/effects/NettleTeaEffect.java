package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;

public class NettleTeaEffect extends ConsumableEffect {

    private final static int healing = 3;

    @Override
    public void activate(Player p) {
        final ConsumableEffect effect = p.getSkills().getLifepoints() < p.getSkills().getMaximumLifepoints() ? new MultiEffect(new HealingEffect(3), new EnergyEffect(5)) : new HealingEffect(3);
        effect.activate(p);
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return healing;
    }
}
