package content.global.skill.skillcapeperks

import core.api.sendMessage
import core.game.interaction.InteractionListener
import core.game.world.GameWorld

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

            // For Temple of Ikov. Do not let player unequip firemaking skillcape in the dark basement (need to keep an active light source).
            if(player.location.isInRegion(10648) && (node.id == 9804 || node.id == 9805) && GameWorld.settings?.skillcape_perks == true) {
                sendMessage(player, "Unequipping that skillcape would leave you without a light source.")
                return@onUnequip false
            }

            val perk = SkillcapePerks.forSkillcape(skillcape)
            perk.deactivate(player)
            return@onUnequip true
        }
    }
}