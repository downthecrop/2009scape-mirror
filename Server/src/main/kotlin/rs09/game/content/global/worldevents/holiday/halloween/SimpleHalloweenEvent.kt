package rs09.game.content.global.worldevents.holiday.halloween

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import rs09.game.content.global.worldevents.PluginSet
import rs09.game.content.global.worldevents.WorldEvent
import rs09.game.world.GameWorld
import java.util.*

class SimpleHalloweenEvent : WorldEvent("hween"){
    override fun checkActive(): Boolean {
        return false
    }

    override fun initialize() {
        plugins = PluginSet(
            GrimDialogue()
        )
        super.initialize()
        GameWorld.settings?.message_model = 800
        GameWorld.settings?.message_string = "A mysterious figure has appeared in Draynor! You should go investigate!"
        log("Initialized.")
    }
}