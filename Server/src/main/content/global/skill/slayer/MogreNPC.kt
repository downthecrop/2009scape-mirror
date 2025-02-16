package content.global.skill.slayer

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import org.rs09.consts.NPCs

/**
 * Represents a mogre npc.
 * @author 'Vexia
 * @author gregf36665
 * @version 2.0
 */
class MogreNPC : AbstractNPC(NPCs.MOGRE_114, null) {

    override fun tick() {
        super.tick()
        val victim = properties.combatPulse.getVictim()
        if (victim != null) {
            if (victim.location.getDistance(getLocation()) > 15) {
                clear()
            }
        }
    }

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return MogreNPC()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOGRE_114)
    }
}
