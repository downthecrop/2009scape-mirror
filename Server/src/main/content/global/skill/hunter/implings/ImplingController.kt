package content.global.skill.hunter.implings

import core.api.*
import core.tools.*
import core.game.world.map.Location
import core.game.node.entity.npc.NPC
import core.game.system.command.*
import kotlin.math.*

/**
 * Manages the high-level behavior of implings in the overworld: Spawning them, clearing them, etc.
 * NOTE: This does not manage the spawns INSIDE puro-puro. Those are actually handled by JSON.
*/
class ImplingController : TickListener, Commands {
    override fun tick() {
        if (--nextCycle > getTicksBeforeNextCycleToDespawn()) 
            return
        if (activeImplings.size > 0) {
            clearSomeImplings(min(activeImplings.size, implingsClearedPerTick))
            return
        }
        generateSpawners()
        nextCycle = secondsToTicks(60 * 30) // 30 minutes
    }

    override fun defineCommands() {
        define ("implings", Privilege.ADMIN, "", "Lists the currently active implings/spawners") { player, _ ->  
            for (i in 0..310)
                setInterfaceText(player, "", 275, i)
            setInterfaceText(player, "Implings", 275, 2)
            for ((index, impling) in activeImplings.withIndex()) {
                var text = "This shouldn't be here -> ${impling.id}"
                if (impling.id < 1028) {
                    val table = ImplingSpawner.forId(impling.id)
                    if (table != null)
                        text = table.name
                }
                else text = impling.name
                setInterfaceText(player, "$text -> ${impling.location}", 275, index + 11)
            }
            openInterface(player, 275)
        }
    }

    companion object {
        val implingsClearedPerTick = 5

        var nextCycle = 0
        var activeImplings = ArrayList<NPC>()

        fun clearSomeImplings (amount: Int) {
            for (i in 0 until amount) {
                val impling = activeImplings.removeAt(0)
                poofClear(impling)
            }
        }

        fun generateSpawners () {
            val typeLocations = ImplingSpawnLocations.values()
            for (set in typeLocations) {
                val locations = set.locations
                val type = set.type
                locations.forEach { generateSpawnersAt(it, type) }
            }
        }

        fun generateSpawnersAt(location: Location, type: ImplingSpawnTypes) {
            for (i in 0 until type.spawnRolls) {
                val spawner = type.table.roll() ?: continue
                if (spawner == ImplingSpawner.Nothing) continue
                val npc = NPC.create (spawner.npcId, location)
                npc.init()
                activeImplings.add(npc)
            }
        }

        fun getTicksBeforeNextCycleToDespawn() : Int {
            return ceil (activeImplings.size / implingsClearedPerTick.toDouble()).toInt()
        }

        fun deregister (impling: NPC, graceful: Boolean = false) : Boolean {
            activeImplings.remove(impling)
            if (graceful)
                poofClear(impling)
            else
                impling.clear()
            return true
        }
    }
}
