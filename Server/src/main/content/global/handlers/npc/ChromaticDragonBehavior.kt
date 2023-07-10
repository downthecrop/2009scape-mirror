package content.global.handlers.npc

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.Entity
import core.game.node.item.Item
import org.rs09.consts.*
import java.util.ArrayList

class ChromaticDragonBehavior : NPCBehavior (*greenDragons, *blueDragons, *redDragons, *blackDragons)
{
    override fun onDropTableRolled (self: NPC, drops: ArrayList<Item>) {

    }

    companion object {
        val greenDragons = intArrayOf (NPCs.GREEN_DRAGON_941, NPCs.GREEN_DRAGON_4677, NPCs.GREEN_DRAGON_4678, NPCs.GREEN_DRAGON_4679, NPCs.GREEN_DRAGON_4680)
        val blueDragons = intArrayOf (NPCs.BLUE_DRAGON_55, NPCs.BLUE_DRAGON_4681, NPCs.BLUE_DRAGON_4682, NPCs.BLUE_DRAGON_4683, NPCs.BLUE_DRAGON_4684)
        val redDragons = intArrayOf (NPCs.RED_DRAGON_53, NPCs.RED_DRAGON_4669, NPCs.RED_DRAGON_4670, NPCs.RED_DRAGON_4671, NPCs.RED_DRAGON_4672)
        val blackDragons = intArrayOf (NPCs.BLACK_DRAGON_54, NPCs.BLACK_DRAGON_4673, NPCs.BLACK_DRAGON_4674, NPCs.BLACK_DRAGON_4675, NPCs.BLACK_DRAGON_4676)
    }
}
