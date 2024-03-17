package content.region.misthalin.digsite.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class MuseumGuardDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            START_DIALOGUE -> npc(FacialExpression.FRIENDLY, "Hello there! Sorry, I can't stop to talk. I'm guarding this", "workman's gate. I'm afraid you can't come through here -", "you'll need to find another way around.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return MuseumGuardDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUSEUM_GUARD_5942)
    }
}