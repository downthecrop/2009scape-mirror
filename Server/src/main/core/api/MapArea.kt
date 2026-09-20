package core.api

import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.RegionZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction

/**
 * Interface that allows a class to define a map area.
 * Optionally-overridable methods include [getRestrictions], [areaEnter], [areaLeave] and [entityStep]
 */
interface MapArea : ContentInterface {
    var zone: MapZone
        get(){
            return zoneMaps[this::class.java.simpleName + "MapArea"]!!
        }
        set(value) {
            zoneMaps[this::class.java.simpleName + "MapArea"] = value
        }

    fun defineAreaBorders() : Array<ZoneBorders>
    fun getRestrictions() : Array<ZoneRestriction> {return arrayOf()}
    fun areaEnter(entity: Entity) {}
    fun areaLeave(entity: Entity, logout: Boolean) {}
    fun entityStep(entity: Entity, location: Location, lastLocation: Location) {}
    /**
     * Extends MapZone to cater to pvp, team games and all other unique targeting systems.
     * Return true/false if you want to override the controls.
     * Return null if you want the default check in MapZone. (default)
     **/
    fun isPvpAllowed(entity: Entity, target: Node?, style: CombatStyle?, message: Boolean): Boolean? { return null }
    /**
     * Extends MapZone to cater to pvp, team games and all other unique targeting systems.
     * Return true/false if you want to allow the deaths, especially false for "safe" areas.
     * Return null if you want the default check in MapZone. (default)
     **/
    fun canStartDeath(entity: Entity, killer: Entity): Boolean? { return null }

    companion object {
        val zoneMaps = HashMap<String, MapZone>()
    }
}
