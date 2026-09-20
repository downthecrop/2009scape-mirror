package content.minigame.greatorbproject

import content.minigame.greatorbproject.OrbProjUtils.GREEN_ATTRACT
import content.minigame.greatorbproject.OrbProjUtils.GREEN_PROJ
import content.minigame.greatorbproject.OrbProjUtils.GREEN_REPEL
import content.minigame.greatorbproject.OrbProjUtils.ORB_FADE_IN
import content.minigame.greatorbproject.OrbProjUtils.ORB_FADE_OUT
import content.minigame.greatorbproject.OrbProjUtils.USE_WAND
import content.minigame.greatorbproject.OrbProjUtils.YELLOW_ATTRACT
import content.minigame.greatorbproject.OrbProjUtils.YELLOW_PROJ
import content.minigame.greatorbproject.OrbProjUtils.YELLOW_REPEL
import content.minigame.greatorbproject.OrbProjUtils.greenOrbs
import content.minigame.greatorbproject.OrbProjUtils.yellowOrbs
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager.isTeleportPermitted
import org.rs09.consts.Items

/**
 * Handles a single round (altar) of Great Orb Project:
 * 1. Spawning the orbs.
 * 2. Moving the orbs as players attract/repel.
 * 3. Scoring the orbs when they reach the altar.
 */

class OrbProjRound(private val session: OrbProjSession) {

    // tracking orbs and the player interacting with each one
    private val activeOrbs = mutableListOf<StackedOrb>()

    // tracks the orb and the player interacting with it
    private data class OrbInteracterData(val player: Player, val isAttracting: Boolean, val startLocation: Location, val team: Team, var tickTimer: Int = 0)
    private val orbInteracters = mutableMapOf<StackedOrb, OrbInteracterData>()

    // there are 3 orbs of each color at a time. this triggers at round start
    fun start() {
        // spawn Green Orbs
        repeat(3) { spawnReplacementOrb(Team.GREEN) }

        // spawn Yellow Orbs
        repeat(3) { spawnReplacementOrb(Team.YELLOW) }
    }

    // called each session tick while the round is active
    fun tick() {
        // process orb movement
        processOrbMovement()

        // check scoring based on orb position
        val orbsToReplace = mutableListOf<Team>()
        val iterator = activeOrbs.iterator()

        while (iterator.hasNext()) {
            val stack = iterator.next()
            stack.ticksAlive++

            if (checkScore(stack)) {
                iterator.remove()
                orbsToReplace.add(stack.team)
            }
        }

        orbsToReplace.forEach { team ->
            spawnReplacementOrb(team)
        }
    }

    // clears all active orbs. Called when the altar round ends (win or session termination).
    fun end() {
        activeOrbs.forEach { stack ->
            stack.all.forEach { orb ->
                animate(orb, ORB_FADE_OUT)
                runTask(orb, 2) {
                    orb.clear()
                }
            }
        }
        activeOrbs.clear()
        orbInteracters.clear()
    }

    /* * * * * * * * * * * * * * * * * * * *
     *
     * HELPER FUNCTIONS FOR THE ROUND LOGIC
     *
     * * * * * * * * * * * * * * * * * * * */

    // generates a random spawn loc for each orb
    private fun getRandomSpawnLocation(): Location? {
        val scoreZone = session.altarScoreZones[session.currentAltarIndex] ?: return null

        // calculate the center of the score zone.
        val centerX = (scoreZone.northEastX + scoreZone.southWestX) / 2
        val centerY = (scoreZone.southWestY + scoreZone.northEastY) / 2
        val center = Location(centerX, centerY, 0)

        var attempts = 0
        while (attempts < 100) {
            // generate a random offset between -7 and 7
            val dx = kotlin.random.Random.nextInt(-7, 8)
            val dy = kotlin.random.Random.nextInt(-7, 8)

            // check if the distance is between 4 and 7 tiles (using this as an initial test, I want a value that's outside the scoring zone but not too far away)
            val distance = maxOf(kotlin.math.abs(dx), kotlin.math.abs(dy))
            if (distance in 4..7) {
                val candidate = center.transform(dx, dy, 0)

                // check if walkable
                if (isTeleportPermitted(candidate)) {
                    return candidate
                }
            }
            attempts++
        }

        // fallback, hopefully all these locs are valid lol
        return center.transform(4, 4, 0)
    }

    // keeps 3 orbs out at all times using our stack model
    private fun spawnReplacementOrb(team: Team) {
        val spawnLoc = getRandomSpawnLocation() ?: return
        val orbs = if (team == Team.GREEN) greenOrbs else yellowOrbs

        val inactive = MinigameOrb(orbs[0], spawnLoc, 0)
        val attract = MinigameOrb(orbs[1], spawnLoc, 1)
        val repel = MinigameOrb(orbs[2], spawnLoc, 2)

        val stack = StackedOrb(inactive, attract, repel, team)

        stack.all.forEach { orb ->
            orb.init()
            animate(orb, ORB_FADE_IN)
        }
        activeOrbs.add(stack)
    }

    // handles the switching of the orb from attract to repel and back
    // note: the 'attract' and 'repel' options are per-person, based on what wand is being wielded, so the orbs spawn
    // as a stack of three (inactive, attract, and repel), and just change visibility based on which wand the player has.
    fun transformOrb(player: Player, orb: NPC) {
        val stack = activeOrbs.find { it.all.contains(orb) } ?: return

        // flip the guiding direction for the player in the event that they click 'change-wand' while interacting with an orb
        val interacter = orbInteracters[stack]
        if (interacter != null && interacter.player == player) {
            orbInteracters[stack] = interacter.copy(isAttracting = !interacter.isAttracting)
        }
    }

    // checks if an orb is in the scoring zone
    private fun checkScore(stack: StackedOrb): Boolean {
        val zone = session.altarScoreZones[session.currentAltarIndex] ?: return false

        if (zone.insideBorder(stack.primary.location)) {
            if (stack.team == Team.GREEN) session.greenScore++ else session.yellowScore++

            orbInteracters.remove(stack)
            stack.all.forEach { orb ->
                animate(orb, ORB_FADE_OUT)
                queueScript(orb, 2) { _ ->
                    orb.clear()
                    return@queueScript stopExecuting(orb)
                }
            }
            return true
        }
        return false
    }

    // visualize the wizardry or some shit
    private fun playOrbVisuals(player: Player, orb: NPC, attract: Boolean, team: Team) {
        face(player, orb)
        if (attract && team == Team.GREEN) {
            visualize(player, USE_WAND, GREEN_ATTRACT)
            spawnProjectile(orb, player, GREEN_PROJ)
        } else if (!attract && team == Team.GREEN) {
            visualize(player, USE_WAND, GREEN_REPEL)
            spawnProjectile(player, orb, GREEN_PROJ)
        } else if (attract && team == Team.YELLOW) {
            visualize(player, USE_WAND, YELLOW_ATTRACT)
            spawnProjectile(orb, player, YELLOW_PROJ)
        } else {
            visualize(player, USE_WAND, YELLOW_REPEL)
            spawnProjectile(player, orb, YELLOW_PROJ)
        }
    }

    // handles the player interacting with the orb
    fun orbTargetLock(orb: NPC, player: Player, attract: Boolean, team: Team) {
        val stack = activeOrbs.find { it.all.contains(orb) } ?: return

        val currentInteracter = orbInteracters[stack]?.player
        if (currentInteracter != null && currentInteracter != player) {
            sendMessage(player, "Someone else is already interacting with this orb.")
            return
        }

        player.walkingQueue.reset()

        // save the player location data. this gets referenced in the orb movement. orb stops if player moves.
        orbInteracters[stack] = OrbInteracterData(player, attract, player.location, team, 0)

        // find the orb in the stack that the player should be interacting with
        val visibleOrb = stack.all.find { !it.isHidden(player) } ?: stack.primary

        // visualize the orb
        playOrbVisuals(player, visibleOrb, attract, team)
    }

    // processes the movements for each orb
    private fun processOrbMovement() {
        val brokenLinks = mutableListOf<StackedOrb>()

        orbInteracters.forEach { (stack, data) ->
            val player = data.player
            val isAttracting = data.isAttracting
            val startLoc = data.startLocation
            val team = data.team
            val primaryOrb = stack.primary

            // find the orb in the stack that the player should be interacting with
            val visibleOrb = stack.all.find { !it.isHidden(player) } ?: stack.primary

            // stop if player moves
            if (player.location != startLoc) {
                brokenLinks.add(stack)
                return@forEach
            }

            // stop if distance greater than 10 tiles
            if (player.location.getDistance(primaryOrb.location) > 10) {
                brokenLinks.add(stack)
                return@forEach
            }

            // stop if line of sight lost
            if (!core.game.node.entity.combat.CombatSwingHandler.isProjectileClipped(player, primaryOrb, false)) {
                brokenLinks.add(stack)
                return@forEach
            }

            // keeps the anims and gfx going against the visible orb
            playOrbVisuals(player, visibleOrb, isAttracting, team)

            // player face the   o r b
            face(player, visibleOrb)

            // math shit. calculates the direction
            var dx = 0
            var dy = 0

            if (isAttracting) {
                // orb moves towards player
                if (primaryOrb.location.x < player.location.x) dx = 1
                else if (primaryOrb.location.x > player.location.x) dx = -1

                if (primaryOrb.location.y < player.location.y) dy = 1
                else if (primaryOrb.location.y > player.location.y) dy = -1
            } else {
                // orb moves away from player
                if (primaryOrb.location.x < player.location.x) dx = -1
                else if (primaryOrb.location.x > player.location.x) dx = 1

                if (primaryOrb.location.y < player.location.y) dy = -1
                else if (primaryOrb.location.y > player.location.y) dy = 1
            }

            // do the movement. check for non-walkable tiles (isTeleportPermitted). stop if you hit a wall
            val nextTile = primaryOrb.location.transform(dx, dy, 0)

            // stop 1 tile away from player
            if (isAttracting && nextTile == player.location) {
                return@forEach
            }

            if (isTeleportPermitted(nextTile)) {
                stack.all.forEach { orb ->
                    orb.walkingQueue.reset()
                    forceWalk(orb, nextTile, "smart")
                }
            } else {
                brokenLinks.add(stack)
            }
        }

        // remove connections between player/orb that were broken by walls or distance
        brokenLinks.forEach { stack ->
            orbInteracters.remove(stack)
            stack.all.forEach { it.walkingQueue.reset() }
        }
    }

    // classes to handle the orbs
    inner class MinigameOrb(id: Int, loc: Location, val state: Int) : NPC(id, loc) {
        var stackParent: StackedOrb? = null

        override fun isHidden(player: Player): Boolean {
            if (super.isHidden(player)) return true

            val ticks = stackParent?.ticksAlive ?: 0

            // only show inactive state for 5 ticks as the fade-in
            if (ticks < 5) {
                return state != 0
            }

            val wand = getItemFromEquipment(player, EquipmentSlot.WEAPON)?.id
            val isAttractor = wand == Items.GREEN_ATTRACTOR_13645 || wand == Items.YELLOW_ATTRACTOR_13643
            val isRepeller = wand == Items.GREEN_REPELLER_13646 || wand == Items.YELLOW_REPELLER_13644

            return when {
                isAttractor -> state != 1
                isRepeller -> state != 2
                else -> state != 0
            }
        }
    }

    inner class StackedOrb(
        val inactive: MinigameOrb,
        val attract: MinigameOrb,
        val repel: MinigameOrb,
        val team: Team
    ) {
        var ticksAlive = 0
        val all = listOf(inactive, attract, repel)
        val primary get() = inactive

        init {
            all.forEach { it.stackParent = this }
        }
    }
}