package content.region.misthalin.varrock.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

@Initializable
class BennyNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return BennyNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BENNY_5925)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.roll(12)) {
            core.api.sendChat(this, messages.random())
        }
    }

    override fun getWalkRadius(): Int {
        return 6
    }

    companion object {
        private val messages = arrayOf(
            "Read all about it!",
            "Varrock Herald, on sale here!",
            "Buy your Varrock Herald now!",
            "Extra! Extra! Read all about it!",
            "Varrock Herald, now only 50 gold!"
        )
    }
}