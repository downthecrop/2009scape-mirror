package content.data.consumables.effects;

import core.game.consumable.ConsumableEffect;
import core.game.node.entity.player.Player;
import core.tools.RandomFunction;

public class RandomSkillEffect extends ConsumableEffect {

    private final int skillSlot, a, b;

    public RandomSkillEffect(final int skillSlot, final int a, final int b) {
        this.skillSlot = skillSlot;
        this.a = a;
        this.b = b;
    }

    @Override
    public void activate(Player p) {
        final SkillEffect effect = new SkillEffect(skillSlot, RandomFunction.random(a, b), 0);
        effect.activate(p);
    }
}
