package rs09.game.interaction.`object`

import core.game.node.Node
import core.game.node.entity.player.link.TeleportManager
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayersMaxNorm
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.update.flag.chunk.GraphicUpdateFlag
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Scenery.OBELISK_14825
import org.rs09.consts.Scenery.OBELISK_14826
import org.rs09.consts.Scenery.OBELISK_14827
import org.rs09.consts.Scenery.OBELISK_14828
import org.rs09.consts.Scenery.OBELISK_14829
import org.rs09.consts.Scenery.OBELISK_14830
import org.rs09.consts.Scenery.OBELISK_14831
import rs09.game.interaction.InteractionListener
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import java.util.*

/**
 * Interaction listener for the Wilderness Obelisks
 * @author Woah
 */
private val OBELISKS: HashMap<Int, Location> = hashMapOf(
    Pair(OBELISK_14829, Location(3156, 3620, 0)), // Level 13
    Pair(OBELISK_14830, Location(3219, 3656, 0)), // Level 19
    Pair(OBELISK_14827, Location(3035, 3732, 0)), // Level 27
    Pair(OBELISK_14828, Location(3106, 3794, 0)), // Level 35
    Pair(OBELISK_14826, Location(2980, 3866, 0)), // Level 44
    Pair(OBELISK_14831, Location(3307, 3916, 0))  // Level 50
)
private val REPLACEMENT_OBELISK = OBELISK_14825
private val OBELISK_TELEPORT_AUDIO = 204
private val OBELISK_TELEPORT_GFX = 342
private val random = Random()

class WildernessObeliskListener : InteractionListener() {

    override fun defineListeners() {

        on(OBELISKS.keys.toIntArray(), SCENERY, "activate") { player, node ->
            val selectedObelisk = OBELISKS[node.id]
            if (selectedObelisk != null) {
                val obelisks = getSurroundingObeliskScenery(node, selectedObelisk)
                obelisks.forEach { obj -> // Replace the scenery with the animated obelisk object
                    SceneryBuilder.replace(obj, obj.transform(REPLACEMENT_OBELISK), 6)
                }
                player.audioManager.send(OBELISK_TELEPORT_AUDIO)
                GameWorld.Pulser.submit(object : Pulse(6, player) {
                    override fun pulse(): Boolean {
                        if (delay == 1) {
                            // Send the graphic in a 3x3 square
                            selectedObelisk.get3x3Tiles().forEach {
                                getRegionChunk(it).flag(GraphicUpdateFlag(Graphics.create(OBELISK_TELEPORT_GFX), it))
                            }
                            return true
                        }
                        // Choose new location to teleport players to
                        val newObeliskLoc = OBELISKS.random()

                        // Get all the players in a 3x3 square
                        for (player in getLocalPlayersMaxNorm(selectedObelisk, 1)) {
                            val surroundingLoc = newObeliskLoc.value.get3x3Tiles()
                            player.packetDispatch.sendMessage("Ancient magic teleports you somewhere in the wilderness.")
                            player.teleporter.send(surroundingLoc.random(), TeleportManager.TeleportType.OBELISK, 2)
                        }
                        super.setDelay(1)
                        return false
                    }

                })
                return@on true
            } else {
                SystemLogger.logErr("Selected Obelisk is null!")
                return@on true
            }
        }
    }

    // Retrieves the scenery at the four corners of the Obelisk square
    private fun getSurroundingObeliskScenery(node: Node, selectedObelisk: Location): Array<Scenery> {
        return arrayOf(
            Scenery(node.id, Location(selectedObelisk.x - 5 / 2, selectedObelisk.y - 5 / 2, 0)),
            Scenery(node.id, Location(selectedObelisk.x + 5 / 2, selectedObelisk.y - 5 / 2, 0)),
            Scenery(node.id, Location(selectedObelisk.x - 5 / 2, selectedObelisk.y + 5 / 2, 0)),
            Scenery(node.id, Location(selectedObelisk.x + 5 / 2, selectedObelisk.y + 5 / 2, 0))
        )
    }

    // Helper random method
    private fun <T, U> Map<T, U>.random(): Map.Entry<T, U> = entries.elementAt(random.nextInt(size))
}