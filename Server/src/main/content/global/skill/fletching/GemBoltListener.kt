package content.global.skill.fletching

import content.global.skill.fletching.Fletching.GemBolts
import content.global.skill.fletching.items.gem.GemBoltCutPulse
import content.global.skill.fletching.items.gem.GemBoltPulse
import core.api.amountInInventory
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import org.rs09.consts.Items
import kotlin.math.min

class GemBoltListener : InteractionListener {
    val gems = intArrayOf(
            Items.OYSTER_PEARL_411,
            Items.OYSTER_PEARLS_413,
            Items.OPAL_1609,
            Items.JADE_1611,
            Items.RED_TOPAZ_1613,
            Items.SAPPHIRE_1607,
            Items.EMERALD_1605,
            Items.RUBY_1603,
            Items.DIAMOND_1601,
            Items.DRAGONSTONE_1615,
            Items.ONYX_6573
    )
    val boltBases = GemBolts.values().map { it.base }.toIntArray()
    val boltTips = GemBolts.values().map { it.tip }.toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CHISEL_1755, *gems) { player, used, with ->
            val gem = Fletching.gemMap[with.id] ?: return@onUseWith true

            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(gem.gem)) {
                override fun create(amount: Int, index: Int) {
                    player.pulseManager.run(GemBoltCutPulse(player, used as? Item, gem, amount))
                }

                override fun getAll(index: Int): Int {
                    return player.inventory.getAmount(gem.gem)
                }
            }.open()
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, boltBases, *boltTips) {player, used, with ->
            val bolt = Fletching.tipMap[with.id] ?: return@onUseWith true
            if (used.id != bolt.base || with.id != bolt.tip) return@onUseWith true


            val handler: SkillDialogueHandler =
                    object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(bolt.product)) {
                        override fun create(amount: Int, index: Int) {
                            player.pulseManager.run(GemBoltPulse(player, used as? Item, bolt, amount))
                        }

                        override fun getAll(index: Int): Int {
                            return min(amountInInventory(player, used.id), amountInInventory(player, with.id))
                        }
                    }
            handler.open()
            return@onUseWith true
        }
    }
}