package content.global.handlers.item

import content.global.travel.glider.GliderPulse
import content.global.travel.glider.Gliders
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import org.rs09.consts.Items

private const val SQUASH_GRAPHICS_BEGIN = 767
private const val SQUASH_GRAPHICS_END = 769
private const val SQUASH_ANIM_BEGIN = 4544
private const val SQUASH_ANIM_END = 4546
private const val LAUNCH_GRAPHICS = 768
private const val LAUNCH_ANIMATION = 4547

/**
 * Handles the grand tree pod options
 * @author Ceikry
 */
class GrandSeedPodHandler : InteractionListener {

    override fun defineListeners() {
        on(intArrayOf(Items.GRAND_SEED_POD_9469), IntType.ITEM, "squash", "launch") { player, _ ->
            val opt = getUsedOption(player)
            if (!removeItem(player, Items.GRAND_SEED_POD_9469)) return@on false
            if (opt == "launch") {
                visualize(player, LAUNCH_ANIMATION, LAUNCH_GRAPHICS)
                delayEntity(player, 7)
                queueScript(player, 3, QueueStrength.SOFT) {stage: Int ->
                    if (stage == 0) {
                        rewardXP(player, Skills.FARMING, 100.0)
                        openOverlay(player, 115)
                        return@queueScript keepRunning(player)
                    }

                    if (stage == 1) {
                        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                        return@queueScript delayScript(player, 3)
                    }

                    if (stage == 2) {
                        teleport(player, Gliders.TA_QUIR_PRIW.location)
                        return@queueScript delayScript(player, 2)
                    }

                    if (stage == 3) {
                        closeOverlay(player)
                        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                    }

                    return@queueScript stopExecuting(player)
                }
            }

            if (opt == "squash") {
                closeAllInterfaces(player)
                visualize(player, SQUASH_ANIM_BEGIN, SQUASH_GRAPHICS_BEGIN)
                delayEntity(player, 12)
                queueScript(player, 3, QueueStrength.SOFT) {stage: Int ->
                    if (stage == 0) {
                        animate(player, 1241, true)
                        return@queueScript keepRunning(player)
                    }

                    if (stage == 1) {
                        teleport(player, Location.create(2464, 3494, 0))
                        return@queueScript keepRunning(player)
                    }

                    if (stage == 2) {
                        visualize(player, 1241, SQUASH_GRAPHICS_END)
                        return@queueScript delayScript(player, 2)
                    }

                    if (stage == 3) {
                        animate(player, SQUASH_ANIM_END, true)
                        adjustLevel(player, Skills.FARMING, -5)
                        return@queueScript keepRunning(player)
                    }

                    return@queueScript stopExecuting(player)
                }
            }

            return@on true
        }
    }
}
