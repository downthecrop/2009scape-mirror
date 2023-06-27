package content.region.asgarnia.burthorpe.dialogue

import content.region.asgarnia.burthorpe.quest.deathplateau.DenulthDialogueFile
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Denulth main dialogue.
 * @author ovenbread
 */
@Initializable
class DenulthDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Death Plateau
        openDialogue(player!!, DenulthDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DenulthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DENULTH_1060)
    }
}