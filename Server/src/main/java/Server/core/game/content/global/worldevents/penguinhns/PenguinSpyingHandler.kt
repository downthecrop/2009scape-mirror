package core.game.content.global.worldevents.penguinhns

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.interaction.Option
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import core.game.content.quest.PluginInteraction
import core.game.content.quest.PluginInteractionManager

class PenguinSpyingHandler : PluginInteraction(8107,8108,8104,8105,8109,8110){

    class SpyPulse(val player: Player) : Pulse() {
        var stage = 0
        val curPoints = player.getAttribute("phns:points",0)
        val weeklyPoints = player.getAttribute("phns:weekly",0)
        val WEEKLY_CAP = 10

        val ANIMATION = Animation(10355)

        override fun pulse(): Boolean {
            when(stage++){
                0 -> player.lock().also { player.animator.animate(ANIMATION) }
                1 -> player.sendMessage("You manage to spy on the penguin.").also { player.setAttribute("/save:phns:points",curPoints + 1);player.setAttribute("/save:phns:weekly",weeklyPoints + 1);player.unlock();}
                2 -> if(weeklyPoints + 1 >= WEEKLY_CAP) player.setAttribute("/save:phns:date", ((System.currentTimeMillis() * 0.001) + 604800).toLong())
                3 -> return true
            }
            return false
        }
    }

    override fun handle(player: Player?, npc: NPC?, option: Option?): Boolean {
        class movePulse : MovementPulse(player,DestinationFlag.ENTITY.getDestination(player,npc)){
            override fun pulse(): Boolean {
                player.let { player?.face(npc);GameWorld.submit(SpyPulse(player!!)) }
                return true
            }
        }
        if(option?.name?.toLowerCase()?.equals("spy-on")!!){
            val currentDate = System.currentTimeMillis() * 0.001
            val playerDate: Long = player?.getAttribute("phns:date",0L)!!
            if(currentDate < playerDate){
                player.sendMessage("You have already earned your maximum number of points this week.").also { return true }
            } else if(playerDate != 0L){
                player.removeAttribute("phns:date")
                player.removeAttribute("phns:weekly")
            }
            if(PenguinManager.tagMapping[npc?.location]?.contains(player.username)!!){
                player.sendMessage("You've already tagged this penguin.")
            } else {
                GameWorld.submit(movePulse())
                PenguinManager.tagMapping[npc?.location]?.add(player.username)
            }
            return true
        }
        return false
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any {
        return Unit
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        PluginInteractionManager.register(this,PluginInteractionManager.InteractionType.NPC)
        return this
    }

}