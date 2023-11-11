package core.game.worldevents.holiday.christmas.randoms

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs09.consts.NPCs

class CookHolidayRandomNPC : HolidayRandomEventNPC(NPCs.COOK_4239) {
    private val cookLines = listOf("@name, are you there?", "Would you like a fresh baked cake, @name?", "Happy Holidays, @name!")
    private var hasTalkedWith = false

    override fun init() {
        ticksLeft = minutesToTicks(3)
        sendChat(this, "Happy Holidays, ${player.username.capitalize()}!", 2)
        super.init()
    }

    override fun tick() {
        if (RandomFunction.roll(15) && !hasTalkedWith)
            sendChat(this, cookLines.random().replace("@name",player.username.capitalize()))

        super.tick()
    }

    override fun talkTo(npc: NPC) {
        face(player)
        hasTalkedWith = true
        openDialogue(player, CookHolidayRandomDialogue(), npc)
    }
}