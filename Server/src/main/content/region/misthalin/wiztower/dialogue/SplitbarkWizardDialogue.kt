package content.region.misthalin.wiztower.dialogue

import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.*

class SplitbarkWizardDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.WIZARD_1263, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, SplitbarkWizardDialogueFile(), node.asNpc())
            return@on true
        }
    }

    // amt is the same each of bark and cloth
    enum class SplitBark(val itemId: Int, val cost: Int, val amt: Int) {
        HELM(Items.SPLITBARK_HELM_3385, 6000, 2),
        BODY(Items.SPLITBARK_BODY_3387, 37000, 4),
        LEGS(Items.SPLITBARK_LEGS_3389, 32000, 3),
        GAUNTLETS(Items.SPLITBARK_GAUNTLETS_3391, 1000, 1),
        BOOTS(Items.SPLITBARK_BOOTS_3393, 1000, 1),
    }
}

class SplitbarkWizardDialogueFile : DialogueLabeller() {

    override fun addConversation() {

        npc("Hello there, can I help you?")
        options(
            DialogueOption("job", "What do you do here?"),
            DialogueOption("drip", "What's that you're wearing?"),
            DialogueOption("armourdirect", "Can you make me some armour please?"),
            DialogueOption("end", "No thanks."),
        )

        label("job")
        npc("I've been studying the practice of making split-bark armour.")
        options(
            DialogueOption("drip", "Split-bark armour, what's that?"),
            DialogueOption("armour", "Can you make me some?"),
        )

        label("drip")
        npc("Split-bark armour is special armour for mages, it's much more resistant to physical attacks than normal robes. It's actually very easy for me to make, but I've been having trouble getting hold of the pieces.")
        options(
            DialogueOption("end", "Well good luck with that."),
            DialogueOption("armour", "Can you make me some?"),
        )

        label("armour")
        npc("I need bark from a hollow tree, and some fine cloth. Unfortunately both these items can be found in Morytania, especially the cloth which is found in the tombs of shades.")
        npc("Of course I'd happily sell you some at a discounted price if you bring me those items.")
        options(
            DialogueOption("end", "Ok, guess I'll go looking then!"),
            DialogueOption("mats", "Ok, how much do I need?"),
        )

        label("mats")
        npc("I need 1 piece of each for either gloves or boots, 2 pieces of each for a hat, 3 pieces of each for leggings, and 4 pieces of each for a top.")
        npc("I'll charge you 1,000 coins for either gloves or boots, 6,000 coins for a hat 32,000 coins for leggings, and 37,000 for a top.")
        player("Ok, guess I'll go looking then!")

        label("armourdirect")
        npc("Certainly, what would like to me to make?")
        manual { player, npc ->
            sendSkillDialogue(player) {
                val items = SplitbarkWizardDialogue.SplitBark.values().map { Item(it.itemId) }.toTypedArray()
                withItems(*items)

                create { id, amount ->
                    val splitBark = SplitbarkWizardDialogue.SplitBark.values().first { it.itemId == id }
                    make(player, npc, splitBark, amount)
                }
            }
            return@manual null
        }

        label("end")
        goto("nowhere")
    }

    // TODO: add fine cloth code back in once Shades of Mort'ton is ready
    private fun make(player: Player, npc: NPC, splitBark: SplitbarkWizardDialogue.SplitBark, amount: Int) {
        val barkAmt = amountInInventory(player, Items.BARK_3239)
        val clothAmt = amountInInventory(player, Items.FINE_CLOTH_3470)

        val money = Item(Items.COINS_995, amount * splitBark.cost)
        val barkRemove = Item(Items.BARK_3239, amount * splitBark.amt)
        val clothRemove = Item(Items.FINE_CLOTH_3470, amount * splitBark.amt)

        // various checks. even if you have full inv, there will always be more free space cleared with the removal of mats, so don't check free space.
        if (amount < 1) {
            return
        }
        if (barkAmt < amount * splitBark.amt) {
            sendDialogue(player, "You don't have enough bark to make that many.")
            return
        }
        /*if (clothAmt < amount * splitBark.amt) {
            sendDialogue(player, "You don't have enough fine cloth to make that many.")
            return
        }*/
        if (!inInventory(player, Items.COINS_995, splitBark.cost * amount)) {
            sendNormalDialogue(player, FacialExpression.SAD, "Sorry, I don't seem to have enough coins.")
            return
        }

        // remove materials and add armour
        if (removeItem(player, money) && removeItem(player, barkRemove) /*&& removeItem(player, clothRemove)*/) {
            addItemOrDrop(player, splitBark.itemId, amount)
            sendNormalDialogue(npc, FacialExpression.HAPPY, "There you go, enjoy your new armour!")
        }
    }
}