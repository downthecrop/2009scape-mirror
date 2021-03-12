package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;

public class MultiEffect extends ConsumableEffect {
    private ConsumableEffect[] effects;
    public MultiEffect(ConsumableEffect... effects){
        this.effects = effects;
    }
    @Override
    public void activate(Player p) {
        for(ConsumableEffect e : effects){
            e.activate(p);
        }
    }

    @Override
    public int getHealthEffectValue(Player player) {
        int healing = 0;
        for (ConsumableEffect effect : effects) {
            healing += effect.getHealthEffectValue(player);
        }
        return healing;
    }
}
