package rs09.game.node.entity.equipment

import rs09.game.node.entity.combat.equipment.EquipmentDegrader
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class BarrowsEquipmentRegister : Plugin<Any>{
    public companion object {
        // Barrows equipment lasts for 15 hours of combat, and each piece has 4 stages of degredation
        @JvmField
        public val TICKS = (15 * 6000) / 4
    }

    val DHAROK_HELM = arrayOf(4716,4880,4881,4882,4883,4884)
    val DHAROK_AXE = arrayOf(4718,4886,4887,4888,4889,4890)
    val DHAROK_BODY = arrayOf(4720,4892,4893,4894,4895,4896)
    val DHAROK_LEGS = arrayOf(4722,4898,4899,4900,4901,4902)

    val GUTHAN_HELM = arrayOf(4724,4904,4905,4906,4907,4908)
    val GUTHAN_SPEAR = arrayOf(4726,4910,4911,4912,4913,4914)
    val GUTHAN_BODY = arrayOf(4728,4916,4917,4918,4919,4920)
    val GUTHAN_SKIRT = arrayOf(4730,4922,4923,4924,4925,4926)

    val TORAG_HELM = arrayOf(4745,4952,4953,4954,4955,4956)
    val TORAG_HAMMER = arrayOf(4747,4958,4959,4960,4961,4962)
    val TORAG_BODY = arrayOf(4749,4964,4965,4966,4967,4968)
    val TORAG_LEGS = arrayOf(4751,4970,4971,4972,4973,4974)

    val VERAC_HELM = arrayOf(4753,4976,4977,4978,4979,4980)
    val VERAC_FLAIL = arrayOf(4755,4982,4983,4984,4985,4986)
    val VERAC_BRASS = arrayOf(4757,4988,4989,4990,4991,4992)
    val VERAC_SKIRT = arrayOf(4759,4994,4995,4996,4997,4998)

    val AHRIM_HOOD = arrayOf(4708,4856,4857,4858,4859,4860)
    val AHRIM_STAFF = arrayOf(4710,4862,4863,4864,4865,4866)
    val AHRIM_TOP = arrayOf(4712,4868,4869,4870,4871,4872)
    val AHRIM_SKIRT = arrayOf(4714,4874,4875,4876,4877,4878)

    val KARIL_COIF = arrayOf(4732,4928,4929,4930,4931,4932)
    val KARIL_CBOW = arrayOf(4734,4934,4935,4936,4937,4938)
    val KARIL_TOP = arrayOf(4736,4940,4941,4942,4943,4944)
    val KARIL_SKIRT = arrayOf(4738,4946,4947,4948,4949,4950)

    override fun newInstance(arg: Any?): Plugin<Any> {
        EquipmentDegrader.registerSet(TICKS, AHRIM_HOOD)
        EquipmentDegrader.registerSet(TICKS, AHRIM_STAFF)
        EquipmentDegrader.registerSet(TICKS, AHRIM_TOP)
        EquipmentDegrader.registerSet(TICKS, AHRIM_SKIRT)
        EquipmentDegrader.registerSet(TICKS, KARIL_COIF)
        EquipmentDegrader.registerSet(TICKS, KARIL_CBOW)
        EquipmentDegrader.registerSet(TICKS, KARIL_TOP)
        EquipmentDegrader.registerSet(TICKS, KARIL_SKIRT)
        EquipmentDegrader.registerSet(TICKS, DHAROK_HELM)
        EquipmentDegrader.registerSet(TICKS, DHAROK_AXE)
        EquipmentDegrader.registerSet(TICKS, DHAROK_BODY)
        EquipmentDegrader.registerSet(TICKS, DHAROK_LEGS)
        EquipmentDegrader.registerSet(TICKS, GUTHAN_HELM)
        EquipmentDegrader.registerSet(TICKS, GUTHAN_SPEAR)
        EquipmentDegrader.registerSet(TICKS, GUTHAN_BODY)
        EquipmentDegrader.registerSet(TICKS, GUTHAN_SKIRT)
        EquipmentDegrader.registerSet(TICKS, TORAG_HELM)
        EquipmentDegrader.registerSet(TICKS, TORAG_HAMMER)
        EquipmentDegrader.registerSet(TICKS, TORAG_BODY)
        EquipmentDegrader.registerSet(TICKS, TORAG_LEGS)
        EquipmentDegrader.registerSet(TICKS, VERAC_HELM)
        EquipmentDegrader.registerSet(TICKS, VERAC_FLAIL)
        EquipmentDegrader.registerSet(TICKS, VERAC_BRASS)
        EquipmentDegrader.registerSet(TICKS, VERAC_SKIRT)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

}
