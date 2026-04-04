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
class HazeelCultistDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun handle(componentID: Int, buttonID: Int, ): Boolean {
        val questStage = getQuestStage(player!!, Quests.HAZEEL_CULT)

        when {
            (questStage in 1..99) -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hi.").also { stage++ }
                1 -> npcl(FacialExpression.ANNOYED, "What? Who are you and why did you come here? It doesn't matter, leave now adventurer. While you still can.",).also { stage = END_DIALOGUE }
            }

            (questStage == 100) -> when (stage) {
                0 -> {
                    if (mahjarratArc(player)) {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 1 }
                    } else {
                        playerl(FacialExpression.FRIENDLY, "Hello.").also { stage = 2 }
                    }
                }
                1 -> npcl(FacialExpression.FRIENDLY, "The adventurer returns! Glory to you for your efforts in bringing about the return of Lord Hazeel!").also { stage = END_DIALOGUE }
                2 -> npcl(FacialExpression.ANNOYED, "It's the meddler! The one who blew Jones' cover! I don't know why you came to this place but it is here that you will die.").also { stage++ }
                3 -> {
                    end()
                    npc.attack(player)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.HAZEEL_CULTIST_894)
}
