package core.game.worldevents.holiday.christmas.randoms

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs09.consts.NPCs

class SantaHolidayRandomNPC() : HolidayRandomEventNPC(NPCs.SANTA_CLAUS_8540) {

    override fun init() {
        spawnLocation = getPathableRandomLocalCoordinate(this, 1, player.location)
        super.init()
        ticksLeft = minutesToTicks(2)
        setAttribute(this, "playerisnice", !RandomFunction.roll(3))
        sendChat(this, "Ho Ho Ho! Merry Christmas, ${player.username.capitalize()}!", 2)
    }

    override fun tick() {
        if (RandomFunction.roll(10))
            sendChat(this, "Ho Ho Ho! Merry Christmas, ${player.username.capitalize()}!")

        super.tick()
    }

    override fun talkTo(npc: NPC) {
        face(player)
        openDialogue(player, SantaHolidayRandomDialogue(), npc)
    }
}