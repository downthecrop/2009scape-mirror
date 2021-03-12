package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;

public class HealingEffect extends ConsumableEffect {
    int amt;
    public HealingEffect(int amount){
        this.amt = amount;
    }
    @Override
    public void activate(Player p) {
        p.getSkills().heal(amt);
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return amt;
    }
}
