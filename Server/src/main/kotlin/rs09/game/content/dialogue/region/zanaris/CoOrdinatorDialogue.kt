package rs09.game.content.dialogue.region.zanaris

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class CoOrdinatorDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY, "Hello, what are you doing?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                when ((1..5).random()) {
                    1 -> npcl(FacialExpression.OLD_DISTRESSED,"Sorry, I don't have time for idle chit-chat, I need to find a Winter Fairy to send to Trollheim!").also { stage = 99 }
                    2 -> npcl(FacialExpression.OLD_DISTRESSED,"Sorry, I don't have time for idle chit-chat, I need to send a fairy to get little Freddie's tooth!").also { stage = 99 }
                    3 -> npcl(FacialExpression.OLD_DISTRESSED,"Sorry, I don't have time for idle chit-chat, I need to send an Autumn Fairy off to Burthorpe!").also { stage = 99 }
                    4 -> npcl(FacialExpression.OLD_DISTRESSED,"Sorry, I don't have time to talk, I need to send a Tooth Fairy to visit Sarah-Jane!").also { stage = 99 }
                    5 -> npcl(FacialExpression.OLD_DISTRESSED,"Sorry, I don't have time to stop, I need to send a weather fairy off to Etceteria!").also { stage = 99 }
                }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CoOrdinatorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CO_ORDINATOR_3302)
    }
}
