package rs09.game.content.global.worldevents.holiday.halloween

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import rs09.game.content.global.worldevents.PluginSet
import rs09.game.content.global.worldevents.WorldEvent
import rs09.game.world.GameWorld
import java.util.*

class SimpleHalloweenEvent : WorldEvent("hween"){
    override fun checkActive(): Boolean {
        return Calendar.getInstance().get(Calendar.MONTH) == Calendar.OCTOBER
    }

    override fun initialize() {
        plugins = PluginSet(
            CandyRewardPlugin(),
            GrimDialogue()
        )
        val grim = NPC(6390, Location(3247,3198))
        grim.isWalks = false
        grim.walkRadius = 0
        grim.isNeverWalks = true
        grim.init()
        super.initialize()
        GameWorld.settings?.message_model = 800
        GameWorld.settings?.message_string = "A mysterious figure has appeared in lumbridge cemetery! You should go investigate!"
        log("Initialized.")
    }
}