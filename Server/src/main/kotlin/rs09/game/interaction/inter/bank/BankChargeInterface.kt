package rs09.game.interaction.inter.bank

import api.*
import core.game.component.Component
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.game.interaction.InterfaceListener

/**
 * Handles bank charge interface for Eniola at ZMI altar.
 * @author vddCore
 */
class BankChargeInterface : InterfaceListener {
    companion object {
        private const val BUTTON_AIR_RUNE = 28
        private const val BUTTON_MIND_RUNE = 29
        private const val BUTTON_WATER_RUNE = 30
        private const val BUTTON_EARTH_RUNE = 31
        private const val BUTTON_FIRE_RUNE = 32
        private const val BUTTON_BODY_RUNE = 33
        private const val BUTTON_COSMIC_RUNE = 34
        private const val BUTTON_CHAOS_RUNE = 35
        private const val BUTTON_ASTRAL_RUNE = 36
        private const val BUTTON_LAW_RUNE = 37
        private const val BUTTON_DEATH_RUNE = 38
        private const val BUTTON_BLOOD_RUNE = 39
        private const val BUTTON_NATURE_RUNE = 40
        private const val BUTTON_SOUL_RUNE = 41

        private val RUNE_BUTTONS = intArrayOf(
            BUTTON_AIR_RUNE, BUTTON_MIND_RUNE, BUTTON_WATER_RUNE, BUTTON_EARTH_RUNE,
            BUTTON_FIRE_RUNE, BUTTON_BODY_RUNE, BUTTON_COSMIC_RUNE, BUTTON_CHAOS_RUNE,
            BUTTON_ASTRAL_RUNE, BUTTON_LAW_RUNE, BUTTON_DEATH_RUNE, BUTTON_BLOOD_RUNE,
            BUTTON_NATURE_RUNE, BUTTON_SOUL_RUNE
        )

        private val BUTTON_TO_RUNE = hashMapOf(
            BUTTON_AIR_RUNE to Items.AIR_RUNE_556,
            BUTTON_MIND_RUNE to Items.MIND_RUNE_558,
            BUTTON_WATER_RUNE to Items.WATER_RUNE_555,
            BUTTON_EARTH_RUNE to Items.EARTH_RUNE_557,
            BUTTON_FIRE_RUNE to Items.FIRE_RUNE_554,
            BUTTON_BODY_RUNE to Items.BODY_RUNE_559,
            BUTTON_COSMIC_RUNE to Items.COSMIC_RUNE_564,
            BUTTON_CHAOS_RUNE to Items.CHAOS_RUNE_562,
            BUTTON_ASTRAL_RUNE to Items.ASTRAL_RUNE_9075,
            BUTTON_LAW_RUNE to Items.LAW_RUNE_563,
            BUTTON_DEATH_RUNE to Items.DEATH_RUNE_560,
            BUTTON_BLOOD_RUNE to Items.BLOOD_RUNE_565,
            BUTTON_NATURE_RUNE to Items.NATURE_RUNE_561,
            BUTTON_SOUL_RUNE to Items.SOUL_RUNE_566
        )
    }

    private fun handleButtonClick(
        player: Player,
        component: Component,
        opcode: Int,
        buttonID: Int,
        slot: Int,
        itemID: Int
    ): Boolean {
        if (buttonID !in RUNE_BUTTONS) return true

        val runeItemId = BUTTON_TO_RUNE[buttonID]

        runeItemId?.let {
            if (amountInInventory(player, it) < 20) {
                sendNPCDialogue(
                    player,
                    NPCs.ENIOLA_6362,
                    "I'm afraid you don't have the necessary runes with you at this time, so " +
                    "I can't allow you to access your account. Please bring 20 runes of one type " +
                    "and you'll be able to open your bank.",
                    FacialExpression.NEUTRAL
                )

                return true
            }

            if (removeItem(player, Item(it, 20))) {
                when (getAttribute(player, "zmi:bankaction", "")) {
                    "open" -> openBankAccount(player)
                    "collect" -> openGrandExchangeCollectionBox(player)
                }
            }
        }

        return true
    }

    override fun defineInterfaceListeners() {
        on(Components.BANK_CHARGE_ZMI_619, ::handleButtonClick)
    }
}