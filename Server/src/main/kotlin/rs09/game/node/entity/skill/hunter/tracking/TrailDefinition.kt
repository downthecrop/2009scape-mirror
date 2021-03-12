package rs09.game.node.entity.skill.hunter.tracking

import core.game.world.map.Location

class TrailDefinition(val offset: Int, val type: TrailType, var inverted: Boolean, val startLocation: Location, val endLocation: Location, val triggerObjectLocation: Location = endLocation){
    override fun toString(): String {
        return "$startLocation $endLocation [offset: $offset] [${type.name}] [inverted: $inverted]"
    }
}
enum class TrailType{
    LINKING,
    INITIAL,
    TUNNEL
}