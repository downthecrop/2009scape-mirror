package core.game.content.ame.events

import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import core.game.content.ame.AntiMacroEvent
import core.game.content.ame.AntiMacroHandler
import java.nio.ByteBuffer

@Initializable
class SandwichLadyEvent : AntiMacroEvent("Sandwich Lady",false,false, 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23){
    override fun parse(buffer: ByteBuffer?) {
    }

    override fun start(player: Player?, login: Boolean, vararg args: Any?): Boolean {
        super.init(player)
        val location = Location.getRandomLocation(player?.location,6,true)
        val npc = SandwichLadyNPC(location,player)
        npc.init()
        if(location == player?.location)
            npc.moveStep()
        return true
    }

    override fun save(buffer: ByteBuffer?) {
    }

    override fun configure() {
    }

    override fun create(player: Player?): AntiMacroEvent {
        val event = SandwichLadyEvent()
        event.player = player
        return event
    }

    override fun getSpawnLocation(): Location {
        return Location(0,0,0)
    }

    override fun newInstance(player: Player?): AntiMacroEvent {
        AntiMacroHandler.register(this)
        return super.newInstance(player)
    }

    class SandwichLadyNPC(loc: Location, var player: Player?) : AbstractNPC(3117,loc){
        val QUOTES = arrayListOf<String>("Hello ${player?.username}, are you there?","Sandwiches, ${player?.username}!","Are you ignoring me?","I have sandwiches for you.","${player?.username}? Can you hear me?")
        override fun init() {
            super.init()
        }

        override fun handleTickActions() {
            if(player == null || player?.isActive == false || player?.location?.withinDistance(this.location) == false){
                onRegionInactivity()
            }
            if(RandomFunction.random(20) == 3){
                sendChat(QUOTES.random())
            }
            class followPulse(val p: Player?) : MovementPulse(this,p,DestinationFlag.ENTITY){
                override fun pulse(): Boolean {
                    face(p)
                    return true
                }
            }
            if(!pulseManager.hasPulseRunning()) {
                pulseManager.run(followPulse(player))
            }
            super.handleTickActions()
        }

        override fun getIds(): IntArray {
            return intArrayOf(3117)
        }

        override fun onRegionInactivity() {
            super.onRegionInactivity()
            clear()
        }

        override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
            return SandwichLadyNPC(location!!,player)
        }

    }
}