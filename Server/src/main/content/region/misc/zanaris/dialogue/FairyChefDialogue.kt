package content.region.misc.zanaris.dialogue

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
class FairyChefDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(core.game.dialogue.FacialExpression.OLD_DEFAULT,"'Ello, sugar. I'm afraid I can't gossip right now, I've got a cake in the oven.").also { stage = 99 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FairyChefDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FAIRY_CHEF_3322, NPCs.FAIRY_CHEF_3323)
    }
}
