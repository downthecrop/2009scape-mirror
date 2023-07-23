package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.getQuestStage
import core.api.sendNPCDialogue
import core.api.setQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class ClerkDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.NEUTRAL, "Hello, welcome to the Civic Office of West Ardougne. How can I help you?").also { stage = 0 }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (getQuestStage(player!!, PlagueCity.PlagueCityQuest)) {

            11 -> when (stage) {
                0 -> options("Who is through that door?", "I'm just looking thanks.").also { stage++ }
                1 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Who is through that door?").also { stage = 2 }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm just looking thanks.").also { stage = END_DIALOGUE }
                }
                2 -> npcl(FacialExpression.FRIENDLY, "The city warder Bravek is in there.").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY,"Can I go in?").also { stage++ }
                4 -> npcl(FacialExpression.FRIENDLY, "He has asked not to be disturbed.").also { stage = END_DIALOGUE }
            }

            12 -> when (stage) {
                0 -> options("I need permission to enter a plague house.", "Who is through that door?", "I'm just looking thanks.").also { stage++ }
                1 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "I need permission to enter a plague house.").also { stage = 2 }
                    2 -> playerl(FacialExpression.FRIENDLY, "Who is through that door?").also { stage = 12 }
                    3 -> playerl(FacialExpression.FRIENDLY, "I'm just looking thanks.").also { stage = END_DIALOGUE }
                }
                2 -> npcl(FacialExpression.FRIENDLY, "Rather you than me! The mourners usually deal with that stuff, you should speak to them. Their headquarters are right near the city gate.").also { stage++ }
                3 -> options("I'll try asking them then.", "Surely you don't let them run everything for you?").also { stage++ }
                4 -> when (buttonID) {
                    1 -> playerl(FacialExpression.HALF_GUILTY, "I'll try asking them then.").also { stage = END_DIALOGUE }
                    2 -> playerl(FacialExpression.HALF_GUILTY, "Surely you don't let them run everything for you?").also { stage = 5 }
                }
                5 -> npcl(FacialExpression.FRIENDLY, "Well, they do know what they're doing here. If they did start doing something badly Bravek, the city warder, would have the power to override them. I can't see that happening though.").also { stage++ }
                6 -> options("I'll try asking them then.", "Can I speak to Bravek anyway?", "This is urgent though! Someone's been kidnapped!").also { stage++ }
                7 -> when (buttonID) {
                    1 -> playerl(FacialExpression.HALF_GUILTY, "I'll try asking them then.").also { stage = END_DIALOGUE }
                    2 -> playerl(FacialExpression.HALF_GUILTY, "Can I speak to Bravek anyway?").also { stage = 8 }
                    3 -> playerl(FacialExpression.HALF_GUILTY, "This is urgent though! Someone's been kidnapped!").also { stage = 11 }
                }
                8 -> npcl(FacialExpression.FRIENDLY, "He has asked not to be disturbed.").also { stage++ }
                9 -> options("This is urgent though! Someone's been kidnapped!", "Okay, I'll leave him alone.", "Do you know when he will be available?").also { stage++ }
                10 -> when (buttonID) {
                    1 -> playerl(FacialExpression.HALF_GUILTY, "This is urgent though! Someone's been kidnapped!").also { stage = 11 }
                    2 -> playerl(FacialExpression.HALF_GUILTY, "Okay, I'll leave him alone.").also { stage = END_DIALOGUE }
                    3 -> playerl(FacialExpression.HALF_GUILTY, "Do you know when he will be available?").also { stage = 14 }
                }
                11 -> npcl(FacialExpression.HALF_GUILTY, "I'll see what I can do I suppose.").also { stage++ }
                12 -> npcl(FacialExpression.HALF_GUILTY, "Mr Bravek, there's a man here who really needs to speak to you.").also { stage++ }
                13 -> {
                    end()
                    setQuestStage(player!!, "Plague City", 13)
                    sendNPCDialogue(player!!, NPCs.BRAVEK_711, "I suppose they can come in then. If they keep it short.").also { stage++ }
                }
                14 -> npcl(FacialExpression.HALF_GUILTY, "Oh I don't know, an hour or so maybe.").also { stage = END_DIALOGUE }
            }

            in 13..15 -> when (stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "Bravek will see you now but keep it short!").also { stage++ }
                1 -> playerl(FacialExpression.FRIENDLY, "Thanks, I won't take much of his time.").also { stage = END_DIALOGUE }
            }

            in 16..100 -> when (stage) {
                0 -> options("Who is through that door?", "I'm just looking thanks.").also { stage++ }
                1 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Who is through that door?").also { stage = 2 }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm just looking thanks.").also { stage = END_DIALOGUE }
                }
                2 -> npcl(FacialExpression.FRIENDLY, "The city warder Bravek is in there.").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY,"Can I go in?").also { stage++ }
                4 -> npcl(FacialExpression.FRIENDLY, "I suppose so.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CLERK_713)
}