package rs09.game.interaction.item.withitem

import api.*
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class OilFishingRodListener : InteractionListener() {
    override fun defineListeners() {
        onUseWith(ITEM, Items.BLAMISH_OIL_1582, Items.FISHING_ROD_307) {player, used, with ->
            player.pulseManager.run(object : Pulse() {
                var counter = 0
                override fun pulse(): Boolean {
                    when (counter++) {
                        1 -> {
                            removeItem(player, used.asItem()) && removeItem(player, with.asItem())
                            addItem(player, Items.VIAL_229)
                            addItem(player, Items.OILY_FISHING_ROD_1585)
                            sendMessage(player, "You rub the oil into the fishing rod.")
                        }
                    }
                    return false
                }
            })
            return@onUseWith true
        }

        onUseWith(ITEM, Items.THIN_SNAIL_3363, Items.PESTLE_AND_MORTAR_233) {player, used, with ->
            if (player.inventory.contains(Items.SAMPLE_BOTTLE_3377, 1)) {
                player.pulseManager.run(object : Pulse() {
                    var counter = 0
                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(Animation(364))
                            3 -> {
                                removeItem(player, Items.THIN_SNAIL_3363)
                                removeItem(player, Items.SAMPLE_BOTTLE_3377)
                                addItem(player, Items.BLAMISH_SNAIL_SLIME_1581)
                            }
                        }
                        return false
                    }
                })
            }
            return@onUseWith true
        }
    }
}