package content.global.skill.farming

import core.api.*
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Components
import org.rs09.consts.Items
import core.game.interaction.InterfaceListener

class ToolLeprechaunInterface : InterfaceListener {
    private val FARMING_TOOLS = Components.FARMING_TOOLS_125
    private val TOOLS_SIDE = Components.FARMING_TOOLS_SIDE_126

    override fun defineInterfaceListeners() {

        onOpen(FARMING_TOOLS){player, component ->
            player.interfaceManager.openSingleTab(Component(TOOLS_SIDE))
            return@onOpen true
        }

        onClose(FARMING_TOOLS){player, _ ->
            player.interfaceManager.closeSingleTab()
        }

        on(FARMING_TOOLS){player, _, opcode, buttonID, _, _ ->
            when(buttonID){
                33 -> doWithdrawal(player, Items.RAKE_5341,::setHasRake,::hasRake)
                34 -> doWithdrawal(player,Items.SEED_DIBBER_5343,::setHasDibber,::hasDibber)
                35 -> doWithdrawal(player,Items.SPADE_952,::setHasSpade,::hasSpade)
                36 -> {
                    val sec = if(hasMagicSecateurs(player)) Items.MAGIC_SECATEURS_7409 else Items.SECATEURS_5329
                    doWithdrawal(player,sec,::setHasSecateurs,::hasSecateurs)
                }
                37 -> {
                    if(!hasWateringCan(player)){
                        player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                    } else {
                        player.inventory.add(Item(getWateringCan(player)))
                        setNoWateringCan(player)
                    }
                }
                38 -> doWithdrawal(player,Items.GARDENING_TROWEL_5325,::setHasGardeningTrowel,::hasGardeningTrowel)
                39 -> doStackedWithdrawal(player,Items.BUCKET_1925,getAmount(opcode),::updateBuckets,::getNumBuckets)
                40 -> doStackedWithdrawal(player,Items.COMPOST_6032,getAmount(opcode),::updateCompost,::getNumCompost)
                41 -> doStackedWithdrawal(player,Items.SUPERCOMPOST_6034,getAmount(opcode),::updateSuperCompost,::getNumSuperCompost)
            }
            return@on true
        }

        on(TOOLS_SIDE){player, _, opcode, buttonID, _, _ ->
            when(buttonID){
                18 -> doDeposit(player,Items.RAKE_5341,::setHasRake,::hasRake)
                19 -> doDeposit(player,Items.SEED_DIBBER_5343,::setHasDibber,::hasDibber)
                20 -> doDeposit(player,Items.SPADE_952,::setHasSpade,::hasSpade)
                21 -> {
                    if(!player.inventory.contains(Items.SECATEURS_5329,1) && !player.inventory.contains(Items.MAGIC_SECATEURS_7409,1)){
                        player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                    } else if (!hasSecateurs(player)){
                        if(player.inventory.contains(Items.MAGIC_SECATEURS_7409,1)){
                            player.inventory.remove(Item(Items.MAGIC_SECATEURS_7409))
                            setHasSecateurs(player,true)
                            setHasMagicSecateurs(player,true)
                        } else {
                            player.inventory.remove(Item(Items.SECATEURS_5329))
                            setHasSecateurs(player,true)
                            setHasMagicSecateurs(player,false)
                        }
                    } else {
                        player.dialogueInterpreter.sendDialogue("You already have one of those stored.")
                    }
                }
                22 -> {
                    val can = getHighestCan(player)
                    if(can == null){
                        player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                    } else if(!hasWateringCan(player)){
                        player.inventory.remove(can)
                        setWateringCan(player,can)
                    } else {
                        player.dialogueInterpreter.sendDialogue("You already have one of those stored.")
                    }
                }
                23 -> doDeposit(player,Items.GARDENING_TROWEL_5325,::setHasGardeningTrowel,::hasGardeningTrowel)
                24 -> doStackedDeposit(player,Items.BUCKET_1925,getAmount(opcode),::updateBuckets,::getNumBuckets)
                25 -> doStackedDeposit(player,Items.COMPOST_6032,getAmount(opcode),::updateCompost,::getNumCompost)
                26 -> doStackedDeposit(player,Items.SUPERCOMPOST_6034,getAmount(opcode),::updateSuperCompost,::getNumSuperCompost)
            }
            return@on true
        }

    }

    private fun doWithdrawal(player: Player?, item: Int, withdrawMethod: (Player?,Boolean) -> Unit, checkMethod: (Player?) -> Boolean){
        player ?: return
        if(!checkMethod.invoke(player)){
            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
        } else {
            if(!player.inventory.hasSpaceFor(Item(item))){
                player.dialogueInterpreter.sendDialogue("You don't have enough space for that.")
                return
            }
            withdrawMethod.invoke(player,false)
            player.inventory.add(Item(item))
        }
    }

    private fun doDeposit(player: Player?, item: Int, depositMethod: (Player?,Boolean) -> Unit, checkMethod: (Player?) -> Boolean){
        player ?: return
        if(!player.inventory.contains(item,1)){
            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
        }
        if(!checkMethod.invoke(player)){
            depositMethod.invoke(player,true)
            player.inventory.remove(Item(item))
        } else {
            player.dialogueInterpreter.sendDialogue("You already have one of those stored.")
        }
    }

    private fun doStackedDeposit(player: Player?, item: Int, amount: Int, updateQuantityMethod: (Player?, Int) -> Unit, quantityCheckMethod: (Player?) -> Int){
        player ?: return
        val hasAmount = player.inventory.getAmount(item)
        var finalAmount = amount
        val spaceLeft = (if(item == Items.BUCKET_1925) 31 else 255) - quantityCheckMethod.invoke(player)

        if(hasAmount == 0){
            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
            return
        }

        if(amount == -2){
            sendInputDialogue(player, true, "Enter the amount:"){value ->
                var amt = value as Int
                if(amt > hasAmount){
                    amt = hasAmount
                }
                if(amt > spaceLeft){
                    amt = spaceLeft
                }
                if(amt == 0){
                    player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                    return@sendInputDialogue
                }
                player.inventory.remove(Item(item,amt))
                updateQuantityMethod.invoke(player,amt)
            }
            return
        }
        if(amount == -1){
            finalAmount = hasAmount
            if(finalAmount > spaceLeft){
                finalAmount = spaceLeft
            }
        }

        if(finalAmount > hasAmount){
            finalAmount = hasAmount
        }
        if(finalAmount > spaceLeft) finalAmount = spaceLeft

        if(!player.inventory.contains(item,finalAmount)){
            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
            return
        }

        if(finalAmount > spaceLeft){
            player.dialogueInterpreter.sendDialogue("You can't store any more of those.")
            return
        }

        player.inventory.remove(Item(item,finalAmount))
        updateQuantityMethod.invoke(player,finalAmount)
    }

    private fun doStackedWithdrawal(player: Player?,item: Int, amount: Int,updateQuantityMethod: (Player?,Int) -> Unit, quantityCheckMethod: (Player?) -> Int){
        player ?: return
        val hasAmount = quantityCheckMethod.invoke(player)
        var finalAmount = amount
        if(hasAmount == 0){
            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
        } else {
            if(amount == -2){
                sendInputDialogue(player, InputType.AMOUNT, "Enter the amount:"){value ->
                    var amt = value as Int
                    if(amt > hasAmount){
                        amt = hasAmount
                    }
                    if(amt > player.inventory.freeSlots()){
                        amt = player.inventory.freeSlots()
                    }
                    if(amt <= 0){
                        player.dialogueInterpreter.sendDialogue("You don't have enough inventory space for that.")
                    } else {
                        player.inventory.add(Item(item, amt))
                        updateQuantityMethod.invoke(player, -amt)
                    }
                }
                return
            }
            if(amount == -1){
                finalAmount = player.inventory.freeSlots()
            }
            if(finalAmount > hasAmount){
                finalAmount = hasAmount
            }
            if(!player.inventory.hasSpaceFor(Item(item,finalAmount)) || finalAmount == 0){
                player.dialogueInterpreter.sendDialogue("You don't have enough inventory space for that.")
                return
            }
            player.inventory.add(Item(item,finalAmount))
            updateQuantityMethod.invoke(player,-finalAmount)
        }
    }

    fun getAmount(opcode: Int): Int{
        return when(opcode) {
            155 -> 1
            196 -> 5
            124 -> -1
            199 -> -2
            else -> 0
        }
    }

    private fun hasRake(player: Player?): Boolean{
        return getVarbit(player!!, 1435) == 1
    }

    private fun setHasRake(player: Player?,hasRake: Boolean){
        setVarbit(player!!, 1435, if (hasRake) 1 else 0, true)
    }

    private fun hasDibber(player: Player?): Boolean{
        return getVarbit(player!!, 1436) == 1
    }

    private fun setHasDibber(player: Player?,hasDibber: Boolean){
        setVarbit(player!!, 1436, if (hasDibber) 1 else 0, true)
    }

    private fun hasSpade(player: Player?): Boolean{
        return getVarbit(player!!, 1437) == 1
    }

    private fun setHasSpade(player: Player?,hasSpade: Boolean){
        setVarbit(player!!, 1437, if (hasSpade) 1 else 0, true)
    }

    private fun hasSecateurs(player: Player?): Boolean{
        return getVarbit(player!!, 1438) == 1
    }

    private fun setHasSecateurs(player: Player?,hasSecateurs: Boolean){
        setVarbit(player!!, 1438, if (hasSecateurs) 1 else 0, true)
    }

    private fun hasWateringCan(player: Player?): Boolean{
        return getVarbit(player!!, 1439) > 0
    }

    private fun getWateringCan(player: Player?): Int{
        var can = getVarbit(player!!, 1439) //Watering cans are stored in the Varp as a number between 1 and 9. Watering Can(0) is 1 and Watering Can(8) is 9
        if(can == 1) can = 0
        return Items.WATERING_CAN_5331 + can
    }
    
    private fun setWateringCan(player: Player?, item: Item){
        val can = when(item.id){
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

    private fun setNoWateringCan(player: Player?){
        setVarbit(player!!, 1439, 0, true)
    }

    private fun hasGardeningTrowel(player: Player?): Boolean {
        return getVarbit(player!!, 1440) == 1
    }

    private fun setHasGardeningTrowel(player: Player?,hasTrowel: Boolean){
        setVarbit(player!!, 1440, if (hasTrowel) 1 else 0, true)
    }

    private fun getNumBuckets(player: Player?): Int {
        return getVarbit(player!!, 1441)
    }

    private fun updateBuckets(player: Player?,amount: Int){
        setVarbit(player!!, 1441, getNumBuckets(player) + amount, true)
    }

    private fun getNumCompost(player: Player?): Int {
        return getVarbit(player!!, 1442)
    }

    private fun updateCompost(player: Player?,amount: Int){
        setVarbit(player!!, 1442, getNumCompost(player) + amount, true)
    }

    private fun getNumSuperCompost(player: Player?): Int {
        return getVarbit(player!!, 1443)
    }

    private fun updateSuperCompost(player: Player?,amount: Int){
        setVarbit(player!!, 1443, getNumSuperCompost(player) + amount, true)
    }

    private fun hasMagicSecateurs(player: Player?): Boolean {
        return getVarbit(player!!, 1848) == 1
    }

    private fun setHasMagicSecateurs(player: Player?,hasMagic: Boolean){
        setVarbit(player!!, 1848, if (hasMagic) 1 else 0, true)
    }

    private fun getHighestCan(player: Player?): Item?{
        player ?: return null
        var highestCan = Item(0)
        for(item in player.inventory.toArray()){
            if(item == null) continue
            if(item.name.contains("Watering")){
                if(item.id > highestCan.id) highestCan = item
            }
        }
        return if(highestCan.id == 0) null else highestCan
    }

}
