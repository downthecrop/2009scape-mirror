package content.region.kandarin.witchhaven.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Does not take quest into consideration
 */

@Initializable
class ColONiallDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Hello. What can I do for you?").also { stage++ }
            1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Oh, I'm just wondering what you're doing.").also { stage++ }
            2 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "A spot of fishing.").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "That doesn't look much like a fishing rod.").also { stage++ }
            4 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "That my friend, depends on what you're fishing for.").also { stage++ }
            5 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "And what would that be?").also { stage++ }
            6 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "A little of this, a little of that; the usual things.").also { stage++ }
            7 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Have you caught much?").also { stage++ }
            8 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "The odd bite here and there. Hmm.").also { stage++ }
            9 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "What?").also { stage++ }
            10 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "You look like a capable lad. Tell you what, when you've got a bit more experience under your belt, get yourself over to Falador.").also { stage++ }
            11 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "What's in Falador?").also { stage++ }
            12 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "An old friend of mine. You'll find him sitting on a bench in Falador Park. See what he can do for you.").also { stage++ }
            13 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "What's his name? Who should I say sent me?").also { stage++ }
            14 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "None of that matters if you can find him and if you're ready my name isn't necessary.").also { stage++ }
            15 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Oh right. I'll get going then. Goodbye.").also { stage++ }
            16 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Goodbye and good luck.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return ColONiallDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COL_ONIALL_4872)
    }
}
