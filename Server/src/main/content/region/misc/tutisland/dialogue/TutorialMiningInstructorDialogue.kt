package content.region.misc.tutisland.dialogue

import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import content.region.misc.tutisland.handlers.TutorialStage
import content.region.misc.tutisland.handlers.sendStageDialog
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item

class MiningInstructorDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MINING_INSTRUCTOR_948, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, MiningInstructorDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class MiningInstructorDialogueFile : DialogueLabeller() {
    fun lostPickaxe(player: Player): Boolean {
        return !inInventory(player, Items.BRONZE_PICKAXE_1265) && !inEquipment(player, Items.BRONZE_PICKAXE_1265)
    }

    override fun addConversation() {
        assignToIds(NPCs.MINING_INSTRUCTOR_948)

        exec { player, _ ->
            when (getAttribute(player, "tutorial:stage", 0)) {
                30 -> loadLabel(player, "hello")
                34 -> loadLabel(player, "prospected")
                35, 36, 37, 38, 39 -> loadLabel(player, if (lostPickaxe(player)) "lost pickaxe" else "nowhere")
                40 -> loadLabel(player, "make wep")
                41, 42, 43, 44, 45, 46 -> loadLabel(player, if (lostPickaxe(player)) "lost pickaxe" else if (!inInventory(player, Items.HAMMER_2347)) "lost hammer" else "nowhere")
                else -> loadLabel(player, "nowhere")
            }
        }

        label("hello")
        npc(ChatAnim.FRIENDLY, "Hi there. You must be new around here. So what do I call you? 'Newcomer' seems so impersonal, and if we're going to be working together, I'd rather tell you by name.", unclosable = true)
        player(ChatAnim.FRIENDLY, "You can call me ${player?.username}.", unclosable = true)
        npc(ChatAnim.FRIENDLY, "Ok then, ${player?.username}. My name is Dezzick and I'm a miner by trade. Let's prospect some of these rocks.", unclosable = true)
        exec { player, _ ->
            setAttribute(player, "tutorial:stage", 31)
            TutorialStage.load(player, 31)
        }

        label("prospected")
        player(ChatAnim.FRIENDLY, "I prospected both types of rock! One set contains tin and the other has copper ore inside.", unclosable = true)
        npc(ChatAnim.FRIENDLY, "Absolutely right, ${player?.username}. These two ore types can be smelted together to make bronze.", unclosable = true)
        npc(ChatAnim.FRIENDLY, "So now you know what ore is in the rocks over there, why don't you have a go at mining some tin and copper? Here, you'll need this to start with.", unclosable = true)
        exec { player, _ ->
            addItem(player, Items.BRONZE_PICKAXE_1265)
            setAttribute(player, "tutorial:stage", 35)
            TutorialStage.load(player, 35)
        }
        item(Item(Items.BRONZE_PICKAXE_1265), "Dezzick gives you a bronze pickaxe!", unclosable = true)
        goto("nowhere")

        label("make wep")
        player(ChatAnim.ASKING, "How do I make a weapon out of this?", unclosable = true)
        npc(ChatAnim.FRIENDLY, "Okay, I'll show you how to make a dagger out of it. You'll be needing this.", unclosable = true)
        exec { player, _ ->
            addItem(player, Items.HAMMER_2347)
            setAttribute(player, "tutorial:stage", 41)
            TutorialStage.load(player, 41)
        }
        item(Item(Items.HAMMER_2347), "Dezzick gives you a hammer!", unclosable = true)
        goto("nowhere")

        label("lost pickaxe")
        exec { player, _ -> addItem(player, Items.BRONZE_PICKAXE_1265) }
        item(Item(Items.BRONZE_PICKAXE_1265), "Dezzick gives you a spare pickaxe.", unclosable = true)
        exec { player, _ ->
            val stage = getAttribute(player, "/save:tutorial:stage", 0)
            if (stage >= 41 && !inInventory(player, Items.HAMMER_2347)) {
                loadLabel(player, "lost hammer")
            }
        }
        goto("nowhere")

        label("lost hammer")
        exec { player, _ -> addItem(player, Items.HAMMER_2347) }
        item(Item(Items.HAMMER_2347), "Dezzick gives you a spare hammer.", unclosable = true)
        goto("nowhere")

        label("nowhere")
        exec { player, _ -> sendStageDialog(player) }
    }
}