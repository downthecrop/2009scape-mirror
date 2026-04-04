package content.global.skill.construction.decoration.pohstorage

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ContainerContext
import core.net.packet.out.ContainerPacket
import org.rs09.consts.Items

/**
 * Handles the interface and interactions for
 * the Construction Costume Room storage boxes and Bookshelves
 * https://youtu.be/tAIDQRh4U2g?si=oDaKEJT-ZPs4MoZa&t=261
 * https://www.youtube.com/watch?v=D2aVfpPCAgY&t=41s - great view of all the treasure chests
 * https://youtu.be/AK6wVwumvHY?si=XAJsQeSdRlBZSM1f - mid-level costume room
 * https://www.youtube.com/watch?v=sNEOq_Barlg&t=126s - 2011, but a good video
 */
class StorageInterface : InterfaceListener {
    companion object {
        private const val INTERFACE = 467
        private const val COMPONENT = 164
        private const val SIZE = 30
        private const val PAGE_SIZE = SIZE - 1
        private const val BUTTON_MORE = Items.MORE_10165
        private const val BUTTON_BACK = Items.BACK_10166

        /**
         * Singleton instance of the storage interface.
         */
        lateinit var instance: StorageInterface
            private set

        /**
         * Public method to get the storage container for a specific family.
         */
        fun getStorageContainer(player: Player, type: StorableFamily) =
            player.getPOHStorageState().getContainer(type)

        /**
         * Opens the storage box.
         *
         * @param player The player.
         * @param type The type of storage to open.
         * @param box the specific tier of storage we are opening.
         */
        fun openStorage(player: Player, type: StorableFamily, box: StorageOptionPlugin.StorageBox) {
            instance.openStorageForType(player, type, box)
        }

        /**
         * Finds the Storable enum entry associated with a given display item ID.
         */
        fun findStorableByDisplayId(id: Int): Storable? =
            Storable.values().find { it.displayId == id }
    }

    /**
     * The attribute of whether the player has read a book or not is stored in a set of two bitfields.
     * There are 70 books so that's why two longs are needed.
     */
    object BookcaseBitfields {
        // 0-63
        const val BOOKCASE_LOW_ATTR = "/save:bookcase_bitfield_low"
        // 64-127
        const val BOOKCASE_HIGH_ATTR = "/save:bookcase_bitfield_high"

        data class BookcaseBits(var low: Long, var high: Long)

        // Retrieves both Long bitfields.
        fun get(player: Player): BookcaseBits {
            val low = getAttribute(player, BOOKCASE_LOW_ATTR, 0L) as? Long ?: 0L
            val high = getAttribute(player, BOOKCASE_HIGH_ATTR, 0L) as? Long ?: 0L
            return BookcaseBits(low, high)
        }

        // Sets both Long bitfields.
        fun set(player: Player, bits: BookcaseBits) {
            setAttribute(player, BOOKCASE_LOW_ATTR, bits.low)
            setAttribute(player, BOOKCASE_HIGH_ATTR, bits.high)
        }

        // Checks if a specific bit is set within the two Longs.
        fun isBitSet(bits: BookcaseBits, bitIndex: Int): Boolean {
            return if (bitIndex < 64) {
                (bits.low and (1L shl bitIndex)) != 0L
            } else {
                val highIndex = bitIndex - 64
                (bits.high and (1L shl highIndex)) != 0L
            }
        }

        // Sets a specific bit within the two Longs.
        fun setBit(bits: BookcaseBits, bitIndex: Int): BookcaseBits {
            if (bitIndex < 64) {
                bits.low = bits.low or (1L shl bitIndex)
            } else {
                val highIndex = bitIndex - 64
                bits.high = bits.high or (1L shl highIndex)
            }
            return bits
        }
    }

    init {
        instance = this
    }

    override fun defineInterfaceListeners() {
        on(INTERFACE) { player, _, _, buttonId, _, _ ->
            val typeName = getAttribute(player, "con:storage:type", null) as? String ?: return@on true
            val type = StorableFamily.valueOf(typeName.uppercase())
            handleStorageInteraction(player, buttonId, type)
            return@on true
        }
    }

    /**
     * Gets the StorableFamilies that should be displayed in the interface for a given primary type.
     * This handles the special case for the Cape Rack because capes are classified as two different families/types
     * (normal and skillcape) but displayed in the same storage.
     */
    private fun getFamiliesToDisplay(primaryType: StorableFamily): List<StorableFamily> {
        return when (primaryType) {
            // if the cape rack can hold skill capes, also show normal capes
            StorableFamily.CAPE_RACK_SKILL -> listOf(StorableFamily.CAPE_RACK, StorableFamily.CAPE_RACK_SKILL)
            // else only display the items for the primary family
            else -> listOf(primaryType)
        }
    }

    /**
     * This handles filtering the list for books.
     * Only items are shown that have already been read.
     * If the book is part of a quest, the quest must be complete before it shows up in the POH.
     * If the index is -1 it is not a book, and not filtered.
     */
    private fun getFilteredItems(player: Player, families: List<StorableFamily>, type: StorableFamily): List<Storable> {
        // filter for normal items
        var allItems = Storable.values().filter { it.type in families }

        // filter for bookcase
        if (type == StorableFamily.BOOKCASE) {
            val bookcaseBits = BookcaseBitfields.get(player)
            allItems = allItems.filter { storable ->
                // check if read
                val isRead = if (storable.bitIndex == -1) true else BookcaseBitfields.isBitSet(bookcaseBits, storable.bitIndex)
                // check if quest is done
                val questMet = storable.questReq == null || isQuestComplete(player, storable.questReq)
                isRead && questMet
            }
        }

        return allItems
    }

    /**
     * Handles interaction with storage interface.
     */
    private fun handleStorageInteraction(player: Player, buttonId: Int, type: StorableFamily) {
        val container = getStorageContainer(player, type)
        val families = getFamiliesToDisplay(type)
        val allItems = getFilteredItems(player, families, type)
        val pageIndex = container.getPageIndex()

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(SIZE) { null }

        var idx = 0
        pageItems.forEach {
            if (idx >= SIZE) return@forEach
            slots[idx++] = it
        }

        val hasPrev = pageIndex > 0
        val hasNext = allItems.size > (pageIndex + 1) * PAGE_SIZE

        if (hasNext && idx < SIZE) slots[idx++] = "MORE"
        if (hasPrev && idx < SIZE) slots[idx] = "BACK"

        val slotIndex = when {
            buttonId in 56..(56 + (SIZE - 1) * 2) step 2 -> (buttonId - 56) / 2
            buttonId in 165..223 step 2 -> (buttonId - 165) / 2
            else -> return
        }

        when (val clicked = slots.getOrNull(slotIndex)) {
            "MORE" -> {
                container.nextPage(allItems.size, PAGE_SIZE)
                openInterface(player, INTERFACE)
                updateStorageInterface(player, type)
            }
            "BACK" -> {
                container.prevPage()
                openInterface(player, INTERFACE)
                updateStorageInterface(player, type)
            }
            is Storable -> processItemTransaction(player, clicked, type)
        }
    }

    /**
     * Handles taking or depositing an item from/to the storage.
     */
    private fun processItemTransaction(player: Player, item: Storable, type: StorableFamily) {
        // container for the interface/capacity check based on the object the player clicked
        val interfaceContainer = getStorageContainer(player, type)

        // container for the actual storage/retrieval based on the item's family
        val targetContainer = getStorageContainer(player, item.type)

        // get stored items from the target container
        val storedItems = targetContainer.getItems().toSet()

        val box = getAttribute(player, "con:storage:box", null) as? StorageOptionPlugin.StorageBox ?: return
        val capacity = box.capacity

        // gets the currently stored IDs
        val storedTakeIds = item.takeIds.filter { it in storedItems }

        if (storedTakeIds.isNotEmpty()) {
            // withdraw items
            if (freeSlots(player) < storedTakeIds.size) {
                sendMessage(player, "You don't have enough inventory space.")
                return
            }
            sendMessage(player, "You take the item from the box.")

            storedTakeIds.forEach { id ->
                addItem(player, id, 1)
                targetContainer.withdraw(id)
            }
        } else {
            // deposit items

            // capacity check
            if (capacity < Int.MAX_VALUE) {

                val familyToCheck = when (type) {
                    // for cape racks, only skillcapes are counted against the capacity
                    StorableFamily.CAPE_RACK_SKILL -> StorableFamily.CAPE_RACK_SKILL
                    // for all other storage, check against the primary family type for capacity
                    else -> type
                }

                // check if skillcape
                if (item.type == familyToCheck) {

                    val capacityCheckItems = interfaceContainer.getItems().toSet()

                    // calculate stored skillcapes
                    val storedSetsCount = Storable.values()
                        .filter { it.type == familyToCheck }
                        .count { storable ->
                            storable.takeIds.any { it in capacityCheckItems }
                        }

                    // enforce capacity limits
                    if (storedSetsCount >= capacity) {
                        val message = when (familyToCheck) {
                            StorableFamily.CAPE_RACK_SKILL -> "You cannot store any more skillcapes in this container."
                            else -> "You cannot store any more items in this container."
                        }
                        sendMessage(player, message)
                        return
                    }
                }
            }

            val presentTakeIds = item.takeIds.filter { player.inventory.contains(it, 1) }

            if (presentTakeIds.isEmpty()) {
                sendMessage(player, "You don't have that item in your inventory.")
                return
            }
            sendMessage(player, "You put the item into the box.")

            presentTakeIds.forEach { id ->
                removeItem(player, Item(id))
                targetContainer.addItem(id)
            }
        }
        updateStorageInterface(player, type)
    }

    private fun updateStorageInterface(player: Player, type: StorableFamily) {
        val container = getStorageContainer(player, type)
        val families = getFamiliesToDisplay(type)
        val allItems = getFilteredItems(player, families, type)
        val allStoredItems = mutableSetOf<Int>()

        families.forEach { family ->
            val familyContainer = getStorageContainer(player, family)
            allStoredItems.addAll(familyContainer.getItems())
        }

        val stored = allStoredItems.toSet()
        val pageIndex = container.getPageIndex()

        val pageItems = allItems.drop(pageIndex * PAGE_SIZE).take(PAGE_SIZE)
        val slots = MutableList<Any?>(SIZE) { null }

        var idx = 0
        pageItems.forEach {
            if (idx >= SIZE) return@forEach
            slots[idx++] = it
        }

        val hasPrev = pageIndex > 0
        val hasNext = allItems.size > (pageIndex + 1) * PAGE_SIZE

        if (hasNext && idx < SIZE) slots[idx++] = "MORE"
        if (hasPrev && idx < SIZE) slots[idx] = "BACK"

        // names each box
        val title = when {
            type.name.contains("FANCY_DRESS") -> "Fancy dress box"
            type.name.contains("TOY_BOX") -> "Toy box"
            type.name.contains("MAGIC_WARDROBE") -> "Magic wardrobe"
            type.name.contains("ARMOUR_CASE") -> "Armour case"
            type.name.contains("BOOKCASE") -> "Bookcase"
            type.name.contains("CAPE_RACK") -> "Cape rack"
            type.name.contains("TREASURE_CHEST_LOW") -> "Low-level Treasure Trail rewards"
            type.name.contains("TREASURE_CHEST_MED") -> "Medium-level Treasure Trail rewards"
            type.name.contains("TREASURE_CHEST_HIGH") -> "High-level Treasure Trail rewards"

            // to catch any other name
            else -> type.name.lowercase().replaceFirstChar(Char::titlecase) + " box"
        }
        // child 225 is the title block
        setInterfaceText(player, title, INTERFACE, 225)

        val itemsArray = slots.mapNotNull {
            when (it) {
                is Storable -> Item(it.displayId)
                "MORE" -> Item(BUTTON_MORE)
                "BACK" -> Item(BUTTON_BACK)
                else -> null
            }
        }.toTypedArray()

        PacketRepository.send(
            ContainerPacket::class.java,
            ContainerContext(player, INTERFACE, COMPONENT, SIZE, itemsArray, false)
        )

        repeat(SIZE) { i ->
            val nameComponent = 55 + i * 2
            val iconComponent = 165 + i * 2
            val hiddenIconComponent = 166 + i * 2

            val obj = slots[i]
            val (name, isStored) = when (obj) {
                is Storable -> {
                    val isStored = obj.takeIds.any { it in stored }
                    obj.setName to isStored
                }
                "MORE" -> "More..." to true
                "BACK" -> "Back..." to true
                else -> "" to false
            }

            setInterfaceText(player, name, INTERFACE, nameComponent)
            setComponentVisibility(player, INTERFACE, nameComponent, false)

            if (obj is Storable) {
                // this displays if the items are stored or not stored
                setComponentVisibility(player, INTERFACE, iconComponent, !isStored)
                setComponentVisibility(player, INTERFACE, hiddenIconComponent, isStored)
            } else {
                // this is for navigation buttons or empty slots
                setComponentVisibility(player, INTERFACE, iconComponent, true)
                setComponentVisibility(player, INTERFACE, hiddenIconComponent, true)
            }
        }
    }

    private fun openStorageForType(player: Player, type: StorableFamily, box: StorageOptionPlugin.StorageBox) {
        setAttribute(player, "con:storage:type", type.name)
        setAttribute(player, "con:storage:box", box)
        openInterface(player, INTERFACE)
        updateStorageInterface(player, type)
    }
}
