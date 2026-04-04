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
class HenryetaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            // stage 3 - poison poured in food (mahjarrat-only stage)
            (questStage == 3) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello. Are you okay?").also { stage++ }
                    1 -> npcl(FacialExpression.SAD, "No, no I am very far from OK. Those hooligans slaughtered my precious Scruffy! I shall never recover! I am emotionally scarred for life!").also { stage = END_DIALOGUE }
                    2 -> playerl(FacialExpression.NEUTRAL, "It WAS only a dog you know...").also { stage = END_DIALOGUE }
                   }
            }

            // stage 0 - unstarted
            // stage 1 - after talking to ceril
            // stage 2 - talk to clivet (set attr for carnillean)
            // stage 2 - talk to clivet (set attr for mahjarrat)
            // stage 4 - alomone either fought or he tells you he needs scroll
            // stage 5 - either returning the armour, or finding the scroll
            (questStage in 0..99) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.FRIENDLY, "Oh, hello. I'm afraid I'm not very presentable at the moment. I've been under incredible stress of late! It's ruining my complexion!").also { stage++ }
                    1 -> playerl(FacialExpression.FRIENDLY, "Why? What's wrong?").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "It's those awful cultists! I just don't feel safe here anymore!").also { stage = END_DIALOGUE }
                }
            }

            // stage 100 - quest complete - either presenting proof that jones is a bad guy (duh) or resurrecting hazeel
            (questStage == 100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 1 }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 3 }
                    }
                }

                1 -> npcl(FacialExpression.HALF_ASKING, "Oh, it's you. I'm very disappointed that you never found those awful cultists. You're quite useless really, aren't you?").also { stage++ }
                2 -> playerl(FacialExpression.HAPPY, "Yup, I guess I am.").also { stage = END_DIALOGUE }

                3 -> npcl(FacialExpression.FRIENDLY, "Hello again adventurer! Since you dealt with those horrible hooligan cultists, things have vastly improved for us around here!").also { stage++ }
                4 -> playerl(FacialExpression.HAPPY, "My pleasure, Lady Henryeta.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HENRYETA_CARNILLEAN_889)
}
