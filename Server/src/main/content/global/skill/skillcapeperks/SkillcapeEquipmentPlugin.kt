package content.global.skill.skillcapeperks

import core.game.interaction.InteractionListener

class SkillcapeEquipmentPlugin : InteractionListener {
    override fun defineListeners() {
        val capeIds = ArrayList<Int>()
        for(cape in content.data.skill.SkillcapePerks.values()){
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