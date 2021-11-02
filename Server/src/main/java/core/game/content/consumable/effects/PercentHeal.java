package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;

public class PercentHeal extends ConsumableEffect {
    double perc = 0.0;

    public PercentHeal(double percent){
        perc = percent;
    }

    @Override
    public void activate(Player p) {
        int maxHp = p.getSkills().getMaximumLifepoints();
        int curHp = p.getSkills().getLifepoints();
        int amount = (int) Math.floor(maxHp * perc);
        amount = Math.min(amount, (int)((1.0 + perc) * (double)maxHp - (double)curHp));
        p.getSkills().healNoRestrictions(amount);
    }
}
