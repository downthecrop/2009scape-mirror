package content.region.morytania.werewolfagility

import core.api.anyInEquipment
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * @author qmqz
 * https://www.youtube.com/watch?v=9u_qJW1eKR0
 */

@Initializable
class AgilityBossDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if(anyInEquipment(player, Items.RING_OF_CHAROS_4202, Items.RING_OF_CHAROSA_6465)) {
            player(FacialExpression.ASKING,"How do I use the agility course?").also { stage = 0 }
        } else {
            npc(FacialExpression.WEREWOLF_NEUTRAL,"Grrr - you don't belong in here, human!").also { stage = 99 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.WEREWOLF_NEUTRAL,"I'll throw you a stick, which you need to",
                "fetch as quickly as possible, ",
                "from the area beyond the pipes.").also { stage++ }

            1 -> npc(FacialExpression.WEREWOLF_NEUTRAL,"Be wary of the deathslide - you must hang by your teeth,",
                "and if your strength is not up to the job you will",
                "fall into a pit of spikes. Also, I would advise not",
                "carrying too much extra weight.").also { stage++ }

            2 -> npc(FacialExpression.WEREWOLF_NEUTRAL,"Bring the stick back to the werewolf waiting at",
                "the end of the death slide to get your agility bonus.").also { stage++ }

            3 ->npc(FacialExpression.WEREWOLF_NEUTRAL,"I will throw your stick as soon as you jump onto the",
                "first stone.").also { stage = 99 }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AgilityBossDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(1661)
    }
}