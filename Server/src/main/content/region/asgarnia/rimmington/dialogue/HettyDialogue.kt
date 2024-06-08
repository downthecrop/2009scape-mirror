package content.region.asgarnia.rimmington.dialogue

import content.region.asgarnia.rimmington.quest.witchpotion.HettyWitchsPotionDialogue
import content.region.asgarnia.rimmington.quest.witchpotion.WitchsPotion
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.npc.NPC
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

/**
 * Handles the Hetty's dialogue.
 */
@Initializable
class HettyDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        val questStage = getQuestStage(player, WitchsPotion.QUEST_NAME)

        when(questStage) {
            0 -> npcl(FacialExpression.NEUTRAL, "What could you want with an old woman like me?").also { stage = 0 }
            in 1..99 -> openDialogue(player, HettyWitchsPotionDialogue(questStage), npc)
            else -> npcl(FacialExpression.ASKING, "How's your magic coming along?").also { stage = 30 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I am in search of a quest.", "I've heard that you are a witch.").also { stage++ }
            1 -> when (buttonId) {
                1 -> playerl(FacialExpression.NEUTRAL, "I am in search of a quest.").also { stage = 10 }
                2 -> playerl(FacialExpression.NEUTRAL, "I've heard that you are a witch.").also { stage = 20 }
            }

            10 -> npcl(FacialExpression.HAPPY, "Hmmm... Maybe I can think of something for you.").also { stage++ }
            11 -> npcl(FacialExpression.HAPPY, "Would you like to become more proficient in the dark arts?").also { stage++ }
            12 -> options("Yes help me become one with my darker side.", "No I have my principles and honour.", "What, you mean improve my magic?").also { stage++ }
            13 -> when (buttonId) {
                1 -> playerl(FacialExpression.HAPPY, "Yes help me become one with my darker side.").also { stage = 14 }
                2 -> playerl(FacialExpression.NEUTRAL, "No I have my principles and honour.").also { stage = 15 }
                3 -> playerl(FacialExpression.ASKING, "What, you mean improve my magic?").also { stage = 40 }
            }

            14 -> openDialogue(player, HettyWitchsPotionDialogue(0), npc)
            15 -> npcl(FacialExpression.HALF_GUILTY, "Suit yourself, but you're missing out.").also { stage = END_DIALOGUE }

            20 -> npcl(FacialExpression.HALF_GUILTY, "Yes it does seem to be getting fairly common knowledge").also { stage++ }
            21 -> npcl(FacialExpression.HALF_GUILTY, "I fear I may get a visit from the witch hunter of Falador before long.").also { stage = END_DIALOGUE }

            30 -> playerl(FacialExpression.HALF_GUILTY, "I'm practicing and slowly getting better.").also { stage++ }
            31 -> npcl(FacialExpression.HALF_GUILTY, "Good, good.").also { stage = END_DIALOGUE }

            40 -> sendDialogue("The witch sighs.").also { stage++ }
            41 -> npcl(FacialExpression.ANNOYED, "Yes, improve your magic...").also { stage++ }
            42 -> npcl(FacialExpression.ASKING, "Do you have no sense of drama?").also { stage++ }
            43 -> options("Yes I'd like to improve my magic.", "No I'm not interested.", "Show me the mysteries of the dark arts...").also { stage++ }
            44 -> when(buttonId) {
                // TODO: Find authentic source for dialogue option
                1 -> playerl(FacialExpression.NEUTRAL, "Yes I'd like to improve my magic.").also { stage = 14 }
                // TODO: Find authentic source for dialogue option
                2 -> playerl(FacialExpression.NEUTRAL, "No I'm not interested.").also { stage = 15 }
                3 -> playerl(FacialExpression.NEUTRAL, "Show me the mysteries of the dark arts...").also { stage++ }
            }
            45 -> sendDialogue("The witch smiles mysteriously.").also { stage = 14 }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HettyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HETTY_307)
    }
}