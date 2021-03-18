package rs09.game.content.ame

import core.game.node.entity.player.Player
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld

private const val DELAY_TICKS = 6000 //60 minutes
class RandomEventManager(val player: Player) {
    var event: RandomEventNPC? = null
    var nextSpawn = 0

    fun tick(){
        if(player.isArtificial) return
        if(GameWorld.ticks > nextSpawn) fireEvent()
    }

    fun fireEvent(){
        val ame = RandomEvents.values().random()
        event = ame.npc.create(player,ame.loot)
        event!!.init()
        nextSpawn = GameWorld.ticks + DELAY_TICKS
        SystemLogger.logRE("Fired ${event!!.name} for ${player.username}")
    }

    fun init(){
        if(player.isArtificial) return
        nextSpawn = GameWorld.ticks + DELAY_TICKS
        SystemLogger.logRE("Initialized REManager for ${player.username}")
    }
}