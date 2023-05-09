package content.data

import core.api.toIntArray
import core.cache.def.impl.ItemDefinition
import org.rs09.consts.Items

/**
 * Represents a distinct charged item, i.e. items with (#).
 * Not to be confused with items with internal charge, like degradation.
 * @author RiL
 */
enum class ChargedItem(val ids: IntArray) {
    AMULET_OF_GLORY((Items.AMULET_OF_GLORY4_1712 downTo Items.AMULET_OF_GLORY_1704 step 2).toIntArray()),
    RING_OF_DUELLING((Items.RING_OF_DUELLING8_2552..Items.RING_OF_DUELLING1_2566 step 2).toIntArray()),
    GAMES_NECKLACE((Items.GAMES_NECKLACE8_3853..Items.GAMES_NECKLACE1_3867 step 2).toIntArray()),
    BROODOO_SHIELDA((Items.BROODOO_SHIELD_10_6215..Items.BROODOO_SHIELD_6235 step 2).toIntArray()),
    BROODOO_SHIELDB((Items.BROODOO_SHIELD_10_6237..Items.BROODOO_SHIELD_6257 step 2).toIntArray()),
    BROODOO_SHIELDC((Items.BROODOO_SHIELD_10_6259..Items.BROODOO_SHIELD_6279 step 2).toIntArray()),
    ROD_OF_IVANDIS((Items.ROD_OF_IVANDIS10_7639..Items.ROD_OF_IVANDIS1_7648).toIntArray()),
    BLACK_MASK((Items.BLACK_MASK_10_8901..Items.BLACK_MASK_8921 step 2).toIntArray()),
    AMULET_OF_GLORYT((Items.AMULET_OF_GLORYT4_10354..Items.AMULET_OF_GLORYT_10362 step 2).toIntArray()),
    CASTLEWAR_BRACE((Items.CASTLEWAR_BRACE3_11079..Items.CASTLEWAR_BRACE1_11083 step 2).toIntArray()),
    FORINTHRY_BRACE((Items.FORINTHRY_BRACE5_11095..Items.FORINTHRY_BRACE1_11103 step 2).toIntArray()),
    SKILLS_NECKLACE((Items.SKILLS_NECKLACE4_11105..Items.SKILLS_NECKLACE_11113 step 2).toIntArray()),
    COMBAT_BRACELET((Items.COMBAT_BRACELET4_11118..Items.COMBAT_BRACELET_11126 step 2).toIntArray()),
    DIGSITE_PENDANT((Items.DIGSITE_PENDANT_5_11194 downTo Items.DIGSITE_PENDANT_1_11190).toIntArray()),
    VOID_SEAL((Items.VOID_SEAL8_11666..Items.VOID_SEAL1_11673).toIntArray()),
    AMULET_OF_FARMING((Items.AMULET_OF_FARMING8_12622 downTo Items.AMULET_OF_FARMING1_12608 step 2).toIntArray()),
    IVANDIS_FLAIL((Items.IVANDIS_FLAIL_30_13117..Items.IVANDIS_FLAIL_1_13146).toIntArray()),
    RING_OF_SLAYING((Items.RING_OF_SLAYING8_13281..Items.RING_OF_SLAYING1_13288).toIntArray()),
    RING_OF_WEALTH((Items.RING_OF_WEALTH4_14646 downTo Items.RING_OF_WEALTH_14638 step 2).toIntArray());

    /**
     * Get the item id for a specific [charge].
     * If [charge] is outside the valid range, return the item id with the closest charge.
     */
    fun forCharge(charge: Int): Int {
        return ids[maxCharge() - if (charge < 1) 1 + maxCharge() - ids.size else if (charge > maxCharge()) maxCharge() else charge]
    }

    /**
     * Get the max charge of this ChargedItem.
     */
    fun maxCharge(): Int = maxCharges[ordinal]

    companion object {
        private val CHARGE_REGEX = Regex("""\(\D?(\d+)\)""")
        private val idMap = HashMap<Int, ChargedItem>()
        private val maxCharges = IntArray(values().size)

        init {
            values().forEach { chargedItem ->
                maxCharges[chargedItem.ordinal] = getMaxCharge(chargedItem)
                chargedItem.ids.forEach { idMap[it] = chargedItem }
            }
        }

        private fun getMaxCharge(chargedItem: ChargedItem): Int {
            return CHARGE_REGEX.find(ItemDefinition.forId(chargedItem.ids.first()).name)!!.groups[1]!!.value.toInt()
        }

        /**
         * Check if the item id is a charged item.
         */
        fun contains(id: Int): Boolean = idMap.containsKey(id)

        /**
         * Get the ChargedItem enum for an item id, or null if it's not a charged item.
         */
        fun forId(id: Int): ChargedItem? = idMap[id]

        /**
         * Get the charge value of an item id, or null if it's not a charged item.
         */
        fun getCharge(id: Int): Int? {
            val chargedItem = forId(id) ?: return null
            return chargedItem.maxCharge() - chargedItem.ids.indexOf(id)
        }
    }
}
