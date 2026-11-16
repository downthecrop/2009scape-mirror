package content.global.handlers.item.equipment

import content.data.Quests
import core.api.*
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items

/**
 * The graphics that run when you equip elemental equipment.
 */

class ElementalEquipmentGraphics : InteractionListener {

    companion object {
        // the graphics that run on equip
        private const val M_SHIELD_EQUIP = 809
        private const val M_HELM_EQUIP = 810
        private const val E_SHIELD_EQUIP = 244
        private const val E_HELM_EQUIP = 811
    }

    override fun defineListeners() {

        onEquip(Items.ELEMENTAL_SHIELD_2890) { player, _ ->
            equipShield(player, Quests.ELEMENTAL_WORKSHOP_I, E_SHIELD_EQUIP)
        }

        onEquip(Items.MIND_SHIELD_9731) { player, _ ->
            equipShield(player, Quests.ELEMENTAL_WORKSHOP_II, M_SHIELD_EQUIP)
        }

        onEquip(Items.ELEMENTAL_HELMET_9729) { player, _ ->
            equipHelm(player, Quests.ELEMENTAL_WORKSHOP_II, E_HELM_EQUIP)
        }

        onEquip(Items.MIND_HELMET_9733) { player, _ ->
            equipHelm(player, Quests.ELEMENTAL_WORKSHOP_II, M_HELM_EQUIP)
        }
    }

    private fun equipShield(player: Player, quest: Quests, gfxId: Int): Boolean {
        if (!isQuestComplete(player, quest)) {
            sendMessage(player, "You need to have completed ${quest.questName} to equip this.")
            return false
        }

        // hide the shield while the gfx plays
        player.appearance.isHideShield = true
        player.updateAppearance()
        visualize(player, -1, Graphics(gfxId, 95))

        // unhide the shield
        queueScript(player, 2, QueueStrength.SOFT) {
            player.appearance.isHideShield = false
            player.updateAppearance()
            stopExecuting(player)
        }

        return true
    }

    private fun equipHelm(player: Player, quest: Quests, gfxId: Int): Boolean {
        if (!isQuestComplete(player, quest)) {
            sendMessage(player, "You need to have completed ${quest.questName} to equip this.")
            return false
        }

        // play the graphics
        visualize(player, -1, gfxId)
        return true
    }
}