package rs09.game.content.consumable.effects

import core.game.node.entity.player.Player
import core.game.content.consumable.ConsumableEffect
import core.game.node.entity.skill.Skills

class RestoreSummoning(val percent: Double) : ConsumableEffect(){
    override fun activate(p: Player?) {
        var amt = (p!!.skills!!.getStaticLevel(Skills.SUMMONING) * percent).toInt()
        if(amt + p.skills.getLevel(Skills.SUMMONING) > p.skills.getStaticLevel(Skills.SUMMONING)) amt = p.skills.getStaticLevel(Skills.SUMMONING) - p.skills.getLevel(Skills.SUMMONING)
        p.skills.setLevel(Skills.SUMMONING, p.skills.getLevel(Skills.SUMMONING) + amt)
    }
}