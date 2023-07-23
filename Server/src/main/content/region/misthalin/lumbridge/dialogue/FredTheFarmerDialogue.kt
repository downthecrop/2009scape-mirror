package content.region.misthalin.lumbridge.dialogue

import content.region.misthalin.lumbridge.quest.sheepshearer.SSFredTheFarmerDialogue
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE

@Initializable
class FredTheFarmerDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun getIds(): IntArray {
        return intArrayOf(758)
    }

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, "Sheep Shearer") in 1..99) {
            openDialogue(player, SSFredTheFarmerDialogue(getQuestStage(player, "Sheep Shearer")), npc)
        } else {
            npc(FacialExpression.ANGRY, "What are you doing on my land? You're not the one", "who keeps leaving all my gates open and letting out all", "my sheep are you?").also { stage = START_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                IfTopic(FacialExpression.NEUTRAL, "I'm looking for a quest.", 1000, getQuestStage(player!!, "Sheep Shearer") == 0),
                Topic(FacialExpression.HALF_GUILTY, "I'm looking for something to kill.", 100),
                Topic(FacialExpression.HALF_GUILTY, "I'm lost.", 200)
            )

            100 -> npc(FacialExpression.HALF_GUILTY, "What, on my land? Leave my livestock alone you", "scoundrel!").also { stage = END_DIALOGUE }

            200 -> npc(FacialExpression.HALF_GUILTY, "How can you be lost? Just follow the road east and", "south. You'll end up in Lumbridge fairly quickly.").also { stage = END_DIALOGUE }

            1000 -> openDialogue(player, SSFredTheFarmerDialogue(getQuestStage(player, "Sheep Shearer")), npc)
        }
        return true
    }
}