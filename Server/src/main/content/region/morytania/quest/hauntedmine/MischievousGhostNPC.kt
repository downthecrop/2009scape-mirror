package content.region.morytania.quest.hauntedmine

import content.data.Quests
import core.api.*
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.path.ClipMaskSupplier
import core.game.world.map.path.Pathfinder
import org.rs09.consts.NPCs

/**
 * Mischievous NPC used during the Haunted Mine quest.
 */

class MischievousGhostNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private var following: Player? = null
    private var isPathingToValve = false
    private val valveTriggerDist = 7
    private val valveLoc = Location.create(2807, 4496, 0)

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC {
        return MischievousGhostNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MISCHIEVOUS_GHOST_1551)
    }

    // the following logic was adapted from the DraynorManorChairNPC
    fun follow(player: Player) {
        pulseManager.run((object : MovementPulse(this, player, Pathfinder.DUMB) {
            override fun pulse(): Boolean {
                face(player)
                return false
            }
        }), PulseType.STANDARD)
    }

    fun stopFollowing() {
        following = null
        resetWalk()
        pulseManager.clear(PulseType.STANDARD)
    }

    fun findDistanceToPlayer(player: Player): Double {
        return this.location.getDistance(player.location)
    }

    // walk to the valve
    fun walkToValve() {
        pulseManager.run((object : MovementPulse(this, valveLoc, Pathfinder.SMART) {
            override fun pulse(): Boolean {
                return false
            }
        }), PulseType.STANDARD)
    }

    override fun handleTickActions() {
        super.handleTickActions()

        if (!this.isWalks) return

        // get target player
        val targetPlayer = getAttribute(HauntedMine.ATTR_HAUNTED_TARGET) as? Player ?: return

        // check if player is close to valve
        if (targetPlayer.location.getDistance(valveLoc) <= valveTriggerDist) {
            // stop following player and move to shut off valve
            if (!isPathingToValve) {
                stopFollowing()
                isPathingToValve = true
            }

            // keep walking towards the valve if the pulse drops
            if (!pulseManager.hasPulseRunning()) {
                walkToValve()
            }

            return
        }

        // follow target player
        if (following != targetPlayer) {
            stopFollowing()
            following = targetPlayer
            follow(targetPlayer)
            face(targetPlayer)
        } else {
            // re-check conditions. if player goes down the lift, quest stage advances and ghost is cleared.
            if (findDistanceToPlayer(targetPlayer) > 27
                || !targetPlayer.isActive
                || targetPlayer.isInvisible
                || getQuestStage(targetPlayer, Quests.HAUNTED_MINE) > 3
            ) {
                this.clear()
            } else {
                if (!pulseManager.hasPulseRunning()) {
                    follow(targetPlayer)
                }
                face(targetPlayer)
            }
        }
    }
}

class MischievousGhostNPCBehavior : NPCBehavior(NPCs.MISCHIEVOUS_GHOST_1551) {

    // logic to check if ghost is at end of path. If it is, turn off valve that powers the lift.
    override fun tick(self: NPC): Boolean {
        val targetPlayer = getAttribute<Player?>(self, HauntedMine.ATTR_HAUNTED_TARGET, null)
        if (targetPlayer != null) {
            if (self.location == location(2807, 4496, 0)) {
                if (getQuestStage(targetPlayer, Quests.HAUNTED_MINE) <= 3) {
                    sendMessage(targetPlayer, "You hear the sound of a valve being turned.")
                    setVarbit(targetPlayer, HauntedMine.LIFT_MACHINERY_VARBIT, 0)
                }
                self.clear()
            }
        }
        return true
    }

    // this lets him walk through walls like a ghost.
    override fun getClippingSupplier(self: NPC): ClipMaskSupplier {
        return GhostClipper
    }
}