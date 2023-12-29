package content.region.kandarin.feldip.gutanoth.handlers

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import content.global.skill.summoning.SummoningPouch
import content.global.skill.summoning.SummoningScroll
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Handles swapping pouches/scrolls for shards.
 * We decided to use the GE set exchange interface for this because it's the only one we could find
 * that allowed us to do what we needed. Jagex did the same thing as far as we can tell.
 * @author Ceikry
 */
object BogrogPouchSwapper {
    //Opcodes for item options
    private const val OP_VALUE = 0
    private const val OP_SWAP_1 = 1
    private const val OP_SWAP_5 = 2
    private const val OP_SWAP_10 = 3
    private const val OP_SWAP_X = 4

    private const val SPIRIT_SHARD = 12183

    //GE Zone borders because shitty hack lol
    private val GEBorders = ZoneBorders(3151,3501,3175,3477)

    @JvmStatic
    fun handle(player: Player, optionIndex: Int, slot: Int): Boolean{
        val item = player.inventory.get(slot) ?: return false
        return when(optionIndex){
            OP_VALUE   -> sendValue(item.id,player)
            OP_SWAP_1  -> swap(player,  1, item.id)
            OP_SWAP_5  -> swap(player,  5, item.id)
            OP_SWAP_10 -> swap(player, 10, item.id)
            OP_SWAP_X  -> true.also{
                sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:"){value ->
                    swap(player, value as Int, item.id)
                }
            }
            else -> false
        }
    }

    private fun swap(player: Player, amount: Int, itemID: Int): Boolean{
        var amt = amount
        val value = getValue(itemID)
        if(value == 0.0){
            return false
        }
        val inInventory = player.inventory.getAmount(itemID)
        if(amount > inInventory)
            amt = inInventory
        player.inventory.remove(Item(itemID,amt))
        player.inventory.add(Item(SPIRIT_SHARD, floor(value * amt).toInt()))
        return true
    }

    private fun sendValue(itemID: Int, player: Player): Boolean{
        val value = getValue(itemID)
        if(value == 0.0){
            return false
        }

        player.sendMessage("Bogrog will give you ${floor(value).toInt()} shards for that.")
        return true
    }

    private fun getValue(itemID: Int): Double{
        var item = SummoningPouch.get(itemID)
        var isScroll = false
        if(item == null) item = SummoningPouch.get(Item(itemID).noteChange)
        if(item == null) item = SummoningPouch.get(SummoningScroll.forItemId(itemID)?.pouch ?: -1).also { isScroll = true }
        item ?: return 0.0
        var shardQuantity = item.items[item.items.size - 1].amount * 0.7
        return if(isScroll) shardQuantity / 20.0 else ceil(shardQuantity)
    }
}
