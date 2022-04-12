package rs09.game.content.global.shops

import api.*
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
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.ceil

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
    private val needsUpdate = HashMap<Int, Boolean>()
    private val restockRates = HashMap<Int,Int>()

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
            cont.listeners.remove(listenerInstances[player.username.hashCode()])
            playerStock.listeners.add(listenerInstances[player.username.hashCode()])
        }
        else
        {
            playerStock.listeners.remove(listenerInstances[player.username.hashCode()])
            cont.listeners.add(listenerInstances[player.username.hashCode()])
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

    private fun getContainer(player: Player) : Container
    {
        val container = if(getServerConfig().getBoolean(Shops.personalizedShops, false))
            stockInstances[player.username.hashCode()] ?: generateStockContainer().also { stockInstances[player.username.hashCode()] = it }
        else
            stockInstances[ServerConstants.SERVER_NAME.hashCode()]!!

        val listener = listenerInstances[player.username.hashCode()]

        if(listener != null && listener.player != player)
        {
            container.listeners.remove(listener)
        }

        if(listener == null || listener.player != player)
        {
            listenerInstances[player.username.hashCode()] = ShopListener(player)
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
        var (isPlayerStock, shopSlot) = getStockSlot(item.id)

        val stockAmt =
            if(isPlayerStock)
                0
            else{
                if(shopSlot != -1) stock[shopSlot].amount
                else 0
            }
        val currentAmt =
            if(isPlayerStock) playerStock.getAmount(item.id)
            else {
                if(shopSlot != -1) shopCont[shopSlot].amount
                else {
                    isPlayerStock = true
                    0
                }
            }

        val price = when(currency)
        {
            Items.TOKKUL_6529 -> item.definition.getConfiguration("tokkul_price", 1)
            Items.ARCHERY_TICKET_1464 -> item.definition.getConfiguration("archery_ticket_price", 1)
            else -> getGPSell(Item(item.id, 1), stockAmt, currentAmt)
        }

        if(!general && stockAmt == 0 && shopSlot == -1)
        {
            return Pair(null, Item(-1,-1))
        }

        return Pair(if (isPlayerStock) playerStock else shopCont, Item(currency, price))
    }

    private fun getGPCost(item: Item, stockAmount: Int, currentAmt: Int): Int{
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

    private fun getGPSell(item: Item, stockAmount: Int, currentAmt: Int): Int{
        if(!item.definition.isUnnoted)
            item.id = item.noteChange
        var mod: Int
        mod = if(stockAmount == 0) 70
        else if(currentAmt == 0) 100
        else if(currentAmt >= stockAmount) 70
        else 100 - (100 - 70) * currentAmt / stockAmount
        if(mod < 1) mod = 1
        mod = max(70, min(100, mod))

        var base = if (highAlch) item.definition.getAlchemyValue(true) else item.definition.value
        base = max(base, item.definition.value)

        val price: Int = ceil(base * mod.toDouble() / 100.0).toInt()
        return max(price, 1)
    }

    fun buy(player: Player, slot: Int, amount: Int)
    {
        if(amount !in 1..Integer.MAX_VALUE) return
        val isMainStock = getAttribute(player, "shop-main", false)
        if(!isMainStock && player.ironmanManager.isIronman)
        {
            sendDialogue(player, "As an ironman, you cannot buy from player stock in shops.")
            return
        }
        val cont = if (isMainStock) getAttribute<Container?>(player, "shop-cont", null) ?: return else playerStock
        val inStock = cont[slot]
        val item = Item(inStock.id, amount)
        if(inStock.amount < amount)
            item.amount = inStock.amount

        if(inStock.amount > stock[slot].amount && !getServerConfig().getBoolean(Shops.personalizedShops, false))
        {
            sendDialogue(player, "As an ironman, you cannot buy overstocked items from shops.")
            return
        }

        val cost = getBuyPrice(player, slot)
        if(cost.id == -1) sendMessage(player, "This shop cannot sell that item.").also { return }

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
                if(!hasSpaceFor(player, item)) {
                    addItem(player, cost.id, cost.amount)
                    sendMessage(player, "You don't have enough inventory space to buy that many.")
                    return
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
                    needsUpdate[player.username.hashCode()] = true
                } else {
                    needsUpdate[ServerConstants.SERVER_NAME.hashCode()] = true
                }
            }
        }
        else
        {
            sendMessage(player, "You don't have enough ${cost.name.toLowerCase()} to buy that many.")
        }
    }

    fun sell(player: Player, slot: Int, amount: Int)
    {
        if(amount !in 1..Integer.MAX_VALUE) return
        val playerInventory = player.inventory[slot]
        if(playerInventory.id in intArrayOf(Items.COINS_995, Items.TOKKUL_6529, Items.ARCHERY_TICKET_1464))
        {
            sendMessage(player, "You can't sell currency to a shop.")
            return
        }
        val item = Item(playerInventory.id, amount)
        val (container,profit) = getSellPrice(player, slot)
        if(profit.amount == -1) sendMessage(player, "This item can't be sold to this shop.").also { return }
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
                return
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
                needsUpdate[player.username.hashCode()] = true
            } else {
                needsUpdate[ServerConstants.SERVER_NAME.hashCode()] = true
            }
        }
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

        return Pair(isPlayerStock, shopSlot)
    }

    companion object {
        //General stores globally share player stock (weird quirk, right?)
        val generalPlayerStock = Container(40, ContainerType.SHOP)
        val listenerInstances = HashMap<Int, ShopListener>()
    }
}