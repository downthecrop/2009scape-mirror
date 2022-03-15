package rs09.game.content.dialogue.region.lunarisle

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class SirsalBankerDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (player.inventory.contains(Items.SEAL_OF_PASSAGE_9083, 1) || player.equipment.contains(Items.SEAL_OF_PASSAGE_9083, 1)) {
            npc(FacialExpression.FRIENDLY, "Good day, how may I help you?").also { stage = 0 }
        } else {
            player(FacialExpression.FRIENDLY, "Hi, I...").also { stage = 2 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("I'd like to access my bank account, please.",
                "I'd like to check my PIN settings.",
                "I'd like to collect items.",
                "What is this place?").also { stage++ }

            1 -> when (buttonId) {
                1 -> end().also { player.bank.open() }
                2 -> end().also { player.bankPinManager.openSettings() }
                3 -> end().also { player.exchangeRecords.openCollectionBox() }
                4 -> player(FacialExpression.HALF_ASKING, "What is this place?").also { stage = 5 }
            }

            2 -> npc(FacialExpression.ANNOYED, "What are you doing here, Fremennik?!").also { stage++ }
            3 -> player(FacialExpression.WORRIED, "I have a seal of pass...").also { stage++ }
            4 -> npc(FacialExpression.ANNOYED, "No you do not! Begone!").also { stage = 99}

            5 -> npcl(FacialExpression.FRIENDLY, "This is a branch of the Bank of Gielinor. We have branches in many towns.").also { stage++ }
            6 -> player(FacialExpression.FRIENDLY, "And what do you do?").also { stage++ }
            7 -> npcl(FacialExpression.FRIENDLY, "We will look after your items and money for you. Leave your valuables with us if you want to keep them safe.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SirsalBankerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIRSAL_BANKER_4519)
    }
}
