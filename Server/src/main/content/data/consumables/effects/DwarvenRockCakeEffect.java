package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;

public class DwarvenRockCakeEffect extends ConsumableEffect {

    private static final DamageEffect effect = new DamageEffect(1, false);

    @Override
    public void activate(Player p) {
        if (p.getSkills().getLifepoints() > 2) {
            effect.activate(p);
        }
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return player.getSkills().getLifepoints() > 2 ? -1 : 0;
    }
}
