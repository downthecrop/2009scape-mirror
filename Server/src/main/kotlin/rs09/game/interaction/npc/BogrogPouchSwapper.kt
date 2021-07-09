package rs09.game.interaction.npc

import api.ContentAPI
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.RunScript
import core.game.node.entity.skill.summoning.SummoningPouch
import core.game.node.entity.skill.summoning.SummoningScroll
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import kotlin.math.ceil

/**
 * Handles swapping pouches/scrolls for shards.
 * We decided to use the GE set exchange interface for this because it's the only one we could find
 * that allowed us to do what we needed. Jagex did the same thing as far as we can tell.
 * @author Ceikry
 */
object BogrogPouchSwapper {
    //Opcodes for item options
    private const val OP_VALUE = 155
    private const val OP_SWAP_1 = 196
    private const val OP_SWAP_5 = 124
    private const val OP_SWAP_10 = 199
    private const val OP_SWAP_X = 234

    private const val SPIRIT_SHARD = 12183

    //GE Zone borders because shitty hack lol
    private val GEBorders = ZoneBorders(3151,3501,3175,3477)

    @JvmStatic
    fun handle(player: Player, opcode: Int, slot: Int, itemID: Int): Boolean{
        if(GEBorders.insideBorder(player)) return false
        return when(opcode){
            OP_VALUE   -> sendValue(player.inventory.get(slot).id,player)
            OP_SWAP_1  -> swap(player, 1, player.inventory.get(slot).id)
            OP_SWAP_5  -> swap(player, 5, player.inventory.get(slot).id)
            OP_SWAP_10 -> swap(player,10,player.inventory.get(slot).id)
            OP_SWAP_X  -> true.also{
                ContentAPI.sendInputDialogue(player, true, "Enter the amount:"){value ->
                    swap(player, value as Int, player.inventory.get(slot).id)
                }
            }
            else -> false
        }
    }

    private fun swap(player: Player, amount: Int, itemID: Int): Boolean{
        var amt = amount
        val value = getValue(itemID)
        if(value == 0){
            return false
        }
        val inInventory = player.inventory.getAmount(itemID)
        if(amount > inInventory)
            amt = inInventory
        player.inventory.remove(Item(itemID,amt))
        player.inventory.add(Item(SPIRIT_SHARD,value * amt))
        player.interfaceManager.close(Component(644))
        return true
    }

    private fun sendValue(itemID: Int, player: Player): Boolean{
        val value = getValue(itemID)
        if(value == 0){
            return false
        }

        player.sendMessage("Bogrog will give you $value shards for that.")
        return true
    }

    private fun getValue(itemID: Int): Int{
        var item = SummoningPouch.get(itemID)
        if(item == null) item = SummoningPouch.get(Item(itemID).noteChange)
        if(item == null) item = SummoningPouch.get(SummoningScroll.forItemId(itemID)?.pouch ?: -1)
        item ?: return 0
        return ceil(item.items[item.items.size - 1].amount * 0.7).toInt()
    }
}