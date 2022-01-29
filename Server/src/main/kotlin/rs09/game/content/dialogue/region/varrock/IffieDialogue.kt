package rs09.game.content.dialogue.region.varrock

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Iffie
 * @author afaroutdude
 */
@Initializable
class IffieDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        npc("Sorry, dearie, if I stop to chat I'll lose count.", "Talk to my sister instead; she likes to chat.", "You'll find her upstairs in the church.")
        stage = 999;
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            999 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return IffieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(5914)
    }
}