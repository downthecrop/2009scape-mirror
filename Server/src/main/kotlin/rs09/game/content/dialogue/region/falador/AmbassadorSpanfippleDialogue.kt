package rs09.game.content.dialogue.region.falador

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
class AmbassadorSpanfippleDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_DEFAULT,"It's all very white round here, isn't it?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                player(FacialExpression.THINKING, "Well, it is the White Knights' Castle.").also { stage++ }
            }

            1 -> {
                npcl(FacialExpression.OLD_DEFAULT, "I think it would all look better in pink. At least then I wouldn't be squinting all the time.").also { stage++ }
            }

            2 -> {
                playerl(FacialExpression.FRIENDLY, "Yes, but then they'd have to become the Pink Knights. I think they'd have problems recruiting then.").also { stage++ }
            }

            3 -> {
                npc(FacialExpression.OLD_DEFAULT, "You're probably right. Maybe brown, then.").also { stage++ }
            }

            4 -> {
                player(FacialExpression.HALF_THINKING, "I think that may be worse...").also { stage++ }
            }

            5 -> {
                npc(FacialExpression.OLD_ANGRY1, "Bah, humans have no sense of style...").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AmbassadorSpanfippleDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AMBASSADOR_SPANFIPPLE_4581)
    }
}