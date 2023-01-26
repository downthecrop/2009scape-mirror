package content.global.ame.events.drilldemon

import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class SeargentDamienDefaultPlugin(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
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