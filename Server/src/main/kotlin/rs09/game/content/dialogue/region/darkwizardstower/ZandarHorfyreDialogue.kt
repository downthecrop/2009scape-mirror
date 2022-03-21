package rs09.game.content.dialogue

import api.teleport
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */
@Initializable
class ZandarHorfyreDialogue(player: Player? = null) : DialoguePlugin(player) {

    override fun newInstance(player: Player) : DialoguePlugin {
        return ZandarHorfyreDialogue(player)
    }

    override fun open(vararg args: Any?) : Boolean {
        npc = args[0] as NPC
        player(FacialExpression.HALF_THINKING,"Who are you?")
        stage = -1
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int) : Boolean {
        when (stage) {
            -1 -> npcl(FacialExpression.NEUTRAL,"My name is Zandar Horfyre, and you ${ player.name } are trespassing in my tower, not to mention attacking my students! I thank you to leave immediately!").also{ stage++ }
            0 -> options("Ok, I was going anyway.", "No, I think I'll stay for a bit.").also{ stage++ }
            1 -> when(buttonId) {
                1 -> player("Ok, I was going anyway.").also{ stage = 10 }
                2 -> player("No, I think I'll stay for a bit.").also{ stage = 20 }
            }

            10 -> npcl(FacialExpression.NEUTRAL,"Good! And don't forget to close the door behind you!").also{ stage++ }
            11 -> stage = END_DIALOGUE

            20 -> npcl(FacialExpression.ANNOYED,"Actually, that wasn't an invitation. I've tried being polite, now we'll do it the hard way!").also{ teleport(player, Location.create(3217, 3177, 0), TeleportManager.TeleportType.INSTANT) }.also{ stage++ }
            21 -> player(FacialExpression.ANGRY, "Zamorak curse that mage!").also{ stage++ }
            22 -> player(FacialExpression.LAUGH, "Actually, I guess he already has!").also{ stage++ }
            23 -> end()
        }
        return true
    }

    override fun getIds() : IntArray {
        return intArrayOf(3308)
    }
}