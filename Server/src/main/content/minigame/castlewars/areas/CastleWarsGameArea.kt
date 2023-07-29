package rs09.game.content.activity.castlewars.areas

import core.api.*
import core.game.component.Component
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.tools.Log
import core.tools.ticksPerMinute
import org.rs09.consts.Components
import rs09.game.content.activity.castlewars.CastleWars

/**
 * Handles the Castle Wars game map
 */
class CastleWarsGameArea : CastleWarsArea(), TickListener {

    companion object {
        private val saradominStandardFloor: ZoneBorders = ZoneBorders(Location.create(2426, 3073, 3), Location.create(2430, 3077, 3))
        private val zamorakStandardFloor: ZoneBorders = ZoneBorders(Location.create(2373, 3134, 3), Location.create(2369, 3130, 3))
        private val saradominUpperFloor: ZoneBorders = ZoneBorders(Location.create(2431, 3080, 2), Location.create(2423, 3072, 2))
        private val zamorakUpperFloor: ZoneBorders = ZoneBorders(Location.create(2368, 3127, 2), Location.create(2376, 3135, 2))
        private val saradominFloor: ZoneBorders = ZoneBorders(Location.create(2420, 3072, 1), Location.create(2431, 3083, 1))
        private val zamorakFloor: ZoneBorders = ZoneBorders(Location.create(2379, 3135, 1), Location.create(2368, 3124, 1))
        private val battleField: ZoneBorders = ZoneBorders(Location.create(2368, 3135, 0), Location.create(2431, 3072, 0))
        private val saradominTunnels: ZoneBorders = ZoneBorders(Location.create(2430, 9481, 0), Location.create(2400, 9504, 0))
        private val zamorakTunnels: ZoneBorders = ZoneBorders(Location.create(2401, 9503, 0), Location.create(2366, 9529, 0))

        val areaBorders = arrayOf(saradominStandardFloor, zamorakStandardFloor, saradominUpperFloor, zamorakUpperFloor, saradominFloor, zamorakFloor, battleField, saradominTunnels, zamorakTunnels)

        val saradominPlayers = mutableSetOf<Player>()
        val zamorakPlayers = mutableSetOf<Player>()

        var ticksLeftInGame = 0

        fun startGame() {
            saradominPlayers.addAll(CastleWarsWaitingArea.waitingSaradominPlayers)
            zamorakPlayers.addAll(CastleWarsWaitingArea.waitingZamorakPlayers)
            CastleWarsWaitingArea.waitingSaradominPlayers.clear()
            CastleWarsWaitingArea.waitingZamorakPlayers.clear()
            ticksLeftInGame = CastleWars.gameTimeMinutes * ticksPerMinute

            // Put all the players in their respawn area
            saradominPlayers.forEach { player ->
                player.properties.teleportLocation = CastleWarsRespawnArea.saradominRespawnRoom.randomWalkableLoc
            }
            zamorakPlayers.forEach { player ->
                player.properties.teleportLocation = CastleWarsRespawnArea.zamorakRespawnRoom.randomWalkableLoc
            }
        }
    }

    private fun endGame() {
        saradominPlayers.forEach { player ->
            player.properties.teleportLocation = CastleWars.lobbyBankArea.randomWalkableLoc
        }
        zamorakPlayers.forEach { player ->
            player.properties.teleportLocation = CastleWars.lobbyBankArea.randomWalkableLoc
        }
        saradominPlayers.clear()
        zamorakPlayers.clear()
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return areaBorders
    }

    override fun areaEnter(entity: Entity) {
        val player = entity as? Player ?: return
        super.areaEnter(player)
        registerTimer (player, spawnTimer("teleblock", (CastleWars.gameTimeMinutes)*60*2))

        if (saradominPlayers.contains(player)) {
            player.interfaceManager.openOverlay(Component(Components.CASTLEWARS_STATUS_OVERLAY_SARADOMIN_58))
            player.equipment.replace(Item(CastleWars.saradominTeamHoodedCloak), 1)
        } else if (zamorakPlayers.contains(player)) {
            player.interfaceManager.openOverlay(Component(Components.CASTLEWARS_STATUS_OVERLAY_ZAMORAK_59))
            player.equipment.replace(Item(CastleWars.zamorakTeamHoodedCloak), 1)
        }
    }

    override fun exitArea(player: Player) {
        super.exitArea(player)
        // Remove player from the players set (whichever one that is)
        saradominPlayers.remove(player)
        zamorakPlayers.remove(player)
    }

    override fun tick() {
        ticksLeftInGame--
        if (ticksLeftInGame == 0) {
            endGame()
        }
    }

}
