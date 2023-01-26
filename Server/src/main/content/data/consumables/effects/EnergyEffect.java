package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;

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
