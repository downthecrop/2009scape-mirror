package content.global.handlers.item.equipment.fistofguthixgloves

import core.api.toIntArray
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import kotlin.math.min

private val FOG_GLOVES = (Items.IRIT_GLOVES_12856..Items.EARTH_RUNECRAFTING_GLOVES_12865).toIntArray()
private val MAX_CHARGES = intArrayOf(100, 100, 100, 100, 1000, 1000, 1000, 1000, 1000, 1000)

/**
 * Listener for Fist of Guthix gloves inspect option.
 * @author RiL
 */
class FOGGlovesListener : InteractionListener {
    override fun defineListeners() {
        on(FOG_GLOVES, IntType.ITEM, "inspect") { player, node ->
            player.sendMessage("${node.name}: ${min(node.asItem().charge, MAX_CHARGES[node.id - Items.IRIT_GLOVES_12856])} charge left.")
            return@on true
        }
    }
}
