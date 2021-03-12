package rs09.game.content.quest.members.thelosttribe

import core.game.content.dialogue.DialoguePlugin
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

@Initializable
/**
 * Dialogue for Kazgar
 * @author Ceikry
 */
class KazgarDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return KazgarDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        options("Who are you?","Can you show me to the mine?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
             0 -> when (buttonId) {
                    1 -> player("Who are you?").also { stage = 10 }
                    2 -> player("Can you show me the way to the other side?").also { stage = 20 }
                }
             10 -> npc("I'm Kazgar, I guide people through the mines.").also { stage = 1000 }
             20 -> npc("Certainly!").also { stage++ }
             21 -> end().also {
                    GoblinFollower.sendToMines(player)
             }

            1000 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(2085)
    }

}