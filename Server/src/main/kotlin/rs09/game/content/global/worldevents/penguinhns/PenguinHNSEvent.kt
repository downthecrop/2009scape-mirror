package rs09.game.content.global.worldevents.penguinhns

import core.game.system.task.Pulse
import org.json.simple.JSONObject
import rs09.ServerStore
import rs09.game.content.global.worldevents.PluginSet
import rs09.game.content.global.worldevents.WorldEvent
import rs09.game.content.global.worldevents.WorldEvents
import rs09.game.world.GameWorld

class PenguinHNSEvent : WorldEvent("penguin-hns"){
    val manager = PenguinManager()
    var lastTrigger: Int = 0
    var tickDelay = if(GameWorld.settings?.isDevMode == true) 100 else 100000

    override fun checkActive(): Boolean {
        return true //this event is always active.
    }

    override fun checkTrigger(): Boolean {
        return PenguinManager.penguins.isEmpty()
    }

    override fun initialize() {
        plugins = PluginSet(
                LarryHandler(),
                NotebookHandler(),
        )
        super.initialize()
        fireEvent()
        log("Penguin HNS initialized.")
    }

    override fun fireEvent() {
        log("Loading penguins...")
        manager.rebuildVars()
        lastTrigger = GameWorld.ticks
        log("Penguins loaded.")
    }

    companion object {
        @JvmStatic
        fun getStoreFile() : JSONObject {
            return ServerStore.getArchive("weekly-penguinhns")
        }
    }

}