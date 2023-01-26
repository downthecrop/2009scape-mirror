package content.region.desert.sophanem.dialogue

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
class EmbalmerDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(core.game.dialogue.FacialExpression.SUSPICIOUS,"What are you doing?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "I have this acne potion which I thought might help ease your itching.").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.ANNOYED, "If I thought that these spots could be cured by some potion, I would have mixed up one myself before now.").also { stage++ }
            2 -> player(core.game.dialogue.FacialExpression.SAD, "Sorry, I was just trying to help.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return EmbalmerDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EMBALMER_1980)
    }
}
