package content.region.fremennik.lighthouse.dialogue

import content.data.GodBook
import content.region.fremennik.lighthouse.quest.horror.HorrorFromTheDeep
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Jossik, in the Lighthouse north of Barbarian Outpost. Horror From The Deep quest.
 */

class JossikDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.JOSSIK_1334), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, JossikDialogueFile(), node.asNpc())
            return@on true
        }
    }
}

class JossikDialogueFile : DialogueLabeller() {

    // note, this jossik is only accessible after the player has completed Horror From The Deep quest
    override fun addConversation() {
        exec { player, _ ->
            if (inInventory(player, Items.RUSTY_CASKET_3849, 1)
                || getAttribute(player, HorrorFromTheDeep.HFTD_NEEDS_CASKET, false)
            ) {
                loadLabel(player, "casket_start")
            } else {
                loadLabel(player, "standard_start")
            }
        }

        label("standard_start")
        npc("Hello again, adventurer.", "What brings you this way?")
        options(
            DialogueOption("shop", "Can I see your wares?"),
            DialogueOption("prayerbooks", "Have you found any prayerbooks?")
        )

        label("shop")
        npc("Sure thing!", "I think you'll agree, my prices are remarkable!")
        exec { player, npc ->
            openNpcShop(player, npc.id)
        }
        goto("end")

        label("prayerbooks")
        exec { player, _ ->
            var missing = false

            // check for fully completed books the player lost
            for (book in GodBook.values()) {
                if (player.savedData.globalData.hasCompletedGodBook(book)
                    && !hasAnItem(player, book.book.id).exists()
                ) {
                    missing = true
                    addItem(player, book.book.id)
                }
            }

            // check for damaged books the player lost
            val damaged = player.savedData.globalData.godBook
            if (damaged != -1 && !hasAnItem(player, GodBook.values()[damaged].damagedBook.id).exists()) {
                missing = true
                addItem(player, GodBook.values()[damaged].damagedBook.id)
            }

            if (missing) {
                loadLabel(player, "found_missing_books")
            } else {
                var hasUncompleted = false
                for (book in GodBook.values()) {
                    if (hasAnItem(player, book.damagedBook.id).exists()) {
                        hasUncompleted = true
                    }
                }

                // todo right here if i drop a book i can still buy another uncompleted one

                val uncompletedCount = GodBook.values().count { !player.savedData.globalData.hasCompletedGodBook(it) }

                if (uncompletedCount == 0 || hasUncompleted) {
                    loadLabel(player, "no_books")
                } else {
                    loadLabel(player, "sell_uncompleted")
                }
            }
        }

        label("found_missing_books")
        npc("As a matter of fact, I did! This book washed up on the", "beach, and I recognised it as yours!")
        goto("end")

        label("no_books")
        npc("No, sorry adventurer, I haven't.")
        goto("end")

        label("sell_uncompleted")
        npc("Funnily enough I have! I found some books in caskets", "just the other day! I'll sell one to you for 5000 coins;", "what do you say?")
        goto("sell_uncompleted_options")

        // get the options for god books
        val bookOptions = mutableListOf<DialogueOption>()
        for (book in GodBook.values()) {
            bookOptions.add(DialogueOption("buy_book_${book.name}", book.book.name, skipPlayer = true) { player, _ ->
                !player.savedData.globalData.hasCompletedGodBook(book)
            })
        }
        bookOptions.add(DialogueOption("cancel_buy", "Don't buy anything.", skipPlayer = true))

        label("sell_uncompleted_options")
        options(*bookOptions.toTypedArray())

        label("cancel_buy")
        goto("end")

        // generate purchase dialogue for books
        for (book in GodBook.values()) {
            label("buy_book_${book.name}")
            exec { player, _ ->
                if (!inInventory(player, Items.COINS_995, 5000)) {
                    loadLabel(player, "buy_no_coins")
                } else if (freeSlots(player) == 0) {
                    loadLabel(player, "buy_no_space")
                } else {
                    if (removeItem(player, Item(Items.COINS_995, 5000))) {
                        player.savedData.globalData.godBook = book.ordinal
                        addItem(player, book.damagedBook.id)
                        loadLabel(player, "buy_success")
                    } else {
                        loadLabel(player, "end")
                    }
                }
            }
        }

        label("buy_no_coins")
        player("Sorry, I don't seem to have enough coins.")
        goto("end")

        label("buy_no_space")
        player("Sorry, I don't have enough inventory space.")
        goto("end")

        label("buy_success")
        npc("Here you go!")
        goto("end")


        // Horror From The Deep
        label("casket_start")
        player(ChatAnim.FRIENDLY, "I see you managed to escape from those monsters intact!")
        exec { player, _ ->
            setAttribute(player, "/save:god_books:access", true)
        }
        npc(ChatAnim.FRIENDLY, "It seems I was not as injured as I thought I was after all! I must thank you again for all of your help!")
        npc(ChatAnim.FRIENDLY, "Now, about that casket you found on that monster's corpse...")
        exec { player, _ ->
            if (inInventory(player, Items.RUSTY_CASKET_3849)) {
                loadLabel(player, "casket_has")
            } else {
                loadLabel(player, "casket_needs")
            }
        }

        label("casket_needs")
        player(ChatAnim.NEUTRAL, "I never had any casket from that monster.")
        npc(ChatAnim.NEUTRAL, "Why, of course you did! You dropped it and I picked it up! I have it right here.")
        npc(ChatAnim.NEUTRAL, "Now, let's have a look at it. There is some writing on it, but it is very faint. Can you make it out?")
        goto("casket_choices")

        label("casket_has")
        player(ChatAnim.FRIENDLY, "I have it here. You said you might be able to tell me something about it...?")
        npc(ChatAnim.FRIENDLY, "I can indeed! Here, let me have a closer look...")
        npc(ChatAnim.FRIENDLY, "Yes! There is something written on it!")
        npc(ChatAnim.FRIENDLY, "It is very faint however... Can you read it?")
        goto("casket_choices")

        label("casket_choices")
        options(
            DialogueOption("choose_sara", "Saradomin", skipPlayer = true),
            DialogueOption("choose_zammy", "Zamorak", skipPlayer = true),
            DialogueOption("choose_guthix", "Guthix", skipPlayer = true)
        )

        // sara
        label("choose_sara")
        player(ChatAnim.FRIENDLY, "I think it says... Saradomin...")
        npc(ChatAnim.FRIENDLY, "Are you sure? I mean, are you REALLY sure?", "Maybe you'd better look again...")
        options(
            DialogueOption("confirm_sara", "Saradomin", skipPlayer = true),
            DialogueOption("choose_zammy", "Zamorak", skipPlayer = true),
            DialogueOption("choose_guthix", "Guthix", skipPlayer = true)
        )

        label("confirm_sara")
        player(ChatAnim.FRIENDLY, "Nope, it definitely says Saradomin.")
        npc(ChatAnim.FRIENDLY, "I think you're right! Hand it over, and let's see what's inside!")
        npc(ChatAnim.FRIENDLY, "Wow! It's a Holy Book of Saradomin! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.")
        exec { player, _ ->
            chooseBook(player, Items.DAMAGED_BOOK_3839)
        }
        goto("end")

        // zammy
        label("choose_zammy")
        player(ChatAnim.FRIENDLY, "I think it says... Zamorak...")
        npc(ChatAnim.FRIENDLY, "Are you sure? I mean, are you REALLY sure?", "Maybe you'd better look again...")
        options(
            DialogueOption("choose_sara", "Saradomin", skipPlayer = true),
            DialogueOption("confirm_zammy", "Zamorak", skipPlayer = true),
            DialogueOption("choose_guthix", "Guthix", skipPlayer = true)
        )

        label("confirm_zammy")
        player(ChatAnim.FRIENDLY, "Nope, it definitely says Zamorak.")
        npc(ChatAnim.FRIENDLY, "I think you're right! Hand it over, and let's see what's inside!")
        npc(ChatAnim.FRIENDLY, "Wow! It's an Unholy Book of Zamorak! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.")
        exec { player, _ ->
            chooseBook(player, Items.DAMAGED_BOOK_3841)
        }
        goto("end")

        // guthix
        label("choose_guthix")
        player(ChatAnim.FRIENDLY, "I think it says... Guthix...")
        npc(ChatAnim.FRIENDLY, "Are you sure? I mean, are you REALLY sure?", "Maybe you'd better look again...")
        options(
            DialogueOption("choose_sara", "Saradomin", skipPlayer = true),
            DialogueOption("choose_zammy", "Zamorak", skipPlayer = true),
            DialogueOption("confirm_guthix", "Guthix", skipPlayer = true)
        )

        label("confirm_guthix")
        player(ChatAnim.FRIENDLY, "Nope, it definitely says Guthix.")
        npc(ChatAnim.FRIENDLY, "I think you're right! Hand it over, and let's see what's inside!")
        npc(ChatAnim.FRIENDLY, "Wow! It's a Balance Book of Guthix! I thought these things had all vanished! Well, it's all yours, I hope you appreciate it.")
        exec { player, _ ->
            chooseBook(player, Items.DAMAGED_BOOK_3843)
        }
        goto("end")
    }
}

// swaps the casket or your IOU attribute for the book of choice
private fun chooseBook(player: Player, book: Int) {
    if (removeItem(player, Items.RUSTY_CASKET_3849)
        || getAttribute(player, HorrorFromTheDeep.HFTD_NEEDS_CASKET, false)
    ) {
        // update the save data
        val godBook = GodBook.forItem(Item(book), true)
        if (godBook != null) {
            player.savedData.globalData.godBook = godBook.ordinal
        }

        // add book
        addItemOrDrop(player, book)
        removeAttribute(player, HorrorFromTheDeep.HFTD_NEEDS_CASKET)
    }
}