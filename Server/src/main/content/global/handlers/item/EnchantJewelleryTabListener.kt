package content.global.handlers.item

import core.ServerConstants
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.info.LogType
import core.game.node.entity.player.info.PlayerMonitor
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Animations
import org.rs09.consts.Items
import org.rs09.consts.Sounds

class EnchantJewelleryTabListener : InteractionListener {
    private val LVL_1_ENCHANT = mapOf(
        Items.SAPPHIRE_RING_1637 to Items.RING_OF_RECOIL_2550,
        Items.SAPPHIRE_NECKLACE_1656 to Items.GAMES_NECKLACE8_3853,
        Items.SAPPHIRE_AMULET_1694 to Items.AMULET_OF_MAGIC_1727,
        Items.SAPPHIRE_BRACELET_11072 to Items.BRACELET_OF_CLAY_11074
    )
    private val LVL_2_ENCHANT = mapOf(
        Items.EMERALD_RING_1639 to Items.RING_OF_DUELLING8_2552,
        Items.EMERALD_NECKLACE_1658 to Items.BINDING_NECKLACE_5521,
        Items.EMERALD_AMULET_1696 to Items.AMULET_OF_DEFENCE_1729,
        Items.EMERALD_BRACELET_11076 to Items.CASTLEWAR_BRACE3_11079
    )
    private val LVL_3_ENCHANT = mapOf(
        Items.RUBY_RING_1641 to Items.RING_OF_FORGING_2568,
        Items.RUBY_NECKLACE_1660 to Items.DIGSITE_PENDANT_5_11194,
        Items.RUBY_AMULET_1698 to Items.AMULET_OF_STRENGTH_1725,
        Items.RUBY_BRACELET_11085 to Items.INOCULATION_BRACE_11088
    )
    private val LVL_4_ENCHANT = mapOf(
        Items.DIAMOND_RING_1643 to Items.RING_OF_LIFE_2570,
        Items.DIAMOND_NECKLACE_1662 to Items.PHOENIX_NECKLACE_11090,
        Items.DIAMOND_AMULET_1700 to Items.AMULET_OF_POWER_1731,
        Items.DIAMOND_BRACELET_11092 to Items.FORINTHRY_BRACE5_11095
    )
    private val LVL_5_ENCHANT = mapOf(
        Items.DRAGONSTONE_RING_1645 to Items.RING_OF_WEALTH_2572,
        Items.DRAGON_NECKLACE_1664 to Items.SKILLS_NECKLACE_11113,
        Items.DRAGONSTONE_AMMY_1702 to Items.AMULET_OF_GLORY_1704,
        Items.DRAGON_BRACELET_11115 to Items.COMBAT_BRACELET_11126
    )
    private val LVL_6_ENCHANT = mapOf(
        Items.ONYX_RING_6575 to Items.RING_OF_STONE_6583,
        Items.ONYX_NECKLACE_6577 to Items.BERSERKER_NECKLACE_11128,
        Items.ONYX_AMULET_6581 to Items.AMULET_OF_FURY_6585,
        Items.ONYX_BRACELET_11130 to Items.REGEN_BRACELET_11133
    )
    private val TAB_MAPPING = arrayOf(
        Pair(Items.ENCHANT_SAPPHIRE_8016, LVL_1_ENCHANT),
        Pair(Items.ENCHANT_EMERALD_8017, LVL_2_ENCHANT),
        Pair(Items.ENCHANT_RUBY_8018, LVL_3_ENCHANT),
        Pair(Items.ENCHANT_DIAMOND_8019, LVL_4_ENCHANT),
        Pair(Items.ENCHANT_DRAGONSTN_8020, LVL_5_ENCHANT),
        Pair(Items.ENCHANT_ONYX_8021, LVL_6_ENCHANT)
    )

    override fun defineListeners() {
        for ((tablet, mapping) in TAB_MAPPING) {
            on(tablet, IntType.ITEM, "break") { player, _ ->
                sendMessage(player, "Try using the tablet on the item instead.") //TODO authentic message
                return@on true
            }
            for ((unenchanted, enchanted) in mapping) {
                onUseWith(IntType.ITEM, tablet, unenchanted) { player, tabItem, node ->
                    var product = enchanted
                    if (product == Items.RING_OF_WEALTH_2572 && ServerConstants.RING_OF_WEALTH_TELEPORT) {
                        product = Items.RING_OF_WEALTH_14638
                    }
                    if (removeItem(player, Item(tabItem.id))) {
                        closeAllInterfaces(player)
                        playAudio(player, Sounds.POH_TABLET_BREAK_979)
                        val anim = Animation(Animations.POH_TABLET_BREAK_4069)
                        animate(player, anim, true)
                        delayEntity(player, anim.duration)
                        queueScript(player, anim.duration, QueueStrength.SOFT) {
                            val item = node.asItem()
                            val ret = replaceSlot(player, item.slot, Item(product), item)
                            if (ret != item) {
                                PlayerMonitor.log(player, LogType.DUPE_ALERT, "Unknown slot-replacement problem when enchanting jewellery (adding $product replaced $ret rather than $item)")
                            }
                            return@queueScript stopExecuting(player)
                        }
                    }
                    return@onUseWith true
                }
            }
        }
    }
}
