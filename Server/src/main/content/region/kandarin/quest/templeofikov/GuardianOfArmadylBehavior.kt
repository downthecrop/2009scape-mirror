package content.region.kandarin.quest.templeofikov

import core.api.isQuestComplete
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class GuardianOfArmadylBehavior : NPCBehavior(*guardianOfArmadylIds) {
    companion object {
        private val guardianOfArmadylIds = intArrayOf(
                NPCs.GUARDIAN_OF_ARMADYL_274,
                NPCs.GUARDIAN_OF_ARMADYL_275
        )
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)
        // Drops Pendant of Armadyl after quest complete when killed.
        if (killer is Player && isQuestComplete(killer, TempleOfIkov.questName)) {
            if(RandomFunction.roll(4)) {
                drops.add(Item(Items.ARMADYL_PENDANT_87))
            }
        }
    }
}