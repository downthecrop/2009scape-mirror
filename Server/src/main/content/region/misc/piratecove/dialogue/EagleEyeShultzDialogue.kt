package content.region.misc.piratecove.dialogue

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
class EagleEyeShultzDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "What do you do for fun on this ship? You know, when you're not doing pirate stuff.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "All sorts! Hide and seek, pin the patch on the pirate, walk the plank!").also { stage++ }
            1 -> playerl(core.game.dialogue.FacialExpression.WORRIED, "What a life! Wait a minute. 'Walk the plank'? Surely that's a bit dangerous?").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.LAUGH, "Well of course, but where's the fun without a few deaths?").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.HALF_THINKING,"I think I'll stick to Runelink.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return EagleEyeShultzDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EAGLE_EYE_SHULTZ_4542)
    }
}
