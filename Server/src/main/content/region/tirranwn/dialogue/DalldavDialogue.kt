package content.region.tirranwn.dialogue

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
class DalldavDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.FRIENDLY,"Can I help you at all?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes please. What are you selling?", "Why do you sell this stuff?", "No thanks.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "Yes please. What are you selling?").also { stage = 10 }
                2 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Why do you sell this stuff? The Crystal Bow is so much better.").also { stage = 20 }
                3 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "No thanks.").also { stage = 99 }
            }

            10 -> end().also { npc.openShop(player) }

            20 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "We keep all these old toys to train our children with, but if people will part with coins for them, then they are theirs!").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return DalldavDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DALLDAV_2356)
    }
}
