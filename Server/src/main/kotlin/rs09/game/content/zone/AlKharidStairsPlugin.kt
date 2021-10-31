package rs09.game.content.zone;

import api.ContentAPI
import core.cache.def.impl.SceneryDefinition
import core.game.content.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class AlKharidStairsPlugin : OptionHandler() {

    // Zekes Shop Upstairs Door replacement
    private val zekeStairsTop = Scenery(35645,Location(3284,3190,1),2,0)
    private val zekeDoorClosed = Scenery(27988,Location(3284,3190,1),0,2)
    private val zekeDoorOpened = Scenery(27989,Location(3285,3190,1),0,3)

    // Crafting Shop Upstairs Door replacement
    private val craftingStairsTop = Scenery(35645,Location(3314,3187,1),2,0)
    private val craftingDoorClosed = Scenery(27988,Location(3314,3187,1),0,3)
    private val craftingDoorOpened = Scenery(27989,Location(3314,3186,1),0,0)

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false
        if(node.location == zekeDoorOpened.location || node.location == craftingDoorOpened.location){
                ContentAPI.sendMessage(player,"Nothing interesting happens.")
        } else{
            DoorActionHandler.handleDoor(player,node.asScenery())
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {

        SceneryBuilder.replace(zekeDoorClosed,zekeDoorOpened)
        SceneryBuilder.add(zekeStairsTop)

        SceneryBuilder.replace(craftingDoorClosed,craftingDoorOpened)
        SceneryBuilder.add(craftingStairsTop)

        SceneryDefinition.forId(27989).handlers["option:close"] = this
        return this
    }
}
