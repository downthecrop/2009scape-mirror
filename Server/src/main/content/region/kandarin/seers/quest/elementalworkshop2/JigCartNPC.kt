package content.region.kandarin.seers.quest.elementalworkshop2

import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.jigCartStates
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.path.ClipMaskSupplier

/**
 * Jig cart in Elemental Workshop II
 */

// the only reason I have this here is to set a custom clipping mask to allow the cart to walk over the track scenery.
// right now the walking part is broken, so the cart just teleports.

class JigCartNPCBehavior : NPCBehavior(*jigCartStates) {
    override fun getClippingSupplier (self: NPC) : ClipMaskSupplier {
        return CartClipper
    }
}

// this clip mask lets the jig cart walk through scenery
object CartClipper : ClipMaskSupplier {
    override fun getClippingFlag (z: Int, x: Int, y: Int) : Int {
        return 0
    }
}