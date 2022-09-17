package rs09.game.content.quest.members.tree

import api.addItemOrDrop
import api.questStage
import api.sendDialogue
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

@Initializable
class KhazardWarlordNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id,location), InteractionListener {

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return KhazardWarlordNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KHAZARD_WARLORD_477)
    }

    override fun defineListeners() {
    }

    override fun finalizeDeath(killer: Entity?) {
        if(questStage(killer as Player,TreeGnomeVillage.questName) == 40) {
            sendDialogue(killer,"As the warlord falls to the ground, a ghostly vapour floats upwards from his battle-worn armour. You search his satchel and find the orbs of protection.")
            addItemOrDrop(killer, Items.ORBS_OF_PROTECTION_588)
        }
        super.finalizeDeath(killer)
    }
}