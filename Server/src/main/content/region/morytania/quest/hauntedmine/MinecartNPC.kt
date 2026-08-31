package content.region.morytania.quest.hauntedmine

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalPlayers
import core.tools.RandomFunction.random
import org.rs09.consts.NPCs

/**
 * Minecart NPCs used during Haunted Mine quest.
 */

// covers the behavior of the animated minecarts. They move back and forth along set paths
// if the player runs into one they push the player back with them while causing continuous damage.

class MinecartNPCBehavior : NPCBehavior(
    NPCs.MINE_CART_1544,
    NPCs.MINE_CART_1545,
    NPCs.MINE_CART_1546,
    NPCs.MINE_CART_1547,
    NPCs.MINE_CART_1548
) {

    var clearTime = 0

    // track location from the previous tick to use later to make a vector
    private data class CartState(val previousLocation: Location, val lastDx: Int, val lastDy: Int)

    private val cartStates = HashMap<Int, CartState>()
    private val playerPreviousLocations = HashMap<Int, Location>()

    // this function records if a player has just passed through a minecart.
    // if the player is running, they move two tiles per tick and can skip through the cart sometimes.
    private fun playerPassedThrough(from: Location, to: Location, cart: Location): Boolean {
        if (from.y == cart.y && to.y == cart.y) {
            val minX = minOf(from.x, to.x)
            val maxX = maxOf(from.x, to.x)
            if (cart.x in minX..maxX) return true
        }
        if (from.x == cart.x && to.x == cart.x) {
            val minY = minOf(from.y, to.y)
            val maxY = maxOf(from.y, to.y)
            if (cart.y in minY..maxY) return true
        }
        return false
    }

    override fun onCreation(self: NPC) {

        self.isWalks = true
        self.isPathBoundMovement = true
        cartStates[self.index] = CartState(self.location, 0, 0)

        // 1544 are the carts in the boss fight
        if (self.id == NPCs.MINE_CART_1544) {
            if (self.properties.spawnLocation == location(2781, 4462, 0)) {
                val movementPath = arrayOf(
                    Location.create(2781, 4462, 0),
                    Location.create(2795, 4462, 0)
                )
                self.configureMovementPath(*movementPath)
            }
            if (self.properties.spawnLocation == location(2785, 4456, 0)) {
                val movementPath = arrayOf(
                    Location.create(2785, 4456, 0),
                    Location.create(2782, 4456, 0)
                )
                self.configureMovementPath(*movementPath)
            }
            if (self.properties.spawnLocation == location(2791, 4456, 0)) {
                val movementPath = arrayOf(
                    Location.create(2791, 4456, 0),
                    Location.create(2794, 4456, 0)
                )
                self.configureMovementPath(*movementPath)
            }
            if (self.properties.spawnLocation == location(2783, 4446, 0)) {
                val movementPath = arrayOf(
                    Location.create(2783, 4446, 0),
                    Location.create(2783, 4453, 0)
                )
                self.configureMovementPath(*movementPath)
            }
            if (self.properties.spawnLocation == location(2785, 4447, 0)) {
                val movementPath = arrayOf(
                    Location.create(2785, 4447, 0),
                    Location.create(2785, 4453, 0)
                )
                self.configureMovementPath(*movementPath)
            }
            if (self.properties.spawnLocation == location(2791, 4447, 0)) {
                val movementPath = arrayOf(
                    Location.create(2791, 4447, 0),
                    Location.create(2791, 4454, 0)
                )
                self.configureMovementPath(*movementPath)
            }
            if (self.properties.spawnLocation == location(2793, 4446, 0)) {
                val movementPath = arrayOf(
                    Location.create(2793, 4446, 0),
                    Location.create(2793, 4454, 0)
                )
                self.configureMovementPath(*movementPath)
            }

        }
        if (self.id == NPCs.MINE_CART_1545
            && self.properties.spawnLocation == location(2727, 4509, 0)
        ) {
            val movementPath = arrayOf(
                Location.create(2727, 4514, 0),
                Location.create(2727, 4491, 0)
            )
            self.configureMovementPath(*movementPath)
        }

        if (self.id == NPCs.MINE_CART_1546
            && self.properties.spawnLocation == location(2697, 4498, 0)
        ) {
            val movementPath = arrayOf(
                Location.create(2696, 4498, 0),
                Location.create(2711, 4498, 0)
            )
            self.configureMovementPath(*movementPath)
        }

        if (self.id == NPCs.MINE_CART_1547
            && self.properties.spawnLocation == location(2715, 4517, 0)
        ) {
            val movementPath = arrayOf(
                Location.create(2696, 4498, 0),
                Location.create(2711, 4498, 0)
            )
            self.configureMovementPath(*movementPath)
        }

        if (self.id == NPCs.MINE_CART_1548
            && self.properties.spawnLocation == location(2739, 4528, 0)
        ) {
            val movementPath = arrayOf(
                Location.create(2739, 4531, 0),
                Location.create(2739, 4528, 0)
            )
            self.configureMovementPath(*movementPath)
        }
    }

    // logic to check for, push, and damage players.
    override fun tick(self: NPC): Boolean {

        val state = cartStates[self.index] ?: CartState(self.location, 0, 0)

        val dx = self.location.x - state.previousLocation.x
        val dy = self.location.y - state.previousLocation.y
        val isMoving = dx != 0 || dy != 0

        // activeDx/y remembers the vector state if the cart is not actively moving
        val activeDx = if (isMoving) dx else state.lastDx
        val activeDy = if (isMoving) dy else state.lastDy

        cartStates[self.index] = CartState(
            previousLocation = self.location,
            lastDx = activeDx,
            lastDy = activeDy
        )

        // Boss fight carts hit the player harder
        val damageRange = if (self.id == NPCs.MINE_CART_1544) random(0, 7) else random(0, 2)

        // track if any nearby player is in the boss fight
        var anyPlayerFighting = false

        // check all local players
        for (p in getLocalPlayers(self.location, 32)) {
            val playerPrev = playerPreviousLocations[p.index]

            val collidesWithCart = p.location == self.location ||
                    (playerPrev != null && playerPassedThrough(playerPrev, p.location, self.location))

            if (collidesWithCart) {
                val pushDestination = when {
                    // push along cart's path
                    activeDx != 0 || activeDy != 0 -> self.location.transform(activeDx, activeDy, 0)
                    // if cart has no path, push player back where they came from
                    playerPrev != null -> playerPrev
                    else -> null
                }

                if (pushDestination != null) {
                    // only hurt if the cart is actually moving. otherwise just push player back
                    if (isMoving) {
                        impact(p, damageRange)
                        animate(p, 1441)
                    }
                    lock(p, 2)
                    teleport(p, pushDestination)
                    p.currentMovement.stop()
                }
            }

            // record player location
            playerPreviousLocations[p.index] = p.location

            // record if someone is fighting
            if (isTreusDaythNearby(p)) {
                anyPlayerFighting = true
            }
        }

        // if nobody is fighting, clear the boss fight carts
        if (self.id == NPCs.MINE_CART_1544 && !anyPlayerFighting) {
            self.clear()
        }

        // this clears the boss fight carts after 10 minutes
        //if (self.id == NPCs.MINE_CART_1544) {
        //    if (clearTime++ > 1000) poofClear(self)
        //}

        return true
    }
}