package content.global.skill.prayer

import core.api.*
import core.game.event.BoneBuryEvent
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs09.consts.Animations
import org.rs09.consts.Sounds

class BoneBuryListener : InteractionListener {
    override fun defineListeners() {
        /**
         * Handles the bury options for bones in Bones.kt
         */
        on(Bones.array, IntType.ITEM, "bury") { player, node ->
            val bones = Bones.forId(node.id) ?: return@on true

            // Checks if the player is delayed from burying a bone and begins the clock if none exists.
            if (!clockReady(player, Clocks.SKILLING)) {
                return@on true
            } else {
                delayClock(player, Clocks.SKILLING, 2)
            }

            // Verifies the bones are in the player's inventory.
            if (!inInventory(player, bones.itemId)) {
                return@on true
            }

            // Replaces the bones slot in the inventory with nothing and checks that the bones are removed.
            if (replaceSlot(player, node.asItem().slot, Item()) != node.asItem()) {
                sendMessage(player, "The gods intervene and you keep your bones!")
                return@on true
            }

            buryBones(player, bones)
            return@on true
        }
    }

    /**
     * Buries the bones
     * @param player the player burying the bones
     * @param bones the bones being buried
     */
    private fun buryBones(player: Player, bones: Bones): Boolean {
        queueScript(player, 0) { stage ->
            when (stage) {
                0 -> {
                    stopWalk(player)
                    lock(player, 2)
                    animate(player, Animations.HUMAN_BURYING_BONES_827)
                    playAudio(player, Sounds.BONES_DOWN_2738)
                    sendMessage(player, "You dig a hole in the ground.")
                    return@queueScript delayScript(player, 2)
                }

                1 -> {
                    sendMessage(player, "You bury the bones.")
                    rewardXP(player, Skills.PRAYER, bones.experience)
                    player.dispatch(BoneBuryEvent(bones.itemId))
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
        return true
    }
}