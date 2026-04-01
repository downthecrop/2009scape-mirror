package content.region.morytania.phas.dialogue

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs


@Initializable
class MetarialusDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.METARIALUS_2322, IntType.NPC, "talk-to") { player, node ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)) {
                DialogueLabeller.open(player, MetarialusDialogueFile(), node as NPC)
            return@on true
            } else {
                DialogueLabeller.open(player, MetarialusDialogueNoGhostSpeakFile(), node as NPC)
                return@on true
            }
        }
    }
}


class MetarialusDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.METARIALUS_2322)

        player(ChatAnim.FRIENDLY, "Hello, I wonder if you could help me on this whole brewing thing...")
        npc(ChatAnim.NEUTRAL, "I might be able to - what do you need to know?")
        goto("main_menu")

        label("main_menu") {
            options(
                DialogueOption("how_brew_ales", "How do I brew ales?", expression = ChatAnim.HALF_ASKING, spokenText = "How do I brew ales?"),
                DialogueOption("how_brew_cider", "How do I brew cider?", expression = ChatAnim.HALF_ASKING, spokenText = "How do I brew cider?"),
                DialogueOption("what_when_matured", "What do I do once my ale has matured?", expression = ChatAnim.HALF_ASKING, spokenText = "What do I do once my ale has matured?"),
                DialogueOption("buy_yeast", "Do you have any spare ale yeast?", expression = ChatAnim.HALF_ASKING, spokenText = "Do you have any spare ale yeast?"),
                DialogueOption("end", "That's all I need to know, thanks.")
            )
        }

        label("how_brew_ales") {
            npc(ChatAnim.NEUTRAL, "Well first off you need to fill the vat with water - two bucketfuls should do the trick. Then you'll need to put in two handfuls of barley malt - that's roasted barley by the way.")
            npc(ChatAnim.NEUTRAL, "After that you'll be putting your main ingredient in - this will decide which ale it is you're brewing. There should be some good guides around with recipes in.")
            npc(ChatAnim.NEUTRAL, "Lastly you pour a pot full of ale yeast in, which'll start it off fermenting. Then all you have to do is wait for the good stuff.")
            goto("main_menu")
        }

        label("how_brew_cider") {
            npc(ChatAnim.NEUTRAL, "First you'll need some apples. You'll need to crush them using this cider-press - four apples should make a full bucket of apple-mush. Take four buckets of mush and fill up the vat.")
            npc(ChatAnim.NEUTRAL, "After that you pour a pot full of ale yeast in, which'll start it off fermenting. Then all you have to do is wait for the good stuff.")
            goto("main_menu")
        }

        label("what_when_matured") {
            npc(ChatAnim.NEUTRAL, "Well, once you've got a full vat of the good stuff, just turn the valve and your barrel will fill up with eight pints of whatever your chosen tipple is. Mind it's an empty barrel, though.")
            goto("main_menu")
        }

        label("buy_yeast") {
            npc(ChatAnim.NEUTRAL, "Well, as a matter of fact I do, although I wouldn't describe",
                                    "it as spare. This ale yeast I've got is the best money can",
                                    "buy, but if you've got a pot I'll fill it for you for",
                                    "5 ecto-tokens - very cheap as it happens.")
            options(
                DialogueOption("confirm_buy", "That's a good deal - please fill my pot with ale yeast for 5 ecto-tokens."),
                DialogueOption("end", "No, that's too much I'm afraid.")
            )
        }

        label("confirm_buy") {
            exec { player, npc ->
                if (!inInventory(player, Items.EMPTY_POT_1931, 1)) {
                    loadLabel(player, "no_pot")
                } else if (!inInventory(player, Items.ECTO_TOKEN_4278, 5)) {
                    loadLabel(player, "no_tokens")
                } else {
                    loadLabel(player, "process_purchase")
                }
            }
        }

        label("no_pot") {
            npc(ChatAnim.NEUTRAL, "I'm afraid you'll need a pot for me to put the yeast in.")
            goto("end")
        }

        label("no_tokens") {
            line("You can not afford that.")
            goto("end")
        }

        label("process_purchase") {
            exec { player, npc ->
                val emptyPots = amountInInventory(player, Items.EMPTY_POT_1931)
                val tokens = amountInInventory(player, Items.ECTO_TOKEN_4278)
                val canAfford = tokens / 5
                val quantity = minOf(emptyPots, canAfford)

                if (removeItem(player, Item(Items.EMPTY_POT_1931, quantity)) && removeItem(player, Item(Items.ECTO_TOKEN_4278, quantity * 5))) {
                    addItem(player, Items.ALE_YEAST_5767, quantity)
                }
            }
            goto("main_menu")
        }

        label("end") {
        }
    }
}


class MetarialusDialogueNoGhostSpeakFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.METARIALUS_2322)

        npc(ChatAnim.WORRIED, "Wooo Woo ooo wooowoo!") // Placeholder taken from restless ghost, can't find source for his no-ghostspeak text
    }
}