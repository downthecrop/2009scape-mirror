package content.global.handlers.iface

import content.global.travel.glider.Gliders
import content.region.karamja.diary.KaramjaAchievementDiary
import core.api.*
import core.game.diary.DiaryLevel
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.context.CameraContext.CameraType
import core.net.packet.context.MinimapStateContext
import core.net.packet.out.CameraViewPacket
import core.net.packet.out.MinimapState
import org.rs09.consts.Components

class GliderInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.GLIDERMAP_138) { player, _, _, button, _, _ ->
            val GLIDER_TRAVEL_DESTINATION_VARP = 153
            val GLIDER_OVERLAY = 115
            val BLANK_MINIMAP = 2
            val glider = Gliders.forId(button) ?: return@on true
            lock(player, 10)
            queueScript(player, 1, QueueStrength.SOFT) { stage ->
                when (stage) {
                    0 -> {
                        setVarp(player, GLIDER_TRAVEL_DESTINATION_VARP, glider.config)
                        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, BLANK_MINIMAP))
                        return@queueScript keepRunning(player)
                    }
                    1 -> {
                        if (glider == Gliders.LEMANTO_ANDRA) {
                            PacketRepository.send(CameraViewPacket::class.java, CameraContext(player, CameraType.SHAKE, 4, 4, 1200, 4, 4))
                            sendMessage(player, "The glider almost gets blown from its path as it withstands heavy winds.")
                            return@queueScript keepRunning(player)
                        }
                        return@queueScript delayScript(player, 1)
                    }
                    2 -> openOverlay(player, GLIDER_OVERLAY)
                    3 -> {
                        unlock(player)
                        teleport(player, glider.location)
                    }
                    4 -> {
                        if (glider == Gliders.LEMANTO_ANDRA) {
                            sendMessage(player, "The glider becomes uncontrollable and crashes down...")
                            PacketRepository.send(CameraViewPacket::class.java, CameraContext(player, CameraType.RESET, 0, 0, 0, 0, 0))
                        }
                        closeOverlay(player)
                        closeInterface(player)
                        PacketRepository.send(MinimapState::class.java, MinimapStateContext(player, 0))
                        setVarp(player, GLIDER_TRAVEL_DESTINATION_VARP, 0)
                        // Use the gnome glider to travel to Karamja
                        if (glider == Gliders.GANDIUS) {
                            finishTask(player, DiaryType.KARAMJA, DiaryLevel.MEDIUM, KaramjaAchievementDiary.Companion.MediumTasks.GNOME_GLIDE_TO_KARAMJA)
                        }
                        return@queueScript stopExecuting(player)
                    }
                }
                return@queueScript keepRunning(player)
            }
            return@on true
        }
    }
}
