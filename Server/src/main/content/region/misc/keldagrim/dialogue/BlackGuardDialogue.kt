package content.region.misc.keldagrim.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class BlackGuardDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        sendDialogue("The guard ignores you.").also { stage = 99 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BlackGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BLACK_GUARD_2130, NPCs.BLACK_GUARD_2131, NPCs.BLACK_GUARD_2132, NPCs.BLACK_GUARD_2133)
    }
}
