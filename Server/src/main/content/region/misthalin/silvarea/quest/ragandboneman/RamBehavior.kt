package content.region.misthalin.silvarea.quest.ragandboneman

import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.npc.drop.DropFrequency
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class RamBehavior : NPCBehavior(*ramIds) {
    companion object {
        private val ramIds = intArrayOf(
                NPCs.RAM_3672,
                NPCs.RAM_3673,
                NPCs.RAM_5168,
                NPCs.RAM_5169,
                NPCs.RAM_5170,
        )
    }

    override fun onDropTableRolled(self: NPC, killer: Entity, drops: ArrayList<Item>) {
        super.onDropTableRolled(self, killer, drops)
        // Drops the Ram Skull during Rag and Bone Man quest
        if (killer is Player && isQuestInProgress(killer, RagAndBoneMan.questName, 1, 99)) {
            if(RandomFunction.roll(4)) {
                drops.add(Item(Items.RAM_SKULL_7818));
            }
        }
    }
}