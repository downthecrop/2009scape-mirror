package content.region.misthalin.barbvillage.dialogue

import content.region.kandarin.quest.scorpioncatcher.SCPeksaDialogue
import content.region.kandarin.quest.scorpioncatcher.ScorpionCatcher
import core.api.getQuestStage
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class PeksaDialogue(player: Player? = null) : DialoguePlugin(player){

    companion object {
        const val GO_SHOPPING = 10
        const val LEAVE = 20
        const val DIALOGUE_SCORPION_CATCHER = 30
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.HALF_GUILTY, "Are you interested in buying or selling a helmet?").also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> {
                showTopics(
                    Topic("I could be, yes.", GO_SHOPPING),
                    Topic("No, I'll pass on that.", LEAVE),
                    IfTopic("I've heard you have a small scorpion in your possession.", DIALOGUE_SCORPION_CATCHER, getQuestStage(player, "Scorpion Catcher") == ScorpionCatcher.QUEST_STATE_OTHER_SCORPIONS)
                )
            }

            GO_SHOPPING -> {
                    end()
                    npc.openShop(player)
                }

            LEAVE -> {
                npcl(FacialExpression.HALF_GUILTY, "Well, come back if you change your mind.").also { stage = END_DIALOGUE }
            }

            DIALOGUE_SCORPION_CATCHER -> {
                openDialogue(player, SCPeksaDialogue(getQuestStage(player, "Scorpion Catcher")), npc)
            }
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PEKSA_538)
    }
}