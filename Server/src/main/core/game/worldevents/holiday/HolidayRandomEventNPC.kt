package core.game.worldevents.holiday

import core.api.*
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.tools.secondsToTicks
import org.rs09.consts.Sounds
import kotlin.reflect.full.createInstance

abstract class HolidayRandomEventNPC(id:Int) : NPC(id) {
    lateinit var player: Player
    var spawnLocation: Location? = null
    var initialized = false
    var finalized = false
    var timerPaused = false
    var ticksLeft = secondsToTicks(30)

    open fun create(player: Player, type: String = ""): HolidayRandomEventNPC {
        val event = this::class.createInstance()
        event.player = player
        event.spawnLocation = RegionManager.getSpawnLocation(player, this)
        return event
    }

    open fun terminate() {
        pulseManager.clear(PulseType.STANDARD)
        if (initialized && !finalized) {
            poofClear(this)
            playGlobalAudio(this.location, Sounds.SMOKEPUFF2_1931, 100)
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
        if (player.getAttribute<HolidayRandomEventNPC?>("holiday-npc", null) != this) {
            terminate()
            return
        }
        if (!player.getAttribute("holidayrandom:pause", false)) {
            ticksLeft--
        }
        if (!pulseManager.hasPulseRunning() && !finalized) {
            follow()
        }
        if (!player.isActive || !player.location.withinDistance(location, 10)) {
            terminate()
        }
        if (ticksLeft <= 0 && initialized) {
            terminate()
            initialized = false
        }
    }

    override fun init() {
        initialized = true
        finalized = false
        timerPaused = false
        spawnLocation ?: terminate()
        location = spawnLocation
        player.setAttribute("holiday-npc", this)
        super.init()
    }

    override fun clear() {
        super.clear()
        if(player.getAttribute<HolidayRandomEventNPC?>("holiday-npc", null) == this) player.removeAttribute("holiday-npc")
    }

    abstract fun talkTo(npc: NPC)
}