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
class NurmofDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.OLD_NORMAL,"Greetings and welcome to my pickaxe shop. Do you want to buy my premium quality pickaxes?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, please.", "No, thank you.", "Are your pickaxes better than other pickaxes, then?").also { stage++ }

            1 -> when (buttonId) {
                1 -> end().also { npc.openShop(player) }
                2 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "No thank you.").also { stage = 99 }
                3 -> player(core.game.dialogue.FacialExpression.HALF_ASKING, "Are your pickaxes better than other pickaxes, then?").also { stage = 10 }
            }

            10 -> npcl(core.game.dialogue.FacialExpression.OLD_NORMAL,"Of course they are! My pickaxes are made of higher grade metal than your ordinary bronze pickaxes, allowing you to mine ore just that little bit faster.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return NurmofDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.NURMOF_594)
    }
}
