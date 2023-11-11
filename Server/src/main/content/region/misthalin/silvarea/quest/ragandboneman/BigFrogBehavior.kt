package content.region.misthalin.silvarea.quest.ragandboneman

import core.api.isQuestInProgress
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class BigFrogBehavior : NPCBehavior(*bigFrogIds) {
    companion object {
        private val bigFrogIds = intArrayOf(
                NPCs.BIG_FROG_1829,
        )
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)
        // Drops the Big Frog Leg during Rag and Bone Man quest
        if (killer is Player && isQuestInProgress(killer, RagAndBoneMan.questName, 1, 99)) {
            if(RandomFunction.roll(4)) {
                drops.add(Item(Items.BIG_FROG_LEG_7908));
            }
        }
    }
}