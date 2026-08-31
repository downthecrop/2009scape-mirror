package content.region.morytania.quest.hauntedmine

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.RegionManager.getLocalPlayers
import core.tools.RandomFunction.random
import org.rs09.consts.NPCs

/**
 * Loading Crane NPC used during the Haunted Mine boss fight.
 */

// covers the behavior of the loading cranes. Usually they do nothing.
// During the Treus Dayth Haunted Mine fight, they attack any player within melee range for 0-10 damage.

class LoadingCraneNPCBehavior : NPCBehavior(NPCs.LOADING_CRANE_1542) {

    // Using this tracker because I'm triggering crane attacks in the tick section instead of as regular attacks. Cranes hit every 3 ticks.
    private companion object {
        const val CRANE_COOLDOWN = "crane_cooldown"
        const val CRANE_ATTACK_ANIM = 1452 // I cycled through hundreds of animations to find this, I'm sure the players will truly appreciate this touch.
    }

    // If the player is in a space around the 3x3 NPC's center location,
    // The crane should rotate towards the player, do a chomp animation, and hit up to 10
    override fun tick(self: NPC): Boolean {

        // only attack once every 3 ticks
        val cooldown = getAttribute(self, CRANE_COOLDOWN, 0)
        if (cooldown > 0) {
            setAttribute(self, CRANE_COOLDOWN, cooldown - 1)
            return true
        }

        // check if boss is in area
        val boss = findLocalNPC(self, NPCs.TREUS_DAYTH_1540)
        if (boss != null) {
            // damage any players that get close
            for (p in getLocalPlayers(self.location)) {
                if (p.location.withinMaxnormDistance(self.centerLocation, 2)
                    && getAttribute(p, HauntedMine.ATTR_DAYTH_FIGHT, false)
                ) {
                    face(self, p, 3)
                    animate(self, CRANE_ATTACK_ANIM)
                    impact(p, random(0, 10))

                    // start cooldown
                    setAttribute(self, CRANE_COOLDOWN, 3)
                    break
                }
            }
        }
        return true
    }
}