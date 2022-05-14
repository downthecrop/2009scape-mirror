package rs09.game.content.quest.members.familycrest

import api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

class SwapGauntletsHelper {
    companion object {
        private val legalGauntlets = setOf(Items.COOKING_GAUNTLETS_775, Items.GOLDSMITH_GAUNTLETS_776, Items.CHAOS_GAUNTLETS_777, Items.FAMILY_GAUNTLETS_778)

        @JvmStatic
        fun swapGauntlets(player: Player, givingGauntletsId: Int): String {
            if (givingGauntletsId !in legalGauntlets) {
                throw IllegalArgumentException("givingGauntletsId not in list of legal gauntlets.")
            }
            if (inInventory(player, givingGauntletsId)) {
                val gauntletString = Item(givingGauntletsId).name
                return "You already have the $gauntletString."
            }
            var otherGauntlets = -1
            val otherPossibleGauntlets = legalGauntlets.toMutableSet()
            otherPossibleGauntlets.remove(givingGauntletsId)
            for (gauntletId in otherPossibleGauntlets) {
                if (inInventory(player, gauntletId))
                {
                    otherGauntlets = gauntletId
                }
            }
            if (otherGauntlets == -1) {
                return "You do not have the gauntlets with you in your inventory."
            }
            val fee = 25000
            val shouldBeFree = getAttribute(player, "family-crest:gauntlets", Items.FAMILY_GAUNTLETS_778) == Items.FAMILY_GAUNTLETS_778
            if (!shouldBeFree && !inInventory(player, Items.COINS_995, fee)) {
                return "You do not have enough coins."
            }
            if ((shouldBeFree || removeItem(player, Item(Items.COINS_995, fee))) && removeItem(player, otherGauntlets)) {
                addItem(player, givingGauntletsId)
                setAttribute(player, "/save:family-crest:gauntlets", givingGauntletsId)
            }
            return ""
        }
    }
}
