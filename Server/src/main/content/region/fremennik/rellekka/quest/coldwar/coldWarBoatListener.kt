package content.region.fremennik.rellekka.quest.coldwar

import content.data.Quests
import core.api.animateInterface
import core.api.closeAllInterfaces
import core.api.closeOverlay
import core.api.delayScript
import core.api.hasRequirement
import core.api.lock
import core.api.openInterface
import core.api.openOverlay
import core.api.queueScript
import core.api.stopExecuting
import core.api.teleport
import core.api.unlock
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.net.packet.PacketRepository
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.MinimapState
import org.rs09.consts.Components
import org.rs09.consts.Scenery

/**
 * The boat from Rellekka to the Iceberg and back. Used during Cold War
 */

// source: https://youtu.be/S-vpnCZad08?si=ien4DJ6aSuEIq6Hv&t=219
class ColdWarBoatListener : InteractionListener {

    // boat from Rellekka to Iceberg
    override fun defineListeners() {
        on(Scenery.BOAT_21176, IntType.SCENERY, "travel") { player, _ ->
            if (hasRequirement(player, Quests.COLD_WAR)) {
                travel(player, true)
            }
            return@on true
        }

        // boat from Iceberg to Rellekka
        on(Scenery.BOAT_21175, IntType.SCENERY, "travel") { player, _ ->
            if (hasRequirement(player, Quests.COLD_WAR)) {
                travel(player, false)
            }
            return@on true
        }
    }

    companion object {
        private const val TRAVEL_IFACE = 505
        private const val BOAT = 2
        private const val TRAVEL_NORTH = 5735
        private const val TRAVEL_SOUTH = 5736
        private const val FADE_TO_BLACK = 115

        // handles travel
        fun travel(player: Player, toBerg : Boolean) {
            queueScript(player, 0, QueueStrength.SOFT) { stage ->
                when (stage) {

                    // fade to black
                    0 -> {
                        closeAllInterfaces(player)
                        openOverlay(player, FADE_TO_BLACK)
                        lock(player, 50)
                        return@queueScript delayScript(player, 2)
                    }

                    // hide minimap, open and animate iface
                    1 -> {
                        openInterface(player, TRAVEL_IFACE)
                        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 2))
                        if (toBerg) {
                            animateInterface(player, TRAVEL_IFACE, BOAT, TRAVEL_NORTH)
                            return@queueScript delayScript(player, Animation(TRAVEL_NORTH).duration)
                        } else {
                            animateInterface(player, TRAVEL_IFACE, BOAT, TRAVEL_SOUTH)
                            return@queueScript delayScript(player, Animation(TRAVEL_SOUTH).duration)
                        }
                    }

                    // move player, close iface
                    2 -> {
                        if (toBerg) {
                            teleport(player, Location.create(2660, 3989, 1))
                        } else {
                            teleport(player, Location.create(2707, 3735, 0))
                        }
                        closeAllInterfaces(player)
                        return@queueScript delayScript(player, 1)
                    }

                    // fade back and unhide map
                    3 -> {
                        openOverlay(player, Components.FADE_FROM_BLACK_170)
                        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                        unlock(player)
                        return@queueScript delayScript(player, 3)
                    }

                    // close the fade overlay
                    4 -> {
                        closeOverlay(player)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
        }
    }
}