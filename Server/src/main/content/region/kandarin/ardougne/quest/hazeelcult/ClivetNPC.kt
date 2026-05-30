package content.region.kandarin.ardougne.quest.hazeelcult

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.*

/**
 * Clivet disappears briefly during the Hazeel Cult quest.
 */

@Initializable
class ClivetNPC(id: Int = 0, location: Location? = null, ) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any, ): AbstractNPC = ClivetNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.CLIVET_893)

    private var invisTicks = 0

    override fun tick() {
        if (isInvisible && invisTicks <= 0) {
            invisTicks = 20
        }

        if (invisTicks > 0) {
            invisTicks--

            if (invisTicks == 0) {
                isInvisible = false
            }
        }

        super.tick()
    }
}
