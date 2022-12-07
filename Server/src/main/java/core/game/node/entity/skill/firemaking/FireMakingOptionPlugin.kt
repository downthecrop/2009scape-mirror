package core.game.node.entity.skill.firemaking

import core.game.node.item.GroundItem
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

class FiremakingListener : InteractionListener
{
    val logs = intArrayOf(1511, 1521, 1513, 1515, 1517, 1519, 2862, 3438, 3440, 3442, 3444, 3446, 3448, 6211, 6213, 6332, 6333, 7404, 7405, 7406, 8934, 9067, 10328, 10329, 10808, 10810, 10812, 11035, 12581, 12583, 3125)

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.TINDERBOX_590, *logs) { player, _, with ->
            player.pulseManager.run(FireMakingPulse(player, with.asItem(), null))
            return@onUseWith true
        }
        onUseWith(IntType.GROUNDITEM, Items.TINDERBOX_590, *logs) { player, _, with ->
            player.pulseManager.run(FireMakingPulse(player, with.asItem(), with as GroundItem))
            return@onUseWith true
        }
    }
}