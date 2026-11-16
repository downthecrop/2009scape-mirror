package core.game.node.scenery

import core.api.getOrStartWorldTimer
import core.api.getWorldTicks
import core.api.queueWorldScript
import core.api.stopExecutingWorldScript
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.world.map.RegionManager.getRegionChunk
import core.game.world.map.build.LandscapeParser
import core.game.world.update.flag.chunk.ObjectUpdateFlag

/**
 * An aiding class for object constructing/removing.
 */
object SceneryBuilder {
    /**
     * Replaces a scenery permanently.
     * @param remove The object to remove.
     * @param construct The object to add.
     * @return `True` if successful.
     */
    @JvmStatic
    @JvmOverloads
    fun replace(remove: Scenery, construct: Scenery, clip: Boolean = true): Boolean {
        return replace(remove, construct, -1, clip)
    }

    /**
     * Replaces the object client sided alone.
     * @param remove The object to remove.
     * @param construct The object to replace with.
     * @param restoreTicks The restoration ticks.
     * @return `True` if successful.
     */
    private fun replaceClientSide(remove: Scenery, construct: Scenery, restoreTicks: Int): Boolean {
        getRegionChunk(remove.getLocation()).flag(ObjectUpdateFlag(remove, true))
        getRegionChunk(construct.getLocation()).flag(ObjectUpdateFlag(construct, false))
        if (restoreTicks > 0) {
            queueWorldScript(restoreTicks) { _ ->
                replaceClientSide(construct, remove, -1)
                stopExecutingWorldScript()
            }
        }
        return true
    }

    /**
     * Replaces a scenery temporarily.
     * @param remove The object to remove.
     * @param construct The object to add.
     * @param restoreTicks The amount of ticks before the object gets restored.
     * @return `True` if successful.
     */
    @JvmStatic
    @JvmOverloads
    fun replace(remove: Scenery, construct: Scenery, restoreTicks: Int, clip: Boolean = true): Boolean {
        if (!clip) {
            return replaceClientSide(remove, construct, restoreTicks)
        }
        val obj = remove.wrapper
        val current = LandscapeParser.removeScenery(obj) ?: return false
        LandscapeParser.addScenery(construct)
        update(current, construct)
        if (restoreTicks < 0) {
            SceneryRestoreTimer.replacementMap.remove(current)
            return true
        }
        val restoreAt = getWorldTicks() + restoreTicks
        SceneryRestoreTimer.replacementMap[construct] = current to restoreAt
        val timer = getOrStartWorldTimer("SceneryRestoreTimer")
        if (timer.nextExecution > restoreAt) {
            timer.nextExecution = restoreAt
        }
        return true
    }

    /**
     * Adds a scenery.
     * @param add The object to add.
     * @return `True` if successful.
     */
    @JvmStatic
    fun add(add: Scenery): Scenery {
        return add(add, -1)
    }

    /**
     * Adds a scenery.
     * @param add The object to add.
     * @param ticks The amount of ticks this object should last for (-1 for permanent).
     * @return `True` if successful.
     */
    @JvmStatic
    fun add(add: Scenery, ticks: Int, vararg items: GroundItem): Scenery {
        val obj = add.wrapper
        LandscapeParser.addScenery(obj)
        update(obj, obj)
        if (ticks > -1) {
            queueWorldScript(ticks) { _ ->
                remove(obj)
                for (item in items) {
                    GroundItemManager.create(item)
                }
                stopExecutingWorldScript()
            }
        }
        return obj
    }

    /**
     * Removes a scenery.
     * @param remove The object to remove.
     * @return `True` if successful.
     */
    @JvmStatic
    fun remove(remove: Scenery?): Boolean {
        val obj = remove?.wrapper ?: return false
        val current = LandscapeParser.removeScenery(obj) ?: return false
        SceneryRestoreTimer.replacementMap.remove(current)
        update(current)
        return true
    }

    /**
     * Removes a scenery.
     * @param remove the object.
     * @param respawnTicks the respawn ticks.
     * @return `True`if removed.
     */
    @JvmStatic
    fun remove(remove: Scenery?, respawnTicks: Int): Boolean {
        if (remove(remove)) {
            queueWorldScript(respawnTicks) { _ ->
                add(remove!!)
                stopExecutingWorldScript()
            }
            return true
        }
        return false
    }

    /**
     * Updates the scenery on all the player's screen.
     * @param objects The sceneries.
     */
    fun update(vararg objects: Scenery?) {
        for (o in objects) {
            if (o == null) {
                continue
            }
            getRegionChunk(o.location).flag(ObjectUpdateFlag(o, !o.isActive))
        }
    }
}
