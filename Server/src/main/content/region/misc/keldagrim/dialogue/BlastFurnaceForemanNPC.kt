package content.region.misc.keldagrim.dialogue

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

@Initializable
class BlastFurnaceForemanNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id,location) {
    companion object {
        val MESSAGES = arrayOf(
                // Mostly https://youtu.be/hZ_q441esf0
                "Somebody fix the conveyor belt!", // When the drive belt or cogs are broken -> conveyor belt.
                "Somebody work the conveyor belt!",
                "Somebody fix that pipe!",
                "Somebody operate the pump!",
                "Somebody refuel the stove!",
                "Keep the temperature controlled.", // https://youtu.be/McMNWADwdF8&t=38
                "Work faster!",
                "That's, it keep it going."

        )
    }

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return BlastFurnaceForemanNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLAST_FURNACE_FOREMAN_2553)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        // TODO: Have chat messages pop up according to the state of the Blast Furnace and not on a dice roll.
        if (RandomFunction.random(20) < 1) {
            core.api.sendChat(this, MESSAGES.random())
        }
    }

}