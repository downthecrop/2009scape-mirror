package content.region.tirranwn.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Not including quest-related dialogue
 */

@Initializable
class ArvelDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    fun gender (male : String = "hero", female : String = "heroine") = if (player.isMale) male else female

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Good day traveller. You are far from home, what brings you here?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "I am a wandering " + gender() + ". I come here in search of adventure.").also { stage++ }
            1 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Sounds ghastly, I just want to live in peace.").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Unfortunately without people like me, peace doesn't last for long.").also { stage++ }
            3 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "True, but then again most adventurers cause as much trouble as they put right.").also { stage++ }
            4 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "You've got a point there... Hmm...").also { stage++ }
            5 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Well, good day traveller. And always do the right thing.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return ArvelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARVEL_2365)
    }
}
