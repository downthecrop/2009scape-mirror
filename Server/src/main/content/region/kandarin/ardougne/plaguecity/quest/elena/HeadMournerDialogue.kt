package content.region.kandarin.ardougne.plaguecity.quest.elena

import core.api.getQuestStage
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class HeadMournerDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.HEAD_MOURNER_716)
        when (getQuestStage(player!!, PlagueCity.PlagueCityQuest)) {

            in 8..10 -> when (stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "Hmmm, how did you get over here? You're not one of this rabble. Ah well, you'll have to stay. Can't risk you going back now.").also { stage++ }
                1 -> options("So what's a mourner?", "I haven't got the plague though...", "I'm looking for a woman named Elena.").also { stage++ }
                2 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "So what's a mourner?").also { stage = 3 }
                    2 -> playerl(FacialExpression.FRIENDLY, "I haven't got the plague though...").also { stage = 6 }
                    3 -> playerl(FacialExpression.FRIENDLY, "I'm looking for a woman named Elena.").also { stage = 7 }
                }
                3 -> npcl(FacialExpression.NEUTRAL, "We're working for King Lathas of East Ardougne. He has tasked us with containing the accursed plague sweeping West Ardougne.").also { stage++ }
                4 -> npcl(FacialExpression.NEUTRAL, "We also do our best to ease these people's suffering. We're nicknamed mourners because we spend a lot of time at plague victim funerals, no one else is allowed to risk attending.").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "It's a demanding job, and we get little thanks from the people here.").also { stage = 1 }
                6 -> npcl(FacialExpression.NEUTRAL, "Can't risk you being a carrier. That protective clothing you have isn't regulation issue. It won't meet safety standards.").also { stage = 1 }
                7 -> npcl(FacialExpression.NEUTRAL, "Ah yes, I've heard of her. A healer I believe. She must be mad coming over here voluntarily.").also { stage++ }
                8 -> npcl(FacialExpression.NEUTRAL, "I hear rumours she has probably caught the plague now. Very tragic, a stupid waste of life.").also { stage = END_DIALOGUE }
            }

            11 -> when (stage) {
                0 -> player!!.dialogueInterpreter.sendDialogue("The door won't open.", "You notice a black cross on the door.").also { stage++ }
                1 -> npcl(FacialExpression.NEUTRAL, "I'd stand away from there. That black cross means that house has been touched by the plague.").also { stage++ }
                2 -> options("But I think a kidnap victim is in here.", "I fear not a mere plague.", "Thanks for the warning.").also { stage++ }
                3 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "But I think a kidnap victim is in here.").also { stage = 5 }
                    2 -> playerl(FacialExpression.FRIENDLY, "I fear not a mere plague.").also { stage = 4 }
                    3 -> playerl(FacialExpression.FRIENDLY, "Thanks for the warning.").also { stage = END_DIALOGUE }

                }
                4 -> playerl(FacialExpression.FRIENDLY, "Thanks for the warning.").also { stage = END_DIALOGUE }
                5 -> npcl(FacialExpression.FRIENDLY, "That's irrelevant. You don't have clearance to go in there.").also { stage++ }
                6 -> playerl(FacialExpression.NEUTRAL, "How do I get clearance?").also { stage++ }
                7 -> npcl(FacialExpression.NEUTRAL, "Well you'd need to apply to the head mourner, or I suppose Bravek the city warder.").also { stage++ }
                8 -> npcl(FacialExpression.NEUTRAL, "I wouldn't get your hopes up though.").also { stage++ }
                9 -> {
                    end()
                    setQuestStage(player!!, "Plague City", 12)
                    stage = END_DIALOGUE
                }
            }

            in 13..15 -> when (stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "Hmmm, how did you get over here? You're not one of this rabble. Ah well, you'll have to stay. Can't risk you going back now.").also { stage++ }
                1 -> options("I need clearance to enter a plague house.", "So what's a mourner?", "I haven't got the plague though...", "I'm looking for a woman named Elena.").also { stage++ }
                2 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "I need clearance to enter a plague house. It's in the south east corner of West Ardougne.").also { stage = 9 }
                    2 -> playerl(FacialExpression.FRIENDLY, "So what's a mourner?").also { stage = 3 }
                    3 -> playerl(FacialExpression.FRIENDLY, "I haven't got the plague though...").also { stage = 6 }
                    4 -> playerl(FacialExpression.FRIENDLY, "I'm looking for a woman named Elena.").also { stage = 7 }
                }
                3 -> npcl(FacialExpression.NEUTRAL, "We're working for King Lathas of East Ardougne. He has tasked us with containing the accursed plague sweeping West Ardougne.").also { stage++ }
                4 -> npcl(FacialExpression.NEUTRAL, "We also do our best to ease these people's suffering. We're nicknamed mourners because we spend a lot of time at plague victim funerals, no one else is allowed to risk attending.").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "It's a demanding job, and we get little thanks from the people here.").also { stage = 1 }
                6 -> npcl(FacialExpression.NEUTRAL, "Can't risk you being a carrier. That protective clothing you have isn't regulation issue. It won't meet safety standards.").also { stage = 1 }
                7 -> npcl(FacialExpression.NEUTRAL, "Ah yes, I've heard of her. A healer I believe. She must be mad coming over here voluntarily.").also { stage++ }
                8 -> npcl(FacialExpression.NEUTRAL, "I hear rumours she has probably caught the plague now. Very tragic, a stupid waste of life.").also { stage = END_DIALOGUE }
                9 -> npcl(FacialExpression.DISGUSTED, "You must be nuts, absolutely not!").also { stage++ }
                10 -> options("There's a kidnap victim inside!", "I've got a gas mask though...", "Yes, I'm utterly crazy.").also { stage++ }
                11 -> when (buttonID) {
                    1 -> playerl(FacialExpression.FRIENDLY, "There's a kidnap victim inside!").also { stage = 12 }
                    2 -> playerl(FacialExpression.FRIENDLY, "I've got a gas mask though...").also { stage = 13 }
                    3 -> playerl(FacialExpression.FRIENDLY, "Yes, I'm utterly crazy.").also { stage = 17 }
                }
                12 -> npcl(FacialExpression.FRIENDLY, "Well they're as good as dead then, no point in trying to save them.").also { stage = END_DIALOGUE }
                13 -> npcl(FacialExpression.FRIENDLY, "It's not regulation. Anyway you're not properly trained to deal with the plague.").also { stage++ }
                14 -> playerl(FacialExpression.FRIENDLY, "How do I get trained?").also { stage++ }
                15 -> npcl(FacialExpression.FRIENDLY, "It requires a strict 18 months of training.").also { stage++ }
                16 -> playerl(FacialExpression.FRIENDLY, "I don't have that sort of time.").also { stage = END_DIALOGUE }
                17 -> playerl(FacialExpression.FRIENDLY, "Yes, I'm utterly crazy.").also { stage++ }
                18 -> npcl(FacialExpression.FRIENDLY, "You're wasting my time, I have a lot of work to do!").also { stage = END_DIALOGUE }
            }

            in 16..100 -> when (stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "I'd stand away from there. That black cross means that house has been touched by the plague.").also { stage = END_DIALOGUE }
            }
        }
    }
}