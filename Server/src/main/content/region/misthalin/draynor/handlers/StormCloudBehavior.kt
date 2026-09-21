package content.region.misthalin.draynor.handlers

import core.api.getAttribute
import core.api.setAttribute
import core.cache.def.impl.NPCDefinition
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.NPCs

/**
 * Makes the lightning bolt NPCs in the Killerwatt Plane less predictable.
 * @author Bishop
 */

class StormCloudBehavior : NPCBehavior(NPCs.STORM_CLOUD_3204) {

    companion object {
        private const val ATTRIBUTE_LIFESPAN = "lifespan"
        private val timeBetweenStrikes = Animation(NPCDefinition.forId(NPCs.STORM_CLOUD_3204).renderAnimationId).duration
    }

    override fun tick(self: NPC): Boolean {
        var lifespan = getAttribute(self, ATTRIBUTE_LIFESPAN, 0)
        if (RandomFunction.roll(10) && lifespan % timeBetweenStrikes == 0) {
            DeathTask.startDeath(self, null)
            return super.tick(self)
        }
        lifespan++
        if (lifespan % timeBetweenStrikes == 0) {
            lifespan = 0
        }
        setAttribute(self, ATTRIBUTE_LIFESPAN, lifespan)
        return super.tick(self)
    }

}