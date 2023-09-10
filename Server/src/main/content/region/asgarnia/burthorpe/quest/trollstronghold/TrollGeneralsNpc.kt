package content.region.asgarnia.burthorpe.quest.trollstronghold

import content.region.kandarin.quest.grandtree.ForemanDialogue
import content.region.kandarin.quest.grandtree.TheGrandTree
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class TrollGeneralsNpc(id: Int = 0, location: Location? = null) : AbstractNPC(id,location) {

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return TrollGeneralsNpc(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TROLL_GENERAL_1115, NPCs.TROLL_GENERAL_1116, NPCs.TROLL_GENERAL_1117)
    }

    override fun finalizeDeath(killer: Entity?) {
        if(isQuestInProgress(killer as Player, TrollStronghold.questName, 1, 7)) {
            produceGroundItem(killer, Items.PRISON_KEY_3135, 1, this.location)
        }
        super.finalizeDeath(killer)
    }
}