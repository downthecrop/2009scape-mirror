package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Eohric sub dialogue file for death plateau.
 * Called by EohricDialogue
 * @author bushtail
 * @author ovenbread
 */
class EohricDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, DeathPlateau.questName)) {
            in 5..9 -> {
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FacialExpression.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 -> showTopics(
                        Topic(FacialExpression.THINKING, "I'm looking for the guard that was on the last night.", 10),
                        Topic(FacialExpression.FRIENDLY, "Do you know of another way up Death Plateau?", 20),
                        Topic(FacialExpression.FRIENDLY, "No, I'm just looking around.", END_DIALOGUE)
                    )
                    10 -> npcl(FacialExpression.FRIENDLY,"There was only one guard on last night. Harold. He's a nice lad, if a little dim.").also { stage++ }
                    11 -> player(FacialExpression.FRIENDLY, "Do you know where he is staying?").also { stage++ }
                    12 -> npc(FacialExpression.FRIENDLY, "Harold is staying at the Toad and Chicken.").also { stage++ }
                    13 -> player(FacialExpression.FRIENDLY, "Thanks!").also {
                        setQuestStage(player!!, "Death Plateau", 10)
                        HaroldDialogueFile.resetNpc(player!!)
                        stage = END_DIALOGUE
                    }
                    20 -> npcl(FacialExpression.FRIENDLY, "No, sorry. I wouldn't want to go north-east from here, it's very rocky and barren.").also { stage = END_DIALOGUE }
                }
            }
            10 -> {
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FacialExpression.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 -> showTopics(
                        Topic(FacialExpression.THINKING, "Where is the guard that was on last night staying?", 10),
                        Topic(FacialExpression.FRIENDLY, "Do you know of another way up Death Plateau?", 20),
                        Topic(FacialExpression.FRIENDLY, "No, I'm just looking around.", END_DIALOGUE)
                    )
                    10 -> npc(FacialExpression.FRIENDLY, "Harold is staying at the Toad and Chicken.").also { stage = END_DIALOGUE }
                    20 -> npcl(FacialExpression.FRIENDLY, "No, sorry. I wouldn't want to go north-east from here, it's very rocky and barren.").also { stage = END_DIALOGUE }
                }
            }
            11 -> {
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FacialExpression.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 -> playerl(FacialExpression.HALF_GUILTY, "I found Harold but he won't talk to me!.").also { stage++ }
                    3 -> npcl(FacialExpression.THINKING, "Hmm. Harold has got in trouble a few over his drinking and gambling. Perhaps he'd open up after a drink?").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "Thanks, I'll try that!").also {
                        setQuestStage(player!!, "Death Plateau", 12)
                        stage = END_DIALOGUE
                    }
                }
            }
            12 -> {
                when (stage) {
                    START_DIALOGUE -> player(FacialExpression.FRIENDLY, "Hi!").also { stage++ }
                    1 -> npc(FacialExpression.FRIENDLY, "Hi, can I help?").also { stage++ }
                    2 -> playerl(FacialExpression.ASKING, "You said Harold had a weakness?").also { stage++ }
                    3 -> npcl(FacialExpression.THINKING,"Yes, if you buy Harold a beer he might talk to you. I also know he has a weakness for gambling. Hope that helps!").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "Thanks for the help!").also {stage = END_DIALOGUE}
                }
            }
        }
    }
}