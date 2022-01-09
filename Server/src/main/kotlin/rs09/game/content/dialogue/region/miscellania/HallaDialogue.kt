package rs09.game.content.dialogue.region.miscellania

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class HallaDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Welcome to Miscellania's first clothing store!", "We sell clothing made especially for Fremenniks", "and Dwarves.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(FacialExpression.ASKING, "Can I help you with anything, your Royal Highness?").also { stage++ }
            }

            1 -> {
                options("I'd like to look at what you have for sale.", "No thank you, I'm fine.", "What's it like living down here?").also { stage++ }
            }

            2 -> when(buttonId){
                1 -> {
                    player(FacialExpression.FRIENDLY, "I'd like to look at what you have for sale.").also { stage = 10 }
                }

                2 -> {
                    player(FacialExpression.NEUTRAL, "No thank you, I'm fine.").also { stage = 99 }
                }

                3 -> {
                    player(FacialExpression.ASKING, "What's it like living down here?").also { stage = 20 }
                }
            }

            10 -> {
                end().also { npc.openShop(player) }
            }

            20 -> {
                npc(FacialExpression.NEUTRAL, "It's very spacious down here.", "One of the dwarves said that the caves go on for miles!").also { stage++ }
            }
            21 -> {
                npc(FacialExpression.NEUTRAL, "The only problem I find is that the lighting's not very good,", "which means I make mistakes when cutting cloth.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return HallaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HALLA_3921)
    }
}