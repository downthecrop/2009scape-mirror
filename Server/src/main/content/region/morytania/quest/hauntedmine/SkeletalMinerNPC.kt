package content.region.morytania.quest.hauntedmine

import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.world.map.RegionManager.getLocalPlayers
import core.tools.RandomFunction
import org.rs09.consts.NPCs

class SkeletalMinerNPC : NPCBehavior(NPCs.CORPSE_1538) {

    override fun tick(self: NPC): Boolean {
        // chance to transform and attack a player who gets close
        for (p in getLocalPlayers(self.location)) {
            if (p.location.withinDistance(self.location, 2) && !p.inCombat() && RandomFunction.roll(10)) {
                self.transform(NPCs.SKELETAL_MINER_1539) // I use self.transform because transformNPC can't be permanent.
                self.attack(p)
            }
        }
        return super.tick(self)
    }

    override fun onDeathFinished(self: NPC, killer: Entity) {
        self.transform(NPCs.CORPSE_1538)
        super.onDeathFinished(self, killer)
    }
}