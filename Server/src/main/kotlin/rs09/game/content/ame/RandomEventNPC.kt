package rs09.game.content.ame

import core.game.interaction.MovementPulse
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.map.path.Pathfinder
import core.game.world.update.flag.context.Graphics
import rs09.game.content.global.WeightBasedTable
import rs09.game.world.GameWorld.Pulser
import rs09.tools.secondsToTicks
import kotlin.random.Random
import kotlin.reflect.full.createInstance

abstract class RandomEventNPC(id: Int) : NPC(id) {
    lateinit var player: Player
    abstract var loot: WeightBasedTable?
    var spawnLocation: Location? = null
    val SMOKE_GRAPHICS = Graphics(86)
    var initialized = false
    var ticksLeft = secondsToTicks(600)

    open fun create(player: Player, loot: WeightBasedTable? = null): RandomEventNPC{
        val event = this::class.createInstance()
        event.loot = loot
        event.player = player
        event.spawnLocation = RegionManager.getSpawnLocation(player,this)
        return event
    }

    open fun terminate(){
        player.antiMacroHandler.event = null
        if(initialized){
            Pulser.submit(object : Pulse(){
                var counter = 0
                override fun pulse(): Boolean {
                    when(counter++){
                        2 -> {
                            isInvisible = true; Graphics.send(SMOKE_GRAPHICS, location)
                        }
                        3 -> clear().also { return true }
                    }
                    return false
                }
            })
        }
    }

    open fun follow(){
        pulseManager.run(object : MovementPulse(this,player, Pathfinder.DUMB){
            override fun pulse(): Boolean {
                face(player)
                7
                return false
            }
        })
    }

    override fun tick() {
        super.tick()
        ticksLeft--
        if(!pulseManager.hasPulseRunning()) follow()
        if(!player.isActive || !player.location.withinDistance(location,10)){
            terminate()
        }
        if(ticksLeft <= 0){
            onTimeUp()
        }
    }

    override fun init() {
        spawnLocation ?: terminate()
        location = spawnLocation
        initialized = true
        super.init()
    }

    open fun onTimeUp(){
        noteAndTeleport()
        terminate()
    }

    fun noteAndTeleport(){
        for(item in player.inventory.toArray()){
            if(item == null) continue
            if(item.noteChange != item.id){
                player.inventory.remove(item)
                player.inventory.add(Item(item.noteChange,item.amount))
            }
        }
        if(Random.nextBoolean()){
            player.properties.teleportLocation = Location.create(3197, 3223, 0)
        } else {
            player.properties.teleportLocation = Location.create(3212, 9620, 0)
        }
        player.graphics(SMOKE_GRAPHICS)
    }

    abstract fun talkTo(npc: NPC)
}