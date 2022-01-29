package rs09.game.content.dialogue.region.dorgeshuun

import api.toIntArray
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * @author qmqz
 */

@Initializable
class randomChildrenDialogue(player: Player? = null) : DialoguePlugin(player){
    var a = FacialExpression.OLD_NORMAL
    var b = FacialExpression.FRIENDLY

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when ((1..5).random()) {
            1 -> npc(FacialExpression.OLD_NORMAL, "Are you a surface-dweller?").also { stage = 0 }
            2 -> npcl(a, "Are you " + player.name + "? Did you help Zanik save the city?").also { stage = 10 }
            3 -> npc(a, "Sorry, I'm not meant to talk to strangers.").also { stage = 99 }
            4 -> npc(a, "Shh! Don't tell anyone!").also { stage = 20 }
            5 -> npc(a, "Help! Help! The surface people are attacking!").also { stage = 30 }
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(b, "Yes...").also { stage++ }
            1 -> npcl(a, "Haha! You look funny! All tall and skinny with tiny eyes!").also { stage = 99 }

            10 -> player(b, "Yes, that was me!").also { stage++ }
            11 -> npcl(a, "When I'm older I'm going to be an adventurer, just like Zanik!").also { stage = 99 }

            20 -> player(b, "Don't tell anyone what?").also { stage++ }
            21 -> npc(a, "SHHH!").also { stage = 99 }

            30 -> player(b, "It's alright, I'm friendly!").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return randomChildrenDialogue(player)
    }

    val ids = 5807..5822

    override fun getIds(): IntArray {
        return ids.toIntArray()
    }
}
