package content.region.asgarnia.rimmington.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.npc.NPC
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Handles Anja's dialogue.
 */
@Initializable
class AnjaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.NEUTRAL, "Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npcl(FacialExpression.ASKING, "Hello @g[sir,madam]. What are you doing in my house?").also { stage++ }
            1 -> options("I'm just wandering around.", "I was hoping you'd give me some free stuff.", "I've come to kill you.").also { stage++ }
            2 -> when (buttonId) {
                1 -> playerl(FacialExpression.NEUTRAL, "I'm just wondering around.").also { stage = 10 }
                2 -> playerl(FacialExpression.NEUTRAL, "I was hoping you'd give me some free stuff.").also { stage = 20 }
                3 -> playerl(FacialExpression.ANGRY_WITH_SMILE, "I've come to kill you.").also { stage = 30 }
            }

            10 -> npcl(FacialExpression.NEUTRAL, "Oh dear, are you lost?").also { stage++ }
            11 -> options("Yes, I'm lost.", "No, I know where I am.").also { stage++ }
            12 -> when (buttonId) {
                1 -> playerl(FacialExpression.NEUTRAL, "Yes, I'm lost.").also { stage = 13 }
                2 -> playerl(FacialExpression.NEUTRAL, "No, I know where I am.").also { stage = 15 }
            }

            13 -> npcl(FacialExpression.FRIENDLY, "Okay, just walk north-east when you leave this house and soon you'll reach the big city of Falador.").also { stage++ }
            14 -> playerl(FacialExpression.FRIENDLY, "Thanks a lot.").also { stage = END_DIALOGUE }
            15 -> npcl(FacialExpression.ASKING, "Oh? Well, would you mind wandering somewhere else?").also { stage++ }
            16 -> npcl(FacialExpression.NEUTRAL, "This is my house.").also { stage++ }
            17 -> playerl(FacialExpression.NEUTRAL, "Meh!").also { stage = END_DIALOGUE }

            20 -> {
                val dialogues = arrayOf("Do you REALLY need it?", "I don't have much on me...", "I don't know...")
                npcl(FacialExpression.NEUTRAL, dialogues[RandomFunction.random(0, 3)])
                stage++
            }

            21 -> playerl(FacialExpression.ASKING, "I promise I'll stop bothering you!").also { stage++ }
            22 -> playerl(FacialExpression.ASKING, "Pleeease!").also { stage++ }
            23 -> playerl(FacialExpression.ASKING, "Pwetty pleathe wiv thugar on top!").also { stage++ }
            24 -> {
                npcl(FacialExpression.NEUTRAL, "Oh, alright. Here you go.")
                addItemOrDrop(player, Items.COINS_995, RandomFunction.random(1, 3))
                stage = END_DIALOGUE
            }

            30 -> {
                stage = END_DIALOGUE
                close()

                val hengel = findLocalNPC(npc, NPCs.HENGEL_2683)
                if(hengel != null)
                    sendChat(hengel, "Aaaaarrgh!")

                sendChat(npc, "Eeeek!")
            }
        }

        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AnjaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ANJA_2684)
    }
}
