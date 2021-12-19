package rs09.game.interaction.item.withobject

import api.*
import core.game.node.entity.skill.gather.woodcutting.WoodcuttingNode
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class FishEasterEgg : InteractionListener() {
    val TREE_IDs = WoodcuttingNode.values().map { it.id }.toIntArray()

    val fish = intArrayOf(Items.RAW_HERRING_345, Items.HERRING_347)
    val doors = intArrayOf(1967,1968)

    override fun defineListeners() {
        onUseWith(SCENERY, fish, *TREE_IDs){player, _, _ ->
            sendMessage(player, "This is not the mightiest tree in the forest.")
            return@onUseWith true
        }

        onUseWith(SCENERY, fish, *doors){player, _, _ ->
            sendMessage(player, "It can't be done!")
            return@onUseWith true
        }
    }
}