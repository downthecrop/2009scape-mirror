package content.region.desert.quest.deserttreasure

import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import org.rs09.consts.NPCs

class ScarabNPC : NPCBehavior(NPCs.SCARABS_1969) {
    var clearTime = 0

    override fun tick(self: NPC): Boolean {
        if (clearTime++ > 100) {
            clearTime = 0
            poofClear(self)
        }
        return true
    }
}