package rs09.game.interaction.npc

import api.isEquipped
import api.sendNPCDialogue
import api.setInterfaceText
import core.game.content.dialogue.FacialExpression
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener
import rs09.tools.END_DIALOGUE

class NPCDepositListener : InteractionListener() {

    override fun defineListeners() {
        on(NPCs.PEER_THE_SEER_1288, NPC,"deposit") { player, _ ->
            if (isEquipped(player, Items.FREMENNIK_SEA_BOOTS_1_14571) ||
                isEquipped(player, Items.FREMENNIK_SEA_BOOTS_2_14572) ||
                isEquipped(player, Items.FREMENNIK_SEA_BOOTS_3_14573)) {
                player.bank.openDepositBox()
                setInterfaceText(player, "Peer the Seer's Deposits", 11, 12)
            } else {
                sendNPCDialogue(player, NPCs.PEER_THE_SEER_1288,
                    "Do not pester me, outerlander! I will only deposit items into the banks of those who have earned Fremennik sea boots!",
                    FacialExpression.ANNOYED).also { END_DIALOGUE }
            }
            return@on true
        }
    }
}