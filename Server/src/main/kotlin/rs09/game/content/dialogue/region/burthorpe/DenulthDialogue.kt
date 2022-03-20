package rs09.game.content.dialogue.region.burthorpe

import api.openDialogue
import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import rs09.game.content.quest.members.deathplateau.DenulthDialogueFile

/**
 * @author qmqz
 */

@Initializable
class DenulthDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        openDialogue(player, DenulthDialogueFile())
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DenulthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DENULTH_1060)
    }
}
