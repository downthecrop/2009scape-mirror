package content.global.skill.farming

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items
import core.game.interaction.InterfaceListener

class ToolLeprechaunInterface : InterfaceListener {
    private val FARMING_TOOLS = Components.FARMING_TOOLS_125
    private val TOOLS_SIDE = Components.FARMING_TOOLS_SIDE_126

    override fun defineInterfaceListeners() {

        onOpen(FARMING_TOOLS) { player, component ->
            openSingleTab(player, TOOLS_SIDE)
            return@onOpen true
        }

        onClose(FARMING_TOOLS) { player, _ ->
            closeTabInterface(player)
            return@onClose true
        }

        on(FARMING_TOOLS) { player, _, opcode, buttonID, _, _ ->
            when (buttonID) {
                33 -> doWithdrawal(player, Items.RAKE_5341, ::setHasRake, ::hasRake)
                34 -> doWithdrawal(player, Items.SEED_DIBBER_5343, ::setHasDibber, ::hasDibber)
                35 -> doWithdrawal(player, Items.SPADE_952, ::setHasSpade, ::hasSpade)
                36 -> {
                    val sec = if (hasMagicSecateurs(player)) Items.MAGIC_SECATEURS_7409 else Items.SECATEURS_5329
                    doWithdrawal(player, sec, ::setHasSecateurs, ::hasSecateurs)
                }
                37 -> {
                    if (!hasWateringCan(player)) {
                        sendMessage(player, "You haven't got a watering can stored in here!")
                    } else {
                        if (freeSlots(player) == 0) {
                            sendMessage(player, "You don't have enough space for that.")
                        }
                        if (addItem(player, getWateringCan(player))) setNoWateringCan(player)
                    }
                }
                38 -> doWithdrawal(player, Items.GARDENING_TROWEL_5325, ::setHasGardeningTrowel, ::hasGardeningTrowel)
                39 -> doStackedWithdrawal(player, Items.BUCKET_1925, getAmount(opcode), ::updateBuckets, ::getNumBuckets)
                40 -> doStackedWithdrawal(player, Items.COMPOST_6032, getAmount(opcode), ::updateCompost, ::getNumCompost)
                41 -> doStackedWithdrawal(player, Items.SUPERCOMPOST_6034, getAmount(opcode), ::updateSuperCompost, ::getNumSuperCompost)
            }
            return@on true
        }

        on(TOOLS_SIDE) { player, _, opcode, buttonID, _, _ ->
            when (buttonID) {
                18 -> doDeposit(player, Items.RAKE_5341, ::setHasRake, ::hasRake)
                19 -> doDeposit(player, Items.SEED_DIBBER_5343, ::setHasDibber, ::hasDibber)
                20 -> doDeposit(player, Items.SPADE_952, ::setHasSpade, ::hasSpade)
                21 -> {
                    if (!inInventory(player, Items.SECATEURS_5329) && !inInventory(player, Items.MAGIC_SECATEURS_7409)) {
                        sendMessage(player, "You haven't got any secateurs to store.")
                    } else if (!hasSecateurs(player)) {
                        if (inInventory(player, Items.MAGIC_SECATEURS_7409)) {
                            removeItem(player, Items.MAGIC_SECATEURS_7409)
                            setHasSecateurs(player,true)
                            setHasMagicSecateurs(player,true)
                        } else {
                            removeItem(player, Items.SECATEURS_5329)
                            setHasSecateurs(player,true)
                            setHasMagicSecateurs(player,false)
                        }
                    } else {
                        sendMessage(player, "You cannot store more than one pair of secateurs in here.")
                    }
                }
                22 -> {
                    val can = getHighestCan(player)
                    if (can == null) {
                        sendMessage(player, "You haven't got a watering can to store.")
                    } else if (!hasWateringCan(player)) {
                        removeItem(player, can)
                        setWateringCan(player,can)
                    } else {
                        sendMessage(player, "You cannot store more than one watering can in here.")
                    }
                }
                23 -> doDeposit(player, Items.GARDENING_TROWEL_5325, ::setHasGardeningTrowel, ::hasGardeningTrowel)
                24 -> doStackedDeposit(player, Items.BUCKET_1925, getAmount(opcode), ::updateBuckets, ::getNumBuckets)
                25 -> doStackedDeposit(player, Items.COMPOST_6032, getAmount(opcode), ::updateCompost, ::getNumCompost)
                26 -> doStackedDeposit(player, Items.SUPERCOMPOST_6034, getAmount(opcode), ::updateSuperCompost, ::getNumSuperCompost)
            }
            return@on true
        }

    }

    private fun doWithdrawal(player: Player?, item: Int, withdrawMethod: (Player?, Boolean) -> Unit, checkMethod: (Player?) -> Boolean) {
        player ?: return
        if (!checkMethod.invoke(player)) {
            val determiner = if (getItemName(item).lowercase().endsWith("s")) "any" else "a"
            sendMessage(player, "You haven't got $determiner ${getItemName(item).lowercase()} stored in here!")
        } else {
            if (!hasSpaceFor(player, Item(item))) {
                sendMessage(player, "You don't have enough space for that.")
                return
            }
            withdrawMethod.invoke(player, false)
            addItem(player, item)
        }
    }

    private fun doDeposit(player: Player?, item: Int, depositMethod: (Player?, Boolean) -> Unit, checkMethod: (Player?) -> Boolean) {
        player ?: return
        if (!inInventory(player, item)) {
            val determiner = if (getItemName(item).lowercase().endsWith("s")) "any" else "a"
            sendMessage(player, "You haven't got $determiner ${getItemName(item).lowercase()} to store.")
            return
        }
        if (!checkMethod.invoke(player)) {
            depositMethod.invoke(player, true)
            removeItem(player, item)
        } else {
            val itemName = when (item) {
                // secateurs and watering cans are handled separately
                Items.RAKE_5341 -> "rake"
                Items.SEED_DIBBER_5343 -> "dibber"
                Items.SPADE_952 -> "spade"
                Items.GARDENING_TROWEL_5325 -> "trowel"
                else -> getItemName(item).lowercase()
            }
            sendMessage(player, "You cannot store more than one $itemName in here.")
        }
    }

    private fun doStackedDeposit(player: Player?, item: Int, amount: Int, updateQuantityMethod: (Player?, Int) -> Unit, quantityCheckMethod: (Player?) -> Int) {
        player ?: return
        val hasAmount = amountInInventory(player, item)
        var finalAmount = amount
        val spaceLeft = (if (item == Items.BUCKET_1925) 31 else 255) - quantityCheckMethod.invoke(player)

        if (hasAmount == 0) {
            val itemName = if (item == Items.BUCKET_1925) "buckets" else getItemName(item).lowercase()
            sendMessage(player, "You haven't got any $itemName to store.")
            return
        }

        if (amount == -2) {
            sendInputDialogue(player, true, "Enter the amount:") { value ->
                var amt = value as Int
                if (amt > hasAmount) {
                    amt = hasAmount
                }
                if (amt > spaceLeft) {
                    amt = spaceLeft
                }
                if (removeItem(player, Item(item, amt))) updateQuantityMethod.invoke(player, amt)
            }
            return
        }

        if (amount == -1) {
            finalAmount = hasAmount
            if (finalAmount > spaceLeft) {
                finalAmount = spaceLeft
            }
        }

        if (finalAmount > hasAmount) {
            finalAmount = hasAmount
        }

        if (finalAmount > spaceLeft) {
            if (item == Items.BUCKET_1925) {
                sendMessage(player, "You can't store that many buckets in here.")
            } else {
                sendMessage(player, "You can't store that much ${getItemName(item).lowercase()} in here.")
            }
            return
        }

        removeItem(player, Item(item, finalAmount))
        updateQuantityMethod.invoke(player,finalAmount)
    }

    private fun doStackedWithdrawal(player: Player?, item: Int, amount: Int, updateQuantityMethod: (Player?, Int) -> Unit, quantityCheckMethod: (Player?) -> Int) {
        player ?: return
        val hasAmount = quantityCheckMethod.invoke(player)
        var finalAmount = amount

        if (hasAmount == 0) {
            val itemName = if (item == Items.BUCKET_1925) "buckets" else getItemName(item).lowercase()
            sendMessage(player, "You haven't got any $itemName stored in here!")
            return
        }

        if (amount == -2) {
            sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:") { value ->
                var amt = value as Int
                if (amt > hasAmount) {
                    amt = hasAmount
                }
                if (amt > freeSlots(player)) {
                    amt = freeSlots(player)
                }
                if (amt <= 0) {
                    sendMessage(player, "You don't have enough inventory space for that.")
                } else {
                    addItem(player, item, amt)
                    updateQuantityMethod.invoke(player, -amt)
                }
            }
            return
        }
        if (amount == -1) {
            finalAmount = freeSlots(player)
        }
        if (finalAmount > hasAmount) {
            finalAmount = hasAmount
        }
        if (!hasSpaceFor(player, Item(item, finalAmount)) || finalAmount == 0) {
            sendMessage(player, "You don't have enough inventory space for that.")
            return
        }
        addItem(player, item, finalAmount)
        updateQuantityMethod.invoke(player, -finalAmount)
    }

    fun getAmount(opcode: Int): Int {
        return when (opcode) {
            155 -> 1
            196 -> 5
            124 -> -1
            199 -> -2
            else -> 0
        }
    }

    private fun hasRake(player: Player?): Boolean {
        return getVarbit(player!!, 1435) == 1
    }

    private fun setHasRake(player: Player?, hasRake: Boolean) {
        setVarbit(player!!, 1435, if (hasRake) 1 else 0, true)
    }

    private fun hasDibber(player: Player?): Boolean {
        return getVarbit(player!!, 1436) == 1
    }

    private fun setHasDibber(player: Player?, hasDibber: Boolean) {
        setVarbit(player!!, 1436, if (hasDibber) 1 else 0, true)
    }

    private fun hasSpade(player: Player?): Boolean {
        return getVarbit(player!!, 1437) == 1
    }

    private fun setHasSpade(player: Player?, hasSpade: Boolean) {
        setVarbit(player!!, 1437, if (hasSpade) 1 else 0, true)
    }

    private fun hasSecateurs(player: Player?): Boolean {
        return getVarbit(player!!, 1438) == 1
    }

    private fun setHasSecateurs(player: Player?, hasSecateurs: Boolean) {
        setVarbit(player!!, 1438, if (hasSecateurs) 1 else 0, true)
    }

    private fun hasWateringCan(player: Player?): Boolean {
        return getVarbit(player!!, 1439) > 0
    }

    private fun getWateringCan(player: Player?): Int {
        var can = getVarbit(player!!, 1439) //Watering cans are stored in the Varp as a number between 1 and 9. Watering Can(0) is 1 and Watering Can(8) is 9
        if (can == 1) can = 0
        return Items.WATERING_CAN_5331 + can
    }
    
    private fun setWateringCan(player: Player?, item: Item) {
        val can = when (item.id) {
            Items.WATERING_CAN_5331 -> 1
            Items.WATERING_CAN1_5333 -> 2
            Items.WATERING_CAN2_5334 -> 3
            Items.WATERING_CAN3_5335 -> 4
            Items.WATERING_CAN4_5336 -> 5
            Items.WATERING_CAN5_5337 -> 6
            Items.WATERING_CAN6_5338 -> 7
            Items.WATERING_CAN7_5339 -> 8
            Items.WATERING_CAN8_5340 -> 9
            else -> 0
        }
        setVarbit(player!!, 1439, can, true)
    }

    private fun setNoWateringCan(player: Player?) {
        setVarbit(player!!, 1439, 0, true)
    }

    private fun hasGardeningTrowel(player: Player?): Boolean {
        return getVarbit(player!!, 1440) == 1
    }

    private fun setHasGardeningTrowel(player: Player?, hasTrowel: Boolean) {
        setVarbit(player!!, 1440, if (hasTrowel) 1 else 0, true)
    }

    private fun getNumBuckets(player: Player?): Int {
        return getVarbit(player!!, 1441)
    }

    private fun updateBuckets(player: Player?, amount: Int) {
        setVarbit(player!!, 1441, getNumBuckets(player) + amount, true)
    }

    private fun getNumCompost(player: Player?): Int {
        return getVarbit(player!!, 1442)
    }

    private fun updateCompost(player: Player?, amount: Int) {
        setVarbit(player!!, 1442, getNumCompost(player) + amount, true)
    }

    private fun getNumSuperCompost(player: Player?): Int {
        return getVarbit(player!!, 1443)
    }

    private fun updateSuperCompost(player: Player?, amount: Int) {
        setVarbit(player!!, 1443, getNumSuperCompost(player) + amount, true)
    }

    private fun hasMagicSecateurs(player: Player?): Boolean {
        return getVarbit(player!!, 1848) == 1
    }

    private fun setHasMagicSecateurs(player: Player?, hasMagic: Boolean) {
        setVarbit(player!!, 1848, if (hasMagic) 1 else 0, true)
    }

    private fun getHighestCan(player: Player?): Item? {
        player ?: return null
        var highestCan = Item(0)
        for (item in player.inventory.toArray()) {
            if (item == null) continue
            if (item.name.contains("Watering")) {
                if (item.id > highestCan.id) highestCan = item
            }
        }
        return if (highestCan.id == 0) null else highestCan
    }

}
