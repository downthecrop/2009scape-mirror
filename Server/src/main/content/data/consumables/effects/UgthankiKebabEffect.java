package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;

public class UgthankiKebabEffect extends ConsumableEffect {

    private static final int healing = 19;

    private static final HealingEffect effect = new HealingEffect(healing);

    @Override
    public void activate(Player p) {
        if (p.getSkills().getLifepoints() < p.getSkills().getMaximumLifepoints()) {
            p.sendChat("Yum!");
        }
        effect.activate(p);
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return healing;
    }
}
