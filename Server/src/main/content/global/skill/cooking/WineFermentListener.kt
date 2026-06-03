package content.global.skill.cooking

import content.global.skill.cooking.fermenting.WineFermentingPulse
import core.api.addItem
import core.api.getDynLevel
import core.api.removeItem
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld.Pulser
import org.rs09.consts.Items

class WineFermentListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.GRAPES_1987, Items.JUG_OF_WATER_1937) { player, used, with ->
            if (getDynLevel(player, Skills.COOKING) < 35) {
                sendMessage(player, "You need a cooking level of 35 to do this.")
                return@onUseWith true
            }
            if (removeItem(player, used.id) && removeItem(player, with.id)) {
                addItem(player, Items.UNFERMENTED_WINE_1995)
                Pulser.submit(WineFermentingPulse(1, player))
                return@onUseWith true
            }
            return@onUseWith false
        }
    }
}