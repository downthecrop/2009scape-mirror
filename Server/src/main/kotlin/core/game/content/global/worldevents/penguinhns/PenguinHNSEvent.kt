package core.game.content.global.worldevents.penguinhns

import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.content.global.worldevents.PluginSet
import core.game.content.global.worldevents.WorldEvent
import core.game.content.global.worldevents.WorldEvents

class PenguinHNSEvent : WorldEvent("penguin-hns"){
    val manager = PenguinManager()
    var lastTrigger: Int = 0
    var tickDelay = if(GameWorld.settings?.isDevMode == true) 100 else 100000

    override fun checkActive(): Boolean {
        return true //this event is always active.
    }

    override fun checkTrigger(): Boolean {
        return GameWorld.ticks - lastTrigger >= tickDelay
    }

    override fun initialize() {
        plugins = PluginSet(
                LarryHandler(),
                NotebookHandler(),
                PenguinSpyingHandler()
        )
        super.initialize()
        GameWorld.Pulser.submit(PenguinRegeneration())
        log("Penguin HNS initialized.")
    }

    override fun fireEvent() {
        log("Reshuffling Penguins...")
        manager.rebuildVars()
        lastTrigger = GameWorld.ticks
        log("Penguins Reshuffled.")
    }

    class PenguinRegeneration : Pulse(25){
        override fun pulse(): Boolean {
            val event = WorldEvents.get("penguin-hns")
            event ?: return true

            if(event.checkTrigger()) event.fireEvent()
            return false
        }
    }

}