package content.global.ame

import core.ServerConstants
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager.TeleportType
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Sounds

fun kidnapPlayer(npc: NPC, player: Player, dest: Location, playerLine: String? = null, callback: (player: Player, npc: NPC) -> Unit) {
    clearScripts(player)
    val lockDuration = if (playerLine != null) 4 else 6
    lock(player, lockDuration)
    queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
        when (stage) {
            0 -> {
                if (playerLine != null) {
                    sendChat(player, playerLine)
                    return@queueScript delayScript(player, 2)
                }
                return@queueScript delayScript(player, 0)
            }
            1 -> {
                sendGraphics(Graphics(1576, 0, 0), player.location)
                animate(player,8939)
                playAudio(player, Sounds.TELEPORT_ALL_200)
                return@queueScript delayScript(player, 3)
            }
            2 -> {
                setAttribute(player, "kidnapped-by-random", true)
                if (getAttribute<Location?>(player, "/save:original-loc", null) == null) {
                    setAttribute(player, "/save:original-loc", player.location)
                }
                teleport(player, dest, TeleportType.INSTANT)
                sendGraphics(Graphics(1577, 0, 0), player.location)
                animate(player, 8941)
                resetAnimator(player)
                callback(player, npc)
                return@queueScript delayScript(player, 2)
            }
            3 -> {
                removeAttribute(player, "kidnapped-by-random") //this is not needed at this point anymore and will reenable the original-loc sanity check tick action
                return@queueScript stopExecuting(player)
            }
            else -> return@queueScript stopExecuting(player)
        }
    }
}

fun returnPlayer(player: Player) {
    player.locks.unlockTeleport()
    val destination = getAttribute(player, "/save:original-loc", ServerConstants.HOME_LOCATION)
    teleport(player, destination!!)
    unlock(player)
    removeAttributes(player, "/save:original-loc", "kidnapped-by-random")
}
