package rs09.game.interaction.item

import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.rs09.consts.Items
import rs09.game.system.SystemLogger

private val LVL_1_ENCHANT = mapOf(
        Items.SAPPHIRE_RING_1637 to Item(Items.RING_OF_RECOIL_2550),
        Items.SAPPHIRE_NECKLACE_1656 to Item(Items.GAMES_NECKLACE8_3853),
        Items.SAPPHIRE_AMULET_1694 to Item(Items.AMULET_OF_MAGIC_1727),
        Items.SAPPHIRE_BRACELET_11072 to Item(Items.BRACELET_OF_CLAY_11074)
)
private val LVL_2_ENCHANT = mapOf(
        Items.EMERALD_RING_1639 to Item(Items.RING_OF_DUELLING8_2552),
        Items.EMERALD_NECKLACE_1658 to Item(Items.BINDING_NECKLACE_5521),
        Items.EMERALD_AMULET_1696 to Item(Items.AMULET_OF_DEFENCE_1729),
        Items.EMERALD_BRACELET_11076 to Item(Items.CASTLEWAR_BRACE3_11079)
)

private val LVL_3_ENCHANT = mapOf(
        Items.RUBY_RING_1641 to Item(Items.RING_OF_FORGING_2568),
        Items.RUBY_NECKLACE_1660 to Item(Items.DIGSITE_PENDANT_5_11194),
        Items.RUBY_AMULET_1698 to Item(Items.AMULET_OF_STRENGTH_1725),
        Items.RUBY_BRACELET_11085 to Item(Items.INOCULATION_BRACE_11088)
)

private val LVL_4_ENCHANT = mapOf(
        Items.DIAMOND_RING_1643 to Item(Items.RING_OF_LIFE_2570),
        Items.DIAMOND_NECKLACE_1662 to Item(Items.PHOENIX_NECKLACE_11090),
        Items.DIAMOND_AMULET_1700 to Item(Items.AMULET_OF_POWER_1731),
        Items.DIAMOND_BRACELET_11092 to Item(Items.FORINTHRY_BRACE5_11095)
)

private val LVL_5_ENCHANT = mapOf(
        Items.DRAGONSTONE_RING_1645 to Item(14646),
        Items.DRAGON_NECKLACE_1664 to Item(Items.SKILLS_NECKLACE4_11105),
        Items.DRAGONSTONE_AMMY_1702 to Item(Items.AMULET_OF_GLORY4_1712),
        Items.DRAGON_BRACELET_11115 to Item(Items.COMBAT_BRACELET4_11118)
)


@Initializable
class EnchantJewelleryTabOption : OptionHandler(){
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false

        val items = when(node?.id){
            8016 -> LVL_1_ENCHANT //Sapphire
            8017 -> LVL_2_ENCHANT
            8018 -> LVL_3_ENCHANT
            8019 -> LVL_4_ENCHANT
            8020 -> LVL_5_ENCHANT
            else -> null
        }

        if(items == null){
            SystemLogger.logWarn("UNHANDLED ENCHANT ITEM TABLET [ID: ${node?.id}")
            return true
        }

        player.audioManager.send(979)
        player.animator.forceAnimation(Animation(4069))

        GlobalScope.launch {
            for(item in player.inventory.toArray()){
                item ?: continue
                if(items.containsKey(item.id)){
                    val product = items[item.id]
                    player.inventory.remove(item)
                    player.inventory.remove(node as Item)
                    player.inventory.add(product)
                    break;
                }
            }
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(8016).handlers["option:break"] = this
        ItemDefinition.forId(8017).handlers["option:break"] = this
        ItemDefinition.forId(8018).handlers["option:break"] = this
        ItemDefinition.forId(8019).handlers["option:break"] = this
        ItemDefinition.forId(8020).handlers["option:break"] = this
        return this
    }

}