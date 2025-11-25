package content.global.handlers.item.equipment

import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class BarrowsEquipmentRegister : Plugin<Any> {
    override fun newInstance(arg: Any?): Plugin<Any> {
        BarrowsEquipment.getAllEquipmentSets().forEach {
            degradationSet ->
            EquipmentDegrader.registerSet(BarrowsEquipment.DEGRADATION_TICKS_PER_TIER, degradationSet.toTypedArray())
        }
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }
}
