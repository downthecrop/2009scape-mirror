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
class GuardHCDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            // stage 0 - unstarted
            (questStage == 0) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello. If you've come to admire the Carnillean family home, I must warn you not to cause any trouble.").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "Due to recent criminal activities against such a prominent member of Ardougnian politics, I have been sent here on special guard duty to ensure the Carnilleans' security.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "Anyone caught interfering with the person or possessions of said Carnilleans will be taken to jail so quickly that their feet won't touch the floor!").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "Well, I wasn't actually planning on doing anything like that...").also { stage++ }
                    5 -> npcl(FacialExpression.FRIENDLY, "Glad to hear it. Let's keep it that way, hmmm?").also { stage = END_DIALOGUE }
                }
            }

            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage == 3) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.SAD, "Today is a dark day. Those cultists have been back, and this time they've gone further than ever before! Murder! We can't afford to keep letting them get away with this.").also { stage = END_DIALOGUE }
                }
            }

            // stage 1 - after talking to ceril
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // stage 4 - alomone either fought or he tells you he needs scroll
            // stage 5 - either returning the armour, or finding the scroll
            (questStage in 1..99) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello there. I hear you're after that cult who broke in the other night. It always gladdens me when civilians assist the law like this.").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm just happy to be of help.").also { stage = END_DIALOGUE }
                }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage == 100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hello there.").also { stage = 1 }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 6 }
                    }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "Hello, adventurer. It's a shame you never found that cult. We have a horrible suspicion that there's been another burglary.").also { stage++ }
                2 -> playerl(FacialExpression.NEUTRAL, "That's worrying.").also { stage++ }
                3 -> npcl(FacialExpression.NEUTRAL, "Yes, it is. The thing is, we can't even work out what they've taken! It's all very odd.").also { stage++ }
                4 -> playerl(FacialExpression.HALF_ASKING, "Is there anything more I can do to help?").also { stage++ }
                5 -> npcl(FacialExpression.NEUTRAL, "I don't think so. Sir Ceril says you've done enough.").also { stage = END_DIALOGUE }

                6 -> npcl(FacialExpression.ANNOYED, "Well well well... if it isn't our very own local hero. Good to see you managed to clear your name. I always had faith in you!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.GUARD_887)
}
