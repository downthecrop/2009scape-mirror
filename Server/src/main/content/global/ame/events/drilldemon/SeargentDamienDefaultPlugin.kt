package content.global.ame.events.drilldemon

import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class SeargentDamienDefaultPlugin(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return SeargentDamienDefaultPlugin(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc("GET BACK TO WORK MAGGOT!")
        stage = END_DIALOGUE
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SERGEANT_DAMIEN_2790)
    }

}