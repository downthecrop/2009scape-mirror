package core.game.worldevents.holiday.halloween.randoms

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.worldevents.holiday.HolidayRandomEventNPC
import core.tools.RandomFunction
import org.rs09.consts.Sounds

class GhostHolidayRandomNPC() : HolidayRandomEventNPC(2716) {
    override fun init() {
        super.init()
        this.isAggressive = false
        playGlobalAudio(this.location, Sounds.BIGGHOST_APPEAR_1595)
        animate(player, 2836)
        sendChat(this, "WooooOOOooOOo")
        sendMessage(player, "The air suddenly gets colder...")
    }

    override fun tick() {
        super.tick()
        if (RandomFunction.roll(10))
            playGlobalAudio(this.location, Sounds.BIGGHOST_LAUGH_1600)
    }

    override fun talkTo(npc: NPC) {
    }
}