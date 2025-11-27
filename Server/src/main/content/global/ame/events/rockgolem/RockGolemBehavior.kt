package content.global.ame.events.rockgolem

import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import org.rs09.consts.NPCs

class RockGolemBehavior() : NPCBehavior(
    NPCs.ROCK_GOLEM_413, NPCs.ROCK_GOLEM_414, NPCs.ROCK_GOLEM_415, NPCs.ROCK_GOLEM_416, NPCs.ROCK_GOLEM_417, NPCs.ROCK_GOLEM_418
) {
    val rangeHandler = SwitchAttack(CombatStyle.RANGE)
    val meleeHandler = SwitchAttack(CombatStyle.MELEE)
    val combatHandler = MultiSwingHandler(rangeHandler, meleeHandler)
    override fun getSwingHandlerOverride(self: NPC, original: CombatSwingHandler): CombatSwingHandler {
        return combatHandler
    }

    override fun getXpMultiplier(self: NPC, attacker: Entity): Double {
        return super.getXpMultiplier(self, attacker) / 16.0
    }
}