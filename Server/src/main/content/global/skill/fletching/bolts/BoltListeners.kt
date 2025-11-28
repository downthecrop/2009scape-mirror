package content.global.skill.fletching.bolts

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
class BoltListeners : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.ITEM, Feathers.all, *BoltCraftInfo.unfinishedBoltIds) { player, feather, unfinishedBolt ->
            val boltCraftInfo = BoltCraftInfo.fromUnfinishedBoltId(unfinishedBolt.id) ?: return@onUseWith false
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(
                    player,
                    SkillDialogue.MAKE_SET_ONE_OPTION,
                    Item(boltCraftInfo.finishedItemId)
                ) {
                    override fun create(amount: Int, index: Int) {
                        if (!playerMeetsInitialRequirements()) return
                        BoltCraftScript(player, boltCraftInfo, feather.asItem(), amount).invoke()
                    }

                    private fun playerMeetsInitialRequirements(): Boolean {
                        if (boltCraftInfo == BoltCraftInfo.BROAD_BOLT && !getSlayerFlags(player).isBroadsUnlocked()) {
                            sendDialogue(player, "You need to unlock the ability to create broad bolts.")
                            return false
                        }
                        if (getDynLevel(player, Skills.FLETCHING) < boltCraftInfo.level) {
                            sendLevelCheckFailDialog(player, boltCraftInfo.level)
                            return false
                        }
                        if (!hasSpaceFor(player, Item(boltCraftInfo.finishedItemId))) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return false
                        }

                        return true
                    }

                    override fun getAll(index: Int): Int {
                        return min(
                            amountInInventory(player, feather.id),
                            amountInInventory(player, unfinishedBolt.id)
                        )
                    }
                }
            handler.open()
            return@onUseWith true
        }
    }
    companion object {
        fun sendLevelCheckFailDialog(player: Player, level: Int) {
            sendDialogue(
                player,
                "You need a fletching level of $level in order to do this."
            )
        }
    }
}