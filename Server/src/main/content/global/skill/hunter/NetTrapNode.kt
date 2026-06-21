package content.global.skill.hunter

import core.api.getStatLevel
import core.game.node.entity.npc.NPC
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

/**
 * Handles the net trap node.
 */
class NetTrapNode(
    npcIds: IntArray,
    level: Int,
    experience: Double,
    rewards: Array<Item>,
    private val summoningLevel: Int
) : TrapNode(npcIds, level, experience, intArrayOf(), rewards) {

    override fun canCatch(wrapper: TrapWrapper, npc: NPC): Boolean {
        if (getStatLevel(wrapper.player, Skills.SUMMONING) < summoningLevel) {
            return false
        }
        return super.canCatch(wrapper, npc)
    }
}