package content.global.skill.fletching.stringing

import content.data.Quests
import content.global.skill.fletching.stringing.StringableCraftInfo.Companion.applicableStringId
import core.api.*
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
class StringingListeners : InteractionListener {
    override fun defineListeners() {
        onUseWith(
            IntType.ITEM,
            StringableCraftInfo.stringItemIds,
            *StringableCraftInfo.unstrungBowIds
        ) { player, string, unstrungBow ->
            val stringableCraftInfo = StringableCraftInfo.forUnstrungBowItemId(unstrungBow.id) ?: return@onUseWith false
            if (string.id != stringableCraftInfo.applicableStringId) {
                sendMessage(player, "That's not the right kind of string for this.")
                return@onUseWith true
            }
            if (stringableCraftInfo == StringableCraftInfo.OGRE_COMP_BOW && getQuestStage(player, Quests.ZOGRE_FLESH_EATERS) < 8) {
                sendMessage(player, "You must have started Zogre Flesh Eaters and asked Grish to string this.")
                return@onUseWith true
            }
            val handler: SkillDialogueHandler =
                object :
                    SkillDialogueHandler(player, SkillDialogue.ONE_OPTION, Item(stringableCraftInfo.strungItemId)) {
                    override fun create(amount: Int, index: Int) {
                        if (getDynLevel(player, Skills.FLETCHING) < stringableCraftInfo.level) {
                            sendLevelCheckFailDialogue(player, stringableCraftInfo.level)
                            return
                        }
                        StringItemScript(player, stringableCraftInfo, amount).invoke()
                    }

                    override fun getAll(index: Int): Int {
                        return amountInInventory(player, string.id)
                    }
                }
            handler.open()
            fixSpacingBetweenTextAndCraftedItemIcon(player)
            return@onUseWith true
        }
    }

    private fun fixSpacingBetweenTextAndCraftedItemIcon(player: Player) {
        PacketRepository.send(
            RepositionChild::class.java,
            ChildPositionContext(player, Components.SKILL_MULTI1_309, 2, 215, 10)
        )
    }
    companion object {
        fun sendLevelCheckFailDialogue(player: Player, level: Int) {
            sendDialogue(
                player,
                "You need a fletching level of $level to string this bow."
            )
        }
    }
}