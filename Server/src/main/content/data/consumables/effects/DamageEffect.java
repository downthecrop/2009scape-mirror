package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.combat.ImpactHandler;
import core.game.node.entity.player.Player;

public class DamageEffect extends ConsumableEffect {
    final double amt;
    final boolean isPercent;
    final int baseAmount;

    public DamageEffect(double amt, boolean isPercent) {
        this(amt, isPercent, 0);
    }

    public DamageEffect(double amt, boolean isPercent, int baseAmount) {
        this.amt = amt;
        this.isPercent = isPercent;
        this.baseAmount = baseAmount;
    }

    @Override
    public void activate(Player p) {
        p.getImpactHandler().manualHit(p, -getHealthEffectValue(p), ImpactHandler.HitsplatType.NORMAL);
    }

    @Override
    public int getHealthEffectValue(Player player) {
        double amount = amt;
        if (isPercent) {
            amount /= 100;
            return (int) -(amount * player.getSkills().getLifepoints() + baseAmount);
        }
        return (int) -(amt + baseAmount);
    }
}
