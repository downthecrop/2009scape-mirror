package content.region.misthalin.digsite.dialogue

import core.api.sendChat
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class EdWoodDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        sendChat(npc, arrayOf("Can't stop. Too busy.", "Wonder when I'll get paid.", "Is it lunch break yet?", "Hey I'm working here. I'm working here.", "This work isn't going to do itself.", "Ouch! That was my finger!").random())
        stage = END_DIALOGUE
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return EdWoodDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ED_WOOD_5964)
    }
}