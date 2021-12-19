package rs09.game.node.entity.npc.other

import api.*
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

@Initializable
class HonourGuardGFI : AbstractNPC {
    //Constructor spaghetti because Arios I guess
    constructor() : super(NPCs.HONOUR_GUARD_5514, null, true) {}
    private constructor(id: Int, location: Location) : super(id, location) {}

    override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC {
        return HonourGuardGFI(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HONOUR_GUARD_5514,NPCs.HONOUR_GUARD_5515,NPCs.HONOUR_GUARD_5516,NPCs.HONOUR_GUARD_5517)
    }

    override fun tick() {
        if(isActive && !inCombat() && RandomFunction.roll(10)){
            val localTrolls = findLocalNPCs(this, intArrayOf(NPCs.ICE_TROLL_FEMALE_5523, NPCs.ICE_TROLL_MALE_5522, NPCs.ICE_TROLL_RUNT_5521, NPCs.ICE_TROLL_GRUNT_5524))
            localTrolls.forEach{troll ->
                if(troll.location.withinDistance(location,6)) {
                    attack(troll)
                    return super.tick()
                }
            }
        }
        if(!isActive){
            properties.combatPulse.stop()
        }
        return super.tick()
    }

}