package content.region.kandarin.quest.grandtree

import content.region.kandarin.quest.grandtree.TheGrandTree.Companion.questName
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener

@Initializable
class BlackDemonNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id,location), InteractionListener {

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return BlackDemonNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLACK_DEMON_677)
    }

    override fun defineListeners() {
    }

    override fun finalizeDeath(killer: Entity?) {
        // In the event that this npcID is used somewhere else...
        if(killer!!.asPlayer().location.regionId == 9882) {
            setQuestStage(killer!!.asPlayer(), questName, 98)
            this.isRespawn = false
        }
        super.finalizeDeath(killer)
    }
}
