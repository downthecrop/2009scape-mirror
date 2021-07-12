package rs09.game.node.entity.npc.other

import api.ContentAPI
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import rs09.game.system.SystemLogger

@Initializable
class IceTrollGFI : AbstractNPC {
    //Constructor spaghetti because Arios I guess
    constructor() : super(NPCs.ICE_TROLL_MALE_5522, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return IceTrollGFI(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ICE_TROLL_FEMALE_5523, NPCs.ICE_TROLL_MALE_5522, NPCs.ICE_TROLL_RUNT_5521, NPCs.ICE_TROLL_GRUNT_5524)
    }

    override fun tick() {
        if(isActive && !inCombat() && RandomFunction.roll(20)){
            val localGuards = ContentAPI.findLocalNPCs(this, intArrayOf(NPCs.HONOUR_GUARD_5514,NPCs.HONOUR_GUARD_5515,NPCs.HONOUR_GUARD_5516,NPCs.HONOUR_GUARD_5517))
            localGuards.forEach{guard ->
                SystemLogger.logInfo("Looping guards...")
                if(guard.location.withinDistance(location,6)) {
                    attack(guard)
                    return super.tick()
                }
            }
        }
        return super.tick()
    }

}