package rs09.game.node.entity.npc.other

import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

@Initializable
class YakNPC : AbstractNPC {
    //Constructor spaghetti because Arios I guess
    constructor() : super(NPCs.ICE_TROLL_MALE_5474, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return YakNPC(id,location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.YAK_5529)
    }

    override fun tick() {
        if(RandomFunction.roll(20)){
            sendChat("Moo")
        }
        super.tick()
    }

}