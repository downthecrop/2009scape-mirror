package content.region.morytania.quest.hauntedmine

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import org.rs09.consts.NPCs

// transforms the possessed pickaxe back into an iron pickaxe after you kill it
class PossessedPickaxeNPC : NPCBehavior(NPCs.POSSESSED_PICKAXE_1536, NPCs.IRON_PICKAXE_1015) {
    override fun onDeathFinished(self: NPC, killer: Entity) {
        self.transform(NPCs.IRON_PICKAXE_1015)
        super.onDeathFinished(self, killer)
    }
}