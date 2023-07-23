package content.region.misthalin.varrock.quest.familycrest


import core.api.*
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class JohnathonAntiPosionInteraction: InteractionListener {
    override fun defineListeners() {
        val poisons = intArrayOf(Items.ANTIPOISON4_2446, Items.ANTIPOISON3_175, Items.ANTIPOISON2_177, Items.ANTIPOISON1_179)

        onUseWith(IntType.NPC, poisons, NPCs.JOHNATHON_668){ player, used, with ->
            val npc = with.asNpc()
            val antip = used.asItem()
            val stage = getQuestStage(player, "Family Crest")

            val index = poisons.indexOf(used.id)
            val returnItem = if(index + 1 == poisons.size) Items.VIAL_229 else poisons[index + 1]

            if(stage == 17 && removeItem(player, antip)){
                addItem(player, returnItem)
                setQuestStage(player, "Family Crest", 18)
                openDialogue(player, NPCs.JOHNATHON_668, npc)
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }

            return@onUseWith true
        }
    }
}