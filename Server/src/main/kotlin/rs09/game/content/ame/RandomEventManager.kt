package rs09.game.content.ame

import api.getAttribute
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction
import rs09.game.system.SystemLogger
import rs09.game.world.GameWorld
import kotlin.random.Random

private const val AVG_DELAY_TICKS = 6000 // 60 minutes
private const val MIN_DELAY_TICKS = AVG_DELAY_TICKS / 2
private const val MAX_DELAY_TICKS = MIN_DELAY_TICKS + AVG_DELAY_TICKS // window of 60 min centered on 60 min (30 to 90 min)
class RandomEventManager(val player: Player) {
    var event: RandomEventNPC? = null
    var enabled: Boolean = false
    var nextSpawn = 0
    val skills = arrayOf("WoodcuttingSkillPulse","FishingPulse","MiningSkillPulse","BoneBuryingOptionPlugin")

    fun tick() {
        if (player.isArtificial) return
        if (GameWorld.ticks > nextSpawn && getAttribute(player, "tutorial:complete", false)) fireEvent()
    }

    fun fireEvent() {
        if(!enabled){
            rollNextSpawn()
            return
        }
        if (player.zoneMonitor.isRestricted(ZoneRestriction.RANDOM_EVENTS)) {
            nextSpawn = GameWorld.ticks + 3000
            return
        }
        val currentAction = player.pulseManager.current.toString()
        var ame = RandomEvents.values().random()
        if(currentAction.contains("WoodcuttingSkillPulse") && Random.nextBoolean()){
            ame = RandomEvents.TREE_SPIRIT
        }
        else if(currentAction.contains("FishingPulse") && Random.nextBoolean()){
            ame = RandomEvents.RIVER_TROLL
        }
        else if(currentAction.contains("MiningSkillPulse") && Random.nextBoolean()){
            ame = RandomEvents.ROCK_GOLEM
        }
        else if(currentAction.contains("BoneBuryingOptionPlugin") && Random.nextBoolean()){
            ame = RandomEvents.SHADE
        }
        else {
            ame = RandomEvents.values().random()
            while(ame.type == "skill"){
                ame = RandomEvents.values().random()
            }
        }
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
        enabled = true
    }

    private fun rollNextSpawn() {
        nextSpawn = GameWorld.ticks + RandomFunction.random(MIN_DELAY_TICKS, MAX_DELAY_TICKS)
    }
}