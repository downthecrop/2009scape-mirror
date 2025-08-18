package content.region.misthalin.lumbridge.handlers

import core.api.*
import core.game.interaction.MovementPulse
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.ImpactHandler.HitsplatType
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.repository.Repository
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * NPCs.WALL_BEAST_7823 starts out as NPCs.HOLE_IN_THE_WALL_2058 first.
 * On death, it will respawn a new hole in the wall.
 *
 * h2QAZzGds9o
 *
 * npc anim 1807
 * {3161,9547,0,0,3}-{3164,9556,0,0,4}-{3162,9574,0,0,3}-{3198,9554,0,0,7}-{3198,9572,0,0,1}-{3215,9560,0,0,1}-{3216,9588,0,0,1}-
 */
class WallBeastBehavior : NPCBehavior(NPCs.HOLE_IN_THE_WALL_2058, NPCs.WALL_BEAST_7823) {
    override fun onCreation(self: NPC) {
        // This NPC should never move.
        self.walkRadius = 0
        self.isNeverWalks = true
    }

    override fun onDeathFinished(self: NPC, entity: Entity) {
        self.reTransform()
        // I don't think this is needed since the respawn will be immediate.
    }
}

class WallBeastNTrigger : MapArea {

    companion object {
        /** Finds the nearest NPC of npcId to location. */
        fun findNearestNPC(location: Location, npcId: Int): NPC? {
            // maybe replace contentAPI's version of this
            return Repository.npcs
                .filter { it.id == npcId }
                .toMutableList()
                .sortedWith { a, b ->
                     return@sortedWith (Location.getDistance(a.location, location) - Location.getDistance(b.location, location)).toInt()
                }
                .firstOrNull()
        }

        fun isHelmetInEquipment(entity: Player): Boolean {
            return inEquipment(entity, Items.SPINY_HELMET_4551) ||
                    inEquipment(entity, Items.SLAYER_HELMET_13263) ||
                    inEquipment(entity, Items.SPIKED_HELMET_13105) ||
                    inEquipment(entity, Items.SLAYER_HELMET_E_14636) ||
                    inEquipment(entity, Items.SLAYER_HELMET_CHARGED_14637)
        }
    }
    override fun defineAreaBorders(): Array<ZoneBorders> {
        // The goals here are in order
        return arrayOf(
                ZoneBorders(3161,9546,3161,9546),
                ZoneBorders(3164,9555,3164,9555),
                ZoneBorders(3162,9573,3162,9573),
                ZoneBorders(3198,9553,3198,9553),
                ZoneBorders(3198,9571,3198,9571),
                ZoneBorders(3215,9559,3215,9559),
                ZoneBorders(3216,9587,3216,9587),
        )
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            val nearbyWallbeast = findNearestNPC(entity.location, NPCs.HOLE_IN_THE_WALL_2058) // wallbeasts are always 1 square north.
            // println(nearbyWallbeast)
            if (nearbyWallbeast != null) {
                lock(entity, 2)
                stopWalk(entity)
                // Stop walk doesn't stop the player from walking on.
                // You have to clear the movement pulse that is continually feeding the walk location.
                val movementPulse = entity.getPulseManager().current;
                if (movementPulse is MovementPulse) {
                    movementPulse.stop();
                }
                face(entity, nearbyWallbeast.location)
                queueScript(nearbyWallbeast, 2, QueueStrength.STRONG) { stage: Int ->
                    when (stage) {
                        0 -> {
                            if (isHelmetInEquipment(entity)) {
                                animate(nearbyWallbeast, 1805)
                            } else {
                                animate(nearbyWallbeast, 1806)
                            }
                            return@queueScript delayScript(nearbyWallbeast, 1)
                        }
                        1 -> {
                            if (isHelmetInEquipment(entity)) {
                                // You can only attack the wallbeast if you have a helmet
                                transformNpc(nearbyWallbeast, NPCs.WALL_BEAST_7823, 300)
                                return@queueScript stopExecuting(nearbyWallbeast)
                            } else {
                                animate(nearbyWallbeast, 1807)
                                return@queueScript delayScript(nearbyWallbeast, 4)
                            }
                        }
                        2 -> {
                            animate(nearbyWallbeast, 1808)
                            return@queueScript stopExecuting(nearbyWallbeast)
                        }
                        else -> return@queueScript stopExecuting(nearbyWallbeast)
                    }
                }
                queueScript(entity, 2, QueueStrength.STRONG) { stage: Int ->
                        when (stage) {
                            0 -> {
                                if (isHelmetInEquipment(entity)) {
                                    return@queueScript stopExecuting(entity)
                                }
                                lock(entity, 5)
                                return@queueScript delayScript(entity, 1)
                            }

                            1 -> {
                                animate(entity, 1810) // 734
                                return@queueScript delayScript(entity, 4)
                            }

                            2 -> {
                                // I don't know how much they are supposed to hit... osrs says up to 18...
                                impact(entity, (13 .. 18).random() , HitsplatType.NORMAL)
                                animate(entity, 1811) // 734
                                return@queueScript stopExecuting(entity)
                            }

                            else -> return@queueScript stopExecuting(entity)
                        }
                }
            }
        }
    }

}