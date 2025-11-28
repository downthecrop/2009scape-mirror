package content.global.skill.fletching.gem

import core.api.amountInInventory
import core.api.getDynLevel
import core.api.hasSpaceFor
import core.api.sendDialogue
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items
import kotlin.math.min

@Suppress("unused") // Reflectively loaded
class GemBoltListeners : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.ITEM, Items.CHISEL_1755, *GemBoltsCraftInfo.gemIds) { player, _, gem ->
            val gemBoltCraftInfo = GemBoltsCraftInfo.forGemId(gem.id) ?: return@onUseWith true

            object : SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(gemBoltCraftInfo.gemItemId)) {
                override fun create(amount: Int, index: Int) {
                    if (getDynLevel(player, Skills.FLETCHING) < gemBoltCraftInfo.level) {
                        sendGemTipCutLevelCheckFailDialog(player, gemBoltCraftInfo.level)
                        return
                    }
                    CutGemsIntoBoltTipsScript(player, gemBoltCraftInfo, amount).invoke()
                }

                override fun getAll(index: Int): Int {
                    return amountInInventory(player, gemBoltCraftInfo.gemItemId)
                }
            }.open()
            return@onUseWith true
        }

        onUseWith(
            IntType.ITEM,
            GemBoltsCraftInfo.untippedBoltIds,
            *GemBoltsCraftInfo.boltTipIds
        ) { player, untippedBolt, boltTip ->
            val bolt = GemBoltsCraftInfo.forTipId(boltTip.id) ?: return@onUseWith false
            if (untippedBolt.id != bolt.untippedBoltItemId) return@onUseWith false

            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(bolt.tippedBoltItemId)) {
                    override fun create(amount: Int, index: Int) {
                        if (!playerMeetsInitialRequirements()) return
                        AttachGemTipToBoltScript(player, bolt, amount).invoke()
                    }

                    private fun playerMeetsInitialRequirements(): Boolean {
                        if (getDynLevel(player, Skills.FLETCHING) < bolt.level) {
                            sendGemTipAttachLevelCheckFailDialog(player, bolt.level)
                            return false
                        }
                        if (!hasSpaceFor(player, Item(bolt.tippedBoltItemId))) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return false
                        }
                        return true
                    }

                    override fun getAll(index: Int): Int {
                        return min(amountInInventory(player, untippedBolt.id), amountInInventory(player, boltTip.id))
                    }
                }
            handler.open()
            return@onUseWith true
        }
    }

    companion object {
        fun sendGemTipCutLevelCheckFailDialog(player: Player, level: Int) {
            sendDialogue(
                player,
                "You need a Fletching level of $level or above to do that."
            )
        }

        fun sendGemTipAttachLevelCheckFailDialog(player: Player, level: Int) {
            sendDialogue(
                player,
                "You need a Fletching level of $level or above to do that."
            )
        }
    }
}