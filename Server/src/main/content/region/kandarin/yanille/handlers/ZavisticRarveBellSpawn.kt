package content.region.kandarin.yanille.handlers

import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs09.consts.NPCs

/**
 * Handles the temporary Zavistic Rarve NPC spawned by the Wizards' Guild bell.
 * @author Edith
 */

object ZavisticRarveBellSpawn {
    const val NPC_ATTRIBUTE = "zavistic_rarve_bell_spawn"
    private var activeSpawn: ZavisticRarveBellNPC? = null

    fun getOrSpawn(player: Player, location: Location): ZavisticRarveBellNPC {
        val existing = activeSpawn

        if (existing != null && existing.isActive) {
            existing.resetDespawnTimer(player)
            return existing
        }

        return ZavisticRarveBellNPC(location).also {
            it.init()
            it.resetDespawnTimer(player)
            activeSpawn = it
        }
    }

    fun resetDespawnTimer(npc: NPC, player: Player) {
        (npc as? ZavisticRarveBellNPC)?.resetDespawnTimer(player)
    }

    fun clear(npc: ZavisticRarveBellNPC) {
        if (activeSpawn == npc) {
            activeSpawn = null
        }
    }

    fun isBellSpawn(npc: NPC): Boolean {
        return getAttribute(npc, NPC_ATTRIBUTE, false)
    }

}

class ZavisticRarveBellNPC(location: Location) : NPC(NPCs.ZAVISTIC_RARVE_2059, location) {
    private var idleTicks = 0
    private var focusedPlayer: Player? = null

    fun resetDespawnTimer(player: Player) {
        idleTicks = 0
        focusedPlayer = player
    }

    override fun init() {
        isRespawn = false
        isWalks = false
        setAttribute(this, ZavisticRarveBellSpawn.NPC_ATTRIBUTE, true)
        super.init()
    }

    override fun handleTickActions() {
        super.handleTickActions()

        idleTicks++

        focusedPlayer?.let {
            if (it.isActive && it.location.withinMaxnormDistance(location, 2)) {
                faceLocation(it.location)
            }
        }

        if (idleTicks >= 400) {
            clear()
        }
    }

    override fun clear() {
        ZavisticRarveBellSpawn.clear(this)
        super.clear()
    }
}