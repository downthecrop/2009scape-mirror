package content.region.fremennik.jatizso.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs
import core.tools.END_DIALOGUE

@Initializable
class GruvaPatrullDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {
    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return GruvaPatrullDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(core.game.dialogue.FacialExpression.FRIENDLY,"Ho! Outerlander.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "What's down the scary-looking staircase?").also { stage++ }
            1 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "These are the stairs down to the mining caves. There are rich veins of many types down there, and miners too. Though be careful; some of the trolls occasionally sneak into the far end of the cave.").also { stage++ }
            2 -> playerl(core.game.dialogue.FacialExpression.NEUTRAL, "Thanks. I'll look out for them.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GRUVA_PATRULL_5500)
    }

}