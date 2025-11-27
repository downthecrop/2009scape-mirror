package content.region.kandarin.quest.observatoryquest

import core.api.getAttribute
import core.api.setAttribute
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.GameWorld
import org.rs09.consts.NPCs

class PoisonSpiderBehavior : NPCBehavior(NPCs.POISON_SPIDER_1009) {
    override fun onCreation(self: NPC) {
        setAttribute(self, "despawn-time", GameWorld.ticks + 100)
    }

    override fun tick(self: NPC): Boolean {
        if (getAttribute(self, "despawn-time", 0) <= GameWorld.ticks && !self.inCombat())
            self.clear()
        return true
    }
}
