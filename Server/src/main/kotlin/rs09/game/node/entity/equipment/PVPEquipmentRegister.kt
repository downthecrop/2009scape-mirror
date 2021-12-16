package rs09.game.node.entity.equipment

import rs09.game.node.entity.combat.equipment.EquipmentDegrader
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class PVPEquipmentRegister : Plugin<Any> {
    // PVP equipment lasts for 1 hour of combat, which is 6000 ticks
    val TICKS = 6000

    val VESTA_BODY = arrayOf(13887,13889)
    val VESTA_SKIRT = arrayOf(13893,13895)
    val VESTA_SWORD = arrayOf(13899,13901)
    val VESTA_SPEAR = arrayOf(13905,13907)

    val STATIUS_BODY = arrayOf(13884,13886)
    val STATIUS_LEGS = arrayOf(13890,13892)
    val STATIUS_HELM = arrayOf(13896,13898)
    val STATIUS_HAMMER = arrayOf(13902,13904)

    val ZURIEL_TOP = arrayOf(13858,13860)
    val ZUREL_BOTTOM = arrayOf(13861,13863)
    val ZURIEL_HOOD = arrayOf(13864,13866)
    val ZURIEL_STAFF = arrayOf(13867, 13869)

    val MORRIGAN_BODY = arrayOf(13870,13872)
    val MORRIGAN_CHAP = arrayOf(13873,13875)
    val MORRIGAN_COIF = arrayOf(13876,13878)


    override fun newInstance(arg: Any?): Plugin<Any> {
        EquipmentDegrader.registerSet(TICKS, VESTA_BODY)
        EquipmentDegrader.registerSet(TICKS, VESTA_SKIRT)
        EquipmentDegrader.registerSet(TICKS, VESTA_SWORD)
        EquipmentDegrader.registerSet(TICKS, VESTA_SPEAR)
        EquipmentDegrader.registerSet(TICKS, STATIUS_BODY)
        EquipmentDegrader.registerSet(TICKS, STATIUS_HAMMER)
        EquipmentDegrader.registerSet(TICKS, STATIUS_HELM)
        EquipmentDegrader.registerSet(TICKS, STATIUS_LEGS)
        EquipmentDegrader.registerSet(TICKS, ZUREL_BOTTOM)
        EquipmentDegrader.registerSet(TICKS, ZURIEL_HOOD)
        EquipmentDegrader.registerSet(TICKS, ZURIEL_STAFF)
        EquipmentDegrader.registerSet(TICKS, ZURIEL_TOP)
        EquipmentDegrader.registerSet(TICKS, MORRIGAN_BODY)
        EquipmentDegrader.registerSet(TICKS, MORRIGAN_CHAP)
        EquipmentDegrader.registerSet(TICKS, MORRIGAN_COIF)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

}
