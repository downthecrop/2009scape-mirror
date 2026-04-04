package content.region.kandarin.ardougne.quest.hazeelcult

import content.data.Quests
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult.Companion.mahjarratArc
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.*

@Initializable
class ClausTheChefDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)
        when {

            // stage 0 - unstarted
            // stage 1 - after talking to ceril
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // stage 3 - poison poured in food (mahjarrat-only stage)
            // stage 4 - alomone either fought or he tells you he needs scroll
            // stage 5 - either returning the armour, or finding the scroll
            (questStage in 0..99) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Sorry, can't stop to chat! You would be amazed at how many meals this family gets through daily!").also { stage = END_DIALOGUE }
                }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage == 100) -> {
                when (stage) {
                    0 -> {
                        if (mahjarratArc(player)) {
                            playerl(FacialExpression.FRIENDLY, "Hey.").also { stage++ }
                        } else {
                            playerl(FacialExpression.FRIENDLY, "Hiya.").also { stage = 2 }
                        }
                    }
                    1 -> npcl(FacialExpression.NEUTRAL, "Oh, hello there. Sorry, but I can't really talk right now. Things haven't been great here recently, and I have a lot of work to do.").also { stage = END_DIALOGUE }

                    2 -> npcl(FacialExpression.HALF_ASKING, "Well hello there adventurer! Are we fit and well?").also { stage++ }
                    3 -> playerl(FacialExpression.FRIENDLY, "Yep, fine thanks.").also { stage++ }
                    4 -> npcl(FacialExpression.FRIENDLY, "Glad to hear it.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CLAUS_THE_CHEF_886)
}
