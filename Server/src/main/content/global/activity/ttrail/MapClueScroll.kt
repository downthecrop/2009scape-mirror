package content.global.activity.ttrail

import core.api.*
import core.game.global.action.DigAction
import core.game.global.action.DigSpadeHandler
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.Items

/**
 * A map clue scroll.
 */

// TODO: This plugin should be converted to a listener as part of a larger rewrite of Treasure Trails.

abstract class MapClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    interfaceId: Int,
    val location: Location?,
    val `object`: Int,
    vararg borders: ZoneBorders
) : ClueScrollPlugin(name, clueId, level, interfaceId, *borders) {

    /**
     * Constructs a new [MapClueScroll] instance without a specific object.
     * @param name the name.
     * @param clueId the clue id.
     * @param level the level.
     * @param interfaceId the interface id.
     * @param location the location.
     */
    constructor(
        name: String?,
        clueId: Int,
        level: ClueLevel?,
        interfaceId: Int,
        location: Location?
    ) : this(name, clueId, level, interfaceId, location, 0)

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (e is Player) {
            if (target.id == `object` && option.name == "Search") {
                if (!inInventory(e, clueId) || target.location != location) {
                    sendMessage(e, "Nothing interesting happens.")
                    return false
                }
                reward(e)
                return true
            }
        }
        return super.interact(e, target, option)
    }

    override fun configure() {
        DigSpadeHandler.register(location, MapDigAction())
        super.configure()
    }

    /**
     * The dig method called when the player digs.
     * @param player the player.
     */
    open fun dig(player: Player) {
        reward(player)
        sendItemDialogue(player, Items.CASKET_405, "You've found a casket!")
    }

    /**
     * Checks if the player has the required items.
     * @param player the player.
     * @return `true` if so.
     */
    open fun hasRequiredItems(player: Player): Boolean {
        return inInventory(player, clueId)
    }

    /**
     * Handles the map digging reward.
     * @author Vexia
     */
    inner class MapDigAction : DigAction {
        override fun run(player: Player) {
            if (!hasRequiredItems(player)) {
                sendMessage(player, "Nothing interesting happens.")
                return
            }
            dig(player)
        }
    }
}