package content.global.handlers.npc

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.*
import kotlin.collections.ArrayList

class ChromaticDragonBehavior : NPCBehavior(*greenDragons, *blueDragons, *redDragons, *blackDragons)
{
    override fun onDropTableRolled (self: NPC, killer: Entity, drops: ArrayList<Item>) {
        val removeList = ArrayList<Item>()
        for (item in drops) {
            when (item.id) {
                Items.BLACK_DRAGON_EGG_12480,
                Items.RED_DRAGON_EGG_12477,
                Items.BLUE_DRAGON_EGG_12478,
                Items.GREEN_DRAGON_EGG_12479 -> removeList.add(item)
            }
        }
        drops.removeAll(removeList)

        if (killer.skills.getStaticLevel(Skills.SUMMONING) >= 99 && RandomFunction.roll(EGG_RATE))
            drops.add(when(self.id) {
                in greenDragons -> Item(Items.GREEN_DRAGON_EGG_12479)
                in blueDragons -> Item(Items.BLUE_DRAGON_EGG_12478)
                in redDragons -> Item(Items.RED_DRAGON_EGG_12477)
                in blackDragons -> Item(Items.BLACK_DRAGON_EGG_12480)
                else -> Item(Items.DRAGON_BONES_536)
            })
    }

    companion object {
        val greenDragons = intArrayOf (NPCs.GREEN_DRAGON_941, NPCs.GREEN_DRAGON_4677, NPCs.GREEN_DRAGON_4678, NPCs.GREEN_DRAGON_4679, NPCs.GREEN_DRAGON_4680)
        val blueDragons = intArrayOf (NPCs.BLUE_DRAGON_55, NPCs.BLUE_DRAGON_4681, NPCs.BLUE_DRAGON_4682, NPCs.BLUE_DRAGON_4683, NPCs.BLUE_DRAGON_4684)
        val redDragons = intArrayOf (NPCs.RED_DRAGON_53, NPCs.RED_DRAGON_4669, NPCs.RED_DRAGON_4670, NPCs.RED_DRAGON_4671, NPCs.RED_DRAGON_4672)
        val blackDragons = intArrayOf (NPCs.BLACK_DRAGON_54, NPCs.BLACK_DRAGON_4673, NPCs.BLACK_DRAGON_4674, NPCs.BLACK_DRAGON_4675, NPCs.BLACK_DRAGON_4676)
        var EGG_RATE = 1000
    }
}
