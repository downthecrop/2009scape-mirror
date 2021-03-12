package rs09.game.world.update

import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.Player
import core.game.world.update.flag.UpdateFlag
import core.game.world.update.flag.context.HitMark
import core.game.world.update.flag.npc.NPCHitFlag
import core.game.world.update.flag.npc.NPCHitFlag1
import core.game.world.update.flag.player.AppearanceFlag
import core.game.world.update.flag.player.HitUpdateFlag
import core.game.world.update.flag.player.HitUpdateFlag1
import core.net.packet.IoBuffer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Holds an entity's update masks.
 * @author Emperor
 */
class UpdateMasks {
    /**
     * The mask data.
     */
    private var maskData = 0

    /**
     * The update masks array.
     */
    private val masks = arrayOfNulls<UpdateFlag<*>?>(SIZE)

    /**
     * Gets the appearanceStamp.
     * @return The appearanceStamp.
     */
    /**
     * Sets the appearanceStamp.
     * @param appearanceStamp The appearanceStamp to set.
     */
    /**
     * The appearance time stamp.
     */
    var appearanceStamp: Long = 0

    /**
     * The synced mask data.
     */
    private var syncedMask = 0

    /**
     * The update masks array.
     */
    private val syncedMasks = arrayOfNulls<UpdateFlag<*>?>(SIZE)

    /**
     * If the update masks are being updated.
     */
    private val updating = AtomicBoolean()
    /**
     * Registers an update flag.
     * @param flag The update flag.
     * @return `True` if successful.
     */
    /**
     * Registers an update flag.
     * @param flag The update flag.
     * @return `True` if successful.
     */
    @JvmOverloads
    fun register(flag: UpdateFlag<*>, synced: Boolean = false): Boolean {
        var synced = synced
        if (updating.get()) {
            return false
        }
        if (flag is AppearanceFlag) {
            appearanceStamp = System.currentTimeMillis()
            synced = true
        }
        if (synced) {
            syncedMasks[flag.ordinal()] = flag
            syncedMask = syncedMask or flag.data()
        }
        maskData = maskData or flag.data()
        masks[flag.ordinal()] = flag
        return true
    }

    /**
     * Unregisters a synced update mask.
     * @param maskData The mask data.
     * @return `True` if the mask got removed.
     */
    fun unregisterSynced(ordinal: Int): Boolean {
        if (syncedMasks[ordinal] != null) {
            syncedMask = syncedMask and syncedMasks[ordinal]!!.data().inv()
            syncedMasks[ordinal] = null
            return true
        }
        return false
    }

    /**
     * Writes the flags.
     * @param p The player.
     * @param e The entity to update.
     * @param buffer The buffer to write on.
     */
    fun write(p: Player?, e: Entity?, buffer: IoBuffer) {
        var maskData = maskData
        if (maskData >= 0x100) {
            maskData = maskData or if (e is Player) 0x10 else 0x8
            buffer.put(maskData).put(maskData shr 8)
        } else {
            buffer.put(maskData)
        }
        for (i in masks.indices) {
            val flag = masks[i]
            flag?.writeDynamic(buffer, p)
        }
    }

    /**
     * Writes the update masks on synchronization.
     * @param p The player.
     * @param e The entity to update.
     * @param buffer The buffer to write on.
     * @param appearance If the appearance mask should be written.
     */
    fun writeSynced(p: Player?, e: Entity?, buffer: IoBuffer, appearance: Boolean) {
        var maskData = maskData
        var synced = syncedMask
        if (!appearance && synced and AppearanceFlag.getData() != 0) {
            synced = synced and AppearanceFlag.getData().inv()
        }
        maskData = maskData or synced
        if (maskData >= 0x100) {
            maskData = maskData or if (e is Player) 0x10 else 0x8
            buffer.put(maskData).put(maskData shr 8)
        } else {
            buffer.put(maskData)
        }
        for (i in masks.indices) {
            var flag = masks[i]
            if (flag == null) {
                flag = syncedMasks[i]
                if (!appearance && flag is AppearanceFlag) {
                    continue
                }
            }
            flag?.writeDynamic(buffer, p)
        }
    }

    /**
     * Adds the dynamic update flags.
     * @param entity The entity.
     */
    fun prepare(entity: Entity) {
        val handler = entity.impactHandler
        for (i in 0..1) {
            if (handler.impactQueue.peek() == null) {
                break
            }
            val impact = handler.impactQueue.poll()
            registerHitUpdate(entity, impact, i == 1)
        }
        updating.set(true)
    }

    /**
     * Registers the hit update for the given [Impact].
     * @param e The entity.
     * @param impact The impact to update.
     * @param secondary If the hit update is secondary.
     */
    private fun registerHitUpdate(e: Entity, impact: ImpactHandler.Impact, secondary: Boolean): HitMark {
        val player = e is Player
        val mark = HitMark(impact.amount, impact.type.ordinal, e)
        if (player) {
            register(if (secondary) HitUpdateFlag1(mark) else HitUpdateFlag(mark))
        } else {
            register(if (secondary) NPCHitFlag1(mark) else NPCHitFlag(mark))
        }
        return mark
    }

    /**
     * Resets the update masks.
     */
    fun reset() {
        for (i in masks.indices) {
            masks[i] = null
        }
        maskData = 0
        updating.set(false)
    }

    fun isUpdating(): Boolean {
        return updating.get()
    }

    /**
     * Checks if an update is required.
     * @return `True` if so.
     */
    val isUpdateRequired: Boolean
        get() = maskData != 0

    /**
     * Checks if synced update masks have been registered.
     * @return `True` if so.
     */
    fun hasSynced(): Boolean {
        return syncedMask != 0
    }

    companion object {
        /**
         * The amount of update masks.
         */
        const val SIZE = 11
    }
}