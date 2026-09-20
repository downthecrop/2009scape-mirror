package content.region.misthalin.wiztower.handlers.rcguild

import core.api.*
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs09.consts.NPCs

/**
 * The wizards of the Runecrafting Guild
 */

// todo the graphics and visuals on this need more polish. Right now it just doesn't look quite right.
//@Initializable
class RcGuildWizardNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private val RUNES_ANIM = 10115
    private val RUNES_GFX = 1774

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return RcGuildWizardNPC(id, location)
    }

    // occasionally plays the little thinking and runes anim/gfx the wizards do
    override fun tick() {
        if (!locks.isMovementLocked && RandomFunction.roll(20)) {
            walkingQueue.reset()
            lock(this, 25)
            runeTime()
        }
        super.tick()
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WIZARD_8034, NPCs.WIZARD_8035, NPCs.WIZARD_8036, NPCs.WIZARD_8037, NPCs.WIZARD_8038)
    }

    private fun runeTime() {
        val anim = Animation(RUNES_ANIM)
        val gfx = Graphics(RUNES_GFX, 0, 25) // delay in cycles, 30 cycles = 1 tick
        visualize(this, anim, gfx)
    }
}

