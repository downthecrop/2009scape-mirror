package content.region.kandarin.feldip.quest.zogreflesheaters

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.timer.impl.Disease
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/** The zombie you have to fight when you click on the skeleton. */
class ZombieBrentleVahnBehavior : NPCBehavior(NPCs.ZOMBIE_1826) {

    override fun beforeAttackFinalized(self: NPC, victim: Entity, state: BattleState) {
        val disease = getOrStartTimer<Disease>(victim, 10)
        disease.hitsLeft = 10
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)
        // Drops backpack when killed.
        if (killer is Player && getQuestStage(killer, ZogreFleshEaters.questName) in 2..4) {
            drops.add(Item(Items.RUINED_BACKPACK_4810))
            setAttribute(killer, ZogreFleshEaters.attributeFoughtZombie, true)
        }
    }

    var clearTime = 0
    override fun tick(self: NPC): Boolean {
        // You have 400 ticks to kill this guy
        if (clearTime++ > 400) {
            clearTime = 0
            poofClear(self)
        }
        return true
    }
}