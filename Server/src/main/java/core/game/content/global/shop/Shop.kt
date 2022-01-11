package core.game.content.global.shop

import api.amountInInventory
import rs09.game.world.GameWorld.ticks
import rs09.game.system.SystemLogger.logInfo
import core.game.node.entity.player.link.diary.DiaryType
import core.cache.def.impl.ItemDefinition
import core.game.container.Container
import core.game.container.ContainerType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items.BLACK_CHAINBODY_1107
import org.rs09.consts.Items.BOWL_1923
import org.rs09.consts.Items.BUCKET_1925
import org.rs09.consts.Items.CAKE_TIN_1887
import org.rs09.consts.Items.CANDLE_36
import org.rs09.consts.Items.CHAOS_RUNE_562
import org.rs09.consts.Items.CHISEL_1755
import org.rs09.consts.Items.DEATH_RUNE_560
import org.rs09.consts.Items.EMPTY_POT_1931
import org.rs09.consts.Items.HAMMER_2347
import org.rs09.consts.Items.JUG_1935
import org.rs09.consts.Items.NEWCOMER_MAP_550
import org.rs09.consts.Items.SECURITY_BOOK_9003
import org.rs09.consts.Items.SHEARS_1735
import org.rs09.consts.Items.TINDERBOX_590
import org.rs09.consts.Items.TOKKUL_6529
import rs09.game.system.config.ItemConfigParser
import java.lang.IndexOutOfBoundsException
import java.util.Arrays
import java.util.ArrayList

/**
 * A class representing a shop.
 *
 * @author 'Vexia
 * @author Jamix77
 */
open class Shop @JvmOverloads constructor(
    /**
     * Represents the title of the shop.
     */
    val title: String,
    /**
     * Represents the items in the store.
     */
    var items: Array<Item>,
    /**
     * Represents if it's a general store.
     */
    val isGeneral: Boolean,
    /**
     * Represents the currency the shop allows.
     */
    val currency: Int = COINS,
    /**
     * If the shop buys for high alch.
     */
    val isHighAlch: Boolean = false
) {
    /**
     * Gets the containers.
     *
     * @return The containers.
     */
    /**
     * Represents the shop containers.
     */
    val containers = arrayOf(Container(40, ContainerType.SHOP), Container(40, ContainerType.SHOP))
    /**
     * Gets the viewers.
     *
     * @return The viewers.
     */
    /**
     * Represents the list of shop viewers.
     */
    val viewers: List<ShopViewer> = ArrayList(20)
    /**
     * Gets the title.
     *
     * @return The title.
     */
    /**
     * Gets the items.
     *
     * @return The items.
     */
    /**
     * Gets the general.
     *
     * @return The general.
     */
    /**
     * Gets the currency.
     *
     * @return The currency.
     */
    /**
     * Gets the bhighAlch.
     *
     * @return the highAlch
     */
    /**
     * Gets the SellAllFor value.
     *
     * @return the sellAllFor
     */
    /**
     * Sell price for all shop items, if needed.
     */
    var sellAllFor = 0
        private set
    /**
     * Gets the lastRestock.
     *
     * @return the lastRestock
     */
    /**
     * Sets the balastRestock.
     *
     * @param lastRestock the lastRestock to set.
     */
    /**
     * The last restock.
     */
    var lastRestock = 0
    /**
     * Check if shop should restock.
     *
     * @return the restock
     */
    /**
     * Sets the restock.
     *
     * @param reStock
     */
    /**
     * If the shop should restock.
     */
    var isRestock = false
    /**
     * Gets the pointShop.
     *
     * @return the pointShop
     */
    /**
     * Sets the pointShop.
     *
     * @param pointShop the pointShop to set.
     */
    /**
     * If it's a point shop.
     */
    var isPointShop = false
    /**
     * Gets the npcs.
     *
     * @return the npcs
     */
    /**
     * Sets the banpcs.
     *
     * @param npcs the npcs to set.
     */
    /**
     * The npcs of the shop.
     */
    var npcs: IntArray = intArrayOf()

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title    the title.
     * @param items    the items.
     * @param general  the general.
     * @param currency the currency.
     * @param highAlch if high alch.
     * @param restock  if restock.
     */
    constructor(
        title: String,
        items: Array<Item>,
        general: Boolean,
        currency: Int,
        highAlch: Boolean,
        restock: Boolean,
        sellAllFor: Int
    ) : this(title, items, general, currency, highAlch) {
        this.sellAllFor = sellAllFor
        isRestock = restock
    }

    /**
     * Constructs a new `Shop` `Object`
     *
     * @param title the shop title
     * @param items items the shop can handle
     * @param general is this a general store
     * @param currency what currency is used
     */
    constructor(title: String, items: Array<Item>, npcs: IntArray, general: Boolean, currency: Int) : this(
        title,
        items,
        general,
        currency,
        false
    ) {
        this.npcs = npcs
    }

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title   the title.
     * @param general the general.
     */
    constructor(title: String, general: Boolean) : this(title, GENERAL_STORE_ITEMS, general) {}

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title    the title.
     * @param general  the general.
     * @param highAlch if highAlch.
     */
    constructor(title: String, general: Boolean, highAlch: Boolean) : this(
        title,
        GENERAL_STORE_ITEMS,
        general,
        COINS,
        highAlch
    ) {
    }

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title    the title.
     * @param general  the general.
     * @param highAlch if highAlch.
     */
    constructor(title: String, general: Boolean, currency: Int, highAlch: Boolean) : this(
        title,
        GENERAL_STORE_ITEMS,
        general,
        currency,
        highAlch
    ) {
    }

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title   the title.
     * @param items   the items.
     * @param npcs    the npcs.
     * @param general the general.
     */
    constructor(title: String, items: Array<Item>, npcs: IntArray, general: Boolean) : this(
        title,
        items,
        general,
        COINS,
        false
    ) {
        this.npcs = npcs
    }

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title   the title.
     * @param npcs    the npcs.
     * @param general the general.
     */
    constructor(title: String, npcs: IntArray, general: Boolean) : this(title, GENERAL_STORE_ITEMS, npcs, general) {
        this.npcs = npcs
    }

    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title    the title.
     * @param npcs     the npcs.
     * @param general  the general.
     * @param highAlch if highAlch.
     */
    constructor(title: String, npcs: IntArray, general: Boolean, highAlch: Boolean) : this(
        title,
        GENERAL_STORE_ITEMS,
        general,
        995,
        highAlch
    ) {
        this.npcs = npcs
    }

    /**
     * Method used to open the shop.
     *
     * @param player the shop.
     */
    fun open(player: Player) {
        ShopViewer.extend(player, this).open()

        // Browse the Lumbridge General Store
        if (title.equals("Lumbridge General Store", ignoreCase = true)) {
            player.achievementDiaryManager.finishTask(player, DiaryType.LUMBRIDGE, 0, 18)
        }

        // Browse through Oziach's Armour Shop
        if (title.equals("Oziach's Armour", ignoreCase = true)) {
            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, 20)
        }
    }

    fun give(player: Player, slot: Int, amount: Int, tabIndex: Int) {
        val container = getContainer(tabIndex)
        val item = container[slot] ?: return
        val add = Item(item.id, amount)
        if (add.amount < 1 || !player.inventory.hasSpaceFor(add)) {
            player.packetDispatch.sendMessage("You have no inventory space at the moment and cannot get anything.")
            return
        }
        add.amount = getAmount(player, add)
        if (add.amount < 1 || !player.inventory.hasSpaceFor(add)) {
            player.packetDispatch.sendMessage("You have no inventory space at the moment and cannot get anything.")
            return
        }
        player.inventory.add(add)
        update()
    }

    /**
     * Method used to buy an item from the shop.
     *
     * @param slot   the slot.
     * @param amount the amount.
     */
    fun buy(player: Player, slot: Int, amount: Int, tabIndex: Int) {
        var amount = amount
        if (tabIndex == 1 && player.ironmanManager.checkRestriction()) {
            return
        }
        val container = getContainer(tabIndex)
        val item = container[slot] ?: return
        if (item.amount == 0) {
            player.packetDispatch.sendMessage("There is no stock of that item at the moment.")
            return
        }
        if (amount > item.amount && item.amount != -1) {
            amount = item.amount
        }
        val add = Item(item.id, amount)
        if (player.inventory.getMaximumAdd(add) < amount) {
            add.amount = player.inventory.getMaximumAdd(add)
        }
        if (add.amount < 1 || !player.inventory.hasSpaceFor(add)) {
            player.packetDispatch.sendMessage("You have no inventory space at the moment and cannot buy anything.")
            return
        }
        add.amount = getAmount(player, add)
        if (add.amount < 1 || !player.inventory.hasSpaceFor(add)) {
            player.packetDispatch.sendMessage("You have no inventory space at the moment and cannot buy anything.")
            return
        }
        val price = add.amount * getBuyPrice(item, player)
        val currency = Item(currency, price)
        if (!canBuy(player, item, price, currency)) {
            return
        }
        if (handleBuy(player, currency)) {
            if (isPointShop) {
                decrementPoints(player, price)
            }
            if (tabIndex == 0) {
                if (container.getAmount(item) != -1) container.replace(
                    Item(
                        item.id,
                        container.getAmount(item) - add.amount
                    ), slot, true
                )
            } else {
                container.remove(add)
                container.shift()
            }

            // Achievement Diary Handlers
            if (add.id == BLACK_CHAINBODY_1107 && title.equals("Wayne's Chains", ignoreCase = true) && !player.getAttribute("diary:falador:black-chain-bought", false)) {
                player.setAttribute("/save:diary:falador:black-chain-bought", true)
            }
            if (add.id == 12622 && title.equals("Sarah's Farming Shop", ignoreCase = true)) {
                player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 0, 0)
            }
            if (add.id == CANDLE_36 && title.equals("Candle Shop", ignoreCase = true)) {
                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 0, 9)
            }
            if (title.equals("Ranging Guild Ticket Exchange", ignoreCase = true)) {
                player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 1, 8)
            }
            player.inventory.add(add)
            update()
        }
    }

    /**
     * Method used to sell an item to the shop.
     *
     * @param slot   the slot.
     * @param amount the amount.
     */
    fun sell(player: Player, slot: Int, amount: Int, tabIndex: Int) {
        var amount = amount
        var tabIndex = tabIndex
        val item = player.inventory[slot] ?: return
        val def = ItemDefinition.forId(item.id)
        if (!canSell(player, item, def)) {
            return
        }
        val container = getContainer(item)
        if (amount > player.inventory.getAmount(item)) {
            amount = player.inventory.getAmount(item)
        }
        var add = Item(item.id, amount)
        if (add.amount > container.getMaximumAdd(add)) {
            add.amount = container.getMaximumAdd(add)
        }
        player.debug("" + add.amount)
        if (!container.hasSpaceFor(add) || add.amount < 1) {
            player.packetDispatch.sendMessage("The shop has ran out of space.")
            return
        }
        val currency = Item(currency, getSellingValue(add, player))
        if (item.definition.isStackable) {
            if (!player.inventory.hasSpaceFor(currency)) {
                player.packetDispatch.sendMessage("You don't have enough space for that many " + currency.name.toLowerCase() + ".")
                return
            }
        }
        player.debug("Selling item")
        if (player.inventory.remove(add, slot, true)) {
            if (currency.amount > player.inventory.getMaximumAdd(currency)) {
                currency.amount = player.inventory.getMaximumAdd(currency)
            }
            if (!add.definition.isUnnoted) {
                add = Item(add.noteChange, add.amount)
            }
            if (container.getAmount(add.id) == -1 || container.add(add)) {
                if (currency.amount > 0) {
                    player.debug("Adding coins to inventory")
                    player.inventory.add(currency)
                }
                val viewer = player.getExtension<ShopViewer>(ShopViewer::class.java)
                tabIndex = if (container === containers[0]) 0 else 1
                sendStock(player, tabIndex)
                if (viewer != null) {
                    viewer.tabIndex = tabIndex
                }
                update()
            }
        }
    }

    /**
     * Values an item.
     *
     * @param player the player.
     * @param viewer the viewer.
     * @param item   the item.
     * @param sell   the sell.
     */
    fun value(player: Player, viewer: ShopViewer, item: Item, sell: Boolean) {
        if (sell) {
            if (isPointShop || item.id == viewer.shop.currency || !item.definition.isTradeable || !viewer.shop.itemAllowed(
                    item.id
                )
            ) {
                player.packetDispatch.sendMessage("You can't sell this item.")
                return
            }
            val value = viewer.shop.getSellingValue(Item(item.id, 1), player)
            var currency =
                if (isPointShop) pointsName else ItemDefinition.forId(viewer.shop.currency).name.toLowerCase()
            if (value == 1 && currency[currency.length - 1] == 's') {
                currency = currency.substring(0, currency.length - 1)
            }
            player.packetDispatch.sendMessage(item.name + ": shop will buy for " + value + " " + currency + ".")
        } else {
            val value = viewer.shop.getBuyPrice(item, player)
            var name =
                if (isPointShop) pointsName + "s" else ItemDefinition.forId(viewer.shop.currency).name.toLowerCase()
            if (value == 1 && name[name.length - 1] == 's') {
                name = name.substring(0, name.length - 1)
            }
            player.packetDispatch.sendMessage("" + item.name + ": currently costs " + value + " " + name + ".")
        }
    }

    /**
     * Method used to send a stock
     *
     * @param player
     * @param tabIndex
     */
    fun sendStock(player: Player, tabIndex: Int) {
        val main = tabIndex == 0
        player.packetDispatch.sendInterfaceConfig(620, 23, !main)
        player.packetDispatch.sendInterfaceConfig(620, 24, main)
        player.packetDispatch.sendInterfaceConfig(620, 29, !main)
        player.packetDispatch.sendInterfaceConfig(620, 25, main)
        player.packetDispatch.sendInterfaceConfig(620, 27, main)
        player.packetDispatch.sendInterfaceConfig(620, 26, false)
        player.packetDispatch.sendAccessMask(1278, if (main) 23 else 24, 620, 0, 40)
    }

    /**
     * Method used to update the viewers.
     */
    fun update() {
        for (viewer in viewers) {
            viewer.update()
        }
    }

    /**
     * Method used to restock the shop.
     */
    fun restock() {
        for (container in containers) {
            for (i in container.toArray().indices) {
                val main = container === containers[0]
                val item = container.toArray()[i] ?: continue
                var reduce = !main
                if (main) {
                    if (item.amount < items[i].amount) {
                        item.amount = item.amount + 1
                    }
                    reduce = item.amount > items[i].amount
                }
                if (reduce) {
                    val amount = item.amount - 1
                    if (amount < 1 && !main) {
                        container.remove(item)
                    } else {
                        item.amount = amount
                    }
                    if (!main) {
                        container.shift()
                    }
                }
            }
        }
        update()
    }

    /**
     * Checks if the player can sell an item to the shop.
     *
     * @param player the player.
     * @param item   the item.
     * @param def    the def.
     * @return `True` if so.
     */
    open fun canSell(player: Player, item: Item, def: ItemDefinition): Boolean {
        if (isPointShop || item.definition.hasDestroyAction() || !def.isTradeable || !itemAllowed(item.id)) {
            player.packetDispatch.sendMessage("You can't sell this item to this shop.")
            return false
        }
        if (item.id == currency) {
            player.packetDispatch.sendMessage("You can't sell " + item.name.toLowerCase() + " to a shop.")
            return false
        }
        return true
    }

    /**
     * Gets the amount to buy/sell.
     *
     * @param player the player.
     * @param add    the added item.
     * @return the amount.
     */
    fun getAmount(player: Player?, add: Item): Int {
        return add.amount
    }

    /**
     * Checks if the player can buy the item.
     *
     * @param player   the player.
     * @param currency the currency.
     * @return `True` if so.
     */
    fun handleBuy(player: Player, currency: Item?): Boolean {
        return isPointShop || player.inventory.remove(currency)
    }

    /**
     * Checks if the player can buy from the shop.
     *
     * @param player   the player.
     * @param item     the item.
     * @param price    the price.
     * @param currency the currency.
     * @return `True` if they can buy.
     */
    fun canBuy(player: Player, item: Item, price: Int, currency: Item): Boolean {
        if (!isPointShop && !player.inventory.containsItem(currency)) {
            player.packetDispatch.sendMessage(
                "You don't have enough " + ItemDefinition.forId(currency.id).name.toLowerCase() + "."
            )
            return false
        }
        if (isPointShop && getPoints(player) < price) {
            player.sendMessage("You don't have enough " + pointsName + "s.")
            return false
        }
        return true
    }

    /**
     * Gets the points.
     *
     * @param player the player.
     * @return the points.
     */
    fun getPoints(player: Player?): Int {
        return 0
    }

    /**
     * Decrements the points.
     *
     * @param player    the player.
     * @param decrement the decrementation.
     */
    fun decrementPoints(player: Player?, decrement: Int) {}

    /**
     * Gets the points name.
     *
     * @return the name.
     */
    val pointsName: String
        get() = ""

    /**
     * Gets the value gained for selling this item to a certain shop.
     *
     * @param item   The item to sell.
     * @param player the player.
     * @return The value.
     */
    fun getSellingValue(item: Item, player: Player): Int {
        var item = item
        if (!item.definition.isUnnoted) {
            player.setAttribute("shop:originalId", item.id)
            item = Item(item.noteChange, item.amount)
        }
        var amount = getContainer(1).getAmount(item)
        if (amount < 1) {
            amount = getContainer(0).getAmount(item)
        }
        return getSellingValue(player, amount, item)
    }

    /**
     * Gets the selling value formula based.
     *
     * @param amount the amount.
     * @param item   the item.
     * @return the selling value.
     */
    private fun getSellingValue(player: Player, amount: Int, item: Item): Int {
        val id = player.getAttribute("shop:originalId", item.id)
        if (item.amount > amountInInventory(player, id)) {
            item.amount = amountInInventory(player, id)
            player.removeAttribute("shop:originalId")
        }
        val diff = if (item.definition.isStackable) 0.005 else 0.05
        var maxMod = 1.0 - amount * diff
        if (maxMod < 0.25) {
            maxMod = 0.25
        }
        var minMod = maxMod - (item.amount - 1) * diff
        if (minMod < 0.25) {
            minMod = 0.25
        }
        val mod = (maxMod + minMod) / 2
        logInfo("" + item.definition.getAlchemyValue(isHighAlch) + " " + mod + " " + item.amount)
        val baseValue = item.definition.getAlchemyValue(isHighAlch)
        var value = (baseValue * mod * item.amount).toInt()
        if (currency == TOKKUL_6529 && item.id == CHAOS_RUNE_562) value = 13 * item.amount
        if (currency == TOKKUL_6529 && item.id == DEATH_RUNE_560) value = 27 * item.amount
        if (item.id == 12183) {
            value = 25 * item.amount
        }
        return value
    }

    /**
     * Gets the buying price.
     *
     * @param item the item.
     * @return the price.
     */
    open fun getBuyPrice(item: Item, player: Player): Int {
        var item = item
        item = Item(item.id, 1)
        var price = item.definition.value
        val sellVal = getSellingValue(item, player)
        if (price < sellVal) {
            price = getSellingValue(player, 0, item) + sellVal - (sellVal - item.definition.maxValue)
        }
        if (price < 0) {
            price = 1
        }
        if (currency == TOKKUL) {
            val tokkul = item.definition.getConfiguration("tokkul_price", -1)
            if (tokkul > 0) {
                price = tokkul
            }
            if (player.achievementDiaryManager.karamjaGlove != -1) {
                price = kotlin.math.floor(price * 0.87).toInt()
            }
        }
        if (currency == ARCHERY_TICKET) {
            val tickets = item.definition.getConfiguration(ItemConfigParser.ARCHERY_TICKET_PRICE, -1)
            if (tickets > 0) {
                price = tickets
            }
        }
        return if (sellAllFor > 0) sellAllFor else price
    }

    /**
     * Checks if the item is allowed to be sold to the shop.
     *
     * @param itemId the item id.
     * @return `True` if so.
     */
    fun itemAllowed(itemId: Int): Boolean {
        if (isGeneral) {
            return true
        }
        var noteId = ItemDefinition.forId(itemId).noteId
        if (!ItemDefinition.forId(itemId).isUnnoted) {
            noteId = ItemDefinition.forId(noteId).noteId
        }
        for (id in items) {
            if (itemId == id.id || noteId > -1 && noteId == ItemDefinition.forId(id.id).noteId) {
                return true
            }
        }
        return false
    }

    /**
     * Gets the container the item should go to.
     *
     * @param item the item.
     * @return the container.
     */
    fun getContainer(item: Item): Container {
        val itemId = item.id
        var noteId = ItemDefinition.forId(itemId).noteId
        if (!ItemDefinition.forId(itemId).isUnnoted) {
            noteId = ItemDefinition.forId(noteId).noteId
        }
        for (i in items) {
            if (i.id == item.id || noteId > -1 && noteId == ItemDefinition.forId(i.id).noteId) {
                return getContainer(0)
            }
        }
        return getContainer(1)
    }

    /**
     * Creates a copy of this shop.
     *
     * @return the shop.
     */
    fun copy(): Shop {
        return Shop(title, items, isGeneral, currency, isHighAlch)
    }

    /**
     * Gets the container on the slot.
     *
     * @param tabIndex the tab index.
     * @return the container.
     */
    fun getContainer(tabIndex: Int): Container {
        if (tabIndex > containers.size) {
            throw IndexOutOfBoundsException("Error! Shop tab index out of bounds.")
        }
        return containers[tabIndex]
    }

    override fun toString(): String {
        return "Shop [containers=" + Arrays.toString(containers) + ", viewers=" + viewers + ", title=" + title + ", items=" + Arrays.toString(
            items
        ) + ", general=" + isGeneral + ", currency=" + currency + ",  highAlch=" + isHighAlch + "]"
    }

    fun setItems(items: ArrayList<Item>) {
        this.items = items.toTypedArray()
    }

    companion object {
        /**
         * Represents the general store items.
         */
        val GENERAL_STORE_ITEMS = arrayOf(
            Item(EMPTY_POT_1931, 5),
            Item(JUG_1935, 5),
            Item(SHEARS_1735, 2),
            Item(BUCKET_1925, 3),
            Item(BOWL_1923, 2),
            Item(CAKE_TIN_1887, 2),
            Item(TINDERBOX_590, 2),
            Item(CHISEL_1755, 2),
            Item(HAMMER_2347, 5),
            Item(NEWCOMER_MAP_550, 5),
            Item(SECURITY_BOOK_9003, 5)
        )

        /**
         * Represents the coins item.
         */
        private const val COINS = 995

        /**
         * Represents the tokkul item id.
         */
        private const val TOKKUL = 6529

        /**
         * Represents the archery ticket item id
         */
        private const val ARCHERY_TICKET = 1464
    }
    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title    the title.
     * @param items    the items.
     * @param isGeneral  the general.
     * @param currency the currency.
     * @param isHighAlch if high alch.
     */
    /**
     * Constructs a new `Shop` `Object`.
     *
     * @param title   the title.
     * @param items   the items.
     * @param general the general.
     */
    init {
        this.getContainer(0).add(*items)
        isRestock = true
        lastRestock = ticks + 100
    }
}