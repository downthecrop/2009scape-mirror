package rs09.game.content.dialogue.region.lunarisle

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Does not include Lunar Diplomacy dialogue
 */

@Initializable
class BabaYagaDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.ASKING, "Ah, a stranger to our island. How can I help?").also { stage ++ }
            1 -> options("Have you got anything to trade?", "It's a very interesting house you have here.", "I'm good thanks, bye.").also { stage ++ }

            2 -> when(buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> playerl(FacialExpression.ASKING, "It's a very interesting house you have here. Does he have a name?").also { stage = 10 }
                3 -> end()
            }

            10 -> npc(FacialExpression.FRIENDLY, "Why of course. It's Berty.").also { stage++ }
            11 -> player(FacialExpression.THINKING, "Berty? Berty the Chicken leg house?").also { stage++ }
            12 -> npc(FacialExpression.LAUGH, "Yes.").also { stage++ }
            13 -> player(FacialExpression.ASKING, "May I ask why?").also { stage++ }
            14 -> npcl(FacialExpression.LAUGH, "It just has that certain ring to it, don't you think? Beeerteeee!").also { stage++ }
            15 -> player(FacialExpression.HALF_WORRIED, "You're ins...").also { stage++ }
            16 -> npc("Insane? Very.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BabaYagaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BABA_YAGA_4513)
    }
}