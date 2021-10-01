package rs09.game.node.entity.state.newsys.states

import core.game.container.impl.EquipmentContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.tools.RandomFunction
import org.json.simple.JSONObject
import org.rs09.consts.Items
import rs09.game.node.entity.state.newsys.PlayerState
import rs09.game.node.entity.state.newsys.State
import rs09.game.system.config.ItemConfigParser
import kotlin.math.ceil

@PlayerState("avadevice")
class AvaDeviceState(player: Player? = null) : State(player) {
    val TICKS = ceil(180 / 0.6).toInt()
    var device = 0

    private val ATTRACTOR_REWARDS = arrayOf(Items.IRON_BAR_2351,
        Items.IRON_KNIFE_863,Items.IRON_DART_807,Items.IRON_DAGGER_1203,Items.IRON_BOLTS_9140,Items.IRON_ARROW_884,Items.IRON_ORE_440,Items.COPPER_ORE_436,Items.IRON_FULL_HELM_1153,Items.IRON_2H_SWORD_1309,Items.STEEL_BAR_2353)
    private val ACCUMULATOR_REWARDS = arrayOf(Items.STEEL_BAR_2353,Items.STEEL_2H_SWORD_1311,Items.STEEL_KNIFE_865,Items.STEEL_DAGGER_1207,Items.STEEL_MED_HELM_1141,Items.STEEL_DART_808,Items.STEEL_BOLTS_9141,Items.STEEL_ARROW_886,Items.IRON_BAR_2351)

    override fun save(root: JSONObject) {
        root.put("device-id",device)
    }

    override fun parse(_data: JSONObject) {
        if(_data.containsKey("device-id")){
            device = _data["device-id"].toString().toInt()
        }
    }

    override fun newInstance(player: Player?): State {
        return AvaDeviceState(player)
    }

    override fun createPulse() {
        player ?: return
        if(device == 0 && !hasDevice(player)) return
        if(player.savedData.globalData.isAvasDisabled) return

        pulse = object : Pulse(TICKS){
            override fun pulse(): Boolean {
                if(!hasDevice(player)){
                    player.clearState("avadevice")
                    pulse = null
                    return true
                }

                if(isInterfered()) return false

                if(RandomFunction.random(10) > 4) return false

                val rewards = if(isAccumulator()) ACCUMULATOR_REWARDS else ATTRACTOR_REWARDS

                val item = Item(rewards.random())

                if (item.definition.getConfiguration(ItemConfigParser.EQUIP_SLOT, -1) == EquipmentContainer.SLOT_ARROWS) {
                    val arrowSlot = player.equipment[EquipmentContainer.SLOT_ARROWS]
                    if (arrowSlot == null || arrowSlot.id == item.getId()) {
                        player.equipment.add(item, true, EquipmentContainer.SLOT_ARROWS)
                        return false
                    }
                }
                player.inventory.add(item, player)

                return !hasDevice(player)
            }
        }
    }

    fun hasDevice(player: Player?) : Boolean{
        player ?: return false
        return player.equipment.containsAtLeastOneItem(intArrayOf(Items.AVAS_ACCUMULATOR_10499,Items.AVAS_ATTRACTOR_10498))
    }

    fun isAccumulator() : Boolean{
        return device == Items.AVAS_ACCUMULATOR_10499
    }

    //just took this method from the old arios pulse as well (I'm being lazy)
    private fun isInterfered(): Boolean {
        player ?: return true
        val cape = player.equipment[EquipmentContainer.SLOT_CAPE]
        if (cape != null && (cape.id == 10498 || cape.id == 10499)) {
            val torso = player.equipment[EquipmentContainer.SLOT_CHEST]
            val modelId = torso?.definition?.maleWornModelId1 ?: -1
            if (modelId == 301 || modelId == 306 || modelId == 3379) {
                player.packetDispatch.sendMessage("Your armour interferes with Ava's device.")
                return true
            }
        }
        return false
    }

}
