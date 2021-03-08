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
        int amount = (int) Math.floor(p.getSkills().getMaximumLifepoints() * perc);
        if(p.getSkills().getLifepoints() + amount > (p.getSkills().getMaximumLifepoints() + (p.getSkills().getMaximumLifepoints() * perc))){
            amount = 0;
        }
        p.getSkills().healNoRestrictions(amount);
    }
}
