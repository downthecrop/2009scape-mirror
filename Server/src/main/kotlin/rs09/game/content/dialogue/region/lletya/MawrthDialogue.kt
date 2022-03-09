package rs09.game.content.dialogue.region.lletya

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
class MawrthDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.FRIENDLY,"Those children are nothing but trouble - if I did not watch them all the time, Seren knows what trouble they would get in to!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(FacialExpression.FRIENDLY, "They look old enough to look after themselves.").also { stage++ }
            1 -> npcl(FacialExpression.FRIENDLY, "They are only 34 and 38, far too young to be left unsupervised.").also { stage++ }
            2 -> player(FacialExpression.FRIENDLY, "Only!?! How old does that make you?").also { stage++ }
            3 -> npcl(FacialExpression.FRIENDLY, "Has no one told you it is rude to ask a lady her age?").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "Sorry, I wasn't thinking. Anyway... I'd better stop distracting you.").also { stage++ }
            5 -> npc(FacialExpression.FRIENDLY, "Okay, See you some other time.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return MawrthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MAWRTH_2366)
    }
}
