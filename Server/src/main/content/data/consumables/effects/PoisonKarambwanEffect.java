package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;

public class PoisonKarambwanEffect extends ConsumableEffect {

    private final PoisonEffect effect = new PoisonEffect(5);

    @Override
    public void activate(Player p) {
        if (p.getSkills().getLifepoints() > 5) {
            effect.activate(p);
        }
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return -5;
    }
}
