package content.data.consumables.effects

import core.game.consumable.ConsumableEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills


class KegOfBeerEffect : ConsumableEffect() {

    override fun activate(p: Player?) {
        val effect = MultiEffect(HealingEffect(15), SkillEffect(Skills.STRENGTH, 10.0, 0.0), SkillEffect(Skills.ATTACK, -40.0, 0.0))
        effect.activate(p)
    }

}