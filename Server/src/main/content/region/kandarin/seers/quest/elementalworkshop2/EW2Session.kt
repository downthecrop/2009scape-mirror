package content.region.kandarin.seers.quest.elementalworkshop2

import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncCart
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncCogs
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncCrane
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncFan
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncSteamPress
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncTank
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.syncTankDoor
import content.region.kandarin.seers.quest.elementalworkshop2.EW2Listeners.Companion.toInstance
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.map.zone.ZoneBorders

/**
 * Handles an instanced Elemental Workshop II session.
 */

class EW2Session(val player: Player? = null) : MapArea {

    companion object {
        const val EW2_REGION = 7760
        private const val ATTR_EW2_SESSION = "ew2-session"
        private const val ATTR_EW2_LOGOUT = "ew2-logout"
    }

    lateinit var region: DynamicRegion
    lateinit var base: Location

    // borders will be registered dynamically in start()
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return emptyArray()
    }

    // start() is triggered by the listener for Scenery.HATCH_18596 and Scenery.STAIRS_18599
    fun start(loc: Location) {
        val p = player ?: return

        region = DynamicRegion.create(EW2_REGION)
        base = region.baseLocation

        zone.register(getRegionBorders(region.id))
        setAttribute(p, ATTR_EW2_SESSION, this)

        // logout listener returns player to the EW1 workshop
        registerLogoutListener(p, ATTR_EW2_LOGOUT) {
            val session = getAttribute<EW2Session?>(p, ATTR_EW2_SESSION, null) ?: return@registerLogoutListener
            p.location = Location(2719, 9892, 0)
            p.locks.unlockTeleport()
            session.cleanup(p)
        }

        // sync the workshop state
        syncCrane(p)
        syncCart(p)
        syncSteamPress(p)
        syncTank(p)
        syncTankDoor(p)
        syncFan(p)
        syncCogs(p)

        val startLoc = toInstance(player, loc)

        // teleport in after the workshop is ready
        queueScript(player, 1, QueueStrength.SOFT) {
            teleport(p, startLoc)
            return@queueScript true
        }
    }

    // clean up the instanced area
    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player && entity == player && !logout) {
            cleanup(entity)
        }
    }

    fun cleanup(p: Player) {
        zone.unregister(getRegionBorders(region.id))
        clearLogoutListener(p, ATTR_EW2_LOGOUT)
        removeAttribute(p, ATTR_EW2_SESSION)
    }
}