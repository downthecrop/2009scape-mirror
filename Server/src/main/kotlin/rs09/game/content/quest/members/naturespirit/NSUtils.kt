package rs09.game.content.quest.members.naturespirit

import api.ContentAPI
import core.game.node.Node
import core.game.node.`object`.Scenery
import core.game.node.`object`.SceneryBuilder
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager.forId
import core.game.world.map.RegionManager.lock
import core.tools.RandomFunction

object NSUtils {
    fun flagFungusPlaced(player: Player) {
        ContentAPI.setAttribute(player, "/save:ns:fungus_placed", true)
    }

    fun flagCardPlaced(player: Player){
        ContentAPI.setAttribute(player, "/save:ns:card_placed", true)
    }

    fun hasPlacedFungus(player: Player): Boolean {
        return ContentAPI.getAttribute(player, "ns:fungus_placed", false)
    }

    fun hasPlacedCard(player: Player): Boolean {
        return ContentAPI.getAttribute(player, "ns:card_placed", false)
    }

    fun onStone(player: Player): Boolean {
        return player.location.equals(3440, 3335, 0)
    }

    fun cleanupAttributes(player: Player){
        player.removeAttribute("ns:fungus_placed")
        player.removeAttribute("ns:card_placed")
    }

    @JvmStatic
    fun castBloom(player: Player): Boolean{
        var success = false
        val region = forId(player.location.regionId)
        if (player.skills.prayerPoints < 1) {
            player.packetDispatch.sendMessage("You don't have enough prayer points to do this.")
            return false
        }
        handleVisuals(player)
        for (o in region.planes[0].objects) {
            for (obj in o) {
                if (obj != null) {
                    if (obj.name.equals("Rotting log", ignoreCase = true) && player.skills.prayerPoints >= 1) {
                        if (player.location.withinDistance(obj.location, 2)) {
                            SceneryBuilder.add(Scenery(3509, obj.location, obj.rotation))
                            success = true
                        }
                    }
                }
            }
        }
        return success
    }

    /**
     * Handles the draining of prayer points and physical graphics and
     * animation.
     */
    private fun handleVisuals(player: Player) {
        player.skills.decrementPrayerPoints(RandomFunction.random(1, 3).toDouble())
        val AROUND_YOU = player.location.surroundingTiles
        for (location in AROUND_YOU) {
            // The graphic is meant to play on a 3x3 radius around you, but not
            // including the tile you are on.
            player.packetDispatch.sendGlobalPositionGraphic(263, location)
        }
    }
}