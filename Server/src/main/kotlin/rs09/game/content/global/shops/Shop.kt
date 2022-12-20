package rs09.game.content.global.shops

import api.*
import api.events.ItemShopPurchaseEvent
import api.events.ItemShopSellEvent
import core.game.component.Component
import core.game.container.Container
import core.game.container.ContainerEvent
import core.game.container.ContainerListener
import core.game.container.ContainerType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs09.consts.Components
import org.rs09.consts.Items
import rs09.ServerConstants
import rs09.game.content.global.shops.Shops.Companion.logShop
import rs09.game.world.GameWorld
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.ceil
import kotlin.math.roundToInt

data class ShopItem(var itemId: Int, var amount: Int, val restockRate: Int = 100)

class ShopListener(val player: Player) : ContainerListener
{
    var enabled = false
    override fun update(c: Container?, event: ContainerEvent?) {
        PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, -1, -1, 92, event!!.items, false, *event.slots))
    }

    override fun refresh(c: Container?) {
        PacketRepository.send(ContainerPacket::class.java, ContainerContext(player, -1, -1, 92, c!!.toArray(), c.capacity(), false))
    }
}

class Shop(val title: String, val stock: Array<ShopItem>, val general: Boolean = false, val currency: Int = Items.COINS_995, val highAlch: Boolean = false)
{
    val stockInstances = HashMap<Int, Container>()
    val playerStock = if (general) generalPlayerStock else Container(40, ContainerType.SHOP)
    val needsUpdate = HashMap<Int, Boolean>()
    val restockRates = HashMap<Int,Int>()

    init {
        if(!getServerConfig().getBoolean(Shops.personalizedShops, false))
            stockInstances[ServerConstants.SERVER_NAME.hashCode()] = generateStockContainer()
    }

    fun openFor(player: Player)
    {
        val cont = getContainer(player)
        setInterfaceText(player, title, 620, 22)
        setAttribute(player, "shop", this)
        setAttribute(player, "shop-cont", cont)
        openInterface(player, Components.SHOP_TEMPLATE_620)
        player.interfaceManager.openSingleTab(Component(Components.SHOP_TEMPLATE_SIDE_621))
        showTab(player, true)
        logShop("Opening shop [Title: $title, Player: ${player.username}]")
    }

    fun showTab(player: Player, main: Boolean)
    {
        val cont = if (main) getAttribute<Container?>(player, "shop-cont", null) ?: return else playerStock

        if(!main)
        {
            cont.listeners.remove(listenerInstances[player.details.uid])
            playerStock.listeners.add(listenerInstances[player.details.uid])
        }
        else
        {
            playerStock.listeners.remove(listenerInstances[player.details.uid])
            cont.listeners.add(listenerInstances[player.details.uid])
        }

        val settings = IfaceSettingsBuilder()
            .enableOptions(0..9)
            .build()

        player.packetDispatch.sendIfaceSettings(settings, if (main) 23 else 24, Components.SHOP_TEMPLATE_620, 0, cont.capacity())
        player.packetDispatch.sendRunScript(150, "IviiiIsssssssss", "", "", "", "", "Buy X", "Buy 10", "Buy 5", "Buy 1", "Value", -1, 0, 4, 10, 92, (620 shl 16) or if (main) 23 else 24)
        player.packetDispatch.sendInterfaceConfig(620, 23, !main)
        player.packetDispatch.sendInterfaceConfig(620, 24, main)
        player.packetDispatch.sendInterfaceConfig(620, 29, !main)
        player.packetDispatch.sendInterfaceConfig(620, 25, main)
        player.packetDispatch.sendInterfaceConfig(620, 27, main)
        player.packetDispatch.sendInterfaceConfig(620, 26, false)

        if (!main) playerStock.refresh()
        else cont.refresh()

        setAttribute(player, "shop-main", main)
    }

    public fun getContainer(player: Player) : Container
    {
        val container = if(getServerConfig().getBoolean(Shops.personalizedShops, false))
            stockInstances[player.details.uid] ?: generateStockContainer().also { stockInstances[player.details.uid] = it }
        else
            stockInstances[ServerConstants.SERVER_NAME.hashCode()]!!

        val listener = listenerInstances[player.details.uid]

        if(listener != null && listener.player != player)
        {
            container.listeners.remove(listener)
        }

        if(listener == null || listener.player != player)
        {
            listenerInstances[player.details.uid] = ShopListener(player)
        }

        return container
    }

    private fun generateStockContainer(): Container
    {
        val container = Container(40, ContainerType.SHOP)
        for(item in stock) {
            container.add(Item(item.itemId,item.amount))
            restockRates[item.itemId] = item.restockRate
        }

        return container
    }

    fun restock()
    {
        stockInstances.filter { needsUpdate[it.key] == true }.forEach{ (player,cont) ->
            for(i in 0 until cont.capacity())
            {
                if(cont[i] == null) continue
                if(stock.size < i + 1) break
                if(GameWorld.ticks % stock[i].restockRate != 0) continue

                if(cont[i].amount < stock[i].amount){
                    cont[i].amount++
                    cont.event.flag(i, cont[i])
                }
                else if(cont[i].amount > stock[i].amount){
                    cont[i].amount--
                    cont.event.flag(i, cont[i])
                }
                if(cont[i].amount != stock[i].amount) needsUpdate[player] = true
            }
            cont.update()
        }
    }

    fun getBuyPrice(player: Player, slot: Int): Item
    {
        val isMainStock = getAttribute(player, "shop-main", true)
        val cont = if (isMainStock) getAttribute<Container?>(player, "shop-cont", null) ?: return Item(-1,-1) else playerStock
        val item = cont[slot]
        val price = when(currency)
        {
            Items.TOKKUL_6529 -> item.definition.getConfiguration("tokkul_price", 1)
            Items.ARCHERY_TICKET_1464 -> item.definition.getConfiguration("archery_ticket_price", 1)
            else -> getGPCost(Item(item.id, 1), if (isMainStock) stock[item.slot].amount else playerStock[slot].amount, if (isMainStock) item.amount else playerStock[slot].amount)
        }

        return Item(currency, price)
    }

    fun getSellPrice(player: Player, slot: Int): Pair<Container?,Item>
    {
        val shopCont = getAttribute<Container?>(player, "shop-cont", null) ?: return Pair(null, Item(-1,-1))
        val item = player.inventory[slot]
        val shopItemId = if (item.definition.isUnnoted) {
            item.id
        }
        else
        {
            item.noteChange
        }
        var (isPlayerStock, shopSlot) = getStockSlot(shopItemId)

        val stockAmt =
            if(isPlayerStock)
                0
            else{
                if(shopSlot != -1) stock[shopSlot].amount
                else 0
            }
        val currentAmt =
            if(isPlayerStock) playerStock.getAmount(shopItemId)
            else {
                if(shopSlot != -1) shopCont[shopSlot].amount
                else {
                    isPlayerStock = true
                    0
                }
            }

        val price = when(currency)
        {
            Items.TOKKUL_6529 -> (item.definition.getConfiguration("tokkul_price", 1) / 10.0).toInt()  // selling items authentically return 10x less tokkul (floored/truncated) than the item's shop price
            Items.ARCHERY_TICKET_1464 -> item.definition.getConfiguration("archery_ticket_price", 1)
            else -> getGPSell(Item(shopItemId, 1), stockAmt, currentAmt)
        }

        if(!general && stockAmt == 0 && shopSlot == -1)
        {
            return Pair(null, Item(-1,-1))
        }

        return Pair(if (isPlayerStock) playerStock else shopCont, Item(currency, price))
    }

    private fun getGPCost(item: Item, stockAmount: Int, currentAmt: Int): Int
    {
        var mod: Int
        mod = if(stockAmount == 0) 100
        else if(currentAmt == 0) 130
        else if(currentAmt >= stockAmount) 100
        else 130 - (130 - 100) * currentAmt / stockAmount
        if(mod < 1) mod = 1
        mod = max(100, min(130, mod))


        val price: Int = ceil(item.definition.value * mod.toDouble() / 100.0).toInt()
/*        if(player.getVarp(532) == 6529){
            price = 3 * price / 2
        }*/
        return max(price, 1)
    }

    private fun getGPSell(item: Item, stockAmount: Int, currentAmt: Int): Int
    {
        val base = item.definition.getAlchemyValue(highAlch)
        var overstock = currentAmt - stockAmount
        if (overstock < 0) {
            return base
        }
        if (overstock > 10) {
            overstock = 10
        }
        val price = (base - (item.definition.value * 0.03 * overstock)).roundToInt()
        if (price < 1) {
            return 1
        }
        return price
    }

    fun buy(player: Player, slot: Int, amount: Int) : TransactionStatus
    {
        if(amount !in 1..Integer.MAX_VALUE) return TransactionStatus.Failure("Invalid amount: $amount")
        val isMainStock = getAttribute(player, "shop-main", false)
        if(!isMainStock && player.ironmanManager.isIronman)
        {
            sendDialogue(player, "As an ironman, you cannot buy from player stock in shops.")
            return TransactionStatus.Failure("Ironman buying from player stock")
        }
        val cont = if (isMainStock) (getAttribute<Container?>(player, "shop-cont", null) ?: return TransactionStatus.Failure("Invalid shop-cont attr")) else playerStock
        val inStock = cont[slot]
        val item = Item(inStock.id, amount)
        if(inStock.amount < amount)
            item.amount = inStock.amount
        if (item.amount > player.inventory.getMaximumAdd(item))
            item.amount = player.inventory.getMaximumAdd(item)

        if(inStock.amount == 0) {
            sendMessage(player, "This item is out of stock.")
            return TransactionStatus.Failure("Shop item out of stock.")
        }

        if(isMainStock && inStock.amount > stock[slot].amount && !getServerConfig().getBoolean(Shops.personalizedShops, false) && player.ironmanManager.isIronman)
        {
            sendDialogue(player, "As an ironman, you cannot buy overstocked items from shops.")
            return TransactionStatus.Failure("Ironman overstock purchase")
        }

        val cost = getBuyPrice(player, slot)
        if(cost.id == -1) sendMessage(player, "This shop cannot sell that item.").also { return TransactionStatus.Failure("Shop cannot sell this item")}

        if(currency == Items.COINS_995){
            var amt = item.amount
            var inStockAmt = inStock.amount
            while(amt-- > 1)
                cost.amount += getGPCost(Item(item.id, 1), if (isMainStock) stock[slot].amount else playerStock[slot].amount, --inStockAmt)
        } else {
          cost.amount = cost.amount * item.amount
        }

        if(inInventory(player, cost.id, cost.amount))
        {
            if(removeItem(player, cost))
            {
                if (item.amount == 0 && amountInInventory(player, cost.id) == 0) {
                    item.amount = 1
                }
                if(!hasSpaceFor(player, item)) {
                    addItem(player, cost.id, cost.amount)
                    sendMessage(player, "You don't have enough inventory space to buy that many.")
                    return TransactionStatus.Failure("Not enough inventory space")
                }

                if(!isMainStock && cont[slot].amount - item.amount == 0)
                {
                    cont.remove(cont[slot], false)
                    cont.refresh()
                }
                else {
                    cont[slot].amount -= item.amount
                    cont.event.flag(slot, cont[slot])
                    cont.update()
                }

                addItem(player, item.id, item.amount)

                if(getServerConfig().getBoolean(Shops.personalizedShops, false)){
                    needsUpdate[player.details.uid] = true
                } else {
                    needsUpdate[ServerConstants.SERVER_NAME.hashCode()] = true
                }
            }
        }
        else
        {
            sendMessage(player, "You don't have enough ${cost.name.toLowerCase()} to buy that many.")
        }

        player.dispatch(ItemShopPurchaseEvent(item.id, item.amount, cost))
        return TransactionStatus.Success()
    }

    fun sell(player: Player, slot: Int, amount: Int) : TransactionStatus
    {
        if(amount !in 1..Integer.MAX_VALUE) return TransactionStatus.Failure("Invalid amount: $amount")
        val playerInventory = player.inventory[slot]
        if(playerInventory.id in intArrayOf(Items.COINS_995, Items.TOKKUL_6529, Items.ARCHERY_TICKET_1464))
        {
            sendMessage(player, "You can't sell currency to a shop.")
            return TransactionStatus.Failure("Tried to sell currency - ${playerInventory.id}")
        }
        val item = Item(playerInventory.id, amount)
        val def = itemDefinition(item.id)

        if (def.hasDestroyAction()) {
            sendMessage(player, "You can't sell this item.")
            return TransactionStatus.Failure("Attempt to sell a destroyable - ${playerInventory.id}.")
        }

        if (!def.isTradeable) {
            sendMessage(player, "You can't sell this item.")
            return TransactionStatus.Failure("Attempt to sell an untradeable - ${playerInventory.id}.")
        }

        val (container,profit) = getSellPrice(player, slot)
        if(profit.amount == -1) sendMessage(player, "This item can't be sold to this shop.").also { return TransactionStatus.Failure("Can't sell this item to this shop - ${playerInventory.id}, general: $general, price: $profit") }
        if(amount > player.inventory.getAmount(item.id))
            item.amount = player.inventory.getAmount(item.id)

        if(currency == Items.COINS_995 && item.amount > 1){
            val id = if(!item.definition.isUnnoted) item.noteChange else item.id
            val (isPlayerStock, shopSlot) = getStockSlot(id)
            var amt = item.amount
            var inStockAmt = container!![shopSlot]?.amount ?: playerStock.getAmount(id)
            while(amt-- > 1)
                profit.amount += getGPSell(Item(item.id, 1), if (isPlayerStock) 0 else stock[shopSlot].amount, ++inStockAmt)
        } else {
            profit.amount = profit.amount * item.amount
        }

        if(removeItem(player, item))
        {
            if(!hasSpaceFor(player, profit)){
                sendMessage(player, "You don't have enough space to do that.")
                addItem(player, item.id, item.amount)
                return TransactionStatus.Failure("Did not have enough inventory space")
            }
            if(container == playerStock && getAttribute(player, "shop-main", false)){
                showTab(player, false)
            }
            else if(!getAttribute(player, "shop-main", false) && container != playerStock)
            {
                showTab(player, true)
            }
            addItem(player, profit.id, profit.amount)
            if(!item.definition.isUnnoted)
            {
                item.id = item.noteChange
            }
            container?.add(item)
            container?.refresh()
            if(getServerConfig().getBoolean(Shops.personalizedShops, false)){
                needsUpdate[player.details.uid] = true
            } else {
                needsUpdate[ServerConstants.SERVER_NAME.hashCode()] = true
            }
        }

        player.dispatch(ItemShopSellEvent(item.id, item.amount, profit))
        return TransactionStatus.Success()
    }

    fun getStockSlot(itemId: Int): Pair<Boolean, Int>
    {
        var shopSlot: Int = -1
        var isPlayerStock = false
        val notechange = itemDefinition(itemId).noteId
        for((stockSlot, shopItem) in stock.withIndex())
        {
            if(shopItem.itemId == itemId || shopItem.itemId == notechange)
                shopSlot = stockSlot
        }
        if(shopSlot == -1)
        {
            for((stockSlot, playerStockItem) in playerStock.toArray().withIndex())
            {
                if(playerStockItem == null) continue
                if(playerStockItem.id == itemId || playerStockItem.id == notechange) {
                    shopSlot = stockSlot
                    isPlayerStock = true
                }
            }
        }

        if(shopSlot == -1) isPlayerStock = true
        return Pair(isPlayerStock, shopSlot)
    }

    companion object {
        //General stores globally share player stock (weird quirk, right?)
        val generalPlayerStock = Container(40, ContainerType.SHOP)
        val listenerInstances = HashMap<Int, ShopListener>()
    }

    sealed class TransactionStatus {
        class Success : TransactionStatus()
        class Failure(val reason: String) : TransactionStatus()
    }
}
