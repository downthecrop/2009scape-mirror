package content.region.kandarin.seers.dialogue

import content.region.kandarin.seers.diary.SeerDiaryDialogue
import content.region.kandarin.quest.scorpioncatcher.SCSeerDialogue
import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher
import core.api.getAttribute
import core.api.getQuestStage
import core.api.isQuestInProgress
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs
import core.game.dialogue.Topic



@Initializable
class SeerDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SEER_388)
    }

    companion object {
        const val OTHER_TOPIC = 10
        const val DIARY = 20
        const val SC_QUEST = 30

        const val SC_QUEST_HELP = 40
        const val SC_QUEST_FRIEND = 50
        const val SC_QUEST_OTHER_SCORPIONS = 60

        const val MANY_GREETINGS = 70
        const val POWER = 80
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.WORRIED, "Uh, what was that dark force? I've never sensed anything like it...").also { stage = START_DIALOGUE } // https://www.youtube.com/watch?v=mYsxit46rGo May 14 2010
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val scorpionCatcherQuestStage = getQuestStage(player, "Scorpion Catcher")
        when (stage) {
            START_DIALOGUE ->
                npcl(FacialExpression.NEUTRAL, "Anyway, sorry about that.").also { stage++ }
            START_DIALOGUE+1 -> {
                if (isQuestInProgress(player, "Scorpion Catcher", 1, 99)) {
                    showTopics(
                        Topic("Talk about Scorpion Catcher.", SC_QUEST, true),
                        Topic("Talk about Achievement Diary.", DIARY, true)
                    )
                }
                else {
                    showTopics(
                        Topic("Talk about something else.", OTHER_TOPIC, true),
                        Topic("Talk about achievement diary.", DIARY, true)
                    )
                }
            }

            SC_QUEST -> {
                if (scorpionCatcherQuestStage == ScorpionCatcher.QUEST_STATE_TALK_SEERS) {
                    showTopics(
                        Topic("I need to locate some scorpions.", SC_QUEST_HELP,),
                        Topic("Your friend Thormac sent me to speak to you.", SC_QUEST_FRIEND,),
                        Topic("I seek knowledge and power!", POWER)
                    )
                }
                else if ((scorpionCatcherQuestStage == ScorpionCatcher.QUEST_STATE_DARK_PLACE) and
                    getAttribute(player!!, ScorpionCatcher.ATTRIBUTE_TAVERLY, false)
                ) {
                    playerl(
                        FacialExpression.NEUTRAL,
                        "Hi, I have retrieved the scorpion from near the spiders."
                    ).also { stage = SC_QUEST_OTHER_SCORPIONS }
                }
                else {
                    npcl(FacialExpression.NEUTRAL, "Good luck finding those scorpions.").also { stage = END_DIALOGUE }
                }
            }
            SC_QUEST_HELP, SC_QUEST_FRIEND, SC_QUEST_OTHER_SCORPIONS -> {
                // Use the current stage value as the entry point to Seers
                openDialogue(player, SCSeerDialogue(scorpionCatcherQuestStage, stage), npc)
            }

            OTHER_TOPIC -> showTopics(
                Topic("Many greetings.", MANY_GREETINGS),
                Topic("I seek knowledge and power!", POWER)
            )
            DIARY -> openDialogue(player, SeerDiaryDialogue(), npc)

            MANY_GREETINGS -> npcl(FacialExpression.NEUTRAL,
                "Remember, whenever you set out to do something, something else must be done first.").also { stage = END_DIALOGUE }

            POWER -> npcl(FacialExpression.NEUTRAL, "Knowledge comes from experience, power comes from battleaxes.").also { stage = END_DIALOGUE }

        }
        return true
    }
}
