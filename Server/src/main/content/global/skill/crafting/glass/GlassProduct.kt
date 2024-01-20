package content.global.skill.crafting.glass

enum class GlassProduct (
        val buttonId: Int,
        val producedItemId: Int,
        val amount: Int,
        val minimumLevel: Int,
        val experience: Double
) {
    VIAL(38, 229, 1, 33, 35.0),
    ORB(39, 567, 1, 46, 52.5),
    BEER_GLASS(40, 1919, 1, 1, 17.5),
    CANDLE_LANTERN(41, 4527, 1, 4, 19.0),
    OIL_LAMP(42, 4525, 1, 12, 25.0),
    LANTERN_LENS(43, 4542, 1, 49, 55.0),
    FISHBOWL(44, 6667, 1, 42, 42.5),
    LIGHT_ORB(45, 10973, 1, 87, 70.0);

    companion object {
        private val BUTTON_MAP = HashMap<Int, GlassProduct>()
        private val PRODUCT_MAP = HashMap<Int, GlassProduct>()

        init {
            for (product in GlassProduct.values()) {
                BUTTON_MAP[product.buttonId] = product
            }

            for (product in GlassProduct.values()) {
                PRODUCT_MAP[product.producedItemId] = product
            }
        }

        fun forButtonID(buttonId: Int): GlassProduct? = BUTTON_MAP[buttonId]
    }
}