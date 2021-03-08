package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.tools.RandomFunction;
import core.game.content.consumable.ConsumableEffect;

public class RandomEnergyEffect extends ConsumableEffect {

    private final int a, b;

    public RandomEnergyEffect(final int a, final int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void activate(Player p) {
        final EnergyEffect effect = new EnergyEffect(RandomFunction.random(a, b));
        effect.activate(p);
    }
}
