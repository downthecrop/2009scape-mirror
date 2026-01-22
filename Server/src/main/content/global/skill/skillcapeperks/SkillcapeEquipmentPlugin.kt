package content.global.skill.skillcapeperks

import core.api.sendMessage
import core.game.container.Container
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.game.container.impl.EquipmentContainer
import content.data.skill.SkillcapePerks as DataSkillcapes
import content.global.skill.slayer.SlayerEquipmentFlags
import org.rs09.consts.Items
import java.util.ArrayList

class SkillcapeEquipmentPlugin : InteractionListener {
    override fun defineListeners() {
        val capeIds = ArrayList<Int>()
        for(cape in DataSkillcapes.values()){
            cape.skillcapeIds.forEach { capeIds.add(it) }
        }
        val capes = capeIds.toIntArray()
        onEquip(capes){player, node -> true}
        onUnequip(capes){player, node ->
            val skillcape = Skillcape.forId(node.id)
            // For Temple of Ikov. Do not let player unequip firemaking skillcape in the dark basement (need to keep an active light source).
            if(player.location.isInRegion(10648) && (node.id == Items.FIREMAKING_CAPE_9804 || node.id == Items.FIREMAKING_CAPET_9805) && GameWorld.settings?.skillcape_perks == true) {
                sendMessage(player, "Unequipping that skillcape would leave you without a light source.")
                return@onUnequip false
            }
            val perk = SkillcapePerks.forSkillcape(skillcape)
            perk.deactivate(player)
            return@onUnequip true
        }
    }
    companion object {
        @JvmStatic
        fun updateCapePerks(player: Player, c: Container, slot: Int) {
            if (slot == EquipmentContainer.SLOT_CAPE) {
                val cape = c.get(slot)
                for (perk in SkillcapePerks.values()) {
                    if (SkillcapePerks.isActive(perk, player)) {
                        perk.deactivate(player)
                    }
                }
                if (cape != null) {
                    val skillcape = Skillcape.forId(cape.id)
                    val perk = SkillcapePerks.forSkillcape(skillcape)
                    if (perk != SkillcapePerks.NONE) {
                        perk.activate(player)
                    }
                }
            }
            EquipmentContainer.updateBonuses(player)
            SlayerEquipmentFlags.updateFlags(player)
        }
    }
}
