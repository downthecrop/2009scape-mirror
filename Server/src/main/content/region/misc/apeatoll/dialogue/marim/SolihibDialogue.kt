package content.region.misc.apeatoll.dialogue.marim

import content.data.Quests
import core.api.hasRequirement
import core.api.openNpcShop
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs

class SolihibDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.SOLIHIB_1433, IntType.NPC, "talk-to") { player, node ->
            if (!hasRequirement(player, Quests.MONKEY_MADNESS)) return@on true
            DialogueLabeller.open(player, SolihibDialogueLabellerFile(), node as NPC)
            return@on true
        }
        on(NPCs.SOLIHIB_1433, IntType.NPC, "trade") { player, _ ->
            if (!hasRequirement(player, Quests.MONKEY_MADNESS)) return@on true
            openNpcShop(player, NPCs.SOLIHIB_1433)
            return@on true
        }
    }

    class SolihibDialogueLabellerFile : DialogueLabeller() {
        override fun addConversation() {
            assignToIds(NPCs.SOLIHIB_1433)

            npc(ChatAnim.FRIENDLY, "Would you like to buy or sell some food?")
            options(
                DialogueOption("trade", "Yes, please."),
                DialogueOption("nowhere", "No, thanks.")
            )
            label("trade")
            exec { player, _ -> openNpcShop(player, NPCs.SOLIHIB_1433) }
            goto("nowhere")
        }
    }
}