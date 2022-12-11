package core.game.node.entity.player.link.request

import core.game.node.entity.player.Player
import rs09.game.ai.AIRepository
import rs09.game.ai.general.scriptrepository.DoublingMoney
import rs09.game.world.GameWorld.ticks

/**
 * Represents a managing class for requests of a player.
 * @author Vexia
 * @author dginovker
 * @version 2.0
 */
class RequestManager(val player: Player) {
    /**
     * Represents the target being requested.
     */
    var target: Player? = null
        private set

    /**
     * Method used to send a request type to a target.
     * @param target the target.
     * @param type the type of request.
     * @return `True` if successful.
     */
    fun request(target: Player, type: RequestType): Boolean {
        if (!canRequest(type, target)) {
            return false
        }
        if (acceptExisting(target, type)) {
            return true
        }
        player.packetDispatch.sendMessage(type.message)
        target.packetDispatch.sendMessage(type.getRequestMessage(player))
        player.setAttribute("lastRequest", type)
        this.target = target

        // If the target is a bot, get their bot manager to handle the request.
       if (target.isArtificial) {
            val bot = AIRepository.PulseRepository[target.username.lowercase()]
            if (bot != null && bot.botScript is DoublingMoney) {
                bot.botScript.tradeReceived(player)
            }
       }
       return true
    }

    /**
     * Method used to check if a player can continue with a request.
     * @param type the type.
     * @param target the target.
     * @return `True` if so.
     */
    private fun canRequest(type: RequestType, target: Player): Boolean {
        if (target === player) {
            return false
        }
        if (!target.location.withinDistance(player.location, 15)) {
            player.packetDispatch.sendMessage("Unable to find " + target.username + ".")
            return false
        }
        if (!target.isActive || target.interfaceManager.isOpened) {
            player.packetDispatch.sendMessage("Other player is busy at the moment.")
            return false
        }
        if (target.getAttribute("busy", 0) > ticks || player.getAttribute("busy", 0) > ticks) {
            player.packetDispatch.sendMessage("Other player is busy at the moment.")
            return false
        }
        return if (!player.zoneMonitor.canRequest(type, target)) {
            false
        } else type.canRequest(player, target)
    }

    /**
     * Method used to check if we can accept an existing request.
     * @param target the target.
     * @param type the type.
     * @return `True` if so.
     */
    private fun acceptExisting(target: Player, type: RequestType): Boolean {
        val lastType = target.getAttribute<RequestType>("lastRequest", null)
        if (lastType === type && player === target.requestManager.target) {
            close(player)
            clear()
            target.requestManager.clear()
            player.setAttribute("busy", ticks + 2)
            target.setAttribute("busy", ticks + 2)
            type.module.open(player, target)
            return true
        }
        close(player)
        return false
    }

    /**
     * Closes the components for the player.
     * @param player the player.
     */
    private fun close(player: Player) {
        player.dialogueInterpreter.close()
        player.interfaceManager.close()
        player.interfaceManager.closeChatbox()
    }

    /**
     * Method used to clear the cached target.
     */
    fun clear() {
        target = null
    }
}