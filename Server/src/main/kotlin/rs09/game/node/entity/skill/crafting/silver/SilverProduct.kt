package rs09.game.node.entity.skill.crafting.silver

import org.rs09.consts.Items

private const val BUTTON_UNBLESSED = 16
private const val BUTTON_UNHOLY = 23
private const val BUTTON_SICKLE = 30
private const val BUTTON_TIARA = 44
private const val BUTTON_DEMONIC_SIGIL = 59
private const val BUTTON_SILVTHRIL_CHAIN = 73
private const val BUTTON_LIGHTNING_ROD = 37
private const val BUTTON_SILVTHRILL_ROD = 52
private const val BUTTON_CROSSBOW_BOLTS = 66

/**
 * Provides silver crafting product definitions.
 *
 * @author vddCore
 */
enum class SilverProduct(
    val buttonId: Int,
    val requiredItemId: Int,
    val producedItemId: Int,
    val amountProduced: Int,
    val minimumLevel: Int,
    val xpReward: Double,
    val strungId: Int
) {
    HOLY(BUTTON_UNBLESSED, Items.HOLY_MOULD_1599, Items.UNSTRUNG_SYMBOL_1714, 1, 16, 50.0, Items.UNBLESSED_SYMBOL_1716),
    UNHOLY(BUTTON_UNHOLY, Items.UNHOLY_MOULD_1594, Items.UNSTRUNG_EMBLEM_1720, 1, 17, 50.0, Items.UNHOLY_SYMBOL_1724),
    SICKLE(BUTTON_SICKLE, Items.SICKLE_MOULD_2976, Items.SILVER_SICKLE_2961, 1, 18, 50.0, -1),
    TIARA(BUTTON_TIARA, Items.TIARA_MOULD_5523, Items.TIARA_5525, 1, 23, 52.5, -1),
    SILVTHRIL_CHAIN(BUTTON_SILVTHRIL_CHAIN, Items.CHAIN_LINK_MOULD_13153, Items.SILVTHRIL_CHAIN_13154, 1, 47, 100.0, -1),
    LIGHTNING_ROD(BUTTON_LIGHTNING_ROD, Items.CONDUCTOR_MOULD_4200, Items.CONDUCTOR_4201, 1, 20, 50.0, -1),
    SILVTHRILL_ROD(BUTTON_SILVTHRILL_ROD, Items.ROD_CLAY_MOULD_7649, Items.SILVTHRILL_ROD_7637, 1, 25, 55.0, -1),
    CROSSBOW_BOLTS(BUTTON_CROSSBOW_BOLTS, Items.BOLT_MOULD_9434, Items.SILVER_BOLTS_UNF_9382, 10, 21, 50.0, -1);

    companion object {
        private val BUTTON_MAP = HashMap<Int, SilverProduct>()
        private val PRODUCT_MAP = HashMap<Int, SilverProduct>()

        init {
            for (product in SilverProduct.values()) {
                BUTTON_MAP[product.buttonId] = product
            }

            for (product in SilverProduct.values()) {
                PRODUCT_MAP[product.producedItemId] = product
            }
        }

        fun forButtonID(buttonId: Int): SilverProduct? = BUTTON_MAP[buttonId]
        fun forProductID(productId: Int): SilverProduct? = PRODUCT_MAP[productId]
    }
}