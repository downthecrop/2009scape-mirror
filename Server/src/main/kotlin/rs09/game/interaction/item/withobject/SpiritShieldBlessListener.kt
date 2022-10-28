package rs09.game.interaction.item.withobject

import api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.Scenery
import rs09.game.interaction.IntType
import rs09.game.interaction.InteractionListener
import rs09.game.world.repository.Repository.sendNews
import java.util.*

/**
 * Listener for blessing spirit shield and attaching sigils
 * @author Byte
 */
@Suppress("unused")
class SpiritShieldBlessListener : InteractionListener {

    companion object {
        private const val REQUIRED_PRAYER_LEVEL_TO_BLESS_SHIELD = 85
        private const val REQUIRED_PRAYER_LEVEL_TO_ATTACH_SIGIL = 90
        private const val REQUIRED_SMITHING_LEVEL_TO_ATTACH_SIGIL = 85

        private val ANIMATION = Animation(898)

        private val COMPONENTS = intArrayOf(
            Items.HOLY_ELIXIR_13754,
            Items.SPIRIT_SHIELD_13734
        )

        private val ALTARS = intArrayOf(
            Scenery.ALTAR_409,
            Scenery.ALTAR_2640,
            Scenery.ALTAR_4008,
            Scenery.ALTAR_18254,
            Scenery.ALTAR_19145,
            Scenery.ALTAR_24343,
            Scenery.SARADOMIN_ALTAR_26287,
            Scenery.ALTAR_27661,
            Scenery.ALTAR_34616,
            Scenery.ALTAR_36972,
            Scenery.ALTAR_39842
        )

        private val SIGILS_SHIELDS_MAP = mapOf(
            Items.ARCANE_SIGIL_13746 to Items.ARCANE_SPIRIT_SHIELD_13738,
            Items.DIVINE_SIGIL_13748 to Items.DIVINE_SPIRIT_SHIELD_13740,
            Items.ELYSIAN_SIGIL_13750 to Items.ELYSIAN_SPIRIT_SHIELD_13742,
            Items.SPECTRAL_SIGIL_13752 to Items.SPECTRAL_SPIRIT_SHIELD_13744
        )

        private val ANVILS = intArrayOf(
            Scenery.ANVIL_2782,
            Scenery.ANVIL_2783,
            Scenery.ANVIL_4306,
            Scenery.ANVIL_6150,
            Scenery.ANVIL_22725,
            Scenery.LATHE_26817,
            Scenery.ANVIL_37622
        )
    }

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, COMPONENTS, *ALTARS) { player, _, _ ->
            if (!inInventory(player, Items.HOLY_ELIXIR_13754) || !inInventory(player, Items.SPIRIT_SHIELD_13734)) {
                sendMessage(player, "You need a holy elixir and an unblessed spirit shield in order to do this.")
                return@onUseWith true
            }

            if (!hasLevelDyn(player, Skills.PRAYER, REQUIRED_PRAYER_LEVEL_TO_BLESS_SHIELD)) {
                sendMessage(player, "You need a Prayer level of $REQUIRED_PRAYER_LEVEL_TO_BLESS_SHIELD in order to bless the shield.")
            }

            if (!removeItem(player, Items.HOLY_ELIXIR_13754)) {
                return@onUseWith false
            }

            if (!removeItem(player, Items.SPIRIT_SHIELD_13734)) {
                return@onUseWith false
            }

            addItem(player, Items.BLESSED_SPIRIT_SHIELD_13736)
            sendMessage(player, "You successfully bless the shield using the holy elixir.")

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, SIGILS_SHIELDS_MAP.keys.toIntArray(), *ANVILS) { player, used, _ ->
            if (!inInventory(player, Items.BLESSED_SPIRIT_SHIELD_13736)) {
                sendMessage(player, "You need a blessed spirit shield in order to continue.")
                return@onUseWith true
            }

            if (!hasLevelDyn(player, Skills.PRAYER, REQUIRED_PRAYER_LEVEL_TO_ATTACH_SIGIL) || !hasLevelDyn(player, Skills.SMITHING, REQUIRED_SMITHING_LEVEL_TO_ATTACH_SIGIL)) {
                sendMessage(player, "You need a Prayer level of $REQUIRED_PRAYER_LEVEL_TO_ATTACH_SIGIL and a Smithing level of $REQUIRED_SMITHING_LEVEL_TO_ATTACH_SIGIL in order to attach the sigil.")
                return@onUseWith true
            }

            if (!inInventory(player, Items.HAMMER_2347)) {
                sendMessage(player, "You need a hammer in order to do this.")
                return@onUseWith true
            }

            sendDialogueOptions(player, "Combine the two?", "Yes", "No")
            addDialogueAction(player) { _, buttonId ->
                when (buttonId) {
                    2 -> {
                        if (removeItem(player, used) && removeItem(player, Items.BLESSED_SPIRIT_SHIELD_13736)) {
                            val product = Item(SIGILS_SHIELDS_MAP[used.id]!!)
                            addItem(player, product.id)
                            animate(player, ANIMATION)
                            sendItemDialogue(player, product.id, "You successfully attach the " + used.name.lowercase(Locale.getDefault()) + " to the blessed spirit shield.")
                            sendNews(player.username + " has just made the " + product.name + ".")
                        }
                    }
                }
            }

            return@onUseWith true
        }
    }
}
