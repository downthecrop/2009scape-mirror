package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;

public class PercentHeal extends ConsumableEffect {
    int base = 0;
    double percent = 0.0;

    public PercentHeal(int base, double percent){
        this.base = base;
        this.percent = percent;
    }

    @Override
    public void activate(Player p) {
        int maxHp = p.getSkills().getMaximumLifepoints();
        int curHp = p.getSkills().getLifepoints();
        int amount = (int) Math.floor(maxHp * percent);
        amount = base + Math.min(amount, (int)((1.0 + percent) * (double)maxHp - (double)curHp));
        p.getSkills().healNoRestrictions(amount);
    }
}
