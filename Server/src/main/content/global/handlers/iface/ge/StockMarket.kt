package content.global.handlers.iface.ge

import core.api.*
import core.game.component.Component
import core.game.ge.OfferState
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ConfigContext
import core.net.packet.context.ContainerContext
import core.net.packet.out.Config
import core.net.packet.out.ContainerPacket
import org.rs09.consts.Components
import core.game.ge.GrandExchange
import core.game.ge.GrandExchangeOffer
import core.game.ge.GrandExchangeRecords
import core.game.ge.PriceIndex
import core.game.interaction.InterfaceListener
import core.tools.Log
import core.tools.SystemLogger
import org.rs09.consts.Sounds
import kotlin.math.min

/**
 * Handles the grand exchange interface (Stock Market)
 * @author Ceikry
 */
class StockMarket : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.STOCKMARKET_105){player, _ ->
            player.packetDispatch.sendInterfaceConfig(105, 193, true)
            player.packetDispatch.sendIfaceSettings(6, 211, 105, -1, -1)
            player.packetDispatch.sendIfaceSettings(6, 209, 105, -1, -1)
            setVarp(player, 1112, -1)
            return@onOpen true
        }

        onClose(Components.STOCKMARKET_105){player, _ ->
            player.packetDispatch.sendRunScript(571, "")
            player.interfaceManager.closeChatbox()
            player.interfaceManager.closeSingleTab()
        }

        onOpen(Components.OBJDIALOG_389){player, _ ->
            player.packetDispatch.sendRunScript(570, "s", "Grand Exchange Item Search")
            return@onOpen true
        }

        onClose(Components.OBJDIALOG_389){player, _ ->
            player.packetDispatch.sendRunScript(5781, "")
            return@onClose true
        }

        on(Components.STOCKSIDE_107){ player, _, op, button, slot, _ ->
            if (button != 18 || slot < 0 || slot > 27) {
                return@on false
            }
            val item = player.inventory[slot] ?: return@on false
            when (op) {
                196 -> player.packetDispatch.sendMessage(item.definition.examine)
                155 -> {
                    val offer = getAttribute(player, "ge-temp", GrandExchangeOffer())
                    val index = getAttribute(player, "ge-index", -1)
                    if(item.id == 995)
                    {
                        sendMessage(player, "You can't sell money!")
                        return@on true
                    }
                    var id = item.id
                    if(!item.definition.isUnnoted)
                        id = item.noteChange
                    if(!PriceIndex.canTrade(id))
                    {
                        sendMessage(player, "This item can't be sold on the Grand Exchange.")
                        return@on true
                    }
                    offer.itemID = id
                    offer.sell = true
                    offer.player = player
                    offer.offeredValue = GrandExchange.getRecommendedPrice(id)
                    offer.amount = item.amount
                    offer.index = index
                    updateVarbits(player, offer, index, true)
                    setAttribute(player, "ge-temp", offer)
                    player.packetDispatch.sendString(GrandExchange.getOfferStats(id, true), 105, 142)
                }
            }
            return@on true
        }

        on(Components.STOCKMARKET_105){player, _, op, button, _, _ ->
            val tempOffer = getAttribute(player, "ge-temp", GrandExchangeOffer())
            var openedIndex = getAttribute(player, "ge-index", -1)
            var openedOffer = GrandExchangeRecords.getInstance(player).getOffer(openedIndex)

            when(button)
            {
                209,211 -> if (openedOffer == null){
                    SystemLogger.logGE("[WARN] Player tried to withdraw item with null openedOffer!")
                    return@on false
                } else withdraw(player, openedOffer, (button - 209) shr 1, op)
                190 -> confirmOffer(player, tempOffer, openedIndex).also { return@on true }
                194 -> player.interfaceManager.openChatbox(Components.OBJDIALOG_389)
                203 -> abortOffer(player, openedOffer)
                18,34,50,69,88,107 -> {
                    openedIndex = (button - 18) shr 4
                    openedOffer = GrandExchangeRecords.getInstance(player).getOffer(openedIndex)
                    if(op == 205) abortOffer(player, openedOffer)
                    else updateVarbits(player, openedOffer, openedIndex)
                    if(openedOffer != null) {
                        player.packetDispatch.sendString(GrandExchange.getOfferStats(openedOffer.itemID, openedOffer.sell), 105, 142)
                    }
                }
                30,46,62,81,100,119 -> {
                    openedIndex = (button - 30) shr 4
                    openedOffer = GrandExchangeRecords.getInstance(player).getOffer(openedIndex)
                    updateVarbits(player, openedOffer, openedIndex)
                    player.interfaceManager.openChatbox(Components.OBJDIALOG_389)
                }
                31,47,63,82,101,120 -> {
                    openedIndex = (button - 31) shr 4
                    openedOffer = GrandExchangeRecords.getInstance(player).getOffer(openedIndex)
                    updateVarbits(player, openedOffer, openedIndex, true)
                    player.interfaceManager.openSingleTab(Component(Components.STOCKSIDE_107)).open(player)
                    player.packetDispatch.sendRunScript(
                            149, "IviiiIsssss", "", "", "", "Examine", "Offer",
                            -1, 0, 7, 4, 93, 7012370
                    )
                    val settings = IfaceSettingsBuilder().enableOptions(0, 1).build()
                    player.packetDispatch.sendIfaceSettings(settings, 18, 107, 0, 27)
                }
                157 -> updateOfferAmount(player, tempOffer, tempOffer.amount - 1)
                159 -> updateOfferAmount(player, tempOffer, tempOffer.amount + 1)
                162 -> updateOfferAmount(player, tempOffer, if(tempOffer.sell) 1 else tempOffer.amount + 1)
                164 -> updateOfferAmount(player, tempOffer, if(tempOffer.sell) 10 else tempOffer.amount + 10)
                166 -> updateOfferAmount(player, tempOffer, if(tempOffer.sell) 100 else tempOffer.amount + 100)
                168 -> {
                    val amt =
                    if(tempOffer.sell)
                        getInventoryAmount(player, tempOffer.itemID)
                    else
                        tempOffer.amount + 1000

                    updateOfferAmount(player, tempOffer, amt)
                }
                170 -> sendInputDialogue(player, false, "Enter the amount:") { value ->
                    if (player.interfaceManager.chatbox.id == 389) {
                        player.interfaceManager.openChatbox(Components.OBJDIALOG_389)
                    }
                    var s = value.toString()
                    s = s.replace("k", "000")
                    s = s.replace("K", "000")
                    s = s.replace("m", "000000")
                    s = s.replace("M", "000000")
                    updateOfferAmount(player, tempOffer, s.toInt())
                    setAttribute(player, "ge-temp", tempOffer)
                }
                180 -> updateOfferValue(player, tempOffer, GrandExchange.getRecommendedPrice(tempOffer.itemID))
                177 -> updateOfferValue(player, tempOffer, (GrandExchange.getRecommendedPrice(tempOffer.itemID) * 0.95).toInt())
                183 -> updateOfferValue(player, tempOffer, (GrandExchange.getRecommendedPrice(tempOffer.itemID) * 1.05).toInt())
                171 -> updateOfferValue(player, tempOffer, tempOffer.offeredValue - 1)
                173 -> updateOfferValue(player, tempOffer, tempOffer.offeredValue + 1)
                185 -> sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:"){value ->
                    if (player.interfaceManager.chatbox.id == 389) {
                        player.interfaceManager.openChatbox(Components.OBJDIALOG_389)
                    }
                    var s = value.toString()
                    updateOfferValue(player, tempOffer, s.toInt())
                    setAttribute(player, "ge-temp", tempOffer)
                }
                195 -> closeInterface(player)
                127 -> {
                    player.interfaceManager.closeSingleTab()
                    player.interfaceManager.closeChatbox()
                }
            }

            setAttribute(player, "ge-index", openedIndex)
            setAttribute(player, "ge-temp", tempOffer)
            return@on true
        }
    }

    fun updateOfferValue(player: Player, offer: GrandExchangeOffer, newAmt: Int)
    {
        offer.offeredValue = newAmt
        setVarp(player, 1111, newAmt)
    }

    fun updateOfferAmount(player: Player, offer: GrandExchangeOffer, newAmt: Int)
    {
        offer.amount = newAmt
        setVarp(player, 1110, newAmt)
    }

    fun abortOffer(player: Player, offer: GrandExchangeOffer?)
    {
        if(offer == null)
        {
            log(this::class.java, Log.WARN,  "Opened offer was null and was attempted to be aborted!")
            return
        }
        sendMessage(player, "Abort request acknowledged. Please be aware that your offer may")
        sendMessage(player, "have already been completed.")

        if(!offer.isActive)
        {
            log(this::class.java, Log.WARN,  "Offer ${offer.uid}[${offer.index}]: ${if(offer.sell) "s" else "b"} ${offer.itemID}x ${offer.amount} was NO LONGER active when abort attempted")
            return
        }
        offer.offerState = OfferState.ABORTED
        if(offer.sell)
            offer.addWithdrawItem(offer.itemID, offer.amountLeft)
        else
            offer.addWithdrawItem(995, offer.amountLeft * offer.offeredValue)
        offer.update()
        offer.visualize(player)
    }

    enum class OfferConfirmResult {
        Success,
        ZeroCoins,
        TooManyCoins,
        NotEnoughItemsOrCoins,
        ItemRemovalFailure,
        OfferPlacementError
    }

    fun confirmOffer(player: Player, offer: GrandExchangeOffer, index: Int) : OfferConfirmResult
    {
        if(offer.offeredValue < 1)
        {
            playAudio(player, Sounds.GE_TRADE_ERROR_4039)
            sendMessage(player, "You can't make an offer for 0 coins.")
            return OfferConfirmResult.ZeroCoins
        }
        if(offer.amount > Int.MAX_VALUE / offer.offeredValue)
        {
            playAudio(player, Sounds.GE_TRADE_ERROR_4039)
            sendMessage(player, "You can't ${if(offer.sell) "sell" else "buy"} this much!")
            return OfferConfirmResult.TooManyCoins
        }
        offer.index = index
        if(offer.sell)
        {
            val maxAmt = getInventoryAmount(player, offer.itemID)
            if(offer.amount > maxAmt)
            {
                playAudio(player, Sounds.GE_TRADE_ERROR_4039)
                sendMessage(player, "You do not have enough of this item in your inventory to cover the")
                sendMessage(player, "offer.")
                return OfferConfirmResult.NotEnoughItemsOrCoins
            }
            val amountUnnoted = min(amountInInventory(player, offer.itemID), offer.amount)
            val amountLeft = offer.amount - amountUnnoted
            val removedUnnoted = if (amountUnnoted > 0) removeItem(player, Item(offer.itemID, amountUnnoted)) else true
            val removeNoted = if (amountLeft > 0) removeItem(player, Item(itemDefinition(offer.itemID).noteId, amountLeft)) else true
            if (!removedUnnoted || !removeNoted) return OfferConfirmResult.ItemRemovalFailure
            if(GrandExchange.dispatch(player, offer))
            {
                player.removeAttribute("ge-temp")
            }
            else
            {
                if (amountUnnoted > 0)
                    addItem(player, offer.itemID, offer.amount - amountLeft)
                if (amountLeft > 0)
                    addItem(player, itemDefinition(offer.itemID).noteId, amountLeft)
                sendMessage(player, "Unable to place GE offer. Please try again.")
                return OfferConfirmResult.OfferPlacementError
            }
        }
        else
        {
            val total = offer.amount * offer.offeredValue
            if(!inInventory(player, 995, total))
            {
                playAudio(player, Sounds.GE_TRADE_ERROR_4039)
                sendMessage(player, "You do not have enough coins to cover the offer.")
                return OfferConfirmResult.NotEnoughItemsOrCoins
            }
            if(GrandExchange.dispatch(player, offer) && removeItem(player, Item(995, total)))
            {
                player.removeAttribute("ge-temp")
            }
        }
        playAudio(player, Sounds.GE_PLACE_ITEM_4043)
        offer.visualize(player)
        toMainInterface(player)
        return OfferConfirmResult.Success
    }

    fun getInventoryAmount(player: Player, itemId: Int): Int {
        val item = Item(itemId)
        var amount = player.inventory.getAmount(item)
        if (item.definition.noteId > -1) {
            amount += player.inventory.getAmount(Item(item.definition.noteId))
        }
        return amount
    }

    companion object {
        @JvmStatic
        fun openFor(player: Player)
        {
            if(player.ironmanManager.checkRestriction())
                return
            if(!player.bankPinManager.isUnlocked)
            {
                player.bankPinManager.openType(4)
                return
            }

            openInterface(player, Components.STOCKMARKET_105)
        }

        @JvmStatic
        fun updateVarbits(player: Player, offer: GrandExchangeOffer?, index: Int, sale: Boolean? = null)
        {
            val isSale = sale ?: offer?.sell ?: false
            var lowPrice = 0
            var highPrice = 0
            val recommendedPrice = GrandExchange.getRecommendedPrice(offer?.itemID ?: 0)
            if (PriceIndex.canTrade(offer?.itemID ?: 0)) {
                lowPrice = (recommendedPrice * 0.95).toInt()
                highPrice = (recommendedPrice * 1.05).toInt()
            }
            setVarp(player, 1109, offer?.itemID ?: -1)
            setVarp(player, 1110, offer?.amount ?: 0)
            setVarp(player, 1111, offer?.offeredValue ?: 0)
            setVarp(player, 1112, index)
            setVarp(player, 1113, if (isSale) 1 else 0)
            setVarp(player, 1114, recommendedPrice)
            setVarp(player, 1115, lowPrice)
            setVarp(player, 1116, highPrice)
            if (offer != null) {
                PacketRepository.send(
                        ContainerPacket::class.java,
                        ContainerContext(player, -1, -1757, 523 + offer.index, offer.withdraw, false)
                )
            }
        }

        @JvmStatic
        fun withdraw(player: Player, offer: GrandExchangeOffer, index: Int, op: Int)
        {
            val item = offer.withdraw[index]
            if(item == null)
            {
                log(this::class.java, Log.WARN,  "Offer withdraw[$index] is null!")
                return
            }

            when (op) {
                // withdraw notes
                155 -> {
                    val note = item.noteChange
                    if (note == -1) {
                        sendMessage(player, "This item cannot be noted")
                        return
                    }
                    if (hasSpaceFor(player, Item(note, item.amount))) {
                        addItem(player, note, item.amount)
                    } else {
                        playAudio(player, Sounds.GE_TRADE_ERROR_4039)
                        sendMessage(player, "You do not have enough room in your inventory.")
                        return
                    }
                }
                // withdraw items
                196 -> {
                    if (hasSpaceFor(player, item)) {
                        addItem(player, item.id, item.amount)
                    } else {
                        playAudio(player, Sounds.GE_TRADE_ERROR_4039)
                        sendMessage(player, "You do not have enough room in your inventory.")
                        return
                    }
                }
            }

            offer.withdraw[index] = null
            if (!offer.isActive && offer.withdraw[0] == null && offer.withdraw[1] == null)
            {
                offer.offerState = OfferState.REMOVED
                if(offer.completedAmount > 0)
                {
                    val newHistory = arrayOfNulls<GrandExchangeOffer>(5)
                    newHistory[0] = offer
                    System.arraycopy(GrandExchangeRecords.getInstance(player).history, 0, newHistory, 1, 4)
                    GrandExchangeRecords.getInstance(player).history = newHistory
                }
                GrandExchangeRecords.getInstance(player).offerRecords[offer.index] = null
                toMainInterface(player)
            }
            offer.update()
            offer.visualize(player)
        }

        /**
         * Returns to the main interface.
         */
        fun toMainInterface(player: Player) {
            PacketRepository.send(Config::class.java, ConfigContext(player, 1112, -1))
            PacketRepository.send(Config::class.java, ConfigContext(player, 1112, -1))
            player.interfaceManager.closeChatbox()
            player.interfaceManager.closeSingleTab()
            player.setAttribute("ge-index", -1)
        }

    }
}
