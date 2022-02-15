package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;
import core.game.node.entity.skill.Skills;

public class SkillEffect extends ConsumableEffect {
    private final int skill_slot;
    private final double base;
    private final double bonus;
    public SkillEffect(int skill_slot, double base, double bonus){
        this.skill_slot = skill_slot;
        this.base = base;
        this.bonus = bonus;
    }
    @Override
    public void activate(Player p) {
        Skills skills = p.getSkills();
        int slevel = skills.getStaticLevel(skill_slot);
        int delta = (int)(base + (bonus * slevel));
        skills.updateLevel(skill_slot, delta, delta >= 0 ? slevel + delta : 0);
    }
}
