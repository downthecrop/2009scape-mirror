package rs09.game.content.dialogue.region.craftingguild

import api.*
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

@Initializable
class MasterCrafterDialogue(player: Player? = null) : DialoguePlugin(player) {

    var CAPE = Items.CRAFTING_CAPE_9780
    var COIN = Items.COINS_995

    override fun newInstance(player: Player) : DialoguePlugin {
        return MasterCrafterDialogue(player)
    }

    override fun open(vararg args: Any?) : Boolean {
        npc = args[0] as NPC
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int) : Boolean {
        when(stage) {
            0 -> if(hasLevelStat(player, Skills.CRAFTING, 99)) {
                    player(FacialExpression.ASKING, "Hey, could I buy a Skillcape of Crafting?").also{ stage = 10 }
                 } else {
                    player(FacialExpression.ASKING,"Hey, what is that cape you're wearing? I don't recognize it.").also { stage++ }
            }
            1 -> npcl(FacialExpression.FRIENDLY, "This? This is a Skillcape of Crafting. It is a symbol of my ability " +
                    "and standing here in the Crafting Guild. If you should ever achieve level 99 Crafting come and talk to me " +
                    "and we'll see if we can sort you out with one.").also{ stage = END_DIALOGUE }
            10 -> npcl(FacialExpression.HAPPY, "Certainly! Right after you pay me 99000 coins.").also{ stage++ }
            11 -> options("Okay, here you go.", "No thanks.").also{ stage++ }
            12 -> when(buttonId) {
                1 -> player(FacialExpression.FRIENDLY, "Okay, here you go.").also{ stage++ }
                2 -> player(FacialExpression.HALF_THINKING, "No, thanks.").also{ stage = END_DIALOGUE }
            }
            13 -> if(inInventory(player, COIN, 99000)) {
                removeItem(player, Item(COIN, 99000), Container.INVENTORY)
                addItem(player, CAPE, 1)
                npcl(FacialExpression.HAPPY, "There you go! Enjoy.").also{ stage = END_DIALOGUE }
            } else {
                npcl(FacialExpression.NEUTRAL, "You don't have enough coins for a cape.").also{ stage = END_DIALOGUE }
            }
            20 -> npcl(FacialExpression.NEUTRAL, "Where's your brown apron? You can't come in here unless you're wearing one.").also{ stage++ }
            21 -> player(FacialExpression.HALF_GUILTY, "Err... I haven't got one.").also{ stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds() : IntArray {
        return intArrayOf(NPCs.MASTER_CRAFTER_805)
    }

}