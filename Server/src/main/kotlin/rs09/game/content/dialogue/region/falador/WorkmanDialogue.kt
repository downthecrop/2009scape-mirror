package rs09.game.content.dialogue.region.falador

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs.WORKMAN_3236

/**
 * @author qmqz
 */

@Initializable
class WorkmanDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hiya.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(FacialExpression.ASKING, "What do you want? I've got work to do!").also { stage++ }
            }

            1 -> {
                player(FacialExpression.ASKING, "Can you teach me anything?").also { stage++ }
            }

            2 -> {
                npcl(FacialExpression.ANNOYED, "No - I've got one lousy apprentice already, and that's quite enough hassle! Go away!").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return WorkmanDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(WORKMAN_3236)
    }
}