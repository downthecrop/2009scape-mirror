package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.carnilleanArc
import core.api.isQuestInProgress
import core.api.produceGroundItem
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.*

/**
 * Alomone drops the Carnillean Amour during the Hazeel Cult quest.
 */

@Initializable
class AlomoneNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC = AlomoneNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALOMONE_891)

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            if (isQuestInProgress(killer, Quests.HAZEEL_CULT, 3, 4) && carnilleanArc(killer)) {
                produceGroundItem(killer, Items.CARNILLEAN_ARMOUR_2405, 1, this.location)
            }
            super.finalizeDeath(killer)
        }
    }
}
