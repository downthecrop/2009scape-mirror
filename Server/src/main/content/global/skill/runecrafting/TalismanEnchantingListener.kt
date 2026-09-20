package content.global.skill.runecrafting

import content.region.asgarnia.falador.diary.FaladorAchievementDiary.Companion.EasyTasks.MAKE_AIR_TIARA
import content.region.misthalin.varrock.diary.VarrockAchivementDiary.Companion.MediumTasks.CRAFT_EARTH_TIARA
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Animator
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items

/**
 * Handles enchanting of runecrafting tiaras and staffs at altars.
 *
 * Source 1: (June 2008) https://www.youtube.com/watch?v=2fWfcItyuRc
 * Source 2 (tiara): (May 2009) https://www.youtube.com/watch?v=VByvjBhrac8
 * Source 3 (staff): (July 2009) https://www.youtube.com/watch?v=arlCYriOEEo
 *
 * November 2008 showing instant crafting of staff when used with altar: https://www.youtube.com/watch?v=-V6_B9BsbNs
 * December 2009 showing instant crafting of tiara when used with altar: https://www.youtube.com/watch?v=XvGa2Sen508
 */
class TalismanEnchantingListener : InteractionListener {

    companion object {
        private val ANIMATION = Animation(791, Animator.Priority.HIGH)
        private val GRAPHICS = Graphics(186, 100)
    }

    // checks that you have the talisman and tiara or staff
    private fun checkRequirements(player: Player, isTiara: Boolean, talismanId: Int, consumedBaseId: Int): Boolean {
        if (!inInventory(player, talismanId)) {
            sendDialogue(player, "You don't have the required talisman.")
            return false
        }
        if (!inInventory(player, consumedBaseId)) {
            sendDialogue(player, "You need a ${if (isTiara) "silver tiara" else "talisman staff"}.")
            return false
        }
        return true
    }

    // enchants a talisman and either a tiara or runecrafting staff into the rune's tiara or talisman staff
    private fun enchantTalisman(player: Player, altar: Altar, isTiara: Boolean) {

        val type = if (isTiara) "tiara" else "staff"
        val talisman = altar.talisman?.item
        val consumedBase = Item(if (isTiara) Items.TIARA_5525 else Items.RUNECRAFTING_STAFF_13629)
        val result = if (isTiara) altar.tiara?.item else altar.staff?.item
        val exp = if (isTiara) altar.tiara?.experience ?: 0.0 else 0.0

        if (result == null || talisman == null) return

        // check requirements
        if (!checkRequirements(player, isTiara, talisman.id, consumedBase.id)) {
            return
        }

        // run the enchantment
        queueScript(player, 0, QueueStrength.SOFT) { counter ->
            lock(player, 3)
            when(counter) {
                0 -> {
                    visualize(player, ANIMATION, GRAPHICS)
                    sendMessage(player, "You bind the power of the talisman into your $type.")
                    return@queueScript delayScript(player, 2)
                }
                1 -> {
                    if (removeItem(player, consumedBase) && removeItem(player, talisman)) {
                        addItem(player, result.id)
                        rewardXP(player, Skills.RUNECRAFTING, exp)

                        // achievement diary check
                        if (isTiara && altar.tiara == Tiara.AIR) {
                            player.achievementDiaryManager.finishTask(player, DiaryType.FALADOR, 0, MAKE_AIR_TIARA)
                        }
                        if (isTiara && altar.tiara == Tiara.EARTH) {
                            player.achievementDiaryManager.finishTask(player, DiaryType.VARROCK, 1, CRAFT_EARTH_TIARA)
                        }
                    }
                    return@queueScript delayScript(player, 1)
                }
                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    // if a talisman is used, player must choose between staff or tiara
    private fun handleTalismanUse(player: Player, altar: Altar) {
        sendDialogueOptions(player, "Do you want to enchant a tiara or staff?", "Tiara.", "Staff.")
        addDialogueAction(player) { _, buttonId ->
            when (buttonId) {
                2 -> enchantTalisman(player, altar, isTiara = true)
                3 -> enchantTalisman(player, altar, isTiara = false)
            }
            return@addDialogueAction
        }
    }

    // using the talisman, tiara, or staff with an altar
    override fun defineListeners() {
        Altar.values().forEach { altar ->
            val talismanId = altar.talisman?.item?.id ?: return@forEach
            onUseWith(IntType.SCENERY, intArrayOf(talismanId, Items.TIARA_5525, Items.RUNECRAFTING_STAFF_13629), altar.objectId) { player, used, _ ->
                when (used.id) {
                    talismanId -> handleTalismanUse(player, altar)
                    Items.TIARA_5525 -> enchantTalisman(player, altar, isTiara = true)
                    Items.RUNECRAFTING_STAFF_13629 -> enchantTalisman(player, altar, isTiara = false)
                }
                return@onUseWith true
            }
        }
    }
}