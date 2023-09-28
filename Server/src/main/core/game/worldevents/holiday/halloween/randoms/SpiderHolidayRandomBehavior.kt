package core.game.worldevents.holiday.halloween.randoms

import core.api.getAttribute
import core.api.playGlobalAudio
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.worldevents.holiday.HolidayRandomEventNPC
import org.rs09.consts.Sounds

class SpiderHolidayRandomBehavior : NPCBehavior() {
    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean): Boolean {
        return false
    }

    override fun onDeathStarted(self: NPC, killer: Entity) {
        getAttribute<HolidayRandomEventNPC?>(self, "holiday-npc", null)?.terminate()
        playGlobalAudio(self.location, Sounds.SMALL_SPIDER_DEATH_3608)
        super.onDeathFinished(self, killer)
    }
}