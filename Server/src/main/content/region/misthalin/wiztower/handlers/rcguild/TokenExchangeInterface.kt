package content.region.misthalin.wiztower.handlers.rcguild

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items

/**
 * Runecrafting Guild Token Exchance Interface
 * Uses 729.cs2 to change button colors if you have enough tokens. Also uses 730.cs2 I think.
 * Source: https://www.youtube.com/watch?v=fOMuuvjy4U0, https://www.youtube.com/watch?v=o4ZDpuCcIcQ
 */

class TokenExchangeInterface : InterfaceListener {

    private val RC_EXCHANGE = Components.RCGUILD_REWARDS_779
    private val TOKENS_INV = 135
    private val ITEM_BUY = 136

    // attributes to track selection
    private val SELECTED_ITEM = "rc_guild_item"
    private val SELECTED_QTY = "rc_guild_qty"
    private val PURCH_PRICE = "rc_guild_price"

    override fun defineInterfaceListeners() {
        onOpen(RC_EXCHANGE) {player, _ ->
            setInterfaceText(player, "Tokens: ${amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)}", RC_EXCHANGE, TOKENS_INV)
            return@onOpen true
        }

        on(RC_EXCHANGE) {player, _, opcode, buttonID, _, _ ->

            // mark the price on the buy button. 155 is buying 1, 196 is buying X
            if (opcode == 155 && buttonID != 163) {
                val item = getItem(buttonID)
                setAttribute(player, SELECTED_ITEM, item.id)

                setInterfaceText(player, "Tokens: ${amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)}", RC_EXCHANGE, TOKENS_INV)
                setInterfaceText(player, "${item.name} (1)", RC_EXCHANGE, ITEM_BUY)
                setAttribute(player, SELECTED_QTY, 1)
                setAttribute(player, PURCH_PRICE, item.price)

            } else if (opcode == 196 && buttonID != 163) {
                val item = getItem(buttonID)
                setAttribute(player, SELECTED_ITEM, item.id)

                sendInputDialogue(player, InputType.AMOUNT, "Enter the amount to buy:") { value ->
                    val amt = value as Int
                    val price = item.price * amt
                    if (amt > 0) {
                        setAttribute(player, SELECTED_QTY, amt)
                        setAttribute(player, PURCH_PRICE, price)
                        setInterfaceText(player, "Tokens: ${amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)}", RC_EXCHANGE, TOKENS_INV)
                        setInterfaceText(player, "${item.name} ($amt)", RC_EXCHANGE, ITEM_BUY)
                    }
                }
            }

            // the actual buy button
            if (buttonID == 163) {
                // purchase
                val selected = getAttribute(player, SELECTED_ITEM, 0)
                val quantity = getAttribute(player, SELECTED_QTY, 0)
                val price = getAttribute(player, PURCH_PRICE, 0)

                if (selected != 0 && quantity > 0) {
                    attemptPurchase(player, selected, price, quantity)
                    removeAttribute(player, SELECTED_ITEM)
                    removeAttribute(player, SELECTED_QTY)
                    removeAttribute(player, PURCH_PRICE)
                } else {
                    sendDialogue(player, "Please select an item first.")
                }
            }

            return@on true
        }

    }

    private fun getItem(buttonID: Int): ShopItem {
        return when(buttonID) {
            6  -> ShopItem(Items.AIR_TALISMAN_1438, 50, "Air talisman")
            13 -> ShopItem(Items.MIND_TALISMAN_1448, 50, "Mind talisman")
            15 -> ShopItem(Items.WATER_TALISMAN_1444, 50, "Water talisman")
            10 -> ShopItem(Items.EARTH_TALISMAN_1440, 50, "Earth talisman")
            11 -> ShopItem(Items.FIRE_TALISMAN_1442, 50, "Fire talisman")
            7 -> ShopItem(Items.BODY_TALISMAN_1446, 50, "Body talisman")
            9 -> ShopItem(Items.COSMIC_TALISMAN_1454, 125, "Cosmic talisman")
            8 -> ShopItem(Items.CHAOS_TALISMAN_1452, 125, "Chaos talisman")
            14 -> ShopItem(Items.NATURE_TALISMAN_1462, 125, "Nature talisman")
            12 -> ShopItem(Items.LAW_TALISMAN_1458, 125, "Law talisman")

            36 -> ShopItem(Items.RUNECRAFTER_HAT_13625, 1000, "Blue hat")
            39 -> ShopItem(Items.RUNECRAFTER_ROBE_13624, 1000, "Blue robe")
            42 -> ShopItem(Items.RUNECRAFTER_SKIRT_13627, 1000, "Blue bottoms")
            45 -> ShopItem(Items.RUNECRAFTER_GLOVES_13628, 1000, "Blue gloves")

            37 -> ShopItem(Items.RUNECRAFTER_HAT_13615, 1000, "Yellow hat")
            40 -> ShopItem(Items.RUNECRAFTER_ROBE_13614, 1000, "Yellow robe")
            43 -> ShopItem(Items.RUNECRAFTER_SKIRT_13617, 1000, "Yellow bottoms")
            46 -> ShopItem(Items.RUNECRAFTER_GLOVES_13618, 1000, "Yellow gloves")

            38 -> ShopItem(Items.RUNECRAFTER_HAT_13620, 1000, "Green hat")
            41 -> ShopItem(Items.RUNECRAFTER_ROBE_13619, 1000, "Green robe")
            44 -> ShopItem(Items.RUNECRAFTER_SKIRT_13622, 1000, "Green bottoms")
            47 -> ShopItem(Items.RUNECRAFTER_GLOVES_13623, 1000, "Green gloves")

            72 -> ShopItem(Items.AIR_ALTAR_TELEPORT_13599, 30, "Air altar tab")
            80 -> ShopItem(Items.MIND_ALTAR_TELEPORT_13600, 32, "Mind altar tab")
            83 -> ShopItem(Items.WATER_ALTAR_TELEPORT_13601, 34, "Water altar tab")
            77 -> ShopItem(Items.EARTH_ALTAR_TELEPORT_13602, 36, "Earth altar tab")
            78 -> ShopItem(Items.FIRE_ALTAR_TELEPORT_13603, 37, "Fire altar tab")
            73 -> ShopItem(Items.BODY_ALTAR_TELEPORT_13604, 38, "Body altar tab")
            75 -> ShopItem(Items.COSMIC_ALTAR_TELEPORT_13605, 39, "Cosmic altar tab")
            74 -> ShopItem(Items.CHAOS_ALTAR_TELEPORT_13606, 40, "Chaos altar tab")
            81 -> ShopItem(Items.ASTRAL_ALTAR_TELEPORT_13611, 41, "Astral altar tab")
            82 -> ShopItem(Items.NATURE_ALTAR_TELEPORT_13607, 42, "Nature altar tab")
            79 -> ShopItem(Items.LAW_ALTAR_TELEPORT_13608, 43, "Law altar tab")
            76 -> ShopItem(Items.DEATH_ALTAR_TELEPORT_13609, 44, "Death altar tab")
            84 -> ShopItem(Items.BLOOD_ALTAR_TELEPORT_13610, 45, "Blood altar tab")
            85 -> ShopItem(Items.RUNECRAFTING_GUILD_TELEPORT_13598, 15, "RC guild tab")

            114 -> ShopItem(Items.RUNECRAFTING_STAFF_13629, 10000, "Runecrafting staff")
            115 -> ShopItem(Items.PURE_ESSENCE_7937, 1, "Pure essence")
            else -> ShopItem(0, 0, "")
        }
    }

    fun attemptPurchase(player: Player, item: Int, price: Int, qty: Int) {
        if (amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650) < price) {
            sendDialogue(player, "You don't have enough tokens for that.")
            return
        }

        if (player.inventory.add(Item(item, qty))) {
            removeItem(player, Item(Items.RUNECRAFTING_GUILD_TOKEN_13650, price), Container.INVENTORY, )
            setInterfaceText(player, "Tokens: ${amountInInventory(player, Items.RUNECRAFTING_GUILD_TOKEN_13650)}", RC_EXCHANGE, TOKENS_INV)
            setInterfaceText(player, "", RC_EXCHANGE, ITEM_BUY)
            sendMessage(player, "Your purchase has been added to your inventory.")
        } else {
            sendDialogue(player, "You don't have enough inventory space for that.")
        }
    }

    internal class ShopItem(val id: Int, val price: Int, val name: String)
}