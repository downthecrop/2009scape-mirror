package content.region.misthalin.lumbridge.quest.lostcity

import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.api.getQuestStage
import core.api.sendDialogue
import core.api.setQuestStage
import content.data.Quests

/**
 * TreeSpiritNPC class to handle the tree spirit that spawns out of the dramen tree
 */
@Initializable
class TreeSpiritNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {
    var target: Player? = null

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return TreeSpiritNPC(id, location)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TREE_SPIRIT_655)
    }
    init {
        isWalks = true
        isRespawn = false
    }

    override fun handleTickActions() {
        if(target==null) {
            clear()
            return
        }
        super.handleTickActions()
        if (!inCombat()) {
            attack(target)
        }
        if (!target!!.isActive() || target!!.getLocation().getDistance(getLocation()) > 15) {
            clear()
            target!!.removeAttribute("treeSpawned")
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            if (getQuestStage(killer, Quests.LOST_CITY) == 20) {
                setQuestStage(killer, Quests.LOST_CITY,21)
                sendDialogue(killer, "With the Tree Spirit defeated you can now chop the tree.")
            }
        }
    }
}
