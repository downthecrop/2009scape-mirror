package content.global.activity.penguinhns

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class PenguinSpyingHandler : InteractionListener {

    override fun defineListeners() {
        on(PENGUINS, IntType.NPC, "spy-on"){ player, node ->
            val npc = node.asNpc()
            if(PenguinManager.hasTagged(player, npc.location)){
                player.sendMessage("You've already tagged this penguin.")
            } else {
                GameWorld.submit(SpyPulse(player, npc))
                playJingle(player, 345)
            }
            return@on true
        }
    }

    val PENGUINS = intArrayOf(8104,8105,8107,8108,8109,8110)

    class SpyPulse(val player: Player, val npc: NPC) : Pulse() {
        var stage = 0
        val curPoints = player.getAttribute("phns:points",0)

        val ANIMATION = Animation(10355)

        override fun pulse(): Boolean {
            when(stage++){
                0 -> player.lock().also { player.animator.animate(ANIMATION) }
                1 -> player.sendMessage("You manage to spy on the penguin.").also {
                    player.setAttribute("/save:phns:points",curPoints + 1)
                    PenguinManager.registerTag(player, npc.location)
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