package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class JethickDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.questRepository.getStage("Plague City") in 0..11) {
            npcl(FacialExpression.FRIENDLY, "Hello I don't recognise you. We don't get many newcomers around here.").also { stage++ }
        } else if (player.questRepository.getStage("Plague City") >= 12) {
            npcl(FacialExpression.FRIENDLY,"Hello. We don't get many newcomers around here.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, PlagueCity.PlagueCityQuest)) {

            in 0..1 -> when (stage) {
                1 -> npcl(FacialExpression.FRIENDLY, "Well King Tyras has wandered off into the west kingdom. He doesn't care about the mess he's left here. The city warder Bravek is in charge at the moment... He's not much better.").also { stage = END_DIALOGUE }
            }

            in 7..9 -> when (stage) {
                1 -> options("Hi, I'm looking for a woman from East Ardougne called Elena.", "So who's in charge here?").also { stage++ }
                2 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Hi, I'm looking for a woman from East Ardougne called Elena.").also { stage = 3 }
                    2 -> playerl(FacialExpression.FRIENDLY, "So who's in charge here?").also { stage = END_DIALOGUE }
                }
                3 -> npcl(FacialExpression.FRIENDLY, "East Ardougnian women are easier to find in East Ardougne. Not many would come to West Ardougne to find one. Although the name is familiar, what does she look like?").also { stage++ }
                4 -> playerl(FacialExpression.NEUTRAL, "Um... brown hair... in her twenties...").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "Hmm, that doesn't narrow it down a huge amount... I'll need to know more than that, or see a picture?").also { stage++ }
                6 -> if (inInventory(player!!, Items.PICTURE_1510)) {
                    sendItemDialogue(player!!, Items.PICTURE_1510, "You show Jethick the picture.").also { stage++ }
                } else {
                    end()
                    stage = END_DIALOGUE
                }
                7 -> npcl(FacialExpression.FRIENDLY, "She came over here to help to aid plague victims. I think she is staying over with the Rehnison family. They live in the small timbered building at the far north side of town.").also { stage++ }
                8 -> npcl(FacialExpression.FRIENDLY, "I've not seen her around here for a while, mind. I don't suppose you could run me a little errand while you're over there? I borrowed this book from them, could you return it?").also { stage++ }
                9 -> options("Yes, I'll return it for you.", "No, I don't have time for that.").also { stage++ }
                10 -> when (buttonID) {
                    1 -> playerl(FacialExpression.NEUTRAL, "Yes, I'll return it for you.").also { stage = 11 }
                    2 -> playerl(FacialExpression.NEUTRAL, "No, I don't have time for that.").also { stage = END_DIALOGUE }
                }
                11 -> if(freeSlots(player) == 0) {
                    end()
                    sendItemDialogue(player!!, Items.BOOK_1509, "Jethick shows you the book, but you don't have room to take it.")
                    stage = END_DIALOGUE
                } else {
                    end()
                    sendItemDialogue(player!!, Items.BOOK_1509, "Jethick gives you a book.")
                    addItem(player!!, Items.BOOK_1509)
                    stage = END_DIALOGUE
                }
            }

            in 10..11 -> when (stage) {
                1 -> playerl(FacialExpression.FRIENDLY, "I'm looking for a woman from East Ardougne called Elena.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY,"Ah yes. She came over here to help the plague victims. I think she is staying over with the Rehnison family.").also { stage++ }
                3 -> npcl(FacialExpression.FRIENDLY, "They live in the small timbered building at the far north side of town. I've not seen her around here in a while, mind.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.JETHICK_725)
}