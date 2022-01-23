package rs09.game.content.ame

import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld

private const val AVG_DELAY_TICKS = 6000 // 60 minutes
private const val MIN_DELAY_TICKS = AVG_DELAY_TICKS / 2
private const val MAX_DELAY_TICKS = MIN_DELAY_TICKS + AVG_DELAY_TICKS // window of 60 min centered on 60 min (30 to 90 min)
class RandomEventManager(val player: Player) {
    var event: RandomEventNPC? = null
    var nextSpawn = 0

    fun tick() {
        if (player.isArtificial) return
        if (GameWorld.ticks > nextSpawn) fireEvent()
    }

    fun fireEvent() {
        if (player.zoneMonitor.isRestricted(ZoneRestriction.RANDOM_EVENTS)) {
            nextSpawn = GameWorld.ticks + 3000
            return
        }
        val ame = RandomEvents.values().random()
        event = ame.npc.create(player,ame.loot,ame.type)
        if (event!!.spawnLocation == null) {
            nextSpawn = GameWorld.ticks + 3000
            return
        }
        event!!.init()
        rollNextSpawn()
        SystemLogger.logRE("Fired ${event!!.name} for ${player.username}")
    }

    fun init() {
        if (player.isArtificial) return
        rollNextSpawn()
        SystemLogger.logRE("Initialized REManager for ${player.username}")
    }

    private fun rollNextSpawn() {
        nextSpawn = GameWorld.ticks + RandomFunction.random(MIN_DELAY_TICKS, MAX_DELAY_TICKS)
    }
}