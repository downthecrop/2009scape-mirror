package content.global.handlers.npc

import content.region.misthalin.lumbridge.quest.sheepshearer.SheepShearer.Companion.ATTR_IS_PENGUIN_SHEEP_SHEARED
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.DeathTask
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds
import core.game.world.map.Location
import core.game.world.map.Direction

private val sheepIds = intArrayOf(
        NPCs.SHEEP_42,
        NPCs.SHEEP_43,
        NPCs.GOLDEN_SHEEP_1271,
        NPCs.GOLDEN_SHEEP_1272,
        NPCs.SHEEP_1529,
        NPCs.SHEEP_1762,
        NPCs.SHEEP_1763,
        NPCs.SHEEP_1764,
        NPCs.SHEEP_1765,
        NPCs.SICK_LOOKING_SHEEP_1_2377,
        NPCs.SICK_LOOKING_SHEEP_2_2378,
        NPCs.SICK_LOOKING_SHEEP_3_2379,
        NPCs.SICK_LOOKING_SHEEP_4_2380,
        NPCs.SHEEP_3310,
        NPCs.SHEEP_3311,
        NPCs.SHEEP_3579,
        NPCs.SHEEP_5148,
        NPCs.SHEEP_5149,
        NPCs.SHEEP_5150,
        NPCs.SHEEP_5151,
        NPCs.SHEEP_5152,
        NPCs.SHEEP_5153,
        NPCs.SHEEP_5154,
        NPCs.SHEEP_5155,
        NPCs.SHEEP_5156,
        NPCs.SHEEP_5157,
        NPCs.SHEEP_5158,
        NPCs.SHEEP_5159,
        NPCs.SHEEP_5160,
        NPCs.SHEEP_5161,
        NPCs.SHEEP_5162,
        NPCs.SHEEP_5163,
        NPCs.SHEEP_5164,
        NPCs.SHEEP_5165,
        NPCs.GOLDEN_SHEEP_5172,
        NPCs.GOLDEN_SHEEP_5173
)

class SheepBehavior : NPCBehavior(*sheepIds), InteractionListener {
    override fun tick(self: NPC): Boolean {
        if (self.properties.combatPulse.isAttacking || DeathTask.isDead(self)) {
            return true
        }
        if (RandomFunction.random(35) == 5) {
            sendChat(self, "Baa!")
        }

        return true
    }

    override fun defineListeners() {
        on(IntType.NPC, "shear") { player, node ->
            val sheep = node as NPC
            sheep.faceTemporary(player, 1)
            if (sheep.id == NPCs.SHEEP_3579) {
                if (player.questRepository.getQuest("Sheep Shearer").isStarted(player)) {
                    setAttribute(player, ATTR_IS_PENGUIN_SHEEP_SHEARED, true)
                }
                animate(player, Animation(893))
                playAudio(player, Sounds.PENGUINSHEEP_ESCAPE_686)
                animate(sheep, Animation(3570))

                sheepBackAway(player, sheep, "The... whatever it is... manages to get away from you!")
                return@on true
            }
            if (!inInventory(player, Items.SHEARS_1735)) {
                sendMessage(player, "You need shears to shear a sheep.")
                return@on true
            }
            if (hasOption(sheep, "attack")) {
                sendMessage(player, "That one looks a little too violent to shear...")
                return@on true
            }
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough space in your inventory to carry any wool you would shear.")
                return@on true
            }
            lock(sheep, 3)
            stopWalk(sheep)
            animate(player, Animation(893))
            val random = RandomFunction.random(1, 5)
            if (random != 4) {
                sheep.locks.lockMovement(2)
                sheep.transform(NPCs.SHEEP_5153)
                playAudio(player, Sounds.SHEAR_SHEEP_761)
                sendMessage(player, "You get some wool.")
                addItem(player, Items.WOOL_1737) // 5160
                GameWorld.Pulser.submit(object : Pulse(80, sheep) {
                    override fun pulse(): Boolean {
                        sheep.reTransform()
                        return true
                    }
                })
            } else {
                sheepBackAway(player, sheep, "The sheep manages to get away from you!")
            }
            return@on true
        }
    }

    fun sheepBackAway(player: Player, sheep: NPC, messagePlayer: String)
    {
        val playerLocation = player.getLocation()
        val sheepLocation =  sheep.getLocation()
        val sheepDirection = Direction.getDirection(sheepLocation, playerLocation) // Get direction sheep is facing, from the player's location
        val sheepOppositeDirection = sheepDirection.getOpposite() // Switch to opposite direction

        val xWalkLocation = sheepLocation.getX() + (sheepOppositeDirection.getStepX() * 3) // Gets x location, if set, 3 steps away from player
        val yWalkLocation = sheepLocation.getY() + (sheepOppositeDirection.getStepY() * 3) // Gets y location, if set, 3 steps away from player
        val sheepWalkToLocation = Location(xWalkLocation, yWalkLocation, sheepLocation.getZ()); // New location for pathfinding

        sendMessage(player, messagePlayer)

        unlock(sheep) //Force unlock of entity movement, to allow moving away
        forceWalk(sheep, sheepWalkToLocation, "dumb")
    }
}