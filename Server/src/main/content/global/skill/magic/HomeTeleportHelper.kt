package content.global.skill.magic

import core.api.delayScript
import core.api.playGlobalAudio
import core.api.queueScript
import core.api.sendMessage
import core.api.stopExecuting
import core.game.event.TeleportEvent
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneRestriction

val HOME_ANIMATIONS = arrayOf(1722, 1723, 1724, 1725, 2798, 2799, 2800, 3195, 4643, 4645, 4646, 4847, 4848, 4849, 4850, 4851, 4852, 65535)
val HOME_GRAPHICS = arrayOf(775, 800, 801, 802, 803, 804, 1703, 1704, 1705, 1706, 1707, 1708, 1709, 1710, 1711, 1712, 1713, 65535)
fun getAudio(count: Int): Int {
    return when (count) {
        0 -> 193
        4 -> 194
        11 -> 195
        else -> -1
    }
}

fun homeTeleport(player: Player, dest: Location) {
    if (player.timers.getTimer("teleblock") != null) {
        sendMessage(player, "A magical force prevents you from teleporting.")
        return
    }
    if (player.locks.isTeleportLocked || player.zoneMonitor.isRestricted(ZoneRestriction.TELEPORT)) {
        sendMessage(player, "A magical force has stopped you from teleporting.")
        return
    }
    queueScript(player, 0, QueueStrength.WEAK) { stage ->
        if (stage == 18) {
            player.properties.teleportLocation = dest
            player.dispatch(TeleportEvent(TeleportManager.TeleportType.NORMAL, TeleportMethod.SPELL, -1, dest))
            return@queueScript stopExecuting(player)
        }
        playGlobalAudio(player.location, getAudio(stage))
        player.packetDispatch.sendGraphic(HOME_GRAPHICS[stage])
        player.packetDispatch.sendAnimation(HOME_ANIMATIONS[stage])
        return@queueScript delayScript(player, 1)
    }
}