package content.region.misthalin.silvarea.dialogue

import content.region.misthalin.silvarea.quest.ragandboneman.OddOldManDialogueFile
import core.api.openDialogue
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

@Initializable
class OddOldManDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // Fallback to default. Always the start of Rag and Bone Man
        openDialogue(player!!, OddOldManDialogueFile(), npc)
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return OddOldManDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ODD_OLD_MAN_3670)
    }
}