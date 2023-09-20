package content.region.misc.keldagrim.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class DwarfsDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> {
                var random = arrayOf(1, 2, 3, 4, 5).random()
                when (random) {
                    1 -> npcl(FacialExpression.OLD_DEFAULT, "I hope the goblins are properly maintaining their stretch of track! I'd hate to see them spoil good Dwarven workmanship.").also { stage = END_DIALOGUE }
                    2 -> npcl(FacialExpression.OLD_DEFAULT, "You're ${player.username}, aren't you? It's a good job you did helping to get this train link open.").also { stage = END_DIALOGUE }
                    3 -> npcl(FacialExpression.OLD_DEFAULT, "Y'know, at first I didn't like the idea of visiting a goblin city, but these cave goblins are alright.").also { stage = END_DIALOGUE }
                    4 -> npcl(FacialExpression.OLD_DEFAULT, "These goblins have so much more advanced technology than us. Take a look at their magical lights! We could never create something like them.").also { stage = END_DIALOGUE }
                    5 -> npcl(FacialExpression.OLD_DEFAULT, "Dorgesh-Kaan's very nice to visit, but I don't think I'd want to live there.").also { stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarfsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARF_5880, NPCs.DWARF_5881, NPCs.DWARF_5882, NPCs.DWARF_5883, NPCs.DWARF_5884, NPCs.DWARF_5885)
    }
}
