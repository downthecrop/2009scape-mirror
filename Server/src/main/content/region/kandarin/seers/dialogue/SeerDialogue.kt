package content.region.kandarin.seers.dialogue

import content.region.kandarin.seers.diary.SeerDiaryDialogue
import core.api.isQuestInProgress
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
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

        const val MANY_GREETINGS = 60
        const val POWER = 70
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.WORRIED, "Uh, what was that dark force? I've never sensed anything like it...").also { stage = START_DIALOGUE } // https://www.youtube.com/watch?v=mYsxit46rGo May 14 2010
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE ->
                npcl(FacialExpression.NEUTRAL, "Anyway, sorry about that.").also { stage++ }
            START_DIALOGUE+1 ->
                showTopics(
                    Topic("Talk about something else.", OTHER_TOPIC, true),
                    Topic("Talk about achievement diary.", DIARY, true)
                )

            DIARY -> openDialogue(player, SeerDiaryDialogue(), npc)

            OTHER_TOPIC -> if (isQuestInProgress(player, "Scorpion Catcher", 1, 99)) {
                npcl(FacialExpression.FURIOUS, "Not implemented").also { stage = END_DIALOGUE }
            }
            else{
                showTopics(
                    Topic("Many greetings.", MANY_GREETINGS),
                    Topic("I seek knowledge and power!", POWER)
                )
            }

            MANY_GREETINGS -> npcl(FacialExpression.NEUTRAL,
                "Remember, whenever you set out to do something, something else must be done first.").also { stage = END_DIALOGUE }

            POWER -> npcl(FacialExpression.NEUTRAL, "Knowledge comes from experience, power comes from battleaxes.").also { stage = END_DIALOGUE }

        }
        return true
    }
}
