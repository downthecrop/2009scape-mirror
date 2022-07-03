package rs09.game.node.entity.skill.thieving

import api.*
import core.game.content.global.action.DoorActionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.tools.RandomFunction
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class PickableDoorListener : InteractionListener {

    companion object {
        val PICKABLE_DOORS = enumValues<PickableDoors>()
            .map { it.id }
            .toIntArray()
    }

    private fun isOnTheInside(player: Player, node: Node): Boolean {
        val relativeDirection = Direction.getLogicalDirection(player.location, node.location)

        return when (node.direction) {
            Direction.SOUTH -> relativeDirection == Direction.WEST
            Direction.EAST -> relativeDirection == Direction.SOUTH
            Direction.NORTH -> relativeDirection == Direction.EAST
            Direction.WEST -> relativeDirection == Direction.NORTH
            else -> false
        }
    }

    private fun attemptPickLock(player: Player, node: Node): Boolean {
        node as Scenery

        if (node.id !in PICKABLE_DOORS) {
            sendMessage(player, "This (ID: ${node.id}) door shouldn't be pickable.")
            sendMessage(player, "Please report this to developers.")
            return true
        }

        if (isOnTheInside(player, node)) {
            sendMessage(player, "This door is already unlocked.")
            return true
        }

        PickableDoors.forID(node.id)?.let {
            if (it.requireLockpick && !inInventory(player, Items.LOCKPICK_1523)) {
                sendMessage(player, "You need a lockpick in order to pick this lock.")
                return true
            }

            player.sendMessage("You attempt to pick the lock.")
            if (it.levelRequirement > getDynLevel(player, Skills.THIEVING)) {
                if (RandomFunction.roll(2)) {
                    impact(player, RandomFunction.random(1, 4))
                    sendMessage(player, "You have activated a trap on the lock.")
                } else {
                    sendMessage(player, "You fail to pick the lock.")
                }

                return true
            }

            if (RandomFunction.roll(3)) {
                rewardXP(player, Skills.THIEVING, it.xpReward)
                DoorActionHandler.handleAutowalkDoor(player, node)

                sendMessage(player, "You manage to pick the lock.")
            } else {
                sendMessage(player, "You fail to pick the lock.")
            }
        }

        return true
    }

    private fun attemptOpen(player: Player, node: Node): Boolean {
        node as Scenery

        if (isOnTheInside(player, node)) {
            DoorActionHandler.handleAutowalkDoor(player, node)
            sendMessage(player, "You go through the door.")
        } else {
            sendMessage(player, "The door is locked.")
        }

        return true
    }

    override fun defineListeners() {
        on(PICKABLE_DOORS, SCENERY, "pick-lock", handler = ::attemptPickLock)
        on(PICKABLE_DOORS, SCENERY, "open", handler = ::attemptOpen)
    }
}