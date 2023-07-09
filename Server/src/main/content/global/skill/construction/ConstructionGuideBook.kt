package content.global.skill.construction

import core.api.sendMessage
import core.game.dialogue.DialogueInterpreter
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import org.rs09.consts.Items

/**
 * Construction Guide Book
 * Used to be book:conguide?
 * @author ovenbreado
 * @author Emperor
 */

class ConstructionGuideBook : InteractionListener {
    companion object {
        private val TITLE = "Construction guide book"
        /** The resources used in beta. */
        private val RESOURCES = arrayOf(
            Item(8794),
            Item(2347),
            Item(4819, 500000),
            Item(4820, 500000),
            Item(1539, 500000),
            Item(4821, 500000),
            Item(4822, 500000),
            Item(4823, 500000),
            Item(4824, 500000),
            Item(961, 500000),
            Item(8779, 500000),
            Item(8781, 500000),
            Item(8783, 500000),
            Item(8791, 500000),
            Item(8785, 500000),
            Item(8787, 500000),
            Item(8789, 500000),  // 16
            Item(8418, 500000),
            Item(8420, 500000),
            Item(8422, 500000),
            Item(8424, 500000),
            Item(8426, 500000),
            Item(8428, 500000),
            Item(8430, 500000),
            Item(8432, 500000),
            Item(8434, 500000),
            Item(8436, 500000)
        )
    }

    override fun defineListeners() {
        // There is supposedly a book here.
        on(Items.CONSTRUCTION_GUIDE_8463, IntType.ITEM, "read") { player, _ ->
            if (settings!!.isDevMode && settings!!.isBeta) {
                for (item in RESOURCES) {
                    if (!player.inventory.contains(item.id, item.amount)) {
                        player.inventory.add(item, player)
                    }
                }
            }
            sendMessage(player, "Upon reading the book you discover you're supposed to use these resources to test out construction. Report all bugs on the forums.")
            return@on true
        }
    }

}