package content.region.karamja.quest.junglepotion

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.node.item.Item
import org.rs09.consts.Items

/**
 * Trufitus dialogue for Jungle Potion quest.
 */

class TrufitusJunglePotionDialogueFile(label : String = "start") : DialogueLabeller() {

    val startPoint = label

    override fun addConversation() {

        // runs at the very start so I can pass custom start points into the dialogue
        exec { player, _ ->
            loadLabel(player, startPoint)
        }

        // selects the start point based on the quest stage. it's labeled as start but I guess it's really not
        label("start")
        exec { player, _ ->
            val stage = getQuestStage(player, Quests.JUNGLE_POTION)
            loadLabel(player, "jpot$stage")
        }

        // ----------------- QUEST NOT STARTED ----------------- //

        label("jpot0")
        npc("Greetings Bwana! I am Trufitus Shakaya of the Tai", "Bwo Wannai village.")
        npc("Welcome to our humble village.")
        options(
            DialogueOption("bwana", "What does Bwana mean?"),
            DialogueOption("bwo", "Tai Bwo Wannai? What does that mean?"),
            DialogueOption("nice", "It's a nice village, but where is everyone?"),
        )

        label("bwana")
        npc("Gracious @g[sir,lady], it means friend. And friends come in", "peace. I assume that you come in peace?")
        options(
            DialogueOption("peace", "Yes, of course I do."),
            DialogueOption("edgy", "What does a warrior like me know about peace?"),
        )

        label("peace")
        npc("Well, that is good news, as I have a proposition for", "you.")
        options(
            DialogueOption("prop", "A proposition eh? Sounds interesting!"),
            DialogueOption("busy", "I am sorry, but I am very busy."),
        )

        label("busy")
        npc("Very well then, may your journeys bring you much joy.")
        npc("Maybe you will pass this way again and you then take up my proposal?")
        npc("But for now, fare thee well.")
        goto("end")

        label("edgy")
        npc("When you grow weary of violence and seek a more enlightened path, please pay me a visit")
        npc("as I may have a proposition for you. Now I need to attend to the plight of my people. Please excuse me...")
        goto("end")

        label("bwo")
        npc("It means 'small clearing in the jungle' but it is now the name of our village.")
        options(
            DialogueOption("bwana", "What does Bwana mean?"),
            DialogueOption("nice", "It's a nice village, but where is everyone?"),
        )

        label("nice")
        npc("My people are afraid to stay in the village. They have returned to the jungle. I need to commune with the gods to see what fate befalls us.")
        npc("You may be able to help with this.")
        goto("helpoptions")

        label("prop")
        npc("I hoped you would think so. My people are afraid to", "stay in the village.")
        npc("They have returned to the jungle and I need to", "commune with the gods")
        npc("to see what fate befalls us. You can help me by", "collecting some herbs that I need.")
        goto("helpoptions")

        label("helpoptions")
        options(
            DialogueOption("yeshelp", "Me? How Can I help?"),
            DialogueOption("notime", "I am very sorry, but I don't have time for that."),
        )

        label("notime")
        npc("Very well then, may your journeys bring you much joy.")
        npc("Maybe you will pass this way again and you then take up my proposal?")
        npc("But for now, fare thee well.")
        goto("end")

        label("yeshelp")
        npc("I need to make a special brew! A potion that helps", "to commune with the gods. For this potion, I need", "special herbs, that are only found deep in the jungle.")
        npc("I can only guide you so far as the herbs are not easy", "to find. With some luck, you will find each herb in turn", "and bring it to me. I will then give you details of where", "to find the next herb.")
        npc("In return for this great favour I will give you training", "in Herblore.")
        options(
            DialogueOption("notready", "Hmmm, sounds difficult, I don't know if I am ready for the challenge."),
            DialogueOption("challengeaccepted", "It sounds like just the challenge for me.", "It sounds like just the challenge for me. And it would make a nice break from killing things!"),
        )

        label("notready")
        npc("Very well then Bwana, maybe you will return to me invigorated and ready to take up the challenge one day?")
        goto("end")

        label("challengeaccepted")
        exec { player, _ ->
            setQuestStage(player, Quests.JUNGLE_POTION, 1)
        }
        npc("That is excellent Bwana! The first herb that you need", "to gather is called")
        npc("Snake Weed.")
        npc("It grows near vines in an area to the south west where")
        npc("the ground turns soft and the water kisses your feet.")
        goto("end")

        // ----------------- QUEST STARTED ----------------- //

        // ----------------- SNAKE WEED ----------------- //

        // need to pick snakeweed
        label("jpot1")
        npc("Hello Bwana, do you have the Snake Weed?")
        exec { player, _ ->
            if (inInventory(player, Items.CLEAN_SNAKE_WEED_1526)) {
                loadLabel(player, "snakelie")
            } else loadLabel(player, "nopicksnake")
        }

        label("snakelie")
        player("Of course!")
        goto("fakesnake")

        label("fakesnake")
        npc("That's not fresh Snake Weed, did you pick it yourself? Go get me some fresh Snake Weed and remember to pick it yourself.")
        goto("end")

        label("nopicksnake")
        player("Not yet, sorry, what's the clue again?")
        goto("snakeclue")

        // picked snakeweed
        label("jpot2")
        npc("Hello Bwana, do you have the Snake Weed?")
        options(
            DialogueOption("snakecheck", "Of course!"),
            DialogueOption("snakeclue", "Not yet, sorry, what's the clue again?"),
        )

        label("snakecheck")
        exec { player, _ ->
            if (getQuestStage(player, Quests.JUNGLE_POTION) <= 1) {
                loadLabel(player, "fakesnake")
            } else {
                if (removeItem(player, Items.CLEAN_SNAKE_WEED_1526)) {
                    sendMessage(player, "You hand the Snake Weed over to Trufitus.")
                    val qStage = getQuestStage(player, Quests.JUNGLE_POTION)
                    if (qStage == 2) setQuestStage(player, Quests.JUNGLE_POTION, 3)
                    loadLabel(player, "snakeinmyboot")
                } else if (inInventory(player, Items.GRIMY_SNAKE_WEED_1525)) {
                    loadLabel(player, "dirtyherb")
                } else {
                    loadLabel(player, "nosnakes")
                }
            }
        }

        label("snakeclue")
        npc("It grows near vines in an area to the south west where the ground turns soft and the water kisses your feet.")
        npc("I really need that Snake Weed if I am to make this potion.")
        goto("end")

        label("nosnakes")
        npc("Please don't try to deceive me.")
        npc("I really need that Snake Weed if I am to make this potion.")
        goto("end")

        label("snakeinmyboot")
        item(Item(Items.CLEAN_SNAKE_WEED_1526), "You give the Snake Weed to Trufitus.")
        npc("Great, you have the Snake Weed! Many thanks. Ok,", "the next herb is called Ardrigal. It is related to the palm", "and grows to the east in its brother's shady profusion.")
        npc("To the east you will find a small peninsula, it is just", "after the cliffs come down to meet the sands, here is", "where you should search for it.")
        goto("end")

        // ----------------- ARDRIGAL ----------------- //

        // given snakeweed, need to pick ardrigal
        label("jpot3")
        npc("Hello Bwana, do you have the Ardrigal?")
        exec { player, _ ->
            if (inInventory(player, Items.CLEAN_ARDRIGAL_1528)) {
                loadLabel(player, "ardylie")
            } else loadLabel(player, "nopickardy")
        }

        label("ardylie")
        player("Of course!")
        goto("fakeardy")

        label("fakeardy")
        npc("That's not fresh Ardrigal, did you pick it yourself? Go get me some fresh Ardrigal and remember to pick it yourself.")
        goto("end")

        label("nopickardy")
        player("Not yet, sorry, what's the clue again?")
        goto("ardrigalclue")

        // picked ardrigal
        label("jpot4")
        npc("Hello Bwana, do you have the Ardrigal?")
        options(
            DialogueOption("ardrigalcheck", "Of course!"),
            DialogueOption("ardrigalclue", "Not yet, sorry, what's the clue again?"),
        )

        label("ardrigalcheck")
        exec { player, _ ->
            if (getQuestStage(player, Quests.JUNGLE_POTION) <= 3) {
                loadLabel(player, "fakeardy")
            } else {
                if (removeItem(player, Items.CLEAN_ARDRIGAL_1528)) {
                    sendMessage(player, "You hand the Ardrigal over to Trufitus.")
                    val qStage = getQuestStage(player, Quests.JUNGLE_POTION)
                    if (qStage == 4) setQuestStage(player, Quests.JUNGLE_POTION, 5)
                    loadLabel(player, "ardrigalsuccess")
                } else if (inInventory(player, Items.GRIMY_ARDRIGAL_1527)) {
                    loadLabel(player, "dirtyherb")
                } else {
                    loadLabel(player, "noardy")
                }
            }
        }

        label("ardrigalclue")
        npc("You are looking for Ardrigal. It is related to the palm", "and grows in its brothers shady profusion.") // authentic lack of apostrophe
        npc("I really need that Ardrigal if I am to make this potion.")
        goto("end")

        label("noardy")
        npc("Please don't try to deceive me.")
        npc("I really need that Ardrigal if I am to make this potion.")
        goto("end")

        label("ardrigalsuccess")
        item(Item(Items.CLEAN_ARDRIGAL_1528), "You give the Ardrigal to Trufitus.")
        npc("Great, you have the Ardrigal! Many thanks.")
        npc("You are doing well Bwana. The next herb is called Sito", "Foil, and it grows best where the ground has been", "blackened by the living flame.")
        goto("end")

        // ----------------- SITO FOIL ----------------- //

        // given ardrigal, need to pick sito foil
        label("jpot5")
        npc("Hello Bwana, do you have the Sito Foil?")
        exec { player, _ ->
            if (inInventory(player, Items.CLEAN_SITO_FOIL_1530)) {
                loadLabel(player, "sitolie")
            } else loadLabel(player, "nopicksito")
        }

        label("sitolie")
        player("Of course!")
        goto("fakesito")

        label("fakesito")
        npc("That's not fresh Sito Foil, did you pick it yourself? Go get me some fresh Sito Foil and remember to pick it yourself.")
        goto("end")

        label("nopicksito")
        player("Not yet, sorry, what's the clue again?")
        goto("sitofoilclue")

        // picked sito foil
        label("jpot6")
        npc("Hello Bwana, do you have the Sito Foil?")
        options(
            DialogueOption("sitofoilcheck", "Of course!"),
            DialogueOption("sitofoilclue", "Not yet, sorry, what's the clue again?"),
        )

        label("sitofoilcheck")
        exec { player, _ ->
            if (getQuestStage(player, Quests.JUNGLE_POTION) <= 5) {
                loadLabel(player, "fakesito")
            } else {
                if (removeItem(player, Items.CLEAN_SITO_FOIL_1530)) {
                    sendMessage(player, "You hand the Sito Foil over to Trufitus.")
                    val qStage = getQuestStage(player, Quests.JUNGLE_POTION)
                    if (qStage == 6) setQuestStage(player, Quests.JUNGLE_POTION, 7)
                    loadLabel(player, "sitofoilsuccess")
                } else if (inInventory(player, Items.GRIMY_SITO_FOIL_1529)) {
                    loadLabel(player, "dirtyherb")
                } else {
                    loadLabel(player, "nosito")
                }
            }
        }

        label("sitofoilclue")
        npc("You are looking for Sito Foil, and it grows best where the ground has been blackened by the living flame.")
        npc("I really need that Sito Foil if I am to make this potion.")
        goto("end")

        label("nosito")
        npc("Please don't try to deceive me.")
        npc("I really need that Sito Foil if I am to make this potion.")
        goto("end")

        label("sitofoilsuccess")
        item(Item(Items.CLEAN_SITO_FOIL_1530), "You give the Sito Foil to Trufitus.")
        npc("Well done Bwana, just two more herbs to collect.")
        npc("The next herb is called Volencia Moss. It clings to", "rocks for its existence. It is difficult to see, so you", "must search for it well.")
        npc("It prefers rocks of high metal content and a frequently", "disturbed environment. There is some, I believe to the", "south east of this village.")
        goto("end")

        // ----------------- VOLENCIA MOSS ----------------- //

        // given sito foil, need to pick volencia moss
        label("jpot7")
        npc("Hello Bwana, do you have the Volencia Moss?")
        exec { player, _ ->
            if (inInventory(player, Items.CLEAN_VOLENCIA_MOSS_1532)) {
                loadLabel(player, "vollylie")
            } else loadLabel(player, "nopickvolly")
        }

        label("vollylie")
        player("Of course!")
        goto("fakevolly")

        label("fakevolly")
        npc("That's not fresh Volencia Moss, did you pick it yourself? Go get me some fresh Volencia Moss and remember to pick it yourself.")
        goto("end")

        label("nopickvolly")
        player("Not yet, sorry, what's the clue again?")
        goto("volenciamossclue")

        // picked valencia moss
        label("jpot8")
        npc("Hello Bwana, do you have the Volencia Moss?")
        options(
            DialogueOption("volenciamosscheck", "Of course!"),
            DialogueOption("volenciamossclue", "Not yet, sorry, what's the clue again?"),
        )

        label("volenciamosscheck")
        exec { player, _ ->
            if (getQuestStage(player, Quests.JUNGLE_POTION) <= 7) {
                loadLabel(player, "fakevolly")
            } else {
                if (removeItem(player, Items.CLEAN_VOLENCIA_MOSS_1532)) {
                    sendMessage(player, "You hand the Volencia Moss over to Trufitus.")
                    val qStage = getQuestStage(player, Quests.JUNGLE_POTION)
                    if (qStage == 8) setQuestStage(player, Quests.JUNGLE_POTION, 9)
                    loadLabel(player, "volenciamosssuccess")
                } else if (inInventory(player, Items.GRIMY_VOLENCIA_MOSS_1531)) {
                    loadLabel(player, "dirtyherb")
                } else {
                    loadLabel(player, "novolenciamoss")
                }
            }
        }

        label("volenciamossclue")
        npc("You are looking for Volencia Moss. It clings to rocks for its existence. It is difficult to see, so you must search for it well")
        npc("It prefers rocks of high metal content and a frequently disturbed environment. There is some, I believe to the south east of this village.")
        npc("I really need that Volencia Moss if I am to make this potion.")
        goto("end")

        label("novolenciamoss")
        npc("Please don't try to deceive me.")
        npc("I really need that Volencia Moss if I am to make this potion.")
        goto("end")

        label("volenciamosssuccess")
        item(Item(Items.CLEAN_VOLENCIA_MOSS_1532), "You give the Volencia Moss to Trufitus.")
        npc("Ah Volencia Moss, beautiful. One final herb and the", "potion will be complete.")
        npc("This is the most difficult to find as it is inhabits the", "darkness of the underground. It is called Rogue's", "Purse, and is only to be found in pothole caverns")
        npc("in the northern part of this island. A secret entrance to", "the pothole caverns is set into the northern cliffs of this land.", "Take care Bwana as it may be dangerous.")
        goto("end")

        // ----------------- ROGUE'S PURSE ----------------- //

        // given valencia moss, need to pick rogue's purse
        label("jpot9")
        npc("Hello Bwana, do you have the Rogue's Purse?")
        exec { player, _ ->
            if (inInventory(player, Items.CLEAN_ROGUES_PURSE_1534)) {
                loadLabel(player, "roguelie")
            } else loadLabel(player, "nopickrogue")
        }

        label("roguelie")
        player("Of course!")
        goto("fakerogue")

        label("fakerogue")
        npc("That's not fresh Rogue's Purse, did you pick it yourself? Go get me some fresh Rogue's Purse and remember to pick it yourself.")
        goto("end")

        label("nopickrogue")
        player("Not yet, sorry, what's the clue again?")
        goto("roguespurseclue")

        // picked rogue's purse
        label("jpot10")
        npc("Hello Bwana, do you have the Rogue's Purse?")
        options(
            DialogueOption("roguespursecheck", "Of course!"),
            DialogueOption("roguespurseclue", "Not yet, sorry, what's the clue again?"),
        )

        label("roguespursecheck")
        exec { player, _ ->
            if (getQuestStage(player, Quests.JUNGLE_POTION) <= 9) {
                loadLabel(player, "fakerogue")
            } else {
                if (removeItem(player, Items.CLEAN_ROGUES_PURSE_1534)) {
                    sendMessage(player, "You hand the Rogue's Purse over to Trufitus.")
                    val qStage = getQuestStage(player, Quests.JUNGLE_POTION)
                    if (qStage == 10) setQuestStage(player, Quests.JUNGLE_POTION, 11)
                    loadLabel(player, "roguespursesuccess")
                } else if (inInventory(player, Items.GRIMY_ROGUES_PURSE_1533)) {
                    loadLabel(player, "dirtyherb")
                } else {
                    loadLabel(player, "noardy")
                }
            }
        }

        label("roguespurseclue")
        npc("You are looking for Rogue's Purse.")
        npc("It inhabits the darkness of the underground, and grows in caverns to the north. A secret entrance to the caverns is set into the northern cliffs, be careful Bwana.")
        npc("I really need that Rogue's Purse if I am to make this potion.")
        goto("end")

        label("noroguespurse")
        npc("Please don't try to deceive me.")
        npc("I really need that Rogue's Purse if I am to make this potion.")
        goto("end")

        label("roguespursesuccess")
        item(Item(Items.CLEAN_ROGUES_PURSE_1534), "You give the Rogue's Purse to Trufitus.")
        goto("jpot11")

        // given rogue's purse, quest complete!
        label("jpot11")
        npc("Most excellent Bwana! You have returned all the herbs", "to me and, I can finish the preparations for the potion,", "and at last divine with the gods.")
        npc("Many blessings on you! I must now prepare, please", "excuse me while I make the arrangements.")
        line("Trufitus shows you some techniques in Herblore. You gain some", "experience in Herblore.")
        exec { player, _ ->
            finishQuest(player, Quests.JUNGLE_POTION)
        }
        goto("end")

        // ----------------- OTHER ----------------- //

        // after the quest
        label("jpot100")
        npc("My greatest respect Bwana, I have communed with", "my gods and the future")
        npc("looks good for my people. We are happy now that the", "gods are not angry with us.")
        npc("With some blessings we will be safe here.")
        npc("You should deliver the good news to Bwana Timfraku,", "Chief of Tai Bwo Wannai. He lives in a raised hut not", "far from here.")
        goto("end")

        // if you try and turn in a dirty herb
        label("dirtyherb")
        npc("Sorry, Bwana, that herb is so dirty that I can't even tell whether it is fresh. Please clean it first.")
        goto("end")

        // if you turn in random shit to him
        label("sorrybwana")
        npc("I'm sorry Bwana but I just don't have a use for that!")
        goto("end")
    }
}

