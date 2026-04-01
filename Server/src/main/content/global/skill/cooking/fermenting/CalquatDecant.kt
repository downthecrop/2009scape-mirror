package content.global.skill.cooking.fermenting

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items

object CalquatDecant {

    // Consumables.java hurts my eyes so I did this instead
    data class CalquatKegs (
        val emptyKeg: Int = Items.CALQUAT_KEG_5769,
        val fourDose: Int, val threeDose: Int = fourDose - 2, val twoDose: Int = threeDose - 2, val oneDose: Int = twoDose - 2, val glass: Int,
        val fourMature: Int, val threeMature: Int = fourMature - 2, val twoMature: Int = threeMature - 2, val oneMature: Int = twoMature - 2, val matureGlass: Int
    )

    val calquatDrinks =
        Brewable.values()
            .filter { it.product.size == 4 }
            .associateWith { CalquatKegs (
                fourDose = it.product[1],
                glass = it.product[0],
                fourMature = it.product[3],
                matureGlass = it.product[2]
            )
        }

    // Array of all kegs with nonzero contents
    val allKegIds: IntArray by lazy {
        calquatDrinks.values.flatMap { keg ->
            listOf(
                keg.fourDose, keg.threeDose, keg.twoDose, keg.oneDose,
                keg.fourMature, keg.threeMature, keg.twoMature, keg.oneMature
            )
        }.distinct().toIntArray()
    }

    // Reverse lookup map
    private val kegLookupMap: Map<Int, Pair<Brewable, CalquatKegs>> by lazy {
        calquatDrinks.entries.flatMap { (brewable, kegs) ->
            listOf(
                kegs.fourDose, kegs.threeDose, kegs.twoDose, kegs.oneDose,
                kegs.fourMature, kegs.threeMature, kegs.twoMature, kegs.oneMature
            ).map { itemId -> itemId to (brewable to kegs) }
        }.toMap()
    }

    // Check what a keg is so it can be related to other kegs
    fun getKeg(itemId: Int): Pair<Brewable, CalquatKegs>? = kegLookupMap[itemId]

    // Check if an item is a keg
    fun isKeg(itemId: Int): Boolean = kegLookupMap.containsKey(itemId)

    // Check how full a keg is
    fun getDoseByKeg(itemId: Int, kegs: CalquatKegs): Int {
        return when (itemId) {
            kegs.fourDose, kegs.fourMature -> 4
            kegs.threeDose, kegs.threeMature -> 3
            kegs.twoDose, kegs.twoMature -> 2
            kegs.oneDose, kegs.oneMature -> 1
            else -> 0
        }
    }

    // Check if a keg contains matured ale
    fun isMature(itemId: Int, kegs: CalquatKegs): Boolean {
        return itemId == kegs.fourMature || itemId == kegs.threeMature ||
               itemId == kegs.twoMature || itemId == kegs.oneMature
    }

    // Identifies a keg's category by its contents
    fun getKegByDose(kegs: CalquatKegs, dose: Int, mature: Boolean): Int {
        return if (!mature) {
            when (dose) {
                4 -> kegs.fourDose
                3 -> kegs.threeDose
                2 -> kegs.twoDose
                1 -> kegs.oneDose
                else -> kegs.emptyKeg
            }
        } else {
            when (dose) {
                4 -> kegs.fourMature
                3 -> kegs.threeMature
                2 -> kegs.twoMature
                1 -> kegs.oneMature
                else -> kegs.emptyKeg
            }
        }
    }

    // Decanting function (NPC) - should work the same as potions
    fun decantCalquat(container: core.game.container.Container): Pair<List<Item>, List<Item>> {
        val doseCount = HashMap<Pair<Brewable, Boolean>, Int>() // (Brewable, isMature), Total Doses
        val toRemove = ArrayList<Item>()
        val toAdd = ArrayList<Item>()

        // Count up all doses grouped by keg type and maturity
        for (item in container.toArray()) {
            if (item == null) continue
            val kegInfo = getKeg(item.id) ?: continue
            val (brewable, kegs) = kegInfo
            val dose = getDoseByKeg(item.id, kegs)
            if (dose == 0) continue
            val mature = isMature(item.id, kegs)
            val key = brewable to mature
            doseCount[key] = (doseCount[key] ?: 0) + dose
            toRemove.add(item)
        }

        // Convert doses back into kegs
        for ((key, totalDoses) in doseCount) {
            val (brewable, mature) = key
            val kegs = calquatDrinks[brewable] ?: continue

            val fullKegs = totalDoses / 4  // Number of 4-dose kegs
            val remainderDose = totalDoses % 4  // Leftover doses

            if (fullKegs > 0)
                toAdd.add(Item(getKegByDose(kegs, 4, mature), fullKegs))
            if (remainderDose > 0)
                toAdd.add(Item(getKegByDose(kegs, remainderDose, mature)))
        }

        // Add empty kegs for removed containers
        val emptyKegs = toRemove.size - toAdd.sumOf { it.amount }
        if (emptyKegs > 0) {
            val firstKegs = calquatDrinks.values.firstOrNull()
            if (firstKegs != null)
                toAdd.add(Item(firstKegs.emptyKeg, emptyKegs))
        }

        return Pair(toRemove, toAdd)
    }

    // Decanting function (manual) - should work the same as potions
    fun combineKegs(player: Player, used: Item, with: Item): Boolean {
        // Identify both kegs
        val (usedBrewable, kegs) = getKeg(used.id) ?: return false
        val (withBrewable, _) = getKeg(with.id) ?: return false

        // Verify the same drink is in both kegs
        if (usedBrewable != withBrewable) return false

        val usedMature = isMature(used.id, kegs)
        val withMature = isMature(with.id, kegs)

        // Verify the same maturity status is in both kegs
        if (usedMature != withMature) return false

        // Get doses
        val usedDose = getDoseByKeg(used.id, kegs)
        val withDose = getDoseByKeg(with.id, kegs)

        // Disallow combining full or empty kegs
        if (usedDose == 4 || withDose == 4 || usedDose == 0 || withDose == 0) {
            return false
        }

        val totalDosage = usedDose + withDose
        val fullDoses = totalDosage / 4
        val leftoverDose = totalDosage % 4

        // Replace the targeted keg with a (4) dose keg
        if (fullDoses != 0) {
            replaceSlot(player, with.slot, Item(getKegByDose(kegs, 4, usedMature)), with, Container.INVENTORY)
        }

        // Replace the targeted keg with the updated dosage amount
        if (leftoverDose != 0 && fullDoses == 0) {
            replaceSlot(player, with.slot, Item(getKegByDose(kegs, leftoverDose, usedMature)), with, Container.INVENTORY)

        // Replace the used with keg with the updated dosage amount
        } else if (leftoverDose != 0) {
            replaceSlot(player, used.slot, Item(getKegByDose(kegs, leftoverDose, usedMature)), used, Container.INVENTORY)
        }

        // Replace the used with keg with an empty keg
        if (leftoverDose == 0 || fullDoses == 0) {
            replaceSlot(player, used.slot, Item(kegs.emptyKeg), used, Container.INVENTORY)
        }

        // Send message/Play sound
        val amountString = when {
            totalDosage >= 4 -> "four"
            totalDosage == 3 -> "three"
            else -> "two"
        }

        sendMessage(player, "You have combined the kegs into $amountString pints.")
        playAudio(player, 2401)

        return true
    }

    // Decant keg into beer glass
    fun pourIntoGlass(player: Player, glass: Item, keg: Item): Boolean {
        // Find keg info
        val kegInfo = getKeg(keg.id) ?: return false
        val (_, kegs) = kegInfo
        val currentDose = getDoseByKeg(keg.id, kegs)
        val mature = isMature(keg.id, kegs)
        if (currentDose == 0) {
            return false
        }

        // Fill the glass
        val glassItem = if (mature) kegs.matureGlass else kegs.glass
        replaceSlot(player, glass.slot, Item(glassItem), glass, Container.INVENTORY)

        // Drain the keg
        val newDose = currentDose - 1
        replaceSlot(player, keg.slot, Item(getKegByDose(kegs, newDose, mature)), keg, Container.INVENTORY)

        return true
    }
}