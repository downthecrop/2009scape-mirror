package content.region.misc.keldagrim.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class BlandebirDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Hello, I wonder if you could help me on this whole brewing thing...").also { stage++ }
            1 -> npcl(FacialExpression.OLD_DEFAULT, "I might be able to - what do you need to know?").also { stage++ }
            2 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "How do I brew ales?", 3),
                    Topic(FacialExpression.FRIENDLY, "How do I brew cider?", 6),
                    Topic(FacialExpression.FRIENDLY, "What do I do once my ale has matured?", 8),
                    Topic(FacialExpression.FRIENDLY, "Do you have any spare ale yeast?", 9),
                    Topic(FacialExpression.FRIENDLY, "That's all I need to know, thanks.", END_DIALOGUE),
            )
            3 -> npcl(FacialExpression.OLD_DEFAULT, "Well first off you need to fill the vat with water - two bucketfuls should do the trick. Then you'll need to put in two handfuls of barley malt - that's roasted barley by the way.").also { stage++ }
            4 -> npcl(FacialExpression.OLD_DEFAULT, "After that you'll be putting your main ingredient in - this will decide which ale it is you're brewing. There should be some good guides around with recipes in.").also { stage++ }
            5 -> npcl(FacialExpression.OLD_DEFAULT, "Lastly you pour a pot full of ale yeast in, which'll start it off fermenting. Then all you have to do is wait for the good stuff.").also {
                stage = 2
            }
            6 -> npcl(FacialExpression.OLD_DEFAULT, "First you'll need some apples. You'll need to crush them using this cider-press - four apples should make a full bucket of apple-mush. Take four buckets of mush and fill up the vat.").also { stage++ }
            7 -> npcl(FacialExpression.OLD_DEFAULT, "After that you pour a pot full of ale yeast in, which'll start it off fermenting. Then all you have to do is wait for the good stuff.").also {
                stage = 2
            }
            8 -> npcl(FacialExpression.OLD_DEFAULT, "Well, once you've got a full vat of the good stuff, just turn the valve and your barrel will fill up with eight pints of whatever your chosen tipple is. Mind it's an empty barrel, though.").also {
                stage = 2
            }
            9 -> npcl(FacialExpression.OLD_DEFAULT, "Well, as a matter of fact I do, although I wouldn't describe it as spare. This ale yeast I've got is the best money can buy, but if you've got a pot I'll fill it for you for 25GP - very cheap as it happens.").also {
                stage = 2
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BlandebirDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLANDEBIR_2321)
    }
}