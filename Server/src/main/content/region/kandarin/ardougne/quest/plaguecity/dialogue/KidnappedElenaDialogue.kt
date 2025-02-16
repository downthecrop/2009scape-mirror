package content.region.kandarin.ardougne.quest.plaguecity.dialogue

import content.data.Quests
import core.api.setQuestStage
import core.api.setVarbit
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs
import org.rs09.consts.Vars

@Initializable
class KidnappedElenaDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (player.questRepository.getStage(Quests.PLAGUE_CITY) >= 16) {
            playerl(FacialExpression.FRIENDLY, "Hi, you're free to go! Your kidnappers don't seem to be about right now.").also { stage = 1 }
        } else {
            npcl(FacialExpression.FRIENDLY, "Go and see my father, I'll make sure he adequately rewards you. Now I'd better leave while I still can.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (stage) {
            1 -> npcl(FacialExpression.FRIENDLY, "Thank you, being kidnapped was so inconvenient. I was on my way back to East Ardougne with some samples, I want to see if I can diagnose a cure for this plague.").also { stage++ }
            2 -> playerl(FacialExpression.FRIENDLY, "Well you can leave via the manhole near the gate.").also { stage++ }
            3 -> npcl(FacialExpression.FRIENDLY, "Go and see my father, I'll make sure he adequately rewards you. Now I'd better leave while I still can.").also { stage++ }
            4 -> {
                end()
                setQuestStage(player!!, Quests.PLAGUE_CITY, 99)
                setVarbit(player, Vars.VARBIT_QUEST_PLAGUE_CITY_RESCUE_ELENA, 1)
                setVarbit(player, Vars.VARBIT_QUEST_PLAGUE_CITY_EDMOND_TUNNELS, 0)
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.KIDNAPPED_ELENA_715)
}