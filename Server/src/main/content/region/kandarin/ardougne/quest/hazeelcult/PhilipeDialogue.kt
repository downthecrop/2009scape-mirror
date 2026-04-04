package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import core.api.getQuestStage
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.*

@Initializable
class PhilipeDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            // stage 0 - unstarted
            (questStage == 0) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.CHILD_NORMAL, "Hello, how are you today?").also { stage++ }
                    1 -> playerl(FacialExpression.FRIENDLY, "Good thank you. And yourself?").also { stage++ }
                    2 -> npcl(FacialExpression.CHILD_NORMAL, "Fine and dandy.").also { stage = END_DIALOGUE }
                }
            }

            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage == 3) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FacialExpression.CHILD_SAD, "Someone killed Scruffy. I liked Scruffy. He never told me off.").also { stage++ }
                    2 -> playerl(FacialExpression.HALF_GUILTY, "Yeah... it's a real shame.").also { stage++ }
                    3 -> npcl(FacialExpression.CHILD_SAD, "I want my mommy.").also { stage = END_DIALOGUE }
                }
            }

            // stage 1 - after talking to ceril
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // stage 4 - alomone either fought or he tells you he needs scroll
            // stage 5 - either returning the armour, or finding the scroll
            (questStage in 1..99) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.CHILD_NORMAL, "Mommy said you're here to kill all the nasty people that keep breaking in.").also { stage++ }
                    1 -> playerl(FacialExpression.FRIENDLY, "Something like that.").also { stage++ }
                    2 -> npcl(FacialExpression.CHILD_FRIENDLY, "Can I watch?").also { stage++ }
                    3 -> playerl(FacialExpression.FRIENDLY, "No!").also { stage = END_DIALOGUE }
                }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage == 100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 1 }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Hello youngster.").also { stage = 4 }
                    }
                }

                1 -> npcl(FacialExpression.CHILD_NORMAL, "What have you brought me? I want some more toys!").also { stage++ }
                2 -> playerl(FacialExpression.FRIENDLY, "I'm afraid I don't have any toys.").also { stage++ }
                3 -> npcl(FacialExpression.CHILD_SAD, "Toys! I want toys!").also { stage = END_DIALOGUE }

                4 -> npcl(FacialExpression.CHILD_NORMAL, "Daddy says you don't like Mr. Jones. Mr. Jones is nice. He brings me toys and sweets.").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "Jones is a bad man, Philipe.").also { stage++ }
                6 -> npcl(FacialExpression.CHILD_NORMAL, "You're a bad " + (if (player.isMale) "man" else "lady") + " I don't like you!").also { stage++ }
                7 -> playerl(FacialExpression.FRIENDLY, "I'll try and console myself about that disappointment somehow.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PHILIPE_CARNILLEAN_888)
}
