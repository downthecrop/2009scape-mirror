package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonAnna
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonBob
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonCarol
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonDavid
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonElizabeth
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeAskPoisonFrank
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeNoiseClue
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeRandomMurderer
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeTakenThread
import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItem
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.Scenery

class MurderMysteryListeners : InteractionListener {
    companion object {
        val UNDUSTEDEVIDENCE = intArrayOf(
            Items.CRIMINALS_DAGGER_1813,
            Items.PUNGENT_POT_1812,
            Items.SILVER_NECKLACE_1796,
            Items.SILVER_CUP_1798,
            Items.SILVER_BOTTLE_1800,
            Items.SILVER_BOOK_1802,
            Items.SILVER_NEEDLE_1804,
            Items.SILVER_POT_1806
        )
        val DUSTEDEVIDENCE = intArrayOf(
            Items.CRIMINALS_DAGGER_1814,
            Items.SILVER_NECKLACE_1797,
            Items.SILVER_CUP_1799,
            Items.SILVER_BOTTLE_1801,
            Items.SILVER_BOOK_1803,
            Items.SILVER_NEEDLE_1805,
            Items.SILVER_POT_1807
        )
        val SILVERBARRELS = intArrayOf(
            Scenery.ANNA_S_BARREL_2656,
            Scenery.BOB_S_BARREL_2657,
            Scenery.CAROL_S_BARREL_2658,
            Scenery.DAVID_S_BARREL_2659,
            Scenery.ELIZABETH_S_BARREL_2660,
            Scenery.FRANK_S_BARREL_2661
        )
        val POISONSPOTS = intArrayOf(
            Scenery.SINCLAIR_FAMILY_COMPOST_HEAP_26120,
            Scenery.SINCLAIR_FAMILY_BEEHIVE_26121,
            Scenery.SINCLAIR_MANSION_DRAIN_2843,
            Scenery.SPIDERS__NEST_26109,
            Scenery.SINCLAIR_FAMILY_FOUNTAIN_2654,
            Scenery.SINCLAIR_FAMILY_CREST_2655
        )
        val SUSPECTPRINTS = intArrayOf(
            Items.ANNAS_PRINT_1816,
            Items.BOBS_PRINT_1817,
            Items.CAROLS_PRINT_1818,
            Items.DAVIDS_PRINT_1819,
            Items.ELIZABETHS_PRINT_1820,
            Items.FRANKS_PRINT_1821
        )
    }

    override fun defineListeners() {
        on(Items.CRIMINALS_DAGGER_1813, GROUNDITEM, "take") { player, groundItem ->
            when (getQuestStage(player, Quests.MURDER_MYSTERY)) {
                1 -> {
                    if (inInventory(player, Items.CRIMINALS_DAGGER_1813) || inInventory(player, Items.CRIMINALS_DAGGER_1814)) {
                    sendDialogue(player, "I already have the murder weapon.")
                    }
                    else if (addItem(player, Items.CRIMINALS_DAGGER_1813)) {
                        removeGroundItem(groundItem as GroundItem)
                        sendDialogue(player, "This knife doesn't seem sturdy enough to have killed Lord Sinclair.")
                    }
                    return@on true
                }
                else -> {
                    sendDialogue(player, "You need the guards' permission to do that.")
                    return@on true
                }
            }
        }
        on(Items.PUNGENT_POT_1812, GROUNDITEM, "take") { player, groundItem ->
            when (getQuestStage(player, Quests.MURDER_MYSTERY)) {
                1 -> {
                    if (inInventory(player, Items.PUNGENT_POT_1812)) {
                        sendDialogue(player, "I already have the poisoned pot.")
                    } else if (addItem(player, Items.PUNGENT_POT_1812)) {
                        removeGroundItem(groundItem as GroundItem)
                        sendDialogue(player, "It seems like Lord Sinclair was drinking from this before he died.")
                    }
                    return@on true
                    }
                else -> {
                    sendDialogue(player, "You need the guards' permission to do that.")
                    return@on true
                }
            }
        }
        on(intArrayOf(Scenery.STURDY_WOODEN_GATE_2664, Scenery.STURDY_WOODEN_GATE_2665), SCENERY, "investigate") { player, _ ->
            when (getQuestStage(player, Quests.MURDER_MYSTERY)) {
                1 -> {
                    sendDialogue(player, "As you approach the gate the guard dog starts barking loudly at you. There is no way an intruder could have committed the murder. It must have been someone the dog knew to get past it quietly.")
                    setAttribute(player, attributeNoiseClue, true)
                    return@on true
                }
                else -> {
                    sendDialogue(player, "You need the guards' permission to do that.")
                    return@on true
                }
            }
        }
        on(Scenery.SMASHED_WINDOW_26110, SCENERY, "investigate") { player, _ ->
            when (getQuestStage(player, Quests.MURDER_MYSTERY)) {
                1 -> {
                    sendDialogue(player, "Some thread seems to have been caught on a loose nail on the window.")
                    if (inInventory(player, Items.CRIMINALS_THREAD_1808) || inInventory(player, Items.CRIMINALS_THREAD_1809) || inInventory(player, Items.CRIMINALS_THREAD_1810)) {
                        sendMessage(player, "You have already taken the thread.")
                    }
                    else if (getAttribute(player, attributeTakenThread, false)) {
                        if (hasSpaceFor(player, Item(Items.CRIMINALS_THREAD_1808))) {
                            when (getAttribute(player, attributeRandomMurderer, 0)) {
                                1, 2 -> addItem(player, Items.CRIMINALS_THREAD_1808)
                                0, 3 -> addItem(player, Items.CRIMINALS_THREAD_1809)
                                else -> addItem(player, Items.CRIMINALS_THREAD_1810)
                            }
                            sendMessage(player, "Lucky for you there's some thread left. You should be less careless in the future.")
                        }
                    }
                    else {
                        if (hasSpaceFor(player, Item(Items.CRIMINALS_THREAD_1808))) {
                            when (getAttribute(player, attributeRandomMurderer, 0)) {
                                1, 2 -> addItem(player, Items.CRIMINALS_THREAD_1808)
                                0, 3 -> addItem(player, Items.CRIMINALS_THREAD_1809)
                                else -> addItem(player, Items.CRIMINALS_THREAD_1810)
                            }
                            sendMessage(player, "You take the thread.")
                            setAttribute(player, attributeTakenThread, true)
                        }
                    }
                    return@on true
                }
                else -> {
                    sendDialogue(player, "You need the guards' permission to do that.")
                    return@on true
                }
            }
        }
        on(Scenery.SMASHED_WINDOW_26110, SCENERY, "break") { player, _ ->
            sendDialogue(player, "You don't want to damage evidence!")
            return@on true
        }
        on(Scenery.SACKS_2663, SCENERY, "investigate") { player, _ ->
            when (getQuestStage(player, Quests.MURDER_MYSTERY)) {
                1 -> {
                    openDialogue(player, FlypaperDialogue())
                    return@on true
                }
                else -> {
                    sendDialogue(player, "You need the guards' permission to do that.")
                    return@on true
                }
            }
        }
        on(SILVERBARRELS, SCENERY, "search") { player, node ->
            when (getQuestStage(player, Quests.MURDER_MYSTERY)) {
                1 -> {
                    when (node.id) {
                        Scenery.ANNA_S_BARREL_2656 -> {
                            if (inInventory(player, Items.SILVER_NECKLACE_1796) || inInventory(player, Items.SILVER_NECKLACE_1797)) {
                                sendDialogue(player, "I already have Anna's Necklace.")
                            }
                            else if (addItem(player, Items.SILVER_NECKLACE_1796)){
                                sendDialogue(player, "There's something shiny hidden at the bottom. You take Anna's Silver Necklace.")
                            }
                        }
                        Scenery.BOB_S_BARREL_2657 -> {
                            if (inInventory(player, Items.SILVER_CUP_1798) || inInventory(player, Items.SILVER_CUP_1799)) {
                                sendDialogue(player, "I already have Bob's cup.")
                            }
                            else if (addItem(player, Items.SILVER_CUP_1798)){
                                sendDialogue(player, "There's something shiny hidden at the bottom. You take Bob's silver cup.")
                            }
                        }
                        Scenery.CAROL_S_BARREL_2658 ->  {
                            if (inInventory(player, Items.SILVER_BOTTLE_1800) || inInventory(player, Items.SILVER_BOTTLE_1801)) {
                                sendDialogue(player, "I already have Carol's bottle.")
                            }
                            else if (addItem(player, Items.SILVER_BOTTLE_1800)){
                                sendDialogue(player, "There's something shiny hidden at the bottom. You take Carol's silver bottle.")
                            }
                        }
                        Scenery.DAVID_S_BARREL_2659 -> {
                            if (inInventory(player, Items.SILVER_BOOK_1802) || inInventory(player, Items.SILVER_BOOK_1803)) {
                                sendDialogue(player, "I already have David's book.")
                            }
                            else if (addItem(player, Items.SILVER_BOOK_1802)){
                                sendDialogue(player, "There's something shiny hidden at the bottom. You take David's silver book.")
                            }
                        }
                        Scenery.ELIZABETH_S_BARREL_2660 -> {
                            if (inInventory(player, Items.SILVER_NEEDLE_1804) || inInventory(player, Items.SILVER_NEEDLE_1805)) {
                                sendDialogue(player, "I already have Elizabeth's Needle.")
                            }
                            else if (addItem(player, Items.SILVER_NEEDLE_1804)){
                                sendDialogue(player, "There's something shiny hidden at the bottom. You take Elizabeth's silver needle.")
                            }
                        }
                        Scenery.FRANK_S_BARREL_2661 -> {
                            if (inInventory(player, Items.SILVER_POT_1806) || inInventory(player, Items.SILVER_POT_1807)) {
                                sendDialogue(player, "I already have Frank's pot.")
                            }
                            else if (addItem(player, Items.SILVER_POT_1806)){
                                sendDialogue(player, "There's something shiny hidden at the bottom. You take Frank's silver pot.")
                            }
                        }
                    }
                    return@on true
                }
                else -> {
                    sendDialogue(player, "You need the guards' permission to do that.")
                    return@on true
                }
            }
        }
        on(POISONSPOTS, SCENERY, "investigate") { player, node ->
            when (node.id) {
                Scenery.SINCLAIR_FAMILY_COMPOST_HEAP_26120 -> {
                    if (!getAttribute(player, attributeAskPoisonAnna, false)) {
                        sendDialogue(player, "It's a heap of compost.")
                    }
                    else if (getAttribute(player, attributeRandomMurderer, 0) == 0) {
                        sendDialogue(player, "The compost is teeming with maggots. Somebody should really do something about it. It's certainly clear nobody's used poison here.")
                        setAttribute(player, attributePoisonClue, 2)
                    }
                    else {
                        sendDialogue(player, "There is a faint smell of poison behind the smell of the compost.")
                    }
                }
                Scenery.SINCLAIR_FAMILY_BEEHIVE_26121 -> {
                    if (!getAttribute(player, attributeAskPoisonBob, false)) {
                        sendDialogue(player, "It's a very old beehive.")
                    }
                    else if (getAttribute(player, attributeRandomMurderer, 0) == 1) {
                        sendDialogue(player, "The beehive buzzes with activity. These bees definitely don't seem poisoned at all.")
                        setAttribute(player, attributePoisonClue, 2)
                    }
                    else {
                        sendDialogue(player, "The hive is empty. There are a few dead bees and a faint smell of poison.")
                    }
                }
                Scenery.SINCLAIR_MANSION_DRAIN_2843 -> {
                    if (!getAttribute(player, attributeAskPoisonCarol, false)) {
                        sendDialogue(player, "It's the drains from the kitchen.")
                    }
                    else if (getAttribute(player, attributeRandomMurderer, 0) == 2) {
                        sendDialogue(player, "The drain is totally blocked. It really stinks. No, it REALLY smells bad. It's certainly clear nobody's cleaned it recently.")
                        setAttribute(player, attributePoisonClue, 2)
                    }
                    else {
                        sendDialogue(player, "The drain seems to have been recently cleaned. You can still smell the faint aroma of poison.")
                    }
                }
                Scenery.SPIDERS__NEST_26109 -> {
                    if (!getAttribute(player, attributeAskPoisonDavid, false)) {
                        sendDialogue(player, "It looks like a spiders' nest of some kind...")
                    }
                    else if (getAttribute(player, attributeRandomMurderer, 0) == 3) {
                        sendDialogue(player, "There is a spiders' nest here. You estimate there must be at least a few hundred spiders ready to hatch. It's certainly clear nobody's used poison here.")
                        setAttribute(player, attributePoisonClue, 2)
                    }
                    else {
                        sendDialogue(player, "A faint smell of poison and a few dead spiders is all that remains of the spiders nest.")
                    }
                }
                Scenery.SINCLAIR_FAMILY_FOUNTAIN_2654 -> {
                    if (!getAttribute(player, attributeAskPoisonElizabeth, false)) {
                        sendDialogue(player, "A fountain with large numbers of insects around the base.")
                    }
                    else if (getAttribute(player, attributeRandomMurderer, 0) == 4) {
                        openDialogue(player, FountainDialogue())
                        setAttribute(player, attributePoisonClue, 2)
                    }
                    else {
                        sendDialogue(player, "There are a lot of dead mosquitos around the base of the fountain. A faint smell of poison is in the air, but the water seems clean.")
                    }
                }
                Scenery.SINCLAIR_FAMILY_CREST_2655 -> {
                    if (!getAttribute(player, attributeAskPoisonFrank, false)) {
                        sendDialogue(player, "The Sinclair Family Crest is hung up here.")
                    }
                    else if (getAttribute(player, attributeRandomMurderer, 0) == 5) {
                        sendDialogue(player, "It looks like the Sinclair family crest but it is very dirty. You can barely make it out under all of the grime. It's certainly clear nobody's cleaned it recently.")
                        setAttribute(player, attributePoisonClue, 2)
                    }
                    else {
                        sendDialogue(player, "The Sinclair family crest. It's shiny and freshly polished and has a slight smell of poison.")
                    }
                }

            }
            return@on true
        }
        onUseWith(ITEM, UNDUSTEDEVIDENCE, Items.POT_OF_FLOUR_1933) { player, used, _ ->
            when (used.id) {
                Items.CRIMINALS_DAGGER_1813 -> {
                    removeItem(player, used)
                    addItem(player, Items.CRIMINALS_DAGGER_1814)
                    sendMessage(player, "You sprinkle a small amount of flour on the murder weapon.")
                    sendMessage(player, "The murder weapon is now coated with a thin layer of flour.")
                }
                Items.PUNGENT_POT_1812 -> {
                    sendMessage(player, "You sprinkle a small amount of flour on the strange smelling pot.")
                    sendMessage(player, "The surface isn't shiny enough to take a fingerprint from.")
                }
                Items.SILVER_NECKLACE_1796 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_NECKLACE_1797)
                    sendMessage(player, "You sprinkle the flour on Anna's necklace.")
                    sendMessage(player, "The necklace is now coated with a thin layer of flour.")
                }
                Items.SILVER_CUP_1798 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_CUP_1799)
                    sendMessage(player, "You sprinkle the flour on Bob's cup.")
                    sendMessage(player, "The cup is now coated with a thin layer of flour.")
                }
                Items.SILVER_BOTTLE_1800 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_BOTTLE_1801)
                    sendMessage(player, "You sprinkle the flour on Carol's bottle.")
                    sendMessage(player, "The bottle is now coated with a thin layer of flour.")
                }
                Items.SILVER_BOOK_1802 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_BOOK_1803)
                    sendMessage(player, "You sprinkle the flour on David's book.")
                    sendMessage(player, "The Book is now coated with a thin layer of flour.")
                }
                Items.SILVER_NEEDLE_1804 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_NEEDLE_1805)
                    sendMessage(player, "You sprinkle the flour on Elizabeth's needle.")
                    sendMessage(player, "The Needle is now coated with a thin layer of flour.")
                }
                Items.SILVER_POT_1806 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_POT_1807)
                    sendMessage(player, "You sprinkle the flour on Frank's pot")
                    sendMessage(player, "The Pot is now coated with a thin layer of flour.")
                }
            }
            removeItem(player, Items.POT_OF_FLOUR_1933)
            addItem(player, Items.EMPTY_POT_1931)
            return@onUseWith true
        }
        onUseWith(ITEM, DUSTEDEVIDENCE, Items.FLYPAPER_1811) { player, used, _ ->
            removeItem(player, Items.FLYPAPER_1811)
            when (used.id) {
                Items.CRIMINALS_DAGGER_1814 -> {
                    removeItem(player, used)
                    addItem(player, Items.CRIMINALS_DAGGER_1813)
                    addItem(player, Items.UNKNOWN_PRINT_1822)
                    sendMessage(player, "You use the flypaper on the floury dagger.")
                    sendMessage(player, "You have a clean impression of the murderer's finger prints.")
                }
                Items.SILVER_NECKLACE_1797 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_NECKLACE_1796)
                    addItem(player, Items.ANNAS_PRINT_1816)
                    sendMessage(player, "You use the flypaper on the flour covered necklace.")
                    sendMessage(player, "You have a clean impression of Anna's finger prints.")
                }
                Items.SILVER_CUP_1799 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_CUP_1798)
                    addItem(player, Items.BOBS_PRINT_1817)
                    sendMessage(player, "You use the flypaper on the flour covered cup.")
                    sendMessage(player, "You have a clean impression of Bob's finger prints.")
                }
                Items.SILVER_BOTTLE_1801 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_BOTTLE_1800)
                    addItem(player, Items.CAROLS_PRINT_1818)
                    sendMessage(player, "You use the flypaper on the flour covered bottle.")
                    sendMessage(player, "You have a clean impression of Carol's finger prints.")
                }
                Items.SILVER_BOOK_1803 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_BOOK_1802)
                    addItem(player, Items.DAVIDS_PRINT_1819)
                    sendMessage(player, "You use the flypaper on the flour covered book.")
                    sendMessage(player, "You have a clean impression of David's finger prints.")
                }
                Items.SILVER_NEEDLE_1805 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_NEEDLE_1804)
                    addItem(player, Items.ELIZABETHS_PRINT_1820)
                    sendMessage(player, "You use the flypaper on the flour covered needle.")
                    sendMessage(player, "You have a clean impression of Elizabeth's finger prints.")
                }
                Items.SILVER_POT_1807 -> {
                    removeItem(player, used)
                    addItem(player, Items.SILVER_POT_1806)
                    addItem(player, Items.FRANKS_PRINT_1821)
                    sendMessage(player, "You use the flypaper on the flour covered pot.")
                    sendMessage(player, "You have a clean impression of Frank's finger prints.")
                }
            }
            return@onUseWith true
        }
        onUseWith(ITEM, SUSPECTPRINTS, Items.UNKNOWN_PRINT_1822) { player, used, with ->
            val SUSPECT = when (used.id) {
                Items.ANNAS_PRINT_1816 -> "Anna"
                Items.BOBS_PRINT_1817  -> "Bob"
                Items.CAROLS_PRINT_1818  -> "Carol"
                Items.DAVIDS_PRINT_1819  -> "David"
                Items.ELIZABETHS_PRINT_1820  -> "Elizabeth"
                Items.FRANKS_PRINT_1821  -> "Frank"
                else -> "Anna"
            }
            if (used.id == getAttribute(player, attributeRandomMurderer, 0) + Items.ANNAS_PRINT_1816) {
                sendDialogue(player, "The fingerprints are an exact match to $SUSPECT's.")
                removeItem(player, with.id)
                addItem(player, Items.KILLERS_PRINT_1815)
            }
            else {
                sendDialogue(player, "They don't seem to be the same. I guess that clears $SUSPECT of the crime. You destroy the useless fingerprint.")
                removeItem(player, used.id)
            }
            return@onUseWith true
        }
        onUseWith(SCENERY, intArrayOf(Items.SILVER_POT_1806, Items.PUNGENT_POT_1812), Scenery.BARREL_OF_FLOUR_26122) { player, _, _ ->
            sendDialogue(player, "You probably shouldn't use evidence from a crime scene to keep flour in.")
            return@onUseWith true
        }
    }
}