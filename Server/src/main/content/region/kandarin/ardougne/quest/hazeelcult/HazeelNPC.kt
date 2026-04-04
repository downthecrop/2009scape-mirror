package content.region.kandarin.ardougne.quest.hazeelcult

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.*

/**
 * Hazeel is spawned in briefly during the Mahjarrat end of Hazeel Cult quest.
 */

@Initializable
class HazeelNPC(id: Int = 0, location: Location? = null, ) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any, ): AbstractNPC = HazeelNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.HAZEEL_892)
}
