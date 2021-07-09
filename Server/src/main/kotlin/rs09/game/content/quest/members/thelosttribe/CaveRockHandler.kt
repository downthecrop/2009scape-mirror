package rs09.game.content.quest.members.thelosttribe

import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Components

@Initializable
/**
 * Handles looking at the rocks in the tunnels leading up to the dorg mines
 * @author Ceikry
 */
class CaveRockHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        for(i in 6921..6924){
            SceneryDefinition.forId(i).handlers["option:look-at"] = this
        }
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        when(node?.id){
            6921 -> showRock(player,6923)
            6922 -> showRock(player,6924)
            6923 -> showRock(player,6922)
            6924 -> showRock(player,6927)
        }
        return true
    }


    fun showRock(player: Player, model: Int){
        player.interfaceManager.open(Component(Components.CAVE_GOBLIN_MARKERS_62))
        player.packetDispatch.sendModelOnInterface(model,62,1,1)
    }

    override fun getDestination(n: Node?, node: Node?): Location {
        n ?: return super.getDestination(n, node)
        node ?: return super.getDestination(n, node)

        var diffX = 0
        var diffY = 0

        if(node.direction == Direction.SOUTH)
            diffX = -1
        if(node.direction == Direction.NORTH)
            diffX = 1
        if(node.direction == Direction.WEST)
            diffY = 1
        if(node.direction == Direction.EAST)
            diffY = -1

        return node.location.transform(diffX,diffY,0)
    }
}