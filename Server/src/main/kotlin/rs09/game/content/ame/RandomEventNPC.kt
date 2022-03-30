package rs09.game.content.ame

import api.poofClear
import core.game.interaction.MovementPulse
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Graphics
import rs09.game.content.ame.events.MysteriousOldManNPC
import rs09.game.content.global.WeightBasedTable
import rs09.tools.secondsToTicks
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
        finalized = true
        pulseManager.clear("amemovement")
        player.antiMacroHandler.event = null
        if (initialized) {
            poofClear(this)
        }
    }

    open fun follow() {
        pulseManager.run((object : MovementPulse(this, player, Pathfinder.DUMB) {
            override fun pulse(): Boolean {
                face(player)
                return false
            }
        }), "amemovement")
    }

    override fun tick() {
        super.tick()
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
        super.init()
    }

    open fun onTimeUp() {
        noteAndTeleport()
        terminate()
    }

    fun noteAndTeleport() {
        for (item in player.inventory.toArray()) {
            if (item == null) continue
            if (item.noteChange != item.id && item.noteChange != -1) {
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
    }

    abstract fun talkTo(npc: NPC)
}