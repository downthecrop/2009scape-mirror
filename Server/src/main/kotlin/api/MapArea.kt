package api

import core.game.node.entity.Entity
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
            return zoneMaps[this.javaClass.simpleName + "MapArea"]!!
        }
        set(value) {
            zoneMaps[this.javaClass.simpleName + "MapArea"] = value
        }

    fun defineAreaBorders() : Array<ZoneBorders>
    fun getRestrictions() : Array<ZoneRestriction> {return arrayOf()}
    fun areaEnter(entity: Entity) {}
    fun areaLeave(entity: Entity, logout: Boolean) {}
    fun entityStep(entity: Entity, location: Location, lastLocation: Location) {}

    companion object {
        val zoneMaps = HashMap<String, MapZone>()
    }
}
