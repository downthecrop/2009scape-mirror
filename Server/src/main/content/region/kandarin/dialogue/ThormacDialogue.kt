package content.region.kandarin.dialogue

import content.region.kandarin.quest.scorpioncatcher.SCThormacDialogue
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class ThormacDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.THORMAC_389)
    }

    companion object {
        const val COMPLETED_QUEST = 1000
        const val ENCHANT_DIALOGUE = 2000
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, "Scorpion Catcher")){
            npc(FacialExpression.HAPPY, "Thank you for rescuing my scorpions.").also {stage = COMPLETED_QUEST}
        }
        else{
            openDialogue(player, SCThormacDialogue(getQuestStage(player, "Scorpion Catcher")), npc)
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            COMPLETED_QUEST -> showTopics(
                Topic(FacialExpression.HAPPY, "That's okay.", END_DIALOGUE),
                Topic(FacialExpression.NEUTRAL, "You said you'd enchant my battlestaff for me", ENCHANT_DIALOGUE)
            )

            ENCHANT_DIALOGUE -> {
                val cost = if (player.equipment.contains(Items.SEERS_HEADBAND_14631, 1)) 27 else 40
                npcl(FacialExpression.HAPPY, "Yes, it'll cost you $cost,000 coins for the materials needed though. " +
                        "Which sort of staff did you want enchanting?").also { stage++ }
            }
            ENCHANT_DIALOGUE + 1 -> {
                end()
                player.interfaceManager.openComponent(332)
            }

        }
        return true
    }

}