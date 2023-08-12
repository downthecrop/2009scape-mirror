package content.region.kandarin.ardougne.dialogue

import content.region.kandarin.ardougne.quest.clocktower.BrotherKojoDialogueFile
import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Brother Kojo main dialogue.
 * @author ovenbread
 */
@Initializable
class BrotherKojoDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Clocktower quest
        openDialogue(player!!, BrotherKojoDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return BrotherKojoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BROTHER_KOJO_223)
    }
}