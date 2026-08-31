package content.region.kandarin.yanille.quest.thehandinthesand.dialogue

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.quest.TheHandintheSand
import core.api.addItem
import core.api.animate
import core.api.face
import core.api.freeSlots
import core.api.getAttribute
import core.api.getQuestStage
import core.api.getScenery
import core.api.hasAnItem
import core.api.inInventory
import core.api.removeItem
import core.api.sendItemDialogue
import core.api.setQuestStage
import core.api.setVarbit
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.FacialExpression
import core.game.interaction.DestinationFlag
import core.game.interaction.MovementPulse
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Dialogue between Betty and the player for The Hand in the Sand quest.
 * Opened from Betty's main dialogue file.
 * @author Edith
 */
class HITSBettyDialogueFile : DialogueLabeller() {

    companion object{
        private const val ANIMATION_PLACE_ON_TABLE_537 = 537
    }

    override fun addConversation() {

        exec { player, _ ->
            val stage = getQuestStage(player, Quests.THE_HAND_IN_THE_SAND)

            when {
                stage >= TheHandintheSand.STAGE_QUEST_COMPLETED_100 -> loadLabel(player, "post_quest_dye")

                stage >= TheHandintheSand.STAGE_SANDY_CONFESSION_55 -> loadLabel(player, "after_sandy_confession")

                stage >= TheHandintheSand.STAGE_DRUG_SANDY_45 &&
                        !hasAnItem(player, Items.TRUTH_SERUM_6952).exists() -> loadLabel(player, "lost_truth_serum")

                stage >= TheHandintheSand.STAGE_DRUG_SANDY_45 -> loadLabel(player, "serum_is_ready")

                getAttribute(player, TheHandintheSand.ATTRIBUTE_TRUTH_SERUM_MADE, false) &&
                        !hasAnItem(player, Items.TRUTH_SERUM_6952).exists() -> loadLabel(player, "lost_truth_serum")

                inInventory(player, Items.TRUTH_SERUM_6952) -> loadLabel(player, "has_truth_serum")
                inInventory(player, Items.ROSE_TINTED_LENS_6956) -> loadLabel(player, "has_rose_lens")
                stage >= TheHandintheSand.STAGE_TRUTH_SERUM_40 -> loadLabel(player, "making_the_lens")
            }
        }
        player(FacialExpression.ASKING, "I've come from Yanille, the wizard says you can make Truth Serum?")
        npc(FacialExpression.HAPPY, "This is true deary, I'll need an empty vial.")
        exec { player, _ ->
            if (inInventory(player, Items.VIAL_229)) {
                loadLabel(player, "player_has_vial")
            }
        }
        player("I'll have to go find one then, I'll be back!")

        label("player_has_vial")
            player(FacialExpression.HAPPY, "I have one here!")
            exec { player, _ ->
                if (removeItem(player, Items.VIAL_229)) {
                    addItem(player, Items.BOTTLED_WATER_6953)
                    setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_TRUTH_SERUM_40)
                }
            }
            npc(FacialExpression.HAPPY, "That's good, now you'll need to make a rose tinted lens. Pink dye can be made from red berries in this bottle to make redberry juice, then add white berries. Just use that on a bullseye lens.")

        label("has_rose_lens")
            npc(FacialExpression.HAPPY, "Wonderful deary. When you're ready, just stand in the open doorway and focus the light on the empty vial on my desk and I'll pour the serum into it.")
            player("Ok, what does that do?")
            npc(FacialExpression.HAPPY, "Why it makes the person who drinks it unable to hide in the shadow of lies. The light of truth will shine!")
            exec { player, betty ->
                val counter = getScenery(3013, 3258, 0) ?: return@exec

                betty.pulseManager.run(object : MovementPulse(betty, counter, DestinationFlag.OBJECT) {
                    override fun pulse(): Boolean {
                        face(betty, counter.location)
                        animate(betty, ANIMATION_PLACE_ON_TABLE_537)
                        setVarbit(player, TheHandintheSand.VARBIT_BETTYS_COUNTER_1537, TheHandintheSand.BETTYS_COUNTER_SHOW_VIAL_1, true)
                        sendItemDialogue(player, Items.VIAL_229, "Betty places a vial on her counter.")
                        return true
                    }
                })
            }

        label("making_the_lens")
            npc("Hello deary! Have you managed to make that lens yet?")
            options(
                DialogueOption("nowhere", "I'm still working on it."),
                DialogueOption("ive_forgotten_how", "I'm afraid I've forgotten how!"),
                DialogueOption("lost_my_bottle", "I've lost my bottle!", optionIf = { player, _ ->
                    !hasAnItem(player, Items.BOTTLED_WATER_6953).exists() &&
                            !hasAnItem(player, Items.REDBERRY_JUICE_6954).exists() &&
                            !hasAnItem(player, Items.PINK_DYE_6955).exists() &&
                            !hasAnItem(player, Items.ROSE_TINTED_LENS_6956).exists()
                })
            )

        label("ive_forgotten_how")
            npc("Pink dye can be made from red berries in the bottle I gave you. Add white berries to make the pink dye and then you need to use that on a bullseye lens. Good luck!")

        label("lost_my_bottle")
            npc("Oh don't worry about that deary, I have plenty and you can start the whole thing again.")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_bottle")
                } else {
                    addItem(player, Items.BOTTLED_WATER_6953)
                }
            }
            npc("Here, have another.")

        label("no_space_bottle")
            npc("I'd let you have another but you don't seem to have a spare hand to carry it, come back when you have room.")

        label("lost_truth_serum")
            player("I've lost it!")
            exec { player, _ ->
                if (freeSlots(player) < 1) {
                    loadLabel(player, "no_space_serum")
                }
            }
            npc("That's not a problem, I kept some of it here just in case, here you are!")
            exec { player, _ ->
                addItem(player, Items.TRUTH_SERUM_6952)
            }
            item(Item(Items.TRUTH_SERUM_6952), "Betty hands you a new vial of truth serum.")

        label("no_space_serum")
            npc("That's not a problem, I kept some of it here just in case, but you have no space for it! Come back when you do")

        label("has_truth_serum")
            npc(FacialExpression.HAPPY, "Ok, now the last ingredient, something personal from the person you need to tell the truth, else it won't work!")
            exec { player, _ ->
                if (inInventory(player, Items.SAND_6958)) {
                    loadLabel(player, "has_sandy_sand")
                }
            }
            player("Ok, I'll see if I can find something.")

        label("has_sandy_sand")
            player(FacialExpression.ASKING, "What about this sand straight from his pocket?")
            npc(FacialExpression.HAPPY, "That's excellent deary!")
            item(Item(Items.SAND_6958), "You hand the sand over and watch Betty sprinkle it in the serum, it fizzes.")
            npc(FacialExpression.HAPPY, "Don't forget to dilute it in something like tea or coffee.")
            exec { player, _ ->
                if (removeItem(player, Item(Items.SAND_6958, 1))) {
                    setQuestStage(player, Quests.THE_HAND_IN_THE_SAND, TheHandintheSand.STAGE_DRUG_SANDY_45)
                }
            }

        label("serum_is_ready")
            npc("Hello deary! How did the serum work?")
            player("I haven't tried it yet.")
            npc("Well don't forget to dillute it in a drink or something else bad things might happen.")
            player("Bad things?")
            npc("Well.... bits might drop off.")
            player("Oh! I... see. I'll remember to dilute it then.")

        label("after_sandy_confession")
            npc("Hello again deary. Come back and tell me what happened when all the fuss is over.")
            player("Ok Betty, I'll be back!")

        label("post_quest_dye")
            npc("Zavistic told me what a good job you did. If you want some more pink dye, I have made up a batch and you can have some for 20 gold.")
            options(
                DialogueOption("decline_pink_dye", "No thanks, Betty.", "No thanks, Betty. Good luck with the shop. I might be back for some dye later."),
                DialogueOption("buy_pink_dye", "Yes, please!")
            )

        label("decline_pink_dye")
            npc("Thanks, @name. See you soon.")

        label("buy_pink_dye")
            exec { player, _ ->
                when {
                    freeSlots(player) < 1 -> loadLabel(player, "no_space_dye")
                    !inInventory(player, Items.COINS_995, 20) -> loadLabel(player, "no_money_dye")
                    else -> loadLabel(player, "give_pink_dye")
                }
            }

        label("no_space_dye")
            npc("I'm afraid you don't have space in your pack for anything more, come back when you do.")

        label("no_money_dye")
            line("You don't have enough money at the moment, come back later.")

        label("give_pink_dye")
            line("You hand over 20 gold pieces in return for the dye")
            exec { player, _ ->
                if (removeItem(player, Item(Items.COINS_995, 20))) {
                    addItem(player, Items.PINK_DYE_6955)
                }
            }
        }

    }