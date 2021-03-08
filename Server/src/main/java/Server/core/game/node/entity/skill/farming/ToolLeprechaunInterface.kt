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
        if(component.id == 125) {
            player?.interfaceManager?.openSingleTab(Component(126))
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

            125 -> {
                when(button){

                    33 -> {
                        if(!hasRake(player)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.RAKE_5341))
                            setHasRake(player,false)
                        }
                    }

                    34 -> {
                        if(!hasDibber(player)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.SEED_DIBBER_5343))
                            setHasDibber(player,false)
                        }
                    }

                    35 -> {
                        if(!hasSpade(player)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.SPADE_952))
                            setHasSpade(player,false)
                        }
                    }

                    36 -> {
                        if(!hasSecateurs(player)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            if(hasMagicSecateurs(player)){
                                player.inventory.add(Item(Items.MAGIC_SECATEURS_7409))
                            } else {
                                player.inventory.add(Item(Items.SECATEURS_5329))
                            }
                            setHasSecateurs(player,false)
                        }
                    }

                    37 -> {
                        if(!hasWateringCan(player)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(getWateringCan(player)))
                            setNoWateringCan(player)
                        }
                    }

                    38 -> {
                        if(!hasGardeningTrowel(player)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.GARDENING_TROWEL_5325))
                            setHasGardeningTrowel(player,false)
                        }
                    }

                    39 -> {
                        if(getNumBuckets(player) == 0){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.BUCKET_1925))
                            updateBuckets(player,-1)
                        }
                    }

                    40 -> {
                        if(getNumCompost(player) == 0){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.COMPOST_6032))
                            updateCompost(player,-1)
                        }
                    }

                    41 -> {
                        if(getNumSuperCompost(player) == 0){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those stored.")
                        } else {
                            player.inventory.add(Item(Items.SUPERCOMPOST_6034))
                            updateSuperCompost(player,-1)
                        }
                    }
                }
            }

            126 -> {

                when(button){

                    18 -> {
                        if(!player.inventory.containsItem(Item(Items.RAKE_5341))){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if(!hasRake(player)) {
                            player.inventory.remove(Item(Items.RAKE_5341))
                            setHasRake(player,true)
                        } else {
                            player.dialogueInterpreter.sendDialogue("You already have one of those stored.")
                        }
                    }

                    19 -> {
                        if(!player.inventory.containsItem(Item(Items.SEED_DIBBER_5343))){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if(!hasDibber(player)){
                            player.inventory.remove(Item(Items.SEED_DIBBER_5343))
                            setHasDibber(player,true)
                        } else {
                            player.dialogueInterpreter.sendDialogue("You already have one of those stored.")
                        }
                    }

                    20 -> {
                        if(!player.inventory.containsItem(Item(Items.SPADE_952))){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if(!hasSpade(player)){
                            player.inventory.remove(Item(Items.SPADE_952))
                            setHasSpade(player,true)
                        }
                    }

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
                                setHasDibber(player,true)
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

                    23 -> {
                        if(!player.inventory.contains(Items.GARDENING_TROWEL_5325,1)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if (!hasGardeningTrowel(player)){
                            player.inventory.remove(Item(Items.GARDENING_TROWEL_5325))
                            setHasGardeningTrowel(player,true)
                        } else {
                            player.dialogueInterpreter.sendDialogue("You already have one of those stored.")
                        }
                    }

                    24 -> {
                        if(!player.inventory.contains(Items.BUCKET_1925,1)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if(getNumBuckets(player) < 31){
                            player.inventory.remove(Item(Items.BUCKET_1925))
                            updateBuckets(player,1)
                        } else {
                            player.dialogueInterpreter.sendDialogue("You already have enough of those stored.")
                        }
                    }

                    25 -> {
                        if(!player.inventory.contains(Items.COMPOST_6032,1)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if(getNumCompost(player) < 255){
                            player.inventory.remove(Item(Items.COMPOST_6032))
                            updateCompost(player,1)
                        } else {
                            player.dialogueInterpreter.sendDialogue("You already have enough of those stored.")
                        }
                    }

                    26 -> {
                        if(!player.inventory.contains(Items.SUPERCOMPOST_6034,1)){
                            player.dialogueInterpreter.sendDialogue("You don't have any of those to store.")
                        } else if(getNumSuperCompost(player) < 255){
                            var amount = when(opcode) {
                                155 -> 1
                                196 -> 5
                                124 -> -1
                                199 -> -2
                                else -> 0
                            }
                            if(amount == -2) {
                                player.setAttribute("runscript", object : RunScript() {
                                    override fun handle(): Boolean {
                                        var amt = getValue().toString().toInt()
                                        if(amt > player.inventory.getAmount(Items.SUPERCOMPOST_6034)){
                                            amt = player.inventory.getAmount(Items.SUPERCOMPOST_6034)
                                        }
                                        if(amt > (255 - getNumSuperCompost(player))){
                                            amt = 255 - getNumSuperCompost(player)
                                        }
                                        player.inventory.remove(Item(Items.SUPERCOMPOST_6034,amt))
                                        updateSuperCompost(player,amt)
                                        return true
                                    }
                                })
                                player.dialogueInterpreter.sendInput(false, "Enter amount:")
                                return true
                            } else if(amount == -1) {
                                amount = player.inventory.getAmount(Item(Items.SUPERCOMPOST_6034))
                                if (amount > (255 - getNumSuperCompost(player))) amount = 255 - getNumSuperCompost(player)
                            }
                            player.inventory.remove(Item(Items.SUPERCOMPOST_6034))
                            updateSuperCompost(player, 1)
                        } else {
                            player.dialogueInterpreter.sendDialogue("You already have enough of those stored.")
                        }
                    }

                }

            }

        }

        player.varpManager.get(varp).send(player)
        return true
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