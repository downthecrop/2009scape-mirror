package rs09.game.content.dialogue.region.apeatoll.marim

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 * Does not include treasure trails dialogue
 */

@Initializable
class DagaDialogue(player: Player? = null) : DialoguePlugin(player){

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.OLD_DEFAULT,"Would you like to buy or sell some scimitars?").also { stage = 0 }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, please.", "No, thanks.", "Do you have any Dragon Scimitars in stock?").also { stage++ }

            1 -> when (buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Yes, please.").also { stage = 10 }
                2 -> player(FacialExpression.FRIENDLY, "No, thanks.").also { stage = END_DIALOGUE }
                3 -> player(FacialExpression.HALF_ASKING, "Do you have any Dragon Scimitars in stock?").also { stage = 30 }
            }

            10 -> end().also { npc.openShop(player) }

            30 -> npcl(FacialExpression.OLD_DEFAULT, "It just so happens I recently got a fresh delivery, do you want to buy one?").also { stage++ }
            31 -> options("Yes, please.", "No, thanks.").also { stage++ }

            32 -> when (buttonId) {
                1 -> if (inInventory(player, Items.COINS_995, 100000)) {
                    end()
                    removeItem(player, Item(Items.COINS_995, 100000), Container.INVENTORY)
                    addItem(player, Items.DRAGON_SCIMITAR_4587, 1)
                } else {
                    npcl(FacialExpression.OLD_NORMAL, "Sorry but you don't have enough to buy one, at the moment it costs 100,000 gold coins.").also { stage = END_DIALOGUE }
                }
                2 -> player(FacialExpression.FRIENDLY, "No thanks.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DagaDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DAGA_1434)
    }
}