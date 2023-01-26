package content.region.fremennik.lunarisle.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Does not include Lunar Diplomacy dialogue
 */

@Initializable
class BabaYagaDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.ASKING, "Ah, a stranger to our island. How can I help?").also { stage ++ }
            1 -> options("Have you got anything to trade?", "It's a very interesting house you have here.", "I'm good thanks, bye.").also { stage ++ }

            2 -> when(buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> playerl(core.game.dialogue.FacialExpression.ASKING, "It's a very interesting house you have here. Does he have a name?").also { stage = 10 }
                3 -> end()
            }

            10 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Why of course. It's Berty.").also { stage++ }
            11 -> player(core.game.dialogue.FacialExpression.THINKING, "Berty? Berty the Chicken leg house?").also { stage++ }
            12 -> npc(core.game.dialogue.FacialExpression.LAUGH, "Yes.").also { stage++ }
            13 -> player(core.game.dialogue.FacialExpression.ASKING, "May I ask why?").also { stage++ }
            14 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "It just has that certain ring to it, don't you think? Beeerteeee!").also { stage++ }
            15 -> player(core.game.dialogue.FacialExpression.HALF_WORRIED, "You're ins...").also { stage++ }
            16 -> npc("Insane? Very.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BabaYagaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BABA_YAGA_4513)
    }
}