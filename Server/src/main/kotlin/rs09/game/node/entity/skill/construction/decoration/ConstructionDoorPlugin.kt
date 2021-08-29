package rs09.game.node.entity.skill.construction.decoration

import core.cache.def.impl.SceneryDefinition
import core.game.content.global.action.DoorActionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.scenery.Scenery
import core.game.node.entity.player.Player
import core.game.node.entity.skill.construction.BuildHotspot
import core.game.node.entity.skill.construction.HousingStyle
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles Construction related doors.
 * @author Emperor
 */
@Initializable
class ConstructionDoorPlugin : OptionHandler() {

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (style in HousingStyle.values()) {
            SceneryDefinition.forId(style.doorId).handlers["option:open"] = this
            SceneryDefinition.forId(style.secondDoorId).handlers["option:open"] = this
        }
        for (deco in BuildHotspot.DUNGEON_DOOR_LEFT.decorations) {
            SceneryDefinition.forId(deco.objectId).handlers["option:open"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:pick-lock"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:force"] = this
        }
        for (deco in BuildHotspot.DUNGEON_DOOR_RIGHT.decorations) {
            SceneryDefinition.forId(deco.objectId).handlers["option:open"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:pick-lock"] = this
            SceneryDefinition.forId(deco.objectId).handlers["option:force"] = this
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val `object` = node as Scenery
        val second = DoorActionHandler.getSecondDoor(`object`, player)
        when (option) {
            "pick-lock", "force" -> return false //TODO
        }
        DoorActionHandler.open(`object`, second, getReplaceId(`object`), getReplaceId(second), true, 500, false)
        return true
    }

    /**
     * Gets the replace id for the door.
     * @param object The door.
     * @return The replace object id.
     */
    private fun getReplaceId(`object`: Scenery): Int {
        for (data in REPLACEMENT) {
            if (`object`.id == data[0]) {
                return data[1]
            }
        }
        return `object`.id + 6
    }

    companion object {
        /**
         * The replacement ids.
         */
         val REPLACEMENT = arrayOf(
                intArrayOf(13100, 13102),
                intArrayOf(13101, 13103),
                intArrayOf(13006, 13008),
                intArrayOf(13007, 13008),
                intArrayOf(13015, 13017),
                intArrayOf(13016, 13018),
                intArrayOf(13094, 13095),
                intArrayOf(13096, 13097),
                intArrayOf(13109, 13110),
                intArrayOf(13107, 13108),
                intArrayOf(13118, 13120),
                intArrayOf(13119, 13121)
        )
    }
}