package content.global.ame

import core.api.*
import core.game.event.EventHook
import core.game.event.TickEvent
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction
import core.tools.SystemLogger
import core.game.system.command.Privilege
import core.game.world.GameWorld
import core.game.world.repository.Repository
import core.tools.Log
import kotlin.random.Random
import content.global.ame.RandomEvents

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
            rollNextSpawn()
            return
        }
        if (getAttribute<content.global.ame.RandomEventNPC?>(player, "re-npc", null) != null) return
        val currentAction = player.pulseManager.current?.toString() ?: "None"
        
        //Give us a decent pool of events to pick from randomly, with the skill-based random event mixed in if applicable.
        //This is just to provide a bit more randomization (you don't want to get the skill-based randoms EVERY TIME when training woodcutting, for example.)
        val ame = listOf (
            RandomEvents.getSkillBasedRandomEvent (player.skills.lastTrainedSkill),
            RandomEvents.getNonSkillRandom(),
            RandomEvents.getNonSkillRandom()
        ).filter{ it != null }.random() ?: RandomEvents.getNonSkillRandom() //Absolute cosmic bit-flip tier safety fallback

        event = ame.npc.create(player,ame.loot,ame.type)
        if (event!!.spawnLocation == null) {
            nextSpawn = GameWorld.ticks + 3000
            log(this::class.java, Log.WARN,  "Tried to spawn random event for ${player.username} but spawn location was null!")
            return
        }
        event!!.init()
        rollNextSpawn()
        log(this::class.java, Log.FINE, "Fired ${event!!.name} for ${player.username}")
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
