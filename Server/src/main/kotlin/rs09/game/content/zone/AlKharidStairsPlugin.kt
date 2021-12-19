package rs09.game.content.zone;

import api.*
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

    private val zekeStairsTop = Scenery(35645,Location(3284,3190,1),2,0)
    private val zekeDoorClosed = Scenery(27988,Location(3284,3190,1),0,2)
    private val zekeDoorOpened = Scenery(27989,Location(3285,3190,1),0,3)

    private val craftingStairsTop = Scenery(35645,Location(3314,3187,1),2,0)
    private val craftingDoorClosed = Scenery(27988,Location(3314,3187,1),0,3)
    private val craftingDoorOpened = Scenery(27989,Location(3314,3186,1),0,0)

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        option ?: return false
        if(node.location == zekeDoorOpened.location || node.location == craftingDoorOpened.location){
                sendMessage(player,"This door appears to be stuck open.")
        } else{
            DoorActionHandler.handleDoor(player,node.asScenery())
        }
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {

        // Zekes Shop Upstairs Door replacement
        SceneryBuilder.replace(zekeDoorClosed,zekeDoorOpened)
        SceneryBuilder.add(zekeStairsTop)

        // Crafting Shop Upstairs Door replacement
        SceneryBuilder.replace(craftingDoorClosed,craftingDoorOpened)
        SceneryBuilder.add(craftingStairsTop)

        SceneryDefinition.forId(27989).handlers["option:close"] = this
        return this
    }
}
