package rs09.game.content.dialogue.region.dorgeshuun

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * todo add teleporting when areas are complete
 */

@Initializable
class DartogDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_NORMAL,"Hello, surface-dweller.").also { stage = 0 }
        return true
    }

    private var fr = FacialExpression.FRIENDLY
    private var ask = FacialExpression.ASKING
    private var nor = FacialExpression.OLD_NORMAL

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> options("Who are you?", "Can you show me the way to the mine?", "Can you show me the way to Lumbridge Castle cellar?").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(ask, "Who are you?").also { stage = 10}
                2 -> npcl(nor, "Of course! You're always welcome in our mines!").also { stage = 20 }
                3 -> player(ask, "Can you show me the way to Lumbridge Castle cellar?").also { stage = 50 }
            }

            10 -> npcl(nor, "The council posted me here to guard this new tunnel. I can also give you directions through the tunnels. A hero like you is always welcome in our mines!").also { stage++ }
            11 -> options("Can you show me the way to the mine?", "Can you show me the way to Lumbridge Castle cellar?", "Maybe some other time").also { stage++ }
            12 -> when (buttonId) {
                1 -> player(ask, "Can you show me the way to the mine?").also { stage = 40 }
                2 -> player(ask, "Can you show me the way to Lumbridge Castle cellar?").also { stage = 50 }
                3 -> player(fr, "Maybe some other time.").also { stage = 99 }
            }

            20 -> {
                end()
                //move player to mine
            }

            40 -> {
                npcl(nor, "Of course! You're always welcome in our mines!").also { stage = 99 }
                //move player to mine
            }

            50 -> {
                npc(nor, "Of course!").also { stage = 99 }
                //move to lumby castle celler
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DartogDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DARTOG_4314)
    }
}
