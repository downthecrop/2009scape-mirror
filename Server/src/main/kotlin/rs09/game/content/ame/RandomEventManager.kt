package rs09.game.content.ame

import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneRestriction
import rs09.game.system.SystemLogger
import rs09.game.world.World

private const val DELAY_TICKS = 6000 //60 minutes
class RandomEventManager(val player: Player) {
    var event: RandomEventNPC? = null
    var nextSpawn = 0

    fun tick(){
        if(player.isArtificial) return
        if(World.ticks > nextSpawn) fireEvent()
    }

    fun fireEvent(){
        if(player.zoneMonitor.isRestricted(ZoneRestriction.RANDOM_EVENTS)){
            nextSpawn = World.ticks + 3000
            return
        }
        val ame = RandomEvents.values().random()
        event = ame.npc.create(player,ame.loot,ame.type)
        if(event!!.spawnLocation == null){
            nextSpawn = World.ticks + 3000
            return
        }
        event!!.init()
        nextSpawn = World.ticks + DELAY_TICKS
        SystemLogger.logRE("Fired ${event!!.name} for ${player.username}")
    }

    fun init(){
        if(player.isArtificial) return
        nextSpawn = World.ticks + DELAY_TICKS
        SystemLogger.logRE("Initialized REManager for ${player.username}")
    }
}