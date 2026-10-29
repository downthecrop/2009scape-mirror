package content.global.activity.penguinhns

import content.data.Quests
import content.global.bots.State
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.game.interaction.MovementPulse
import core.game.node.Node
import core.game.system.command.sets.QuestCommandSet
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import org.rs09.consts.NPCs
import kotlin.math.abs
import kotlin.math.min

class PenguinSpyingHandler : InteractionListener {

    override fun defineListeners() {
        on(PENGUINS, IntType.NPC, "spy-on") { player, node ->
            val npc = node.asNpc()
            if(PenguinManager.hasTagged(player, npc.properties.spawnLocation)) {
                player.sendMessage("You've already tagged this penguin.")
            } else {
                Pulser.submit(SpyPulse(player, npc))
                playJingle(player, 345)
            }
            return@on true
        }

        setDest(IntType.NPC, PENGUINS, "spy-on") { player, node ->
            if(player.location.withinDistance(node.location, 7)) {
                return@setDest player.location
            }
            return@setDest node.location
        }
    }

    val PENGUINS = intArrayOf(NPCs.PUMPKIN_8077, NPCs.BARREL_8104, NPCs.BUSH_8105, NPCs.CACTUS_8107, NPCs.CRATE_8108, NPCs.ROCK_8109, NPCs.TOADSTOOL_8110)

    class SpyPulse(val player: Player, val npc: NPC) : Pulse() {
        var stage = 0
        val curPoints = player.getAttribute("phns:points", 0)
        val penguin = Penguin.forLocation(npc.properties.spawnLocation)

        val pengPoints = if (isQuestComplete(player, Quests.COLD_WAR)) {
            penguin?.points ?: 1
        } else {
            1
        }

        val newTotal = if(curPoints > 50) {
            curPoints
        } else {
            min(50, curPoints + pengPoints)
        }

        val ANIMATION = Animation(10355)

        override fun pulse(): Boolean {
            when(stage++){
                0 -> player.lock().also { player.animator.animate(ANIMATION) }
                1 -> player.sendMessage("You manage to spy on the penguin.").also {
                    setAttribute(player, "/save:phns:points", newTotal)
                    PenguinManager.registerTag(player, npc.properties.spawnLocation)
                    player.unlock()
                }
                2 -> return true
            }
            return false
        }

        override fun stop() {
            super.stop()
            player.unlock()
        }
    }
}