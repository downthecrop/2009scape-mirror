package content.region.fremennik.lunarisle.dialogue

import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * @author qmqz
 * Does not include Lunar Diplomacy dialogue
 */

@Initializable
class PaulinePolarisDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(core.game.dialogue.FacialExpression.FRIENDLY,"Hello there.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.ASKING, "Ah, a stranger to our island. How can I help?").also { stage ++ }
            1 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "Well, I've actually come here on a diplomatic mission. I want to try and settle some of the disputes between the Fremenniks and your clan.").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "The Rremenniks? Pah! They are just too ignorant and stubborn to listen to anything we have to say - how can we possibly associate with a race that won't listen?").also { stage++ }
            3 -> playerl(core.game.dialogue.FacialExpression.FRIENDLY, "I think they are very eager to listen, but they feel like you are keeping secrets from them.").also { stage++ }
            4 -> npc(core.game.dialogue.FacialExpression.HALF_THINKING, "Secrets?").also { stage++ }
            5 -> playerl(core.game.dialogue.FacialExpression.HALF_ASKING, "Yes, as in your magic ways. To be honest, I think it is fear of the unknown.").also { stage++ }
            6 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Well, when the day comes that a Fremennik can prove they have the patience and interest in learning our ways, then we will perhaps share our secrets.").also { stage++ }
            7 -> player(core.game.dialogue.FacialExpression.FRIENDLY, "I'm hoping I might be able to achieve that.").also { stage++ }
            8 -> npc(core.game.dialogue.FacialExpression.FRIENDLY, "Good luck. It's far from simple.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return PaulinePolarisDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PAULINE_POLARIS_4514)
    }
}