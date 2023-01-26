package content.region.misc.apeatoll.dialogue.marim

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * quest dialogue is missing on rs3 and osrs, someone will need to youtube it
 */

@Initializable
class MuruwoiDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.OLD_ANGRY1,"Grr ... Get out of my way...")
        stage = 99
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return MuruwoiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MURUWOI_1450)
    }
}