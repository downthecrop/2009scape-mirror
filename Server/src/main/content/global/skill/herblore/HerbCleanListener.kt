package content.global.skill.herblore

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import java.util.*

/**
 * Dirty herb cleaning listener
 * @author Woah
 */
class HerbCleanListener : InteractionListener {
    override fun defineListeners() {
        on(IntType.ITEM, "clean") { player, node ->
            lock(player, 1)
            if (!requireQuest(player, "Druidic Ritual", "before you can use Herblore.")) return@on true
            val herb: Herbs = Herbs.forItem(node as Item) ?: return@on true

            if (getDynLevel(player, Skills.HERBLORE) < herb.level) {
                sendMessage(player, "You cannot clean this herb. You need a Herblore level of " + herb.level + " to attempt this.")
                return@on true
            }

            val exp = herb.experience
            replaceSlot(player, node.asItem().slot, herb.product, node.asItem())
            rewardXP(player, Skills.HERBLORE, exp)
            playAudio(player, 5153)
            sendMessage(player, "You clean the dirt from the " + herb.product.name.lowercase(Locale.getDefault()).replace("clean", "").trim { it <= ' ' } + " leaf.")
            return@on true
        }
    }
}