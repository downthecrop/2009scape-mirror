package rs09.game.content.dialogue.region.dwarvenmine

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
class DwarvenMineGuardDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        player(FacialExpression.FRIENDLY,"Hello.").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npcl(FacialExpression.OLD_ANGRY1, "Don't distract me while I'm on duty! This mine has to be protected!").also { stage++ }
            1 -> player(FacialExpression.FRIENDLY, "What's going to attack a mine?").also { stage++ }
            2 -> npcl(FacialExpression.OLD_ANGRY1, "Goblins! They wander everywhere, attacking anyone they think is small enough to be an easy victim. We need more cannons to fight them off properly.").also { stage++ }
            3 -> player(FacialExpression.FRIENDLY, "Well, I've done my bit to help with that.").also { stage++ }
            4 -> npcl(FacialExpression.OLD_ANGRY1, "Yes, I heard. Now please let me get on with my guard duties.").also { stage++ }
            5 -> player(FacialExpression.FRIENDLY, "Alright, I'll leave you alone now.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarvenMineGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_206)
    }
}
