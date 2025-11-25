package content.global.handlers.item.equipment

import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Barrows equipment information and utilities.
 * @author 'Vexia - original code
 * @author Damighty - Kotlin conversion and refactor
 */
object BarrowsEquipment {
    // Barrows equipment lasts for 15 hours of combat. Each piece has 4 degradation tiers (100, 75, 50, 25).
    const val DEGRADATION_TICKS_PER_TIER = (15 * 6000) / 4 // 22500 ticks per tier
    private const val MAX_DURABILITY = DEGRADATION_TICKS_PER_TIER * 4

    /**
     * A data class for each Barrows piece. Holds all information related to a specific piece of Barrows gear.
     *
     * @param brother The Barrows brother this item belongs to.
     * @param equipmentType The slot type ("weapon", "body", "legs", "helm").
     * @param itemName The formatted name of the item.
     * @param baseRepairCost The full repair cost in GP.
     * @param degradationStates A list of item IDs, from fully repaired (index 0) to broken (index 5).
     */
    data class BarrowsItemDefinition(
        val brother: String,
        val equipmentType: String,
        val itemName: String,
        val baseRepairCost: Int,
        val degradationStates: List<Int>
    ) {
        val repairedId: Int = degradationStates.first()
        val brokenId: Int = degradationStates.last()

        /** Checks if a given item ID belongs to this specific equipment set. */
        fun contains(itemId: Int): Boolean = itemId in degradationStates

        /** Gets the degradation index for a given item ID (0=repaired, 1=100, ..., 5=broken). */
        fun getDegradationIndex(itemId: Int): Int = degradationStates.indexOf(itemId)
    }

    /** All Barrows equipment data. */
    private val barrowsItemDefinitions = listOf(
        // Dharok
        BarrowsItemDefinition("dharok", "helm", "Dharok's helm", 60_000,
            listOf(Items.DHAROKS_HELM_4716, Items.DHAROKS_HELM_100_4880,
            Items.DHAROKS_HELM_75_4881, Items.DHAROKS_HELM_50_4882,
            Items.DHAROKS_HELM_25_4883, Items.DHAROKS_HELM_0_4884)),
        BarrowsItemDefinition("dharok", "weapon", "Dharok's greataxe", 100_000,
            listOf(Items.DHAROKS_GREATAXE_4718, Items.DHAROKS_AXE_100_4886,
            Items.DHAROKS_AXE_75_4887, Items.DHAROKS_AXE_50_4888,
            Items.DHAROKS_AXE_25_4889, Items.DHAROKS_AXE_0_4890)),
        BarrowsItemDefinition("dharok", "body", "Dharok's platebody", 90_000,
            listOf(Items.DHAROKS_PLATEBODY_4720, Items.DHAROKS_BODY_100_4892,
            Items.DHAROKS_BODY_75_4893, Items.DHAROKS_BODY_50_4894,
            Items.DHAROKS_BODY_25_4895, Items.DHAROKS_BODY_0_4896)),
        BarrowsItemDefinition("dharok", "legs", "Dharok's platelegs", 80_000,
            listOf(Items.DHAROKS_PLATELEGS_4722, Items.DHAROKS_LEGS_100_4898,
            Items.DHAROKS_LEGS_75_4899, Items.DHAROKS_LEGS_50_4900,
            Items.DHAROKS_LEGS_25_4901, Items.DHAROKS_LEGS_0_4902)),
        // Guthan
        BarrowsItemDefinition("guthan", "helm", "Guthan's helm", 60_000,
            listOf(Items.GUTHANS_HELM_4724, Items.GUTHANS_HELM_100_4904,
            Items.GUTHANS_HELM_75_4905, Items.GUTHANS_HELM_50_4906,
            Items.GUTHANS_HELM_25_4907, Items.GUTHANS_HELM_0_4908)),
        BarrowsItemDefinition("guthan", "weapon", "Guthan's warspear", 100_000,
            listOf(Items.GUTHANS_WARSPEAR_4726, Items.GUTHANS_SPEAR_100_4910,
            Items.GUTHANS_SPEAR_75_4911, Items.GUTHANS_SPEAR_50_4912,
            Items.GUTHANS_SPEAR_25_4913, Items.GUTHANS_SPEAR_0_4914)),
        BarrowsItemDefinition("guthan", "body", "Guthan's platebody", 90_000,
            listOf(Items.GUTHANS_PLATEBODY_4728, Items.GUTHANS_BODY_100_4916,
            Items.GUTHANS_BODY_75_4917, Items.GUTHANS_BODY_50_4918,
            Items.GUTHANS_BODY_25_4919, Items.GUTHANS_BODY_0_4920)),
        BarrowsItemDefinition("guthan", "legs", "Guthan's chainskirt", 80_000,
            listOf(Items.GUTHANS_CHAINSKIRT_4730, Items.GUTHANS_SKIRT_100_4922,
            Items.GUTHANS_SKIRT_75_4923, Items.GUTHANS_SKIRT_50_4924,
            Items.GUTHANS_SKIRT_25_4925, Items.GUTHANS_SKIRT_0_4926)),
        // Torag
        BarrowsItemDefinition("torag", "helm", "Torag's helm", 60_000,
            listOf(Items.TORAGS_HELM_4745, Items.TORAGS_HELM_100_4952,
            Items.TORAGS_HELM_75_4953, Items.TORAGS_HELM_50_4954,
            Items.TORAGS_HELM_25_4955, Items.TORAGS_HELM_0_4956)),
        BarrowsItemDefinition("torag", "weapon", "Torag's hammers", 100_000,
            listOf(Items.TORAGS_HAMMERS_4747, Items.TORAGS_HAMMER_100_4958,
            Items.TORAGS_HAMMER_75_4959, Items.TORAGS_HAMMER_50_4960,
            Items.TORAGS_HAMMER_25_4961, Items.TORAGS_HAMMER_0_4962)),
        BarrowsItemDefinition("torag", "body", "Torag's platebody", 90_000,
            listOf(Items.TORAGS_PLATEBODY_4749, Items.TORAGS_BODY_100_4964,
            Items.TORAGS_BODY_75_4965, Items.TORAGS_BODY_50_4966,
            Items.TORAGS_BODY_25_4967, Items.TORAGS_BODY_0_4968)),
        BarrowsItemDefinition("torag", "legs", "Torag's platelegs", 80_000,
            listOf(Items.TORAGS_PLATELEGS_4751, Items.TORAGS_LEGS_100_4970,
            Items.TORAGS_LEGS_75_4971, Items.TORAGS_LEGS_50_4972,
            Items.TORAGS_LEGS_25_4973, Items.TORAGS_LEGS_0_4974)),
        // Verac
        BarrowsItemDefinition("verac", "helm", "Verac's helm", 60_000,
            listOf(Items.VERACS_HELM_4753, Items.VERACS_HELM_100_4976,
            Items.VERACS_HELM_75_4977, Items.VERACS_HELM_50_4978,
            Items.VERACS_HELM_25_4979, Items.VERACS_HELM_0_4980)),
        BarrowsItemDefinition("verac", "weapon", "Verac's flail", 100_000,
            listOf(Items.VERACS_FLAIL_4755, Items.VERACS_FLAIL_100_4982,
            Items.VERACS_FLAIL_75_4983, Items.VERACS_FLAIL_50_4984,
            Items.VERACS_FLAIL_25_4985, Items.VERACS_FLAIL_0_4986)),
        BarrowsItemDefinition("verac", "body", "Verac's brassard", 90_000,
            listOf(Items.VERACS_BRASSARD_4757, Items.VERACS_TOP_100_4988,
            Items.VERACS_TOP_75_4989, Items.VERACS_TOP_50_4990,
            Items.VERACS_TOP_25_4991, Items.VERACS_TOP_0_4992)),
        BarrowsItemDefinition("verac", "legs", "Verac's plateskirt", 80_000,
            listOf(Items.VERACS_PLATESKIRT_4759, Items.VERACS_SKIRT_100_4994,
            Items.VERACS_SKIRT_75_4995, Items.VERACS_SKIRT_50_4996,
            Items.VERACS_SKIRT_25_4997, Items.VERACS_SKIRT_0_4998)),
        // Ahrim
        BarrowsItemDefinition("ahrim", "helm", "Ahrim's hood", 60_000,
            listOf(Items.AHRIMS_HOOD_4708, Items.AHRIMS_HOOD_100_4856,
            Items.AHRIMS_HOOD_75_4857, Items.AHRIMS_HOOD_50_4858,
            Items.AHRIMS_HOOD_25_4859, Items.AHRIMS_HOOD_0_4860)),
        BarrowsItemDefinition("ahrim", "weapon", "Ahrim's staff", 100_000,
            listOf(Items.AHRIMS_STAFF_4710, Items.AHRIMS_STAFF_100_4862,
            Items.AHRIMS_STAFF_75_4863, Items.AHRIMS_STAFF_50_4864,
            Items.AHRIMS_STAFF_25_4865, Items.AHRIMS_STAFF_0_4866)),
        BarrowsItemDefinition("ahrim", "body", "Ahrim's robetop", 90_000,
            listOf(Items.AHRIMS_ROBETOP_4712, Items.AHRIMS_TOP_100_4868,
            Items.AHRIMS_TOP_75_4869, Items.AHRIMS_TOP_50_4870,
            Items.AHRIMS_TOP_25_4871, Items.AHRIMS_TOP_0_4872)),
        BarrowsItemDefinition("ahrim", "legs", "Ahrim's robeskirt", 80_000,
            listOf(Items.AHRIMS_ROBESKIRT_4714, Items.AHRIMS_SKIRT_100_4874,
            Items.AHRIMS_SKIRT_75_4875, Items.AHRIMS_SKIRT_50_4876,
            Items.AHRIMS_SKIRT_25_4877, Items.AHRIMS_SKIRT_0_4878)),
        // Karil
        BarrowsItemDefinition("karil", "helm", "Karil's coif", 60_000,
            listOf(Items.KARILS_COIF_4732, Items.KARILS_COIF_100_4928,
            Items.KARILS_COIF_75_4929, Items.KARILS_COIF_50_4930,
            Items.KARILS_COIF_25_4931, Items.KARILS_COIF_0_4932)),
        BarrowsItemDefinition("karil", "weapon", "Karil's crossbow", 100_000,
            listOf(Items.KARILS_CROSSBOW_4734, Items.KARILS_X_BOW_100_4934,
            Items.KARILS_X_BOW_75_4935, Items.KARILS_X_BOW_50_4936,
            Items.KARILS_X_BOW_25_4937, Items.KARILS_X_BOW_0_4938)),
        BarrowsItemDefinition("karil", "body", "Karil's leathertop", 90_000,
            listOf(Items.KARILS_LEATHERTOP_4736, Items.KARILS_TOP_100_4940,
            Items.KARILS_TOP_75_4941, Items.KARILS_TOP_50_4942,
            Items.KARILS_TOP_25_4943, Items.KARILS_TOP_0_4944)),
        BarrowsItemDefinition("karil", "legs", "Karil's leatherskirt", 80_000,
            listOf(Items.KARILS_LEATHERSKIRT_4738, Items.KARILS_SKIRT_100_4946,
            Items.KARILS_SKIRT_75_4947, Items.KARILS_SKIRT_50_4948,
            Items.KARILS_SKIRT_25_4949, Items.KARILS_SKIRT_0_4950))
    )

    /** Cached access to an item's full definition from its ID */
    private val itemIdToDefinitionMap: Map<Int, BarrowsItemDefinition> by lazy {
        barrowsItemDefinitions.flatMap { def ->
            def.degradationStates.map { id -> id to def }
        }.toMap()
    }

    /** Gets all degradation state arrays for degradation registration */
    fun getAllEquipmentSets(): Collection<IntArray> = barrowsItemDefinitions.map { it.degradationStates.toIntArray() }

    /** Gets all repairable Barrows item IDs (anything not fully repaired) */
    fun getAllRepairableBarrowsIds(): List<Int> = itemIdToDefinitionMap.filter { !isFullyRepaired(it.key) }.keys.toList()

    /** Gets the full definition for a Barrows item */
    @JvmStatic
    fun getDefinition(itemId: Int): BarrowsItemDefinition? = itemIdToDefinitionMap[itemId]

    /** Checks if an item ID is any Barrows item */
    @JvmStatic
    fun isBarrowsItem(itemId: Int): Boolean = itemId in itemIdToDefinitionMap

    /** Checks if a Barrows item is fully repaired */
    @JvmStatic
    fun isFullyRepaired(itemId: Int): Boolean = getDefinition(itemId)?.repairedId == itemId

    /** Checks if a Barrows item is broken */
    @JvmStatic
    fun isBroken(itemId: Int): Boolean = getDefinition(itemId)?.brokenId == itemId

    /**
     * Calculates the repair cost for a degraded Barrows item
     * @param item The degraded Barrows item
     * @return The repair cost in GP, or -1 if the item cannot be repaired
     */
    fun getRepairCost(item: Item): Int {
        val def = getDefinition(item.id) ?: return -1
        if (isFullyRepaired(item.id)) return -1

        val totalRemainingDurability = calculateTotalRemainingDurability(item, def)
        val durabilityLost = MAX_DURABILITY - totalRemainingDurability
        val cost = ((durabilityLost.toDouble() / MAX_DURABILITY) * def.baseRepairCost).toInt()

        return cost
    }

    /**
     * Reduces the durability of a Barrows item by 20% of its total remaining durability
     * @param item The Barrows item
     * @return The item with reduced durability, or null if the item is not a valid Barrows piece
     */
    @JvmStatic
    fun graveDeathDurabilityReduction(item: Item): Item? {
        val def = getDefinition(item.id) ?: return null
        if (isBroken(item.id)) return item

        val totalRemainingDurability = calculateTotalRemainingDurability(item, def)
        val durabilityReduction = (totalRemainingDurability * 0.2).toInt()
        val newRemainingDurability = totalRemainingDurability - durabilityReduction

        return createItemFromDurability(def, newRemainingDurability, item.amount)
    }

    /**
     * Calculates the total remaining durability ticks (charge) for a Barrows item
     */
    private fun calculateTotalRemainingDurability(item: Item, def: BarrowsItemDefinition): Int {
        return when (val index = def.getDegradationIndex(item.id)) {
            -1 -> 0 // Invalid
            0 -> MAX_DURABILITY // Fully repaired
            5 -> 0 // Broken
            else -> {
                // For degraded items (100, 75, 50, 25), durability is the number of fully remaining tiers below it, plus the charge of the current tier.
                // Index 1 (100) has 3 tiers below it. Index 4 (25) has 0 tiers below it.
                val remainingTiers = 4 - index
                (remainingTiers * DEGRADATION_TICKS_PER_TIER) + item.charge
            }
        }
    }

    /**
     * Creates a new Item instance based on a total durability value
     */
    private fun createItemFromDurability(def: BarrowsItemDefinition, durability: Int, amount: Int): Item {
        if (durability <= 0) {
            return Item(def.brokenId, amount)
        }
        // Tier is 1-indexed (1=25, 2=50, 3=75, 4=100)
        val tier = ((durability - 1) / DEGRADATION_TICKS_PER_TIER) + 1
        // Degradation index is 4-indexed (4=25, 3=50, 2=75, 1=100)
        val degradationIndex = 5 - tier

        val newId = def.degradationStates[degradationIndex]
        val newCharge = (durability - 1) % DEGRADATION_TICKS_PER_TIER + 1

        return Item(newId, amount, newCharge)
    }
}
