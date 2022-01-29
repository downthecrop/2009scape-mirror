package rs09.game.content.dialogue.region.falador

import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 */

@Initializable
class DrunkenManDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> {
                npc(FacialExpression.DRUNK, "... whassup?").also { stage++ }
            }

            1 -> {
                player(FacialExpression.ASKING, "Are you alright?").also { stage++ }
            }

            2 -> {
                npc(FacialExpression.DRUNK, "... see... two of you... why there two of you?").also { stage++ }
            }

            3 -> {
                player(FacialExpression.FRIENDLY, "There's only one of me, friend.").also { stage++ }
            }

            4 -> {
                npc(FacialExpression.DRUNK, "... no, two of you... you can't count...",
                    "... maybe you drunk too much...").also { stage++ }
            }

            5 -> {
                player(FacialExpression.FRIENDLY, "Whatever you say, friend.").also { stage++ }
            }

            6 -> {
                npc(FacialExpression.DRUNK, "... giant hairy cabbages...").also { stage = 99 }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DrunkenManDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DRUNKEN_MAN_3222)
    }
}