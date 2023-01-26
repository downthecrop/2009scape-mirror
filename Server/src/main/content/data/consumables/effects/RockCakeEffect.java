package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;

public class RockCakeEffect extends ConsumableEffect {

    private static final DamageEffect effect = new DamageEffect(10, true);

    @Override
    public void activate(Player p) {
        if (p.getSkills().getLifepoints() > 1) {
            effect.activate(p);
        }
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return (int) (player.getSkills().getLifepoints() * -0.1);
    }
}
