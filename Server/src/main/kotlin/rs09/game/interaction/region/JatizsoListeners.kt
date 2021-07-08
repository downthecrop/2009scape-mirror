package rs09.game.interaction.region

import api.ContentAPI
import core.game.world.map.Direction
import core.game.world.map.zone.ZoneBorders
import rs09.game.interaction.InteractionListener

class JatizsoListeners : InteractionListener() {
    val GATES_CLOSED = intArrayOf(21403,21405)
    val NORTH_GATE_ZONE = ZoneBorders(2414,3822,2417,3825)
    val WEST_GATE_ZONE = ZoneBorders(2386,3797,2390,3801)
    override fun defineListeners() {
        on(GATES_CLOSED, SCENERY, "open"){player, node ->
            if(NORTH_GATE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = ContentAPI.getScenery(node.location.transform(1, 0, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.EAST)
                    ContentAPI.replaceScenery(other.asScenery(), other.id - 1, -1, Direction.SOUTH)
                } else {
                    val other = ContentAPI.getScenery(node.location.transform(-1, 0, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.SOUTH)
                    ContentAPI.replaceScenery(other.asScenery(), other.id, -1, Direction.EAST)
                }

                ContentAPI.playAudio(player, ContentAPI.getAudio(81))
            } else if(WEST_GATE_ZONE.insideBorder(player)){
                if(node.id == GATES_CLOSED.first()){
                    val other = ContentAPI.getScenery(node.location.transform(0, 1, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.WEST)
                    ContentAPI.replaceScenery(other.asScenery(), other.id + 1, -1, Direction.NORTH)
                } else {
                    val other = ContentAPI.getScenery(node.location.transform(0, -1, 0)) ?: return@on true
                    ContentAPI.replaceScenery(node.asScenery(), node.id + 1, -1, Direction.NORTH)
                    ContentAPI.replaceScenery(other.asScenery(), other.id + 1, -1, Direction.WEST)
                }

                ContentAPI.playAudio(player, ContentAPI.getAudio(81))
            }
            return@on true
        }
    }
}