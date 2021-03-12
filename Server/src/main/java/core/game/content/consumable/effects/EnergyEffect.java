package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;

public class EnergyEffect extends ConsumableEffect {
    double amt;
    public EnergyEffect(int amt){
        this.amt = amt;
    }
    @Override
    public void activate(Player p) {
        p.getSettings().updateRunEnergy(-amt);
    }
}
