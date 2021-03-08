package core.game.node.entity.skill.farming

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.RunScript
import core.game.node.entity.skill.crafting.jewellery.JewelleryCrafting
import core.game.node.item.Item
import core.game.system.SystemLogger
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Components
import core.tools.Items

private const val varp = 615
@Initializable
class ToolLeprechaunInterface : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(Components.farming_tools_125,this)
        ComponentDefinition.put(Components.farming_tools_side_126,this)
        return this
    }

    override fun open(player: Player?, component: Component?) {
        component ?: return
        super.open(player, component)
        player?.varpManager?.flagSave(varp)
        if(component.id == Components.farming_tools_125) {
            player?.interfaceManager?.openSingleTab(Component(Components.farming_tools_side_126))
            component.setCloseEvent { pl, _ ->
                pl?.interfaceManager?.closeSingleTab()
                true
            }
        }
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        component ?: return false

        when(component.id){

            Components.farming_tools_125 -> {
                when(button){
                    33 -> doWithdrawal(player,Items.RAKE_5341,::setHasRake,::hasRake)

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
            }

            Components.farming_tools_side_126 -> {

                when(button){

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

            }

        }

        player.varpManager.get(varp).send(player)
        return true
    }

    private fun doWithdrawal(player: Player?, item: Int, withdrawMethod: (Player?,Boolean) -> Unit, checkMethod: (Player?) -> Boolean){
        player ?: return
        if(!checkMethod.invoke(player)){
            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
        } else {
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
        val spaceLeft = 255 - quantityCheckMethod.invoke(player)

        if(amount == -2){
            player.setAttribute("runscript", object : RunScript() {
                override fun handle(): Boolean {
                    var amt = getValue().toString().toInt()
                    if(amt > hasAmount){
                        amt = hasAmount
                    }
                    if(amt > spaceLeft){
                        amt = spaceLeft
                    }
                    player.inventory.remove(Item(item,amt))
                    updateQuantityMethod.invoke(player,amt)
                    player.varpManager.get(varp).send(player)
                    return true
                }
            })
            player.dialogueInterpreter.sendInput(false, "Enter amount:")
            return
        }
        if(amount == -1){
            finalAmount = hasAmount
            if(finalAmount > spaceLeft){
                finalAmount = spaceLeft
            }
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
                player.setAttribute("runscript", object : RunScript() {
                    override fun handle(): Boolean {
                        var amt = getValue().toString().toInt()
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
                            player.varpManager.get(varp).send(player)
                        }
                        return true
                    }
                })
                player.dialogueInterpreter.sendInput(false, "Enter amount:")
                return
            }
            if(amount == -1){
                finalAmount = player.inventory.freeSlots()
            }
            if(finalAmount > hasAmount){
                finalAmount = hasAmount
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
        return checkBit(player,0)
    }

    private fun setHasRake(player: Player?,hasRake: Boolean){
        player?.varpManager?.get(varp)?.setVarbit(0,if(hasRake) 1 else 0)
    }

    private fun hasDibber(player: Player?): Boolean{
        return checkBit(player,1)
    }

    private fun setHasDibber(player: Player?,hasDibber: Boolean){
        player?.varpManager?.get(varp)?.setVarbit(1,if(hasDibber) 1 else 0)
    }

    private fun hasSpade(player: Player?): Boolean{
        return checkBit(player,2)
    }

    private fun setHasSpade(player: Player?,hasSpade: Boolean){
        player?.varpManager?.get(varp)?.setVarbit(2,if(hasSpade) 1 else 0)
    }

    private fun hasSecateurs(player: Player?): Boolean{
        return checkBit(player,3)
    }

    private fun setHasSecateurs(player: Player?,hasSecateurs: Boolean){
        player?.varpManager?.get(varp)?.setVarbit(3,if(hasSecateurs) 1 else 0)
    }

    private fun hasWateringCan(player: Player?): Boolean{
        return checkBitRange(player,4,7) > 0
    }

    private fun getWateringCan(player: Player?): Int{
        var can = checkBitRange(player,4,7) //Watering cans are stored in the Varp as a number between 1 and 9. Watering Can(0) is 1 and Watering Can(8) is 9
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
        player?.varpManager?.get(varp)?.setVarbit(4,can)
    }

    private fun setNoWateringCan(player: Player?){
        player?.varpManager?.get(varp)?.setVarbit(4,0)
    }

    private fun hasGardeningTrowel(player: Player?): Boolean {
        return checkBit(player,8)
    }

    private fun setHasGardeningTrowel(player: Player?,hasTrowel: Boolean){
        player?.varpManager?.get(varp)?.setVarbit(8,if(hasTrowel) 1 else 0)
    }

    private fun getNumBuckets(player: Player?): Int {
        return checkBitRange(player,9,13)
    }

    private fun updateBuckets(player: Player?,amount: Int){
        player?.varpManager?.get(varp)?.setVarbit(9,getNumBuckets(player) + amount)
    }

    private fun getNumCompost(player: Player?): Int {
        return checkBitRange(player,14,21)
    }

    private fun updateCompost(player: Player?,amount: Int){
        player?.varpManager?.get(varp)?.setVarbit(14,getNumCompost(player) + amount)
    }

    private fun getNumSuperCompost(player: Player?): Int {
        return checkBitRange(player,22,29)
    }

    private fun updateSuperCompost(player: Player?,amount: Int){
        player?.varpManager?.get(varp)?.setVarbit(22,getNumSuperCompost(player) + amount)
    }

    private fun hasMagicSecateurs(player: Player?): Boolean {
        return checkBit(player,30)
    }

    private fun setHasMagicSecateurs(player: Player?,hasMagic: Boolean){
        player?.varpManager?.get(varp)?.setVarbit(30,if(hasMagic) 1 else 0)
    }

    private fun checkBitRange(player: Player?,start: Int, end: Int): Int{
        return (player?.varpManager?.get(varp)?.getBitRangeValue(start,end) ?: 0) ushr start
    }

    private fun checkBit(player: Player?,offset: Int): Boolean{
        return player?.varpManager?.get(varp)?.getVarbitValue(offset) == 1
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