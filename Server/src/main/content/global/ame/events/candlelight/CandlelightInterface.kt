package content.global.ame.events.candlelight

import core.api.*
import core.api.utils.PlayerCamera
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs09.consts.Scenery

/**
 * Candlelight Interface CANDLELIGHT_178
 *
 */
class CandlelightInterface : InterfaceListener, InteractionListener, MapArea {
    companion object {
        const val CANDLELIGHT_INTERFACE = 178
        const val CANDLELIGHT_RETURN_LOC = "/save:original-loc"
        const val CANDLELIGHT_CANDLE_ARRAY = "/save:candlelight:candle-array"
        const val CANDLELIGHT_CAMERA_AT = "candlelight:camera-at"

        val CANDLE_LOC_ARRAY = arrayOf(
             Location(1967, 4997),
             Location(1968, 4998),
             Location(1967, 4999),
             Location(1968, 5000),
             Location(1967, 5001),
             Location(1968, 5002),
             Location(1967, 5003),
             Location(1968, 5004),
             Location(1967, 5005),
             Location(1968, 5006),
             Location(1967, 5007),
        )

        fun initCandlelight(player: Player) {
            val candleArray = intArrayOf(0,0,0,0,0,0,2,2,2,2,2)
            candleArray.shuffle()
            setAttribute(player, CANDLELIGHT_CANDLE_ARRAY, candleArray)
            for (candleIndex in 0..10) {
                setVarbit(player, 1771 + candleIndex, candleArray[candleIndex])
            }
        }

        fun areCandlesLit(player: Player): Boolean {
            val candleArray = getAttribute(player, CANDLELIGHT_CANDLE_ARRAY, intArrayOf(0,0,0,0,0,0,0,0,0,0,0))
            for (candle in candleArray) {
                if (candle == 0) {
                    return false
                }
            }
            return true
        }

        fun lightCandle(player: Player) {
            var currentCamLoc = getAttribute(player, CANDLELIGHT_CAMERA_AT, Location(1968, 5002))
            val candleIndex = CANDLE_LOC_ARRAY.indexOf(currentCamLoc)
            val varbit = candleIndex + 1771 // Essentially all varbits are 1771 .. 1881

            if (candleIndex != -1) {
                val candleArray = getAttribute(player, CANDLELIGHT_CANDLE_ARRAY, intArrayOf(0,0,0,0,0,0,0,0,0,0,0))
                if (candleArray[candleIndex] == 0) {
                    candleArray[candleIndex] = 1
                    setAttribute(player, CANDLELIGHT_CANDLE_ARRAY, candleArray)
                    setVarbit(player, varbit, 1)
                    sendMessage(player, "You light the candle.")
                } else if (candleArray[candleIndex] == 1) {
                    sendMessage(player, "This candle is already lit.")
                } else {
                    sendMessage(player, "This candle is too short to light.")
                }
            } else {
                sendMessage(player, "There is nothing to light here.")
            }
        }

        fun moveCamera(player: Player, direction: Direction, firstTime: Boolean = false) {
            var currentCamLoc = getAttribute(player, CANDLELIGHT_CAMERA_AT, Location(1968, 5002))
            when(direction) {
                Direction.NORTH -> currentCamLoc = currentCamLoc.transform(Direction.NORTH)
                Direction.SOUTH -> currentCamLoc = currentCamLoc.transform(Direction.SOUTH)
                Direction.EAST -> currentCamLoc = currentCamLoc.transform(Direction.EAST)
                Direction.WEST -> currentCamLoc = currentCamLoc.transform(Direction.WEST)
                else -> {}
            }
            if (currentCamLoc.x < 1967) { currentCamLoc.x = 1967 }
            if (currentCamLoc.x > 1968) { currentCamLoc.x = 1968 }
            if (currentCamLoc.y < 4997) { currentCamLoc.y = 4997 }
            if (currentCamLoc.y > 5007) { currentCamLoc.y = 5007 }
            setAttribute(player, CANDLELIGHT_CAMERA_AT, currentCamLoc)
            PlayerCamera(player).rotateTo(currentCamLoc.x - 30, currentCamLoc.y,0,200) // height is kind of a relative value?
            PlayerCamera(player).panTo(currentCamLoc.x + 2, currentCamLoc.y,350, if(firstTime) 400 else 10)
        }
    }

    override fun defineInterfaceListeners() {
        on(CANDLELIGHT_INTERFACE){ player, component, opcode, buttonID, slot, itemID ->
            when (buttonID) {
                1 -> moveCamera(player, Direction.WEST)
                2 -> moveCamera(player, Direction.EAST)
                3 -> lightCandle(player)/* Light */
                4 -> moveCamera(player, Direction.SOUTH)
                5 -> moveCamera(player, Direction.NORTH)
                9 -> closeInterface(player)
            }
            return@on true
        }

        onOpen(CANDLELIGHT_INTERFACE){ player, component ->
            // Move camera
            return@onOpen true
        }

        onClose(CANDLELIGHT_INTERFACE){ player, component ->
            PlayerCamera(player).reset()
            // Reset camera
            return@onClose true
        }
    }


    override fun defineListeners() {
        on((11364 .. 11394).toIntArray(), SCENERY, "light") { player, node ->
            setAttribute(player, CANDLELIGHT_CAMERA_AT, Location(node.location.x, node.location.y))
            moveCamera(player, Direction.NORTH_WEST, true)
            openInterface(player, CANDLELIGHT_INTERFACE)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(SCENERY, (11364 .. 11394).toIntArray(),"light"){ player, node ->
            return@setDest Location(1970, node.location.y)
        }
    }


    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders.forRegion(7758))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.RANDOM_EVENTS, ZoneRestriction.CANNON, ZoneRestriction.FOLLOWERS, ZoneRestriction.TELEPORT, ZoneRestriction.OFF_MAP)
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            initCandlelight(entity)
            entity.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 12)
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            entity.interfaceManager.restoreTabs()
        }
    }


}