package rs09.game.content.dialogue.region.miscellania

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Dialogue for the Fishmonger in Misc.
 * @author qmqz
 */

@Initializable
class GreengrocerMiscDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.FRIENDLY,"Welcome, Sir.",
            "I sell only the finest and freshest vegetables!").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage){
            0 -> {
                end().also { npc.openShop(player) }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GreengrocerMiscDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GREENGROCER_1394)
    }
}