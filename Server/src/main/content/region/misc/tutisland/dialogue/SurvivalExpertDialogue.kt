package content.region.misc.tutisland.dialogue

import core.api.addItemOrDrop
import core.api.inInventory
import core.api.setAttribute
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import content.region.misc.tutisland.handlers.TutorialStage
import content.region.misc.tutisland.handlers.sendStageDialog
import core.api.getAttribute
import core.api.inEquipment
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item

class SurvivalExpertDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.SURVIVAL_EXPERT_943, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, SurvivalExpertDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class SurvivalExpertDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.SURVIVAL_EXPERT_943)

        exec { player, _ ->
            when (val stage = getAttribute(player, "/save:tutorial:stage", 0)) {
                4 -> loadLabel(player, "hello")
                5, 6, 7, 8, 9, 10, 12, 13, 14 -> {
                    if (!inInventory(player, Items.BRONZE_AXE_1351) && !inEquipment(player, Items.BRONZE_AXE_1351)) {
                        loadLabel(player, "spare axe")
                    }
                    if (!inInventory(player, Items.TINDERBOX_590)) {
                        loadLabel(player, "spare tinderbox")
                    }
                    if (stage >= 11 && !inInventory(player, Items.SMALL_FISHING_NET_303)) {
                        loadLabel(player, "spare net")
                    }
                    loadLabel(player, "nowhere")
                }
                11 -> loadLabel(player, "fishing")
                else -> loadLabel(player, "nowhere")
            }
        }

        label("hello")
        npc(ChatAnim.FRIENDLY, "Hello there, newcomer. My name is Brynna. My job is to teach you a few survival tips and tricks. First off we're going to start with the most basic survival skill of all: making a fire.", unclosable = true)
        exec { player, _ ->
            addItemOrDrop(player, Items.TINDERBOX_590)
            addItemOrDrop(player, Items.BRONZE_AXE_1351)
        }
        item(Item(Items.TINDERBOX_590), Item(Items.BRONZE_AXE_1351), "The Survival Guide gives you a <col=08088A>tinderbox</col> and a <col=08088A>bronze</col>", "<col=08088A>axe</col>!", unclosable = true)
        exec { player, _ ->
            setAttribute(player, "tutorial:stage", 5)
            TutorialStage.load(player, 5)
        }
        goto("nowhere")

        label("fishing")
        npc(ChatAnim.FRIENDLY, "Well done! Next we need to get some food in our bellies. We'll need something to cook. There are shrimp in the pond there, so let's catch and cook some.", unclosable = true)
        exec { player, _ -> addItemOrDrop(player, Items.SMALL_FISHING_NET_303) }
        item(Item(Items.SMALL_FISHING_NET_303), "The Survival Guide gives you a", "<col=08088A>net</col>!", unclosable = true)
        exec { player, _ ->
            setAttribute(player, "tutorial:stage", 12)
            TutorialStage.load(player, 12)
        }
        goto("nowhere")

        label("spare axe")
        exec { player, _ -> addItemOrDrop(player, Items.BRONZE_AXE_1351) }
        item(Item(Items.BRONZE_AXE_1351), "The Survival Guide gives you a spare bronze axe.", unclosable = true)
        exec { player, _ ->
            if (!inInventory(player, Items.TINDERBOX_590)) {
                loadLabel(player, "spare tinderbox")
            }
            val stage = getAttribute(player, "/save:tutorial:stage", 0)
            if (stage >= 11 && !inInventory(player, Items.SMALL_FISHING_NET_303)) {
                loadLabel(player, "spare net")
            }
        }
        goto("nowhere") //closes the dialogue, letting the hook reopen the tutorial stage dialog as appropriate

        label("spare tinderbox")
        exec { player, _ -> addItemOrDrop(player, Items.TINDERBOX_590) }
        item(Item(Items.TINDERBOX_590), "The Survival Guide gives you a spare tinderbox.", unclosable = true)
        exec { player, _ ->
            val stage = getAttribute(player, "/save:tutorial:stage", 0)
            if (stage >= 11 && !inInventory(player, Items.SMALL_FISHING_NET_303)) {
                loadLabel(player, "spare net")
            }
        }
        goto("nowhere")

        label("spare net")
        exec { player, _ -> addItemOrDrop(player, Items.SMALL_FISHING_NET_303) }
        item(Item(Items.SMALL_FISHING_NET_303), "The Survival Guide gives you a spare net.", unclosable = true)
        goto("nowhere")

        label("nowhere")
        exec { player, _ -> sendStageDialog(player) }
    }
}
