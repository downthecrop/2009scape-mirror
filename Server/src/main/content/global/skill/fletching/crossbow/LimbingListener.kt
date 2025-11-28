package content.global.skill.fletching.crossbow

import core.api.getDynLevel
import core.api.hasSpaceFor
import core.api.sendDialogue
import core.api.sendMessage
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.net.packet.PacketRepository
import core.net.packet.context.ChildPositionContext
import core.net.packet.out.RepositionChild
import org.rs09.consts.Components

@Suppress("unused") // Reflectively loaded
class LimbingListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(
            IntType.ITEM,
            UnfinishedCrossbowCraftInfo.limbIds,
            *UnfinishedCrossbowCraftInfo.stockIds
        ) { player, limb, stock ->
            val unfinishedCrossbowCraftInfo = UnfinishedCrossbowCraftInfo.forStockId(stock.id) ?: return@onUseWith false
            if (unfinishedCrossbowCraftInfo.limbItemId != limb.id) {
                sendMessage(player, "That's not the right limb to attach to that stock.")
                return@onUseWith true
            }
            val handler: SkillDialogueHandler = object : SkillDialogueHandler(
                player,
                SkillDialogue.ONE_OPTION,
                Item(unfinishedCrossbowCraftInfo.unstrungCrossbowItemId)
            ) {
                override fun create(amount: Int, index: Int) {
                    if (!playerMeetsInitialRequirements()) return
                    LimbingScript(player, unfinishedCrossbowCraftInfo, amount).invoke()
                }

                private fun playerMeetsInitialRequirements(): Boolean {
                    if (getDynLevel(player, Skills.FLETCHING) < unfinishedCrossbowCraftInfo.level) {
                        sendLevelCheckFailDialog(player, unfinishedCrossbowCraftInfo.level)
                        return false
                    }
                    if (!hasSpaceFor(player, Item(unfinishedCrossbowCraftInfo.unstrungCrossbowItemId))) {
                        sendDialogue(player, "You do not have enough inventory space.")
                        return false
                    }

                    return true
                }

                override fun getAll(index: Int): Int {
                    return player.inventory.getAmount(stock.asItem())
                }
            }
            handler.open()
            fixTextOverlappingTheCrossbowIcon(player)
            return@onUseWith true
        }
    }


    private fun fixTextOverlappingTheCrossbowIcon(player: Player) {
        PacketRepository.send(
            RepositionChild::class.java,
            ChildPositionContext(player, Components.SKILL_MULTI1_309, 2, 210, 10)
        )
    }

    companion object {
        fun sendLevelCheckFailDialog(player: Player, level: Int) {
            sendDialogue(
                player,
                "You need a fletching level of $level to attach these limbs."
            )
        }
    }
}