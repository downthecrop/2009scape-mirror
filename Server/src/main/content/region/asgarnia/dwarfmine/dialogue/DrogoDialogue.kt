package content.region.asgarnia.dwarfmine.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class DrogoDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"'Ello. Welcome to my Mining shop, friend.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Do you want to trade?", "Hello, shorty.", "Why don't you ever restock ores and bars?").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Do you want to trade?").also { stage = 10 }
                2 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Hello, shorty.").also { stage = 20 }
                3 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Why don't you ever restock ores and bars?").also { stage = 30 }
            }

            10 -> end().also { npc.openShop(player) }
            20 -> npc(core.game.dialogue.FacialExpression.OLD_ANGRY1,"I may be short, but at least I've got manners.").also { stage = 99 }
            30 -> npc(core.game.dialogue.FacialExpression.OLD_DEFAULT,"The only ores and bars I sell are those sold to me.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return DrogoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DROGO_DWARF_579)
    }
}
