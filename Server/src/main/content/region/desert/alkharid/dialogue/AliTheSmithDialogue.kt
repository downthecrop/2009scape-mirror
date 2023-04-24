package content.region.desert.alkharid.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class AliTheSmithDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun newInstance(player: Player?): DialoguePlugin {
        return AliTheSmithDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(FacialExpression.FRIENDLY,"Hello there!")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(FacialExpression.FRIENDLY,"You seem rather cheerful. Is there anything you're after?").also { stage++ }
            1 -> options("What can you tell me about Al Kharid?", "So, what do you do here?", "I hear you work for Ali Morrisane...", "I hear you've been threatening the other shopkeepers.").also { stage++ }
            2 -> when(buttonId){
                1 -> playerl(FacialExpression.ASKING, "What can you tell me about Al Kharid?").also { stage = 10 }
                2 -> playerl(FacialExpression.ASKING, "So, what do you do here?").also { stage = 20 }
                3 -> playerl(FacialExpression.ASKING, "I hear you work for Ali Morrisane...").also { stage = 30 }
                4 -> playerl(FacialExpression.HALF_ASKING, "I hear you've been threatening the other shopkeepers.").also { stage = 40 }
            }

            10 ->npcl(FacialExpression.FRIENDLY,"Well, it's hot and full of sand.").also { stage++ }
            11 ->playerl(FacialExpression.ASKING,"Anything else?").also { stage++ }
            12 ->npcl(FacialExpression.FRIENDLY,"Don't ask me, I'm not from around here.").also { stage++ }
            13 ->playerl(FacialExpression.HALF_ASKING,"So where are you from?").also { stage++ }
            14 ->npcl(FacialExpression.HALF_GUILTY,"A town to the south of the Shantay Pass, called Pollnivneach.").also { stage = 1 }

            20 ->npcl(FacialExpression.SAD,"Not very much, at the moment. I came from further south to set up a smithy here...").also { stage++ }
            21 ->npcl(FacialExpression.SAD,"...but we still haven't set up the shops, so we have no customers yet.").also { stage = 1 }

            30 -> npcl(FacialExpression.FRIENDLY,"Yes, he's the one who persuaded us to come here.").also { stage++ }
            31 -> npcl(FacialExpression.HALF_GUILTY,"He said we'd have lots of customers once we set up our shops, and all he asks for is a cut of the profits.").also { stage++ }
            32 -> playerl(FacialExpression.ASKING,"Maybe I should talk to him...").also { stage++ }
            33 -> npcl(FacialExpression.FRIENDLY,"Of course. He's always happy to talk to possible business partners.").also { stage = 1 }

            40 -> npcl(FacialExpression.SUSPICIOUS,"What? Who's spreading that rumour?").also { stage++ }
            41 -> playerl(FacialExpression.THINKING,"Well, one of the shopkeepers told me a man with a large hammer came to threaten them.").also { stage++ }
            42 -> npcl(FacialExpression.SUSPICIOUS,"Don't pay any attention to those people.").also { stage++ }
            43 -> npcl(FacialExpression.LAUGH,"They're just worried that they'll lose money when we open our shops.").also { stage = 99 }
            99 -> {
                end()
            }

        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_SMITH_2820)
    }
}