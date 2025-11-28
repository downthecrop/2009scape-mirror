package content.global.skill.fletching.darts

import content.data.Quests
import content.global.skill.fletching.Feathers
import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import kotlin.math.min

@Suppress("unused") // Reflectively loaded
class DartListeners : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Feathers.STANDARD, *DartCraftInfo.tipIDs) { player, feather, dartTip ->
            val dartCraftInfo = DartCraftInfo.fromTipID(dartTip.id) ?: return@onUseWith false
            val handler: SkillDialogueHandler =
                object :
                    SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(dartCraftInfo.dartItemId)) {
                    override fun create(amount: Int, index: Int) {
                        if (!playerMeetsInitialRequirements()) return
                        DartCraftScript(player, dartCraftInfo, amount).invoke()
                    }

                    private fun playerMeetsInitialRequirements(): Boolean {
                        if (getDynLevel(player, Skills.FLETCHING) < dartCraftInfo.level) {
                            sendLevelCheckFailDialog(player, dartCraftInfo.level)
                            return false
                        }
                        if (!isQuestComplete(player, Quests.THE_TOURIST_TRAP)) {
                            sendDialogue(player, "You need to have completed Tourist Trap to fletch darts.")
                            return false
                        }
                        if (!hasSpaceFor(player, Item(dartCraftInfo.dartItemId))) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return false
                        }
                        return true
                    }

                    override fun getAll(index: Int): Int {
                        return min(
                            amountInInventory(player, feather.id),
                            amountInInventory(player, dartTip.id)
                        )
                    }
                }
            handler.open()
            return@onUseWith true
        }
    }
    companion object {
        fun sendLevelCheckFailDialog(player: Player, level: Int) {
            sendDialogue(player, "You need a fletching level of $level to do this.")
        }
    }
}