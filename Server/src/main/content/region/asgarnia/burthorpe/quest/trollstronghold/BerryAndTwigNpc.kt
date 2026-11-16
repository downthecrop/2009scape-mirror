package content.region.asgarnia.burthorpe.quest.trollstronghold

import core.api.transformNpcWithHpCarryover
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs
import kotlin.collections.indexOfFirst

@Initializable
class BerryAndTwigNpc(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    val sleepingIds = intArrayOf(NPCs.BERRY_1127, NPCs.TWIG_1126)
    val awakeIds = intArrayOf(NPCs.BERRY_1129, NPCs.TWIG_1128)
    val RETURN_TO_SPAWN = "return-to-spawn"

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return BerryAndTwigNpc(id, location)
    }

    override fun getIds(): IntArray {
        return sleepingIds + awakeIds
    }

    fun changeState(isAsleep: Boolean, restoreTicks: Int) {
        val current = if (isAsleep) sleepingIds else awakeIds
        val target = if (isAsleep) awakeIds else sleepingIds
        val which = current.indexOfFirst { it == id }
        if (which != -1) {
            transformNpcWithHpCarryover(this, target[which], restoreTicks)
        }
    }

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        val attackable = super.isAttackable(entity, style, message)
        if (attackable) {
            changeState(false, -1)
        }
        return attackable
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (getAttribute(RETURN_TO_SPAWN, false)) {
            changeState(true, -1)
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        super.finalizeDeath(killer)
        reTransform()
    }
}