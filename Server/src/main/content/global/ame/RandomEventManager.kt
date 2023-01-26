package content.global.ame

import core.api.Commands
import core.api.LoginListener
import core.game.event.EventHook
import core.game.event.TickEvent
import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction
import core.api.Event
import core.tools.SystemLogger
import core.game.system.command.Privilege
import core.game.world.GameWorld
import core.game.world.repository.Repository
import kotlin.random.Random

class RandomEventManager(val player: Player? = null) : LoginListener, EventHook<TickEvent>, Commands {
    var event: content.global.ame.RandomEventNPC? = null
    var enabled: Boolean = false
    var nextSpawn = 0
    val skills = arrayOf("WoodcuttingSkillPulse","FishingPulse","MiningSkillPulse","BoneBuryingOptionPlugin")

    override fun login(player: Player) {
        if(player.isArtificial) return
        val instance = content.global.ame.RandomEventManager(player)
        player.hook(Event.Tick, instance)
        setAttribute(player, "random-manager", instance)
        instance.rollNextSpawn()
        instance.enabled = true
        SystemLogger.logRE("Initialized REManager for ${player.username}.")
    }

    override fun process(entity: Entity, event: TickEvent) {
        if (GameWorld.ticks > nextSpawn && getAttribute(player!!, "tutorial:complete", false)) fireEvent()
    }

    fun fireEvent() {
        if(!enabled){
            rollNextSpawn()
            return
        }
        if (player!!.zoneMonitor.isRestricted(ZoneRestriction.RANDOM_EVENTS)) {
            nextSpawn = GameWorld.ticks + 3000
            return
        }
        if (getAttribute<content.global.ame.RandomEventNPC?>(player, "re-npc", null) != null) return
        val currentAction = player.pulseManager.current?.toString() ?: "None"
        val ame: content.global.ame.RandomEvents = if(currentAction.contains("WoodcuttingSkillPulse") && Random.nextBoolean()){
            content.global.ame.RandomEvents.TREE_SPIRIT
        } else if(currentAction.contains("FishingPulse") && Random.nextBoolean()){
            content.global.ame.RandomEvents.RIVER_TROLL
        } else if(currentAction.contains("MiningSkillPulse") && Random.nextBoolean()){
            content.global.ame.RandomEvents.ROCK_GOLEM
        } else if(currentAction.contains("BoneBuryingOptionPlugin") && Random.nextBoolean()){
            content.global.ame.RandomEvents.SHADE
        } else {
            content.global.ame.RandomEvents.values().filter { it.type != "skill" }.random()
        }
        event = ame.npc.create(player,ame.loot,ame.type)
        if (event!!.spawnLocation == null) {
            nextSpawn = GameWorld.ticks + 3000
            SystemLogger.logWarn(this::class.java, "Tried to spawn random event for ${player.username} but spawn location was null!")
            return
        }
        event!!.init()
        rollNextSpawn()
        SystemLogger.logRE("Fired ${event!!.name} for ${player.username}")
    }

    private fun rollNextSpawn() {
        nextSpawn = GameWorld.ticks + RandomFunction.random(content.global.ame.RandomEventManager.Companion.MIN_DELAY_TICKS, content.global.ame.RandomEventManager.Companion.MAX_DELAY_TICKS)
    }

    override fun defineCommands() {
        define("targeted-ame", Privilege.ADMIN, "targeted-ame username", "Summons a random for the given user") {player, args ->
            val username = args[1]
            val target = Repository.getPlayerByName(username)

            if (target == null)
                reject(player, "Unable to find player $username!")

            content.global.ame.RandomEventManager.Companion.getInstance(target!!)?.fireEvent()
        }
    }

    companion object {
        const val AVG_DELAY_TICKS = 6000 // 60 minutes
        const val MIN_DELAY_TICKS = content.global.ame.RandomEventManager.Companion.AVG_DELAY_TICKS / 2
        const val MAX_DELAY_TICKS = content.global.ame.RandomEventManager.Companion.MIN_DELAY_TICKS + content.global.ame.RandomEventManager.Companion.AVG_DELAY_TICKS // window of 60 min centered on 60 min (30 to 90 min)

        @JvmStatic fun getInstance(player: Player): content.global.ame.RandomEventManager?
        {
            return getAttribute(player, "random-manager", null)
        }
    }
}