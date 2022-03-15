package rs09.game.interaction.item

import api.*
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

/**
 * @author bushtail
 */

class AbyssalBookListener : InteractionListener() {

    val BOOK = Items.ABYSSAL_BOOK_5520

    override fun defineListeners() {
        on(BOOK, ITEM, "read") { player, _ ->
            if(hasLevelStat(player, Skills.RUNECRAFTING, 99)) {
                sendMessage(player, "Nothing interesting happens.")
                return@on false
            }  else {
                rewardXP(player, Skills.RUNECRAFTING, 1000.00)
                removeItem(player, BOOK, Container.INVENTORY)
                return@on true
            }
            return@on false
        }
    }
}