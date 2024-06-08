package content.region.asgarnia.rimmington.dialogue

import core.api.findLocalNPC
import core.api.sendChat
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.npc.NPC
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Handles the Hengel's dialogue.
 */
@Initializable
class HengelDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.NEUTRAL, "Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.ASKING, "What are you doing here?").also { stage++ }
            1 -> options("I'm just wandering around.", "I was hoping you'd give me some free stuff.", "I've come to kill you.").also { stage++ }
            2 -> when (buttonId) {
                1 -> playerl(FacialExpression.NEUTRAL, "I'm just wondering around.").also { stage = 10 }
                2 -> playerl(FacialExpression.NEUTRAL, "I was hoping you'd give me some free stuff.").also { stage = 20 }
                3 -> playerl(FacialExpression.ANGRY_WITH_SMILE, "I've come to kill you.").also { stage = 30 }
            }

            10 -> npcl(FacialExpression.ASKING, "You do realise you're wandering around in my house?").also { stage++ }
            11 -> playerl(FacialExpression.NEUTRAL, "Yep.").also { stage++ }
            12 -> npcl(FacialExpression.ANNOYED, "Well please get out!").also { stage++ }
            13 -> playerl(FacialExpression.NEUTRAL, "Sheesh, keep your wig on!").also { stage = END_DIALOGUE }

            20 -> npcl(FacialExpression.ANNOYED, "No, I jolly well wouldn't!").also { stage++ }
            21 -> npcl(FacialExpression.ANNOYED, "Get out of my house!").also { stage++ }
            22 -> playerl(FacialExpression.NEUTRAL, "Meanie!").also { stage = END_DIALOGUE }

            30 -> {
                stage = END_DIALOGUE
                close()

                val anja = findLocalNPC(npc, NPCs.ANJA_2684)
                if(anja != null)
                    sendChat(anja, "Eeeek!")

                sendChat(npc, "Aaaaarrgh!")
            }
        }

        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HengelDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HENGEL_2683)
    }
}
