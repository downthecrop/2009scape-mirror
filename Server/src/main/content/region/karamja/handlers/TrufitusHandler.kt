package content.region.karamja.handlers

import content.data.Quests
import content.region.karamja.quest.junglepotion.JunglePotionListeners.Companion.giveHerb
import content.region.karamja.quest.junglepotion.TrufitusJunglePotionDialogueFile
import core.api.getQuestStage
import core.game.dialogue.DialogueLabeller
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Trufitus Shakaya handler. Speaks in Jungle Potion, Shilo Village, and Tai Bwo Wannai Trio quests.
 */

class TrufitusHandler : InteractionListener {
    override fun defineListeners() {

        // talking to Truffy
        on(NPCs.TRUFITUS_740, IntType.NPC, "talk-to") { player, node ->
            //val shiloStage = getQuestStage(player, Quests.SHILO_VILLAGE)

            //if (shiloStage > SHILO_SHOWN_BELT) DialogueLabeller.open(player, TrufitusShiloDialogueFile(), node.asNpc())
            //else DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile(), node.asNpc())

            // TODO: remove the above comments when pulling into Shilo Village, and remove the below line
            DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile(), node.asNpc())

            return@on true
        }

        // using various items with Truffy
        onUseAnyWith(IntType.NPC, NPCs.TRUFITUS_740) { player, used, with ->
            val jStage = getQuestStage(player, Quests.JUNGLE_POTION)

            when (used.id) {
                // --------------- Herbs for Jungle Potion --------------- //

                // clean herbs
                Items.CLEAN_SNAKE_WEED_1526 -> {
                    when (jStage) {
                        // has asked for snakeweed
                        in 1..2 -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("snakecheck"), with.asNpc())
                        // if you've turned in snakeweed
                        in 3..100 -> giveHerb(player, used.asItem())
                        // hasn't asked yet
                        else -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                    }
                }

                Items.CLEAN_ARDRIGAL_1528 -> {
                    when (jStage) {
                        // has asked for ardrigal
                        in 3..4 -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("ardrigalcheck"), with.asNpc())
                        // if you've turned in ardrigal
                        in 5..100 -> giveHerb(player, used.asItem())
                        // hasn't asked yet
                        else -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                    }
                }

                Items.CLEAN_SITO_FOIL_1530 -> {
                    when (jStage) {
                        // has asked for sito foil
                        in 5..6 -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sitofoilcheck"), with.asNpc())
                        // if you've turned in sito foil
                        in 7..100 -> giveHerb(player, used.asItem())
                        // hasn't asked yet
                        else -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                    }
                }

                Items.CLEAN_VOLENCIA_MOSS_1532 -> {
                    when (jStage) {
                        // has asked for volencia moss
                        in 7..8 -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("volenciamosscheck"), with.asNpc())
                        // if you've turned in volencia moss
                        in 9..100 -> giveHerb(player, used.asItem())
                        // hasn't asked yet
                        else -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                    }
                }

                Items.CLEAN_ROGUES_PURSE_1534 -> {
                    when (jStage) {
                        // has asked for rogue's purse
                        in 9..10 -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("roguespursecheck"), with.asNpc())
                        // if you've turned in rogue's purse
                        in 11..100 -> giveHerb(player, used.asItem())
                        // hasn't asked yet
                        else -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                    }
                }

                // grimy herbs
                Items.GRIMY_SNAKE_WEED_1525 -> {
                    // after you've picked a snake weed
                    if (jStage == 2) DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("dirtyherb"), with.asNpc())
                    else DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                }

                Items.GRIMY_ARDRIGAL_1527 -> {
                    // after you've picked an ardrigal
                    if (jStage == 4) DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("dirtyherb"), with.asNpc())
                    else DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                }

                Items.GRIMY_SITO_FOIL_1529 -> {
                    // after you've picked a sito foil
                    if (jStage == 6) DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("dirtyherb"), with.asNpc())
                    else DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                }

                Items.GRIMY_VOLENCIA_MOSS_1531 -> {
                    // after you've picked a volencia moss
                    if (jStage == 8) DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("dirtyherb"), with.asNpc())
                    else DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                }

                Items.GRIMY_ROGUES_PURSE_1533 -> {
                    // after you've picked a rogue's purse
                    if (jStage == 10) DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("dirtyherb"), with.asNpc())
                    else DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
                }

                // --------------- Items for Shilo Village --------------- //

                // --------------- Fallback --------------- //
                else -> DialogueLabeller.open(player, TrufitusJunglePotionDialogueFile("sorrybwana"), with.asNpc())
            }
            return@onUseAnyWith true
        }
    }
}