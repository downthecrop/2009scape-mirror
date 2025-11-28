package content.global.skill.fletching.gem

import org.rs09.consts.Items

/**
 * Provides information pertaining to crafting different gem tipped bolts
 * @property untippedBoltItemId the base untipped bolt item id
 * @property gemItemId the gem item id used to craft the bolt tips
 * @property gemCutAnimationId the animation to play when the player cuts the gem
 * @property tipItemId the item id of the resulting bolt tips
 * @property tippedBoltItemId the item id of the finished gem-tipped bolt
 * @property level the required level to craft the gem tipped bolt or tips themselves
 * @property experience gained creating one set of gem tips, or per bolt tipped
 */
enum class GemBoltsCraftInfo(
    var untippedBoltItemId: Int,
    var gemItemId: Int,
    var gemCutAnimationId: Int,
    var tipItemId: Int,
    var tippedBoltItemId: Int,
    var level: Int,
    var experience: Double
) {
    OPAL(Items.BRONZE_BOLTS_877, Items.OPAL_1609, 890, Items.OPAL_BOLT_TIPS_45, Items.OPAL_BOLTS_879, 11, 1.6),
    JADE(Items.BLURITE_BOLTS_9139, Items.JADE_1611, 891, Items.JADE_BOLT_TIPS_9187, Items.JADE_BOLTS_9335, 26, 2.4),
    PEARL(Items.IRON_BOLTS_9140, Items.OYSTER_PEARL_411, 4470, Items.PEARL_BOLT_TIPS_46, Items.PEARL_BOLTS_880, 41, 3.2),
    PEARLS(
        Items.IRON_BOLTS_9140,
        Items.OYSTER_PEARLS_413,
        4470,
        Items.PEARL_BOLT_TIPS_46,
        Items.PEARL_BOLTS_880,
        41,
        3.2
    ),
    RED_TOPAZ(
        Items.STEEL_BOLTS_9141,
        Items.RED_TOPAZ_1613,
        892,
        Items.TOPAZ_BOLT_TIPS_9188,
        Items.TOPAZ_BOLTS_9336,
        48,
        3.9
    ),
    SAPPHIRE(
        Items.MITHRIL_BOLTS_9142,
        Items.SAPPHIRE_1607,
        888,
        Items.SAPPHIRE_BOLT_TIPS_9189,
        Items.SAPPHIRE_BOLTS_9337,
        56,
        4.7
    ),
    EMERALD(
        Items.MITHRIL_BOLTS_9142,
        Items.EMERALD_1605,
        889,
        Items.EMERALD_BOLT_TIPS_9190,
        Items.EMERALD_BOLTS_9338,
        58,
        5.5
    ),
    RUBY(Items.ADAMANT_BOLTS_9143, Items.RUBY_1603, 887, Items.RUBY_BOLT_TIPS_9191, Items.RUBY_BOLTS_9339, 63, 6.3),
    DIAMOND(
        Items.ADAMANT_BOLTS_9143,
        Items.DIAMOND_1601,
        886,
        Items.DIAMOND_BOLT_TIPS_9192,
        Items.DIAMOND_BOLTS_9340,
        65,
        7.0
    ),
    DRAGONSTONE(
        Items.RUNE_BOLTS_9144,
        Items.DRAGONSTONE_1615,
        885,
        Items.DRAGON_BOLT_TIPS_9193,
        Items.DRAGON_BOLTS_9341,
        71,
        8.2
    ),
    ONYX(Items.RUNE_BOLTS_9144, Items.ONYX_6573, 2717, Items.ONYX_BOLT_TIPS_9194, Items.ONYX_BOLTS_9342, 73, 9.4);

    companion object {
        private val gemBoltCraftInfoByGemId = values().associateBy { it.gemItemId }
        private val gemBoltCraftInfoByBoltTipId = values().associateBy { it.tipItemId }

        val untippedBoltIds: IntArray =
            values().map { gemBoltsCraftInfo: GemBoltsCraftInfo -> gemBoltsCraftInfo.untippedBoltItemId }.distinct()
                .toIntArray()

        val gemIds: IntArray =
            values().map { gemBoltsCraftInfo: GemBoltsCraftInfo -> gemBoltsCraftInfo.gemItemId }.toIntArray()

        val boltTipIds: IntArray =
            values().map { gemBoltsCraftInfo: GemBoltsCraftInfo -> gemBoltsCraftInfo.tipItemId }.toIntArray()

        fun forGemId(gemId: Int): GemBoltsCraftInfo? {
            return gemBoltCraftInfoByGemId[gemId]
        }

        fun forTipId(tipId: Int): GemBoltsCraftInfo? {
            return gemBoltCraftInfoByBoltTipId[tipId]
        }
    }
}