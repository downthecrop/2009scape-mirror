package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.player.Player;

public class PoisonEffect extends ConsumableEffect {

    private final int amount;

    public PoisonEffect(final int amount) {
        this.amount = amount;
    }

    @Override
    public void activate(Player p) {
        p.getImpactHandler().manualHit(p, amount, ImpactHandler.HitsplatType.POISON);
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return -amount;
    }
}
