package rs09.game.content.consumable.effects

import core.game.content.consumable.ConsumableEffect
import core.game.content.consumable.effects.HealingEffect
import core.game.content.consumable.effects.MultiEffect
import core.game.content.consumable.effects.SkillEffect
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills


class KegOfBeerEffect : ConsumableEffect() {

    override fun activate(p: Player?) {
        val effect = MultiEffect(HealingEffect(15), SkillEffect(Skills.STRENGTH, 10.0, 0.0), SkillEffect(Skills.ATTACK, -40.0, 0.0))
        effect.activate(p)
    }

}