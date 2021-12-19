package rs09.game.content.global.worldevents.holiday.halloween

import rs09.game.content.global.worldevents.PluginSet
import rs09.game.content.global.worldevents.WorldEvent
import rs09.game.world.World

class SimpleHalloweenEvent : WorldEvent("hween"){
    override fun checkActive(): Boolean {
        return false
    }

    override fun initialize() {
        plugins = PluginSet(
            GrimDialogue()
        )
        super.initialize()
        World.settings?.message_model = 800
        World.settings?.message_string = "A mysterious figure has appeared in Draynor! You should go investigate!"
        log("Initialized.")
    }
}