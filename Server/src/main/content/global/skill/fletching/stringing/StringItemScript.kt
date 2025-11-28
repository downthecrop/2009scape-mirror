package content.global.skill.fletching.stringing

import content.global.skill.fletching.AchievementDiaryAttributeKeys
import content.global.skill.fletching.Zones
import content.global.skill.fletching.stringing.StringableCraftInfo.Companion.applicableStringId
import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item

/**
 * Represents queueScript to string a bow/crossbow
 * @author Ceikry
 * @param player the player.
 * @param stringableCraftInfo contains crafting information about what we're stringing
 * @param amount the amount of items to string
 */
class StringItemScript(
    private val player: Player,
    private val stringableCraftInfo: StringableCraftInfo,
    private var amount: Int
) {
    private val initialDelay = 1
    private val subsequentDelay = 2

    fun invoke() {
        queueScript(player, initialDelay) { stage ->
            if (!clockReady(player, Clocks.SKILLING)) return@queueScript keepRunning(player)

            if (getDynLevel(player, Skills.FLETCHING) < stringableCraftInfo.level) {
                StringingListeners.sendLevelCheckFailDialogue(player, stringableCraftInfo.level)
                return@queueScript stopExecuting(player) // Check each iteration since dynLevel can change (status effects ending, skill assist session end...)
            }

            if (
                removeItemsIfPlayerHasEnough(
                    player,
                    Item(stringableCraftInfo.unstrungItemId),
                    Item(stringableCraftInfo.applicableStringId)
                )
            ) {
                player.animate(stringableCraftInfo.animation)

                addItem(player, stringableCraftInfo.strungItemId, 1)
                rewardXP(player, Skills.FLETCHING, stringableCraftInfo.experience)
                sendMessage(player, "You add a string to the bow.")
                if (stringableCraftInfo == StringableCraftInfo.MAGIC_SHORTBOW) {
                    handleSeersMagicShortbowAchievement()
                }
            } else {
                return@queueScript stopExecuting(player)
            }

            if (stage >= amount - 1) {
                return@queueScript stopExecuting(player)
            }

            return@queueScript delayClock(player, Clocks.SKILLING, subsequentDelay, true)
        }
    }

    private fun handleSeersMagicShortbowAchievement() {
        if (Zones.inAnyZone(player, Zones.seersMagicShortbowAchievementZones) &&
            getAttribute(player, AchievementDiaryAttributeKeys.FLETCHED_UNSTRUNG_MAGIC_SHORTBOW, false)
        ) {
            player.achievementDiaryManager.finishTask(player, DiaryType.SEERS_VILLAGE, 2, 2)
        }
    }
}