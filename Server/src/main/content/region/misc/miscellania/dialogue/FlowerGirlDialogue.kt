package content.region.misc.miscellania.dialogue

import core.api.addItemOrDrop
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.NPCs

/**
 * Dialogue for the Flower Girl in Misc.
 * @author qmqz
 */

@Initializable
class FlowerGirlDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        //issues getting throne of miscellania status
        /*
        when (player.questRepository.getQuest("Throne of Miscellania").isCompleted(player)) {
            true -> npc(FacialExpression.HAPPY, "Good day, Your Royal Highness.").also { stage = 1 }
            false -> npc(FacialExpression.NEUTRAL, "Hello.").also { stage = 1 }
        }
        */
        npc(core.game.dialogue.FacialExpression.NEUTRAL, "Hello.").also { stage = 1 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            1 -> {
                player(core.game.dialogue.FacialExpression.ASKING, "Good day. What are you doing?").also { stage++ }
            }

            2 -> {
                /*
                when (player.questRepository.getQuest("Throne of Miscellania").isCompleted(player)) {
                    true -> npc(FacialExpression.HAPPY, "I'm selling flowers, 15gp for three. Would you like some, Your Highness?").also { stage++ }
                    false -> npc(FacialExpression.NEUTRAL, "I'm selling flowers, 15gp for three. Would you like some?").also { stage++ }
                }
                 */
                npc(core.game.dialogue.FacialExpression.NEUTRAL, "I'm selling flowers, 15gp for three. Would you like some?").also { stage++ }
            }

            3 -> {
                options("Yes, please.", "No, thank you.").also { stage++ }
            }

            4 -> when(buttonId){
                1 -> {
                    if (player.inventory.contains(995,15)) {
                            npc(core.game.dialogue.FacialExpression.HAPPY, "Thank you! Here you go.").also { stage = 99 }
                            player.inventory.remove(Item(995, 15))
                            addItemOrDrop(player, 2460, 1)
                    } else {
                        player(core.game.dialogue.FacialExpression.HALF_THINKING, "I'm sorry, but I don't have 15gp.").also { stage = 99 }
                    }
                }
                2 -> {
                    player(core.game.dialogue.FacialExpression.NEUTRAL, "No, thank you.").also { stage = 99 }
                }
            }

            99 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FlowerGirlDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FLOWER_GIRL_1378)
    }
}