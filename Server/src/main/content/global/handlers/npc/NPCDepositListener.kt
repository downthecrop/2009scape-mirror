package content.global.handlers.npc

import core.api.anyInEquipment
import core.api.openDepositBox
import core.api.sendNPCDialogue
import core.api.setInterfaceText
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.END_DIALOGUE

class NPCDepositListener : InteractionListener {

    override fun defineListeners() {
        on(NPCs.PEER_THE_SEER_1288, IntType.NPC, "deposit") { player, _ ->
            if (anyInEquipment(player, Items.FREMENNIK_SEA_BOOTS_1_14571, Items.FREMENNIK_SEA_BOOTS_2_14572, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                openDepositBox(player)
                setInterfaceText(player, "Peer the Seer's Deposits", 11, 12)
            } else {
                sendNPCDialogue(player, NPCs.PEER_THE_SEER_1288,
                    "Do not pester me, outerlander! I will only deposit items into the banks of those who have earned Fremennik sea boots!",
                    core.game.dialogue.FacialExpression.ANNOYED).also { END_DIALOGUE }
            }
            return@on true
        }
    }
}