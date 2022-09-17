package rs09.game.node.entity.skill.skillcapeperks

import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

class SkillcapeEquipmentPlugin : InteractionListener {
    override fun defineListeners() {
        val capeIds = ArrayList<Int>()
        for(cape in core.game.content.global.SkillcapePerks.values()){
            cape.skillcapeIds.forEach { capeIds.add(it) }
        }
        val capes = capeIds.toIntArray()

        onEquip(capes){player, node ->
            val skillcape = Skillcape.forId(node.id)
            val perk = SkillcapePerks.forSkillcape(skillcape)
            perk.activate(player)
            return@onEquip true
        }

        onUnequip(capes){player, node ->
            val skillcape = Skillcape.forId(node.id)
            val perk = SkillcapePerks.forSkillcape(skillcape)
            perk.deactivate(player)
            return@onUnequip true
        }
    }
}