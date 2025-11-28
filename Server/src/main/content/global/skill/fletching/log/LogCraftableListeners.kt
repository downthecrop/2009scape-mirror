package content.global.skill.fletching.log

import content.data.Quests
import content.global.skill.fletching.log.GrammarHelpers.aOrAn
import content.global.skill.fletching.log.GrammarHelpers.makeFriendlyName
import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items

@Suppress("unused") // Reflectively loaded
class LogCraftableListeners : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.KNIFE_946, *LogCraftInfo.logIds) { player, knife, log ->
            val applicableCraftInfosForLog = LogCraftInfo.forLogId(log.id) ?: return@onUseWith false
            val craftableItems =
                applicableCraftInfosForLog.map { logCraftable -> Item(logCraftable.finishedItemId) }.toTypedArray()

            val handler: SkillDialogueHandler = object : SkillDialogueHandler(
                player,
                SkillDialogue.forLength(applicableCraftInfosForLog.size),
                *craftableItems
            ) {
                override fun create(amount: Int, index: Int) {
                    if (!playerMeetsInitialRequirements(applicableCraftInfosForLog[index])) return
                    CraftItemWithLogScript(player, applicableCraftInfosForLog[index], amount).invoke()
                }

                private fun playerMeetsInitialRequirements(logCraftInfo: LogCraftInfo): Boolean {
                    if (getDynLevel(player, Skills.FLETCHING) < logCraftInfo.level) {
                        sendLevelCheckFailDialog(player, logCraftInfo)
                        return false
                    }
                    if (logCraftInfo == LogCraftInfo.OGRE_ARROW_SHAFT &&
                        !isQuestStarted(player, Quests.BIG_CHOMPY_BIRD_HUNTING)
                    ) {
                        sendDialogue(player, "You must have started ${Quests.BIG_CHOMPY_BIRD_HUNTING} to make those.")
                        return false
                    }
                    if (logCraftInfo == LogCraftInfo.OGRE_COMP_BOW) {
                        if (getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) < 8) {
                            sendMessage(
                                player,
                                "You must have started Zogre Flesh Eaters and asked Grish to string this."
                            )
                            return false
                        }
                        if (amountInInventory(player, Items.WOLF_BONES_2859) < 1) {
                            sendMessage(player, "You need to have Wolf Bones in order to make this.")
                            return false
                        }
                    }
                    return true
                }

                override fun getAll(index: Int): Int {
                    return amountInInventory(player, log.id)
                }
            }
            handler.open()
            return@onUseWith true
        }
    }

    companion object {
        fun sendLevelCheckFailDialog(player: Player, logCraftInfo: LogCraftInfo) {
            val finishedItem = Item(logCraftInfo.finishedItemId)
            val friendlyCraftedItemName = makeFriendlyName(finishedItem.name)
            sendDialogue(
                player,
                "You need a Fletching skill of ${logCraftInfo.level} or above to make ${
                    aOrAn(
                        friendlyCraftedItemName
                    )
                } $friendlyCraftedItemName."
            )
        }
    }
}