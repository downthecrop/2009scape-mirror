package content.global.ame

import content.global.ame.events.surpriseexam.MysteriousOldManNPC
import core.api.playGlobalAudio
import core.api.poofClear
import core.api.sendMessage
import core.api.utils.WeightBasedTable
import core.game.interaction.MovementPulse
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Graphics
import core.integrations.discord.Discord
import core.tools.secondsToTicks
import core.tools.ticksToCycles
import org.rs09.consts.Sounds
import kotlin.math.ceil
import kotlin.math.min
import kotlin.random.Random
import kotlin.reflect.full.createInstance

abstract class RandomEventNPC(id: Int) : NPC(id) {
    lateinit var player: Player
    abstract var loot: WeightBasedTable?
    var spawnLocation: Location? = null
    val SMOKE_GRAPHICS = Graphics(86)
    var initialized = false
    var finalized = false
    var timerPaused = false
    var ticksLeft = secondsToTicks(180)

    open fun create(player: Player, loot: WeightBasedTable? = null, type: String = ""): RandomEventNPC {
        val event = this::class.createInstance()
        if (event is MysteriousOldManNPC) event.type = type
        event.loot = loot
        event.player = player
        event.spawnLocation = RegionManager.getSpawnLocation(player, this)
        return event
    }

    open fun terminate() {
        pulseManager.clear(PulseType.STANDARD)
        if (initialized && !finalized) {
            poofClear(this)
            playGlobalAudio(this.location, Sounds.SMOKEPUFF_1930, ticksToCycles(1))
        }
        finalized = true
    }

    open fun follow() {
        pulseManager.run((object : MovementPulse(this, player, Pathfinder.DUMB) {
            override fun pulse(): Boolean {
                face(player)
                return false
            }
        }), PulseType.STANDARD)
    }

    override fun tick() {
        super.tick()
        if(player.getAttribute<RandomEventNPC?>("re-npc", null) != this){
            terminate()
            return
        }
        if (!player.getAttribute("random:pause", false)) {
            ticksLeft--
        }
        if (!pulseManager.hasPulseRunning() && !finalized) {
            follow()
        }
        if (!player.isActive || !player.location.withinDistance(location, 10)) {
            terminate()
        }
        if (ticksLeft <= 0 && initialized) {
            onTimeUp()
            initialized = false
        }
    }

    override fun init() {
        initialized = true
        finalized = false
        timerPaused = false
        spawnLocation ?: terminate()
        location = spawnLocation
        player.setAttribute("re-npc", this)
        super.init()
        super.aggressiveHandler = AggressiveHandler(this, object : AggressiveBehavior() {
            override fun canSelectTarget(entity: Entity, target: Entity): Boolean {
                return target == player
            }
        })
    }

    open fun onTimeUp() {
        noteAndTeleport()
        terminate()
    }

    fun noteAndTeleport() {
        player.pulseManager.clear()
        for (item in player.inventory.toArray()) {
            if (item == null) continue
            if (item.definition.isUnnoted) {
                player.inventory.remove(item)
                player.inventory.add(Item(item.noteChange, item.amount))
            }
        }
        if (Random.nextBoolean()) {
            player.properties.teleportLocation = Location.create(3197, 3223, 0)
        } else {
            player.properties.teleportLocation = Location.create(3212, 9620, 0)
        }
        player.graphics(SMOKE_GRAPHICS)
        Discord.postPlayerAlert(player.username, "Ignored Random")
    }

    override fun clear() {
        super.clear()
        if(player.getAttribute<RandomEventNPC?>("re-npc", null) == this) player.removeAttribute("re-npc")
    }

    abstract fun talkTo(npc: NPC)

    override fun isAttackable(entity: Entity, style: CombatStyle, message: Boolean): Boolean {
        if (entity != player) {
            if (entity is Player) {
                sendMessage(entity, "It isn't interested in fighting you.") //TODO authentic message
            }
            return false
        }
        return super.isAttackable(entity, style, message)
    }

    fun idForCombatLevel(ids: List<Int>, player: Player): Int {
        val index = min(ids.size, ceil(player.properties.currentCombatLevel / 20.0).toInt()) - 1
        return ids[index]
    }
}
