package content.region.morytania.quest.hauntedmine

import content.data.Quests
import core.api.*
import core.game.dialogue.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Zealot's dialogue, the quest-giver of Haunted Mine.
 */

class ZealotDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.ZEALOT_1528), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, ZealotDialogueFile(), node as NPC)
            return@on true
        }
    }
}


// If you ask "Where is the plugin for space to skip dialogue?" I will send you a sternly-worded
// message on why you should appreciate this bespoke, small-batch, hand-crafted dialogue.  >:(
class ZealotDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            val questStage = getQuestStage(player, Quests.HAUNTED_MINE)
            when {
                questStage >= 100 -> loadLabel(player, "post_quest")
                questStage > 0 -> loadLabel(player, "during_quest")
                else -> loadLabel(player, "pre_quest")
            }
        }

        /** * * * * * * * * * * *
         * BEFORE THE QUEST
         ** * * * * * * * * * * */

        label("pre_quest")
        player("Hello there.")
        npc("State thy allegiance stranger.")
        options(
            DialogueOption("sara_path", "I follow the path of Saradomin.", skipPlayer = true),
            DialogueOption("zammy_path", "I serve only Zamorak.", skipPlayer = true),
            DialogueOption("guthix_path", "My beliefs are in accord with the teachings of Guthix.", skipPlayer = true),
            DialogueOption("no_path", "I have no particular allegiance.")
        )

        label("sara_path")
        player("I follow the path of Saradomin. Strength through wisdom.")
        npc("Ah, a wise choice my friend. But if you truly follow Saradomin, what brings you to these evil lands?")
        options(
            DialogueOption("sara_quests", "I come seeking challenges and quests."),
            DialogueOption("sara_lied", "Ok I lied.", skipPlayer = true),
            DialogueOption("sara_ask", "I could ask the same of you.")
        )

        label("sara_lied")
        player("Ok I lied, I don't support Saradomin. I'm only here for dastardly evil purposes.")
        npc("Then leave me, I have no time to listen to your poisonous tongue.")
        goto("end")

        label("sara_quests")
        npc("A noble cause indeed. One that I can sympathise with, for I myself am upon a quest. One that will allow Saradomin to be glorified even in these dark lands.")
        goto("hub_quest_info")

        label("sara_ask")
        npc("Saradomin has granted you wisdom. The evil creatures of this land are cunning and you are right to question anyone's purposes here.")
        npc("I am upon a quest. One that will allow Saradomin to be glorified even in these dark lands.")
        goto("hub_quest_info")

        label("zammy_path")
        player("I serve only Zamorak. Strength through chaos.")
        npc("Then begone. Normally I would strike you down in an instant, but for now I have no time. I am on a quest of the utmost importance.")
        options(
            DialogueOption("zammy_quest", "What is this quest of which you speak?"),
            DialogueOption("zammy_bye", "Fine, I didn't want to talk to you either.")
        )

        label("zammy_bye")
        npc("Fear not fiend, even though I may leave you be, you shall not escape justice at the hands of the righteous.")
        goto("end")

        label("zammy_quest")
        npc("It is no concern of yours, but rest assured it will help in crushing you and those others who serve your vile master.")
        player("I doubt that.")
        npc("I would expect nothing less of you, misguided as you already are.")
        goto("end")

        label("guthix_path")
        player("My beliefs are in accord with the teachings of Guthix. Balance is power.")
        npc("A wishy washy set of beliefs. You should follow Saradomin and find a real purpose in your life.")
        options(
            DialogueOption("guthix_purpose", "And what sort of purpose would that be?"),
            DialogueOption("guthix_choices", "I can make my own choices.", skipPlayer = true)
        )

        label("guthix_choices")
        player("I can make my own choices, who are you to dispute them?")
        npc("I look only to help those who are misguided.")
        goto("end")

        label("guthix_purpose")
        npc("To further the glory of Saradomin of course. Take myself, I am upon a quest that will allow Saradomin to be glorified even in these dark lands.")
        goto("hub_quest_info")

        label("no_path")
        npc("Then your life is without reason or direction. I would talk to you further on this matter, but for now my attentions must be elsewhere.")
        options(
            DialogueOption("no_bye", "Then I shall leave you to your business."),
            DialogueOption("no_what", "Why, what are you doing?")
        )

        label("no_bye")
        npc("I shall pray that Saradomin grants you the wisdom to see through your folly.")
        goto("end")

        label("no_what")
        npc("I am upon a quest. One that will allow Saradomin to be glorified even in these dark lands.")
        goto("hub_quest_info")

        label("hub_quest_info")
        options(
            DialogueOption("explain_history", "What quest is that then?"),
            DialogueOption("bye_good_luck", "I hope it goes well. Goodbye.")
        )

        label("bye_good_luck")
        goto("end")

        label("explain_history")
        npc("Well, below the very ground you stand upon lies an intricate network of tunnels that make up the abandoned Mort Ridge mines. These mines used to be a valuable resource to the inhabitants of Morytania.")
        npc("As with many things, this changed when Misthalin was finally able to push the minions of Morytania back, and Saradomin himself blessed the river Salve.")
        npc("The mines are also the site where the legendary artefact, the salve amulet, was thought to have been carved from the crystals that grow in the lower levels of the mine.")
        npc("These crystals are thought to be formed by the blessed water of the river Salve as it percolates through the rocks to the lower levels of the mine.")
        npc("It is not known what happened to the salve amulet, but", "it is generally believed that by encapsulating within it", "some of the goodness of Saradomin himself, it afforded", "the bearer power over evil.")
        npc("Saradomin has entrusted me alone with the task of regaining access to the mines, whatever the dangers.")
        player("How can an abandoned mine be dangerous?")
        npc("Because the mines are said to be haunted. The crystals and the ridge used to be protected by Zamorak's mage, Treus Dayth.")
        npc("Dayth failed and fell during and incursion by Saradominists, who then made it into the mines and were able to remove a shard of crystal.")
        npc("Zamorak, furious at his mage, condemned Dayth's soul indefinitely to an existence of turmoil in the mines. Dayth's soul is bound to prevent the crystals from ever being reached again.")
        npc("Further, Zamorak's then ruler upon the land ordered the slaughter of anyone within the mines and that the mines be sealed.")
        exec { player, _ ->
            // quest start
            if (getQuestStage(player, Quests.HAUNTED_MINE) == 0) {
                setQuestStage(player, Quests.HAUNTED_MINE, 1)
            }
        }
        goto("hub_post_history")

        label("hub_post_history")
        options(
            DialogueOption("other_way", "Is there any other way into the mines?"),
            DialogueOption("artefact_help", "How does a lost artefact that was only legendary help?"),
            DialogueOption("why_slaughtered", "Why was everyone in the mines slaughtered?", skipPlayer = true),
            DialogueOption("must_be_going", "I must be going now.", skipPlayer = true)
        )

        label("hub_post_key")
        options(
            DialogueOption("where_entrance", "Where is the second entrance to the mines?"),
            DialogueOption("discreet_star", "How can you be discreet with THAT star emblazoned on your robes?"),
            DialogueOption(
                "borrow_key", "Can I borrow your key?", skipPlayer = true,
                optionIf = { player, _ ->
                    getAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, false)
                }),
            DialogueOption("artefact_help", "How does a lost artefact that was only legendary help?"),
            DialogueOption("why_slaughtered", "Why was everyone in the mines slaughtered?", skipPlayer = true)
        )

        label("hub_post_entrance")
        options(
            DialogueOption(
                "borrow_key", "Can I borrow your key?", skipPlayer = true,
                optionIf = { player, _ ->
                    getAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, false)
                }),
            DialogueOption("artefact_help", "How does a lost artefact that was only legendary help?"),
            DialogueOption("why_slaughtered", "Why was everyone in the mines slaughtered?", skipPlayer = true),
            DialogueOption("must_be_going", "I must be going now.", skipPlayer = true)
        )

        label("hub_post_artefact")
        options(
            DialogueOption("other_way", "Is there any other way into the mines?"),
            DialogueOption("why_slaughtered", "Why was everyone in the mines slaughtered?", skipPlayer = true),
            DialogueOption("must_be_going", "I must be going now.", skipPlayer = true)
        )

        label("hub_post_slaughter")
        options(
            DialogueOption("other_way", "Is there any other way into the mines?"),
            DialogueOption("artefact_help", "How does a lost artefact that was only legendary help?"),
            DialogueOption("must_be_going", "I must be going now.", skipPlayer = true)
        )

        label("must_be_going")
        player("I must be going now, good luck on your quest.")
        npc("You should believe not in luck, but in the protection of Saradomin. Farewell.")
        goto("end")

        label("other_way")
        npc("Indeed I have found hope that this may be so. By making discreet inquiries I have been led to believe that a second entrance may exist into the mines.")
        exec { player, _ -> setAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, true) }
        npc("Further, I have come into possession of a key that will apparently afford entrance to the lower levels of the mines once inside.")
        goto("hub_post_key")

        label("artefact_help")
        npc("Although the amulet was lost, it is my hope that if I can make into the mines I can craft another.")
        goto("hub_post_artefact")

        label("why_slaughtered")
        player("Why was everyone in the mines slaughtered? Surely it wasn't their fault that the crystals formed?")
        npc("It is said that some of the workers in the mines, merely from being in the presence of these crystals, found themselves bestowed with the love of Saradomin.")
        npc("The advantage to our cause was considered too great by some within Morytania. They set about mercilessly and brutally killing everyone in the mines rather than risk losing them to us.")
        npc("Such is the treachery of the servants of evil, that they even destroy their own kind without a moments thought.") // "moments" typo is authentic
        npc("The mines are now rumored to be haunted by the souls of all those brutally and needlessly killed.")
        goto("hub_post_slaughter")

        label("where_entrance")
        npc("Of this I am not yet sure. From what I have been able to gather, it was not originally built for humans to enter by. Where it may be, I am still endeavoring to find out.")
        goto("hub_post_entrance")

        label("discreet_star")
        npc("Saradomin watches over me, I am not troubled by those who are not meant to notice.")
        player("Even so, you're not exactly keeping quiet about your allegiance are you?")
        npc("Saradomin will protect me. Even if I should fall there are many behind me who would stand in my place.")
        options(
            DialogueOption("consolation", "Is that much consolation?", skipPlayer = true),
            DialogueOption("no_one_behind", "I can't see anyone behind you."),
            DialogueOption(
                "borrow_key", "And I would be one of them. So, can I borrow that key?", skipPlayer = true,
                optionIf = { player, _ ->
                    getAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, false)
                }),
            DialogueOption("wrong_doubt", "You're right, I was wrong to doubt Saradomin."),
            DialogueOption("nuts_bye", "You're nuts, goodbye.")
        )

        label("consolation")
        player("Is that much consolation when you get chopped up into little pieces?")
        npc("I would rather be cut into little pieces whilst proclaiming the goodness of Saradomin than hiding my faith.")
        goto("end")

        label("no_one_behind")
        npc("Although you may not realise it, Saradomin works quietly in many people, such is the folly of looking with your eyes and not your heart.")
        goto("end")

        label("wrong_doubt")
        npc("Let not your faith in Saradomin falter, for all else is insignificant.")
        goto("end")

        label("nuts_bye")
        goto("end")

        label("borrow_key")
        player("Can I borrow your key?")
        npc("I am afraid my task can be entrusted to nobody else. Saradomin has asked me alone to do this. I cannot take the slightest risk of losing this key. The stakes are too high.")
        options(
            DialogueOption("please_borrow", "Please? I'll be very careful with it."),
            DialogueOption("didnt_want_key", "I didn't want that key anyway.")
        )

        label("didnt_want_key")
        npc("Witness the goodness of Saradomin, that he has granted you the wisdom to see our positions in perspective.")
        goto("end")

        label("please_borrow")
        npc("You know not what you deal with. There is evil in that mine and, whatever your assurances, I cannot risk you falling to the creatures within.")
        options(
            DialogueOption("safer_in_mine", "And you think you'll be safer in the mine than me?"),
            DialogueOption("trade_for_it", "I'll trade for it."),
            DialogueOption("pretty_please", "Pretty pretty please?"),
            DialogueOption("didnt_want_key", "I didn't want that key anyway.")
        )

        label("safer_in_mine")
        npc("Of course I'll be safe, my faith in Saradomin protects me.")
        goto("end")

        label("trade_for_it")
        npc("Saradomin provides for my every need. There is nothing you could offer me.")
        goto("end")

        label("pretty_please")
        npc("Saradomin grant me patience. The answer is no.")
        goto("end")

        /** * * * * * * * * * * *
         * DURING THE QUEST
         ** * * * * * * * * * * */

        label("during_quest")
        player("Hello there.")
        npc("Hello again. I'm afraid I'm rather preoccupied. What was it you wanted?")
        options(
            DialogueOption("explain_history", "Can you explain the history of these mines again?"),
            DialogueOption("where_entrance", "Where is the second entrance to the mines?"),
            DialogueOption(
                "borrow_key", "Can I borrow your key?", skipPlayer = true,
                optionIf = { player, _ ->
                    getAttribute(player, HauntedMine.ATTR_KEY_MENTIONED, false)
                }),
            DialogueOption("artefact_help", "How does a lost artefact that was only legendary help?"),
            DialogueOption("why_slaughtered", "Why was everyone in the mines slaughtered?", skipPlayer = true)
        )

        /** * * * * * * * * * * *
         * AFTER THE QUEST
         ** * * * * * * * * * * */

        label("post_quest")
        options(
            DialogueOption("post_recreate", "I've done it, I've managed to recreate the salve amulet!"),
            DialogueOption("post_powers", "What powers does the salve amulet have?")
        )

        label("post_powers")
        npc("From what I have heard they would be put to best use in the slaying of the evil undead, the zombies and skeletons that walk this land.")
        player("Thanks for the tip.")
        goto("end")

        label("post_recreate")
        exec { player, _ ->
            if (getAttribute(player, HauntedMine.ATTR_KEY_RETURNED, false)) {
                loadLabel(player, "key_already_returned")
            } else {
                loadLabel(player, "key_not_returned")
            }
        }

        label("key_already_returned")
        npc("Then it is fortunate for us that Saradomin has guided you. Success certainly doesn't seem likely to have stemmed from rational behaviour on your part.")
        player("You still haven't forgiven me for borrowing your key have you?")
        npc("Borrowed!? You stole it!")
        player("I'd better be going now.")
        goto("end")

        label("key_not_returned")
        npc("That hardly seems likely. There is no way you could have made it to the bottom of the mines without the key which is in my possession.")
        options(
            DialogueOption("found_another_way", "I found another way down.", skipPlayer = true),
            DialogueOption("correction_key", "Correction, the key that was in your possession."),
            DialogueOption("fine_dont_believe", "Fine, don't believe me.")
        )

        label("found_another_way")
        player("I, er... found another way down.")
        npc("Mock me not, this is a serious task I am undertaking. I don't have the time for your halfwitted attempt at humour.")
        goto("end")

        label("fine_dont_believe")
        goto("end")

        label("correction_key")
        npc("My key, where is it? You stole it?")
        npc("Return that key to me now!")
        exec { player, _ ->
            if (inInventory(player, Items.ZEALOTS_KEY_4078)) {
                loadLabel(player, "have_key_to_return")
            } else {
                loadLabel(player, "dont_have_key_to_return")
            }
        }

        label("dont_have_key_to_return")
        player("I seem to have misplaced it, sorry.")
        npc("Arrrrrrrgh, you're unbelievable. Get out of my sight.")
        goto("end")

        label("have_key_to_return")
        options(
            DialogueOption("return_key", "Ok, I've finished with it now anyway."),
            DialogueOption("keep_key", "Actually maybe I'll just keep hold of it for now.")
        )

        label("keep_key")
        npc("Meddling fool, there are bigger things at stake here than you and your petty thieving intellect can understand.")
        goto("end")

        label("return_key")
        npc("Good, maybe all is not lost, despite your apparent lack of common sense.")
        player("Er... you're welcome?")
        exec { player, _ ->
            if (removeItem(player, Items.ZEALOTS_KEY_4078)) {
                setAttribute(player, HauntedMine.ATTR_KEY_RETURNED, true)
            }
        }
        goto("end")

        label("end")
    }
}