package rs09.game.interaction.item.withitem

import api.*
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import kotlin.collections.toIntArray
import kotlin.math.min

class PoisonedWeaponListeners : InteractionListener() {
    override fun defineListeners() {
        val poisons = intArrayOf(Items.WEAPON_POISON_187, Items.WEAPON_POISON_PLUS_5937, Items.WEAPON_POISON_PLUS_PLUS_5940)
        val poisonableItems = PoisonSets.itemMap.keys.toIntArray()
        val poisonedItems = PoisonSets.itemMap.values.toIntArray()

        onUseWith(ITEM, poisons, *poisonableItems){player, used, with ->
            val index = poisons.indexOf(used.id)
            val product = PoisonSets.itemMap[with.id]!![index]
            val amt = min(5, with.asItem().amount)

            if(removeItem(player, Item(with.id, amt))) {
                removeItem(player, used.id) //Mico's Code. See: https://gitlab.com/skelsoft/redwings/-/issues/13
                addItemOrDrop(player, product, amt)
                addItemOrDrop(player, Items.VIAL_229)
                sendMessage(player, "You poison the ${with.name.toLowerCase()}.")
            }
            return@onUseWith true
        }

        onUseWith(ITEM, Items.CLEANING_CLOTH_3188, *poisonedItems) {player, _, with ->
            val base = PoisonSets.getBase(with.id) ?: return@onUseWith false
            val amt = min(5, with.asItem().amount)
            removeItem(player, Item(with.id, amt))
            addItemOrDrop(player, base, amt)
            return@onUseWith true
        }
    }

    //below enum copied from old file, some data is probably wrong.
    internal enum class PoisonSets(val base: Int, val p: Int, val pp: Int, val ppp: Int){
        BRONZE_ARROW(882, 883, 5616, 5622),
        IRON_ARROW(884, 885, 5617, 5623),
        STEEL_ARROW(886, 887, 5618, 5624),
        MITHRIL_ARROW(888, 889, 5619, 5625),
        ADAMANT_ARROW(890, 891, 5620, 5626),
        RUNE_ARROW(892, 893, 5621, 5627),

        IRON_KNIFE(863, 871, 5655, 5662),
        BRONZE_KNIFE(864, 870, 5654, 5661),
        STEEL_KNIFE(865, 872, 5656, 5663),
        MITHRIL_KNIFE(866, 873, 5657, 5664),
        ADAMANT_KNIFE(867, 875, 5659, 5666),
        RUNE_KNIFE(868, 876, 5660, 5667),
        BLACK_KNIFE(869, 874, 5658, 5665),

        BRONZE_DART(806, 812, 5628, 5635),
        IRON_DART(807, 813, 5629, 5636),
        STEEL_DART(808, 814, 5630, 5637),
        BLACK_DART(3093, 3094, 5631, 5638),
        MITHRIL_DART(809, 815, 5632, 5639),
        ADAMANT_DART(810, 816, 5633, 5640),

        RUNE_DART(811, 817, 5634, 5641),
        IRON_JAVELIN(825, 831, 5641, 5648),
        BRONZE_JAVELIN(826, 832, 5642, 5649),
        STEEL_JAVELIN(827, 833, 5643, 5650),
        MITHRIL_JAVELIN(828, 834, 5644, 5651),
        ADAMANT_JAVELIN(829, 835, 5645, 5652),
        RUNE_JAVELIN(830, 836, 5646, 5653),

        IRON_DAGGER(1203, 1219, 5668, 5686),
        BRONZE_DAGGER(1205, 1221, 5670, 5688),
        STEEL_DAGGER(1207, 1223, 5672, 5690),
        MITHRIL_DAGGER(1209, 1225, 5674, 5692),
        ADAMANT_DAGGER(1211, 1227, 5676, 5694),
        RUNE_DAGGER(1213, 1229, 5678, 5696),
        DRAGON_DAGGER(1215, 1231, 5680, 5698),
        BLACK_DAGGER(1217, 1233, 5682, 5700),

        BRONZE_SPEAR(1237, 1251, 5704, 5618),
        IRON_SPEAR(1239, 1253, 5706, 5620),
        STEEL_SPEAR(1241, 1255, 5708, 5622),
        MITHRIL_SPEAR(1243, 1257, 5710, 5624),
        ADAMANT_SPEAR(1245, 1259, 5712, 5626),
        RUNE_SPEAR(1247, 1261, 5714, 5628),
        DRAGON_SPEAR(1249, 1263, 5716, 5730),
        BLACK_SPEAR(4580, 4582, 5734, 5636);

        companion object {
            val itemMap = values().map { it.base to intArrayOf(it.p,it.pp,it.ppp) }.toMap()

            fun getBase(poisoned: Int) : Int? {
                for ((base,set) in itemMap.entries) {
                    if (set.contains(poisoned)) return base
                }
                return null
            }
        }
    }
}