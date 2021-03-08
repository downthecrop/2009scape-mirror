package core.game.content.consumable.effects;

import core.game.node.entity.player.Player;
import core.game.content.consumable.ConsumableEffect;
import core.game.node.entity.skill.Skills;

public class WizardsMindBombEffect extends ConsumableEffect {

    private final static int healing = 1;

    @Override
    public void activate(Player p) {
        final int magicLevelBoost = p.getSkills().getLevel(Skills.MAGIC) > 50 ? 3 : 2;
        final MultiEffect effect = new MultiEffect(new SkillEffect(Skills.MAGIC, magicLevelBoost, 0), new HealingEffect(healing), new SkillEffect(Skills.ATTACK, -3, 0), new SkillEffect(Skills.STRENGTH, -4, 0), new SkillEffect(Skills.DEFENCE, -4, 0));
        effect.activate(p);
    }

    @Override
    public int getHealthEffectValue(Player player) {
        return healing;
    }
}
