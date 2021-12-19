package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;
import core.game.node.entity.skill.Skills;

public class SummoningEffect extends ConsumableEffect {
    double base, bonus;
    public SummoningEffect(double base, double bonus){
        this.base = base;
        this.bonus = bonus;
    }
    @Override
    public void activate(Player p) {
        int level = p.getSkills().getStaticLevel(Skills.SUMMONING);
        double amt = base + (level * bonus);
        p.getSkills().updateLevel(Skills.SUMMONING, (int)amt, level);
    }
}
