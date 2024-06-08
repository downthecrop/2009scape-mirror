package content.global.skill.prayer

import core.api.*
import core.game.event.BoneBuryEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Animations
import org.rs09.consts.Sounds

class BoneBuryListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles the bury options for bones in Bones.kt
         */
        on(Bones.array, IntType.ITEM, "bury") { player, node ->
            val bones = Bones.forId(node.id) ?: return@on true
            if (!clockReady(player, Clocks.SKILLING)) return@on true
            if (!inInventory(player, node.id)) return@on true

            stopWalk(player)
            lock(player, 2)
            delayClock(player, Clocks.SKILLING, 2)
            sendMessage(player, "You dig a hole in the ground.")
            animate(player, Animations.HUMAN_BURYING_BONES_827)
            playAudio(player, Sounds.BONES_DOWN_2738)

            // A strong queue is required in the event a player moves immediately after clicking the bones
            queueScript(player, 1, QueueStrength.STRONG) {
                if (removeBones(player, node.asItem())) {
                    sendMessage(player, "You bury the bones.")
                    rewardXP(player, Skills.PRAYER, bones.experience)
                    player.dispatch(BoneBuryEvent(bones.itemId))
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }
    }

    private fun removeBones(player: Player, item: Item): Boolean {
        val removedBones = replaceSlot(player, item.slot, Item())
        return removedBones == item && removedBones.slot == item.slot
    }
}