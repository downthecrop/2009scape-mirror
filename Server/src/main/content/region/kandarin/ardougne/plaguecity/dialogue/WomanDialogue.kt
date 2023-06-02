package content.region.kandarin.ardougne.plaguecity.dialogue

import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction

@Initializable
class WomanDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        when (RandomFunction.random(1,4)) {
            1 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello, how's it going?").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Bah, those mourners... they're meant to be helping us, but I think they're doing more harm here than good. They won't even let me send a letter out to my family.").also { stage++ }
                2 -> options("Have you seen a lady called Elena around here?", "You should stand up to them more.").also { stage++ }
                3 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Have you seen a lady called Elena around here?").also { stage = 4 }
                    2 -> playerl(FacialExpression.FRIENDLY, "You should stand up to them more.").also { stage = 6 }
                }
                4 -> npcl(FacialExpression.FRIENDLY, "Yes, I've seen her. Very helpful person.").also { stage++ }
                5 -> npcl(FacialExpression.FRIENDLY, "Not for the last few days though... I thought maybe she'd gone home.").also { stage = END_DIALOGUE }
                6 -> npcl(FacialExpression.FRIENDLY, "Oh I'm not one to cause a fuss.").also { stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello, how's it going?").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "Life is tough.").also { stage++ }
                2 -> options("Yes, living in a plague city must be hard.", "I'm sorry to hear that.", "I'm looking for a lady called Elena.").also { stage++ }
                3 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Yes, living in a plague city must be hard.").also { stage = 4 }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm sorry to hear that.").also { stage = 6 }
                    3 -> playerl(FacialExpression.FRIENDLY, "I'm looking for a lady called Elena.").also { stage = 7 }
                }
                4 -> npcl(FacialExpression.FRIENDLY, "Plague? Pah, that's no excuse for the treatment we've received. It's obvious pretty quickly if someone has the plague.").also { stage++ }
                5 -> npcl(FacialExpression.FRIENDLY, "I'm thinking about making a break for it. I'm perfectly healthy, not gonna infect anyone.").also { stage = END_DIALOGUE }
                6 -> npcl(FacialExpression.FRIENDLY, "Well, ain't much either you or me can do about it.").also { stage = END_DIALOGUE }
                7 -> npcl(FacialExpression.FRIENDLY, "I've not heard of her. Old Jethick knows a lot of people, maybe he'll know where you can find her.").also { stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello, how's it going?").also { stage++ }
                1 -> npcl(FacialExpression.FRIENDLY, "We don't have good days here anymore. Curse King Tyras.").also { stage++ }
                2 -> options("Oh okay, bad day then.", "Why, what has he done?", "I'm looking for a woman called Elena.").also { stage++ }
                3 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Oh okay, bad day then.").also { stage = END_DIALOGUE }
                    2 -> playerl(FacialExpression.FRIENDLY, "Why, what has he done?").also { stage = 4 }
                    3 -> playerl(FacialExpression.FRIENDLY, "I'm looking for a lady called Elena.").also { stage = 5 }
                }
                4 -> npcl(FacialExpression.FRIENDLY, "His army curses our city with this plague then wanders off again, leaving us to clear up the pieces.").also { stage = END_DIALOGUE }
                5 -> npcl(FacialExpression.FRIENDLY, "Not heard of her.").also { stage = END_DIALOGUE }
            }
            4 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello, how's it going?").also { stage++ }
                1 -> npcl(FacialExpression.HALF_ASKING, "An outsider! Can you get me out of this hell hole?").also { stage++ }
                2 -> playerl(FacialExpression.HALF_GUILTY, "Sorry, that's not what I'm here to do.").also { stage = END_DIALOGUE }
            }
        }
    }
}