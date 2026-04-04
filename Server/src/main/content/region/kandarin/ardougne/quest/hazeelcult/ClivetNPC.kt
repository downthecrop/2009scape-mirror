package content.region.kandarin.ardougne.quest.hazeelcult

import core.api.*
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

    private var invisibilityTimerRunning = false

    override fun tick() {

        if (isInvisible && !invisibilityTimerRunning) {
            invisibilityTimerRunning = true

            runTask(this, 20) {
                isInvisible = false
                invisibilityTimerRunning = false
            }
        }

        super.tick()
    }
}
