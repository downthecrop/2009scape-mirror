package content.region.morytania.canifis.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
open class TaxidermistDialogue(player: Player? = null) : DialoguePlugin(player) {
    companion object{
        const val YES = 10
        const val NO = 20
        const val WHAT = 30
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.HAPPY, "Oh, hello. Have you got something you want preserving?").also { stage++ }
        return true
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE + 1 -> showTopics(
                Topic("Yes please.", YES),
                Topic("Not right now.", NO),
                Topic("What?", WHAT)
            )

            YES -> npcl(FacialExpression.HAPPY, "Give it to me to look at then.").also { stage = END_DIALOGUE }
            NO -> npcl(FacialExpression.ANNOYED, "Well, you go kill things so I can stuff them, eh?").also { stage = END_DIALOGUE }
            WHAT -> npcl(FacialExpression.NEUTRAL, " If you bring me a monster head or a very big fish, I can preserve it for you so you can mount it in your house.").also { stage++ }
            WHAT + 1 -> npcl(FacialExpression.HAPPY, "I hear there are all sorts of exotic creatures in the Slayer Tower -- I'd like a chance to stuff one of them!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TAXIDERMIST_4246)
    }
}
