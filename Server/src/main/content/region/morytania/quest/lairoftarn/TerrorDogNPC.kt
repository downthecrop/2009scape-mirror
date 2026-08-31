package content.region.morytania.quest.lairoftarn

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Terror Dogs, from the Lair of Tarn
 */

@Initializable
class TerrorDogNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return TerrorDogNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TERROR_DOG_5417, NPCs.TERROR_DOG_5418)
    }

    // TODO: this should be removed if/when NPC stacking is properly fixed (ref gitlab issue #2268)
    override fun shouldPreventStacking(mover: Entity?): Boolean {
        return mover !is Player
    }
}