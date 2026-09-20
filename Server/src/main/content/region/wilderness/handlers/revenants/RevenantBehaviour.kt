package content.region.wilderness.handlers.revenants

import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.RegionManager
import core.game.world.map.path.ClipMaskSupplier

/**
 * Absolutely prevents revenants from entering safe zones.
 * @author Bishop
 */

class RevenantBehaviour : NPCBehavior(*RevenantType.getAllIds().toIntArray()) {

    private object SafeZoneClipMaskSupplier : ClipMaskSupplier {
        override fun getClippingFlag(z: Int, x: Int, y: Int): Int {
            return if (RevenantNPC.SAFE_ZONES.any { it.insideBorder(x, y) }) {
                -1
            } else {
                RegionManager.getClippingFlag(z, x, y)
            }
        }
    }

    override fun getClippingSupplier(self: NPC): ClipMaskSupplier {
        return SafeZoneClipMaskSupplier
    }

}