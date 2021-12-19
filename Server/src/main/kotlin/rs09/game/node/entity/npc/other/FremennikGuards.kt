package rs09.game.node.entity.npc.other

import api.*
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class FremennikGuards : AbstractNPC {
    //Constructor spaghetti because Arios I guess
    constructor() : super(NPCs.GUARD_5489, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return FremennikGuards(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_5489, NPCs.GUARD_5490)
    }

    override fun tick() {
        if(this.isRespawn) {
            when (id) {
                NPCs.GUARD_5489 -> if (getWorldTicks() % 5 == 0) sendChat(this, "JATIZSO!")
                NPCs.GUARD_5490 -> if (getWorldTicks() % 8 == 0) sendChat(this, "NEITIZNOT!")
            }
        }
        super.tick()
    }
}