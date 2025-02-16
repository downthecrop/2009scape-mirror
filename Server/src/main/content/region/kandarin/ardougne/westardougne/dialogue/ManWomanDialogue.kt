package content.region.kandarin.ardougne.westardougne.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs09.consts.NPCs

@Initializable
class ManWomanDialogue(player: Player? = null) : DialoguePlugin(player) {

    companion object {
        const val DIAG1 = 10
        const val DIAG2 = 20
        const val DIAG3 = 30
        const val DIAG4 = 40
        const val DIAG5 = 50
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val path = RandomFunction.random(1, 6)
        val msg = when(path){
            1,5  -> "Good day."
            2 -> "Hi there."
            3, 4 -> "Hello, how's it going?"
            else -> "Hello"
        }

        playerl(FacialExpression.FRIENDLY, msg).also { stage = path * 10 }

        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (stage){
            DIAG1 -> npcl(FacialExpression.HALF_ASKING, "An outsider! Can you get me out of this hell hole?").also { stage++ }
            DIAG1 + 1 -> playerl(FacialExpression.SAD, "Sorry, that's not what I'm here to do.").also { stage = END_DIALOGUE }

            DIAG2 -> npcl(FacialExpression.ANNOYED, "Go away. People from the outside shut us in like animals. I have nothing to say to you.").also { stage = END_DIALOGUE }

            DIAG3 -> npcl(FacialExpression.SAD, "Life is tough.").also { stage++ }
            DIAG3 + 1 -> showTopics(
                Topic("Yes, living in a plague city must be hard.", DIAG3 + 2),
                Topic("I'm sorry to hear that.", DIAG3 + 3),
                Topic("I'm looking for a lady called Elena.", DIAG3 + 4)
            )
            DIAG3 + 2 -> npcl(FacialExpression.HALF_GUILTY, "Plague? Pah, that's no excuse for the treatment we've received. It's obvious pretty quickly if someone has the plague. I'm thinking about making a break for it. I'm perfectly healthy, not gonna infect anyone.").also { stage = END_DIALOGUE }
            DIAG3 + 3 -> npcl(FacialExpression.SAD, "Well, aint much either you or me can do about it.").also { stage = END_DIALOGUE }
            DIAG3 + 4 -> npcl(FacialExpression.NEUTRAL, "I've not heard of her. Old Jethick knows a lot of people, maybe he'll know where you can find her.").also { stage = END_DIALOGUE }

            DIAG4 -> npcl(FacialExpression.ANNOYED, "Bah, those mourners... they're meant to be helping us, but I think they're doing more harm here than good. They won't even let me send a letter out to my family.").also{ stage++}
            DIAG4 + 1 -> showTopics(
                Topic("Have you seen a lady called Elena around here?", DIAG4 + 2),
                Topic("You should stand up to them more.", DIAG4 + 3)
            )
            DIAG4 + 2 -> npcl(FacialExpression.SAD, "Yes, I've seen her. Very helpful person. Not for the last few days though... I thought maybe she'd gone home.").also { stage = END_DIALOGUE }
            DIAG4 + 3 -> npcl(FacialExpression.HALF_GUILTY, " Oh I'm not one to cause a fuss.").also { stage = END_DIALOGUE }

            DIAG5 -> npcl(FacialExpression.ANGRY, "We don't have good days here anymore. Curse King Tyras.").also { stage++ }
            DIAG5 + 1 -> showTopics(
                Topic("Oh ok, bad day then.", END_DIALOGUE),
                Topic("Why, what has he done?", DIAG5 + 2),
                Topic("I'm looking for a woman called Elena.", DIAG5 + 3)
            )
            DIAG5 + 2 -> npcl(FacialExpression.ANGRY, "His army curses our city with this plague then wanders off again, leaving us to clear up the pieces.").also { stage = END_DIALOGUE }
            DIAG5 + 3 -> npcl(FacialExpression.NEUTRAL, "Not heard of her.").also { stage = END_DIALOGUE }
        }
    return true
    }

    override fun getIds(): IntArray {

        return intArrayOf(NPCs.MAN_728, NPCs.MAN_729, NPCs.MAN_351,
            NPCs.WOMAN_352, NPCs.WOMAN_353, NPCs.WOMAN_354, NPCs.WOMAN_360, NPCs.WOMAN_361, NPCs.WOMAN_362, NPCs.WOMAN_363)
    }
}