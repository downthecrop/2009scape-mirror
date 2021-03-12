package core.game.node.entity.equipment

import core.game.node.entity.combat.equipment.EquipmentDegrader
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class PVPEquipmentRegister : Plugin<Any> {
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
        EquipmentDegrader.registerSet(VESTA_BODY)
        EquipmentDegrader.registerSet(VESTA_SKIRT)
        EquipmentDegrader.registerSet(VESTA_SWORD)
        EquipmentDegrader.registerSet(VESTA_SPEAR)
        EquipmentDegrader.registerSet(STATIUS_BODY)
        EquipmentDegrader.registerSet(STATIUS_HAMMER)
        EquipmentDegrader.registerSet(STATIUS_HELM)
        EquipmentDegrader.registerSet(STATIUS_LEGS)
        EquipmentDegrader.registerSet(ZUREL_BOTTOM)
        EquipmentDegrader.registerSet(ZURIEL_HOOD)
        EquipmentDegrader.registerSet(ZURIEL_STAFF)
        EquipmentDegrader.registerSet(ZURIEL_TOP)
        EquipmentDegrader.registerSet(MORRIGAN_BODY)
        EquipmentDegrader.registerSet(MORRIGAN_CHAP)
        EquipmentDegrader.registerSet(MORRIGAN_COIF)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

}