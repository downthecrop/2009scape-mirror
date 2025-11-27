package content.region.kandarin.quest.observatoryquest

import content.data.Quests
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class GoblinGuardNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return GoblinGuardNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SLEEPING_GUARD_6122, NPCs.GOBLIN_GUARD_489)
    }

    override fun finalizeDeath(entity: Entity) {
        if (entity is Player) {
            val player = entity.asPlayer()
            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) >= 4) {
                setAttribute(player, ObservatoryQuest.attributeKilledGuard, true)
            }
            super.finalizeDeath(player)
        }
    }
}