package content.global.ame.events

import core.api.getAttribute
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import org.rs09.consts.NPCs

class HostileRandomEventBehavior : NPCBehavior(
	NPCs.EVIL_CHICKEN_2463, NPCs.EVIL_CHICKEN_2464, NPCs.EVIL_CHICKEN_2465, NPCs.EVIL_CHICKEN_2466, NPCs.EVIL_CHICKEN_2467, NPCs.EVIL_CHICKEN_2468,
    NPCs.RIVER_TROLL_391, NPCs.RIVER_TROLL_392, NPCs.RIVER_TROLL_393, NPCs.RIVER_TROLL_394, NPCs.RIVER_TROLL_395, NPCs.RIVER_TROLL_396,
    NPCs.SHADE_425, NPCs.SHADE_426, NPCs.SHADE_427, NPCs.SHADE_428, NPCs.SHADE_429, NPCs.SHADE_430, NPCs.SHADE_431,
    NPCs.TREE_SPIRIT_438, NPCs.TREE_SPIRIT_439, NPCs.TREE_SPIRIT_440, NPCs.TREE_SPIRIT_441, NPCs.TREE_SPIRIT_442, NPCs.TREE_SPIRIT_443,
    NPCs.ZOMBIE_419, NPCs.ZOMBIE_420, NPCs.ZOMBIE_421, NPCs.ZOMBIE_422, NPCs.ZOMBIE_423, NPCs.ZOMBIE_424
) {
    override fun getXpMultiplier(self: NPC, attacker: Entity): Double {
        val xprate = super.getXpMultiplier(self, attacker)
        return if (getAttribute(self, "spawned-by-ame", false)) xprate / 16.0 else xprate
    }
}