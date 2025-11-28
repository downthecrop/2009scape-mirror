package content.global.skill.fletching.arrow

import content.global.skill.fletching.Feathers
import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Items

@Suppress("unused") // Reflectively loaded
class ArrowListeners : InteractionListener {
    private val arrowShaft = Items.ARROW_SHAFT_52
    val headlessArrow = Items.HEADLESS_ARROW_53
    
    companion object {
        const val FLIGHTED_OGRE_ARROW_LEVEL = 5
        fun sendLevelCheckFailDialog(player: Player, level: Int) {
            sendDialogue(
                player,
                "You need a fletching level of $level to do this."
            )
        }
    }

    override fun defineListeners() {
        onUseWith(IntType.ITEM, arrowShaft, *Feathers.all) { player, shaft, feather ->
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(headlessArrow)) {
                    override fun create(amount: Int, index: Int) {
                        if (!hasSpaceFor(player, Item(headlessArrow))) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return
                        }
                        HeadlessArrowCraftScript(player, feather.id, amount).invoke()
                    }

                    override fun getAll(index: Int): Int {
                        return amountInInventory(player, headlessArrow)
                    }
                }
            handler.open()
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, headlessArrow, *ArrowCraftInfo.arrowTipIds) { player, headlessArrow, arrowTip ->
            val arrowCraftInfo = ArrowCraftInfo.fromTipId(arrowTip.id) ?: return@onUseWith false
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(arrowCraftInfo.arrowItemId)) {
                    override fun create(amount: Int, index: Int) {
                        if (!playerMeetsInitialRequirements()) return
                        TippedArrowCraftScript(player, arrowCraftInfo, amount).invoke()
                    }

                    private fun playerMeetsInitialRequirements(): Boolean {
                        if (arrowCraftInfo == ArrowCraftInfo.BROAD_ARROW && !getSlayerFlags(player).isBroadsUnlocked()) {
                            sendDialogue(player, "You need to unlock the ability to create broad arrows.")
                            return false
                        }

                        if (getDynLevel(player, Skills.FLETCHING) < arrowCraftInfo.level) {
                            sendLevelCheckFailDialog(player, arrowCraftInfo.level)
                            return false
                        }

                        if (!hasSpaceFor(player, Item(arrowCraftInfo.arrowItemId))) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return false
                        }
                        return true
                    }

                    override fun getAll(index: Int): Int {
                        return amountInInventory(player, arrowTip.id)
                    }
                }
            handler.open()
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, Items.OGRE_ARROW_SHAFT_2864, *Feathers.all) { player, ogreArrowShaft, feather ->
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(Items.FLIGHTED_OGRE_ARROW_2865)) {
                    override fun create(amount: Int, index: Int) {
                        if (!playerMeetsInitialRequirements()) return
                        FlightedOgreArrowCraftScript(player, feather.id, amount).invoke()
                    }

                    private fun playerMeetsInitialRequirements(): Boolean {
                        if (getDynLevel(player, Skills.FLETCHING) < FLIGHTED_OGRE_ARROW_LEVEL) {
                            sendLevelCheckFailDialog(player, FLIGHTED_OGRE_ARROW_LEVEL)
                            return false
                        }

                        if (!hasSpaceFor(player, Item(Items.FLIGHTED_OGRE_ARROW_2865))) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return false
                        }
                        return true
                    }

                    override fun getAll(index: Int): Int {
                        return amountInInventory(player, Items.OGRE_ARROW_SHAFT_2864)
                    }
                }
            handler.open()
            return@onUseWith true
        }
    }
}