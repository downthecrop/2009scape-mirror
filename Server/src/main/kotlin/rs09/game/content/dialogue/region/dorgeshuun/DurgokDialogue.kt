package rs09.game.content.dialogue.region.dorgeshuun

import api.addItemOrDrop
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class DurgokDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.OLD_NORMAL,"Frogburger! There's nothing like grilled frog in a bun. Do you want one? Only 10gp!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, please.", "No, thanks.").also { stage++ }
            1 -> when(buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Yes please!").also { stage = 10 }
                2 -> player(FacialExpression.FRIENDLY, "No thanks.").also { stage = 99 }
            }

            10 -> if (player.inventory.contains(Items.COINS_995, 10)) {
                player.inventory.remove(Item(Items.COINS_995, 10))
                addItemOrDrop(player, Items.FROGBURGER_10962, 1)
                npc(FacialExpression.OLD_NORMAL, "There you go.").also { stage = 99 }
            } else {
                npc(FacialExpression.OLD_NORMAL, "I'm sorry, but you need 10gp for that.").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DurgokDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DURGOK_5794)
    }
}
