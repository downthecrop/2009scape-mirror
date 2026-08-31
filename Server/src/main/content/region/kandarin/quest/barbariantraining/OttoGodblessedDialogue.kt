package content.region.kandarin.quest.barbariantraining

import content.data.Quests
import content.region.fremennik.diary.FremennikAchievementDiary.Companion.EasyTasks
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Barbarian Training state is stored in 7 persistent attributes (stages 0-3 each):
 *
 * Stage values: 0 = not started, 1 = started, 2 = ready to turn in, 3 = done
 *
 * Varbits are set when the primary training in each category
 * is initialized, to satisfy checks found in CS2 scripts.
 *
 * @author Bishop
 */

class OttoGodblessedDialogue : InteractionListener {
    override fun defineListeners() {
        on(NPCs.OTTO_GODBLESSED_2725, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, OttoGodblessedDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class OttoGodblessedDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        // Parse training status from attributes
        val rodStage      = getAttribute(player!!, BarbarianTraining.attributeRod, 0)
        val fistStage     = getAttribute(player!!, BarbarianTraining.attributeFist, 0)
        val herbloreStage = getAttribute(player!!, BarbarianTraining.attributeHerblore, 0)
        val spearStage    = getAttribute(player!!, BarbarianTraining.attributeSpear, 0)
        val hastaStage    = getAttribute(player!!, BarbarianTraining.attributeHasta, 0)
        val bowfireStage  = getAttribute(player!!, BarbarianTraining.attributeBowFire, 0)
        val pyreshipStage = getAttribute(player!!, BarbarianTraining.attributePyreShip, 0)

        val anyStarted = (rodStage + fistStage + herbloreStage + spearStage + hastaStage + bowfireStage + pyreshipStage) > 0
        val allDone = rodStage >= 3 && fistStage >= 3 && herbloreStage >= 3 && spearStage >= 3 && hastaStage >= 3 && bowfireStage >= 3 && pyreshipStage >= 3
        val pyreshipStarted = pyreshipStage >= 1

        label("initialize")
            exec { player, _ ->
                when {
                    !anyStarted -> loadLabel(player, "first_time")
                    allDone -> loadLabel(player, "miniquest_complete")
                    pyreshipStarted -> loadLabel(player, "notes_check_beginning")
                    else -> loadLabel(player, "book_check_beginning")
                }
            }

        label("first_time")
            npc(ChatAnim.ASKING, "Good day, you seem a hearty warrior. Maybe even some barbarian blood in that body of yours?")
            options(
                DialogueOption("related", "I really don't think I am related to any barbarian."),
                DialogueOption("i_am_barb", "You think so?", expression = ChatAnim.ASKING),
                DialogueOption("who", "Who are you?", "Who are you and why are you here?", expression = ChatAnim.ASKING),
                DialogueOption("busy", "Sorry, I'm too busy to talk about genealogy today.")
            )

        label("first_time_minus")
            options(
                DialogueOption("related", "I really don't think I am related to any barbarian."),
                DialogueOption("i_am_barb", "You think so?", expression = ChatAnim.ASKING),
                DialogueOption("busy", "Sorry, I'm too busy to talk about genealogy today.")
            )

        label("related") // Sourced from RS3 transcript
            npc("Your scepticism will be your loss.")
            goto("end")

        label("i_am_barb")
            npc("Who can tell? My forefathers weren't averse to","travelling, so it is possible. They tended to cause too","much trouble in your so-called civilised lands, however,","so most returned to their ancestral lands.")
            npc("In any case, I think you are ready to learn our more","secret tribal feats for yourself.")
            player(ChatAnim.ASKING, "Oh, that sounds interesting, what sort of skills would","these be?")
            npc("To begin with I can supply knowledge in the ways of","firemaking, our special rod fishing tricks and a selection","of spear skills.")
            player(ChatAnim.ASKING, "There are more advanced stages though, to judge by","your description?")
            npc("Your perception is creditable. I can eventually teach of","more advanced firemaking techniques, and the rod","fishing skills are but a preliminary to our special potions","and brews.")
            npc("These secrets must, however, wait until you have","learned of the more basic skills.")
            goto("training_menu")

        label("who") // Sourced from transcript
            npc("Me? I am Otto, known as the Godblessed. I follow the ways of our barbarian ancestors, communing with the spirits of the dead and teaching their ways to worthy disciples.")
            npc("Maybe there's even some barbarian blood in that body of yours? You look a likely sort to learn of these ways.")
            goto("first_time_minus")

        label("busy") // Sourced from transcript
            npc("We will talk when you learn to be less impetuous.")
            goto("end")


        label("notes_check_beginning")
            exec { player, _ ->
                if (inInventory(player, Items.MY_NOTES_11339) || inBank(player, Items.MY_NOTES_11339)) {
                    loadLabel(player, "book_check_beginning")
                } else {
                    loadLabel(player, "need_notes_beginning")
                }
            }

        label("need_notes_beginning") // Sourced from RS3 transcript
            npc("I see you have free space and no way to record any", "information you may recover from the caverns.")
            npc("Please take this book as a record of your", "researches - between the spirits and your diligence", "I expect it will always be up to date.")
            exec { player, _ ->
                addItemOrDrop(player, Items.MY_NOTES_11339, 1)
            }
            player(ChatAnim.ASKING, "Why might I need such a thing? I already have a book of notes.")
            npc("Those who have previously entered the cavern have not all returned, some of them seemed intent upon research and discovery as much as loot and daring deeds.")
            npc("It would be a shame if you were to recover any of their notes and be unable to record them handily. I hope the new book will give you space for such discoveries.")
            goto("book_check_beginning")


        label("book_check_beginning")
            exec { player, _ ->
                if (inInventory(player, Items.BARBARIAN_SKILLS_11340) || inBank(player, Items.BARBARIAN_SKILLS_11340)) {
                    loadLabel(player, "training_menu")
                } else {
                    loadLabel(player, "need_book_beginning")
                }
            }

        label("need_book_beginning")
            npc(ChatAnim.ASKING, "I see you have free space and no record of the tasks I","have set you. Please take this book as a record of your","progress - between the spirits and your diligence I","expect it will always be up to date.")
            exec { player, _ ->
                addItemOrDrop(player, Items.BARBARIAN_SKILLS_11340, 1)
            }
            goto("training_menu")


        label("training_menu")

            val trainingOptions = mutableListOf<DialogueOption>()

            when {
                fistStage < 3 -> trainingOptions.add(
                    DialogueOption("fishing_fist", "Please, supply me details of your cunning with harpoons.", expression = ChatAnim.ASKING)
                )
                spearStage < 3 -> trainingOptions.add(
                    DialogueOption("smithing_spear", "Tell me more about the use of spears.", expression = ChatAnim.ASKING)
                )
                hastaStage < 3 -> trainingOptions.add(
                    DialogueOption("smithing_hasta", "What of the one-handed spears of which you spoke?", expression = ChatAnim.ASKING)
                )
            }

            when {
                rodStage < 3 -> trainingOptions.add(
                    DialogueOption("fishing_rod", "Are there any ways to use a fishing rod which I might learn?", expression = ChatAnim.ASKING)
                )
                herbloreStage < 3 -> trainingOptions.add(
                    DialogueOption("herblore", "What was that secret knowledge of Herblore we talked of?", expression = ChatAnim.ASKING)
                )
            }

            when {
                bowfireStage < 3 -> trainingOptions.add(
                    DialogueOption("bow_fire", "My mind is ready for your Firemaking wisdom, please instruct me.", expression = ChatAnim.ASKING)
                )
                pyreshipStage < 3 -> trainingOptions.add(
                    DialogueOption("pyre_ship", if (pyreshipStage == 2) "I've created a pyre ship!" else "I have completed Firemaking with a bow. What follows this?", expression = ChatAnim.ASKING)
                )
            }

            if (pyreshipStage in 1..2) { trainingOptions.add(
                DialogueOption("spirits", "Could you tell me more of these spirits you continually refer to?", expression = ChatAnim.ASKING)
                )
            }

            // If only one training topic remains, skip the menu and go there directly
            exec { player, _ ->
                if (trainingOptions.size == 1) {
                    loadLabel(player, trainingOptions[0].goto)
                    return@exec
                }
            }

            options(*trainingOptions.toTypedArray())


        label("fishing_fist")
            exec { player, _ ->
                when (fistStage) {
                    0 -> loadLabel(player, "fishing_fist_teach")
                    1 -> loadLabel(player, "fishing_fist_remind")
                    2 -> loadLabel(player, "fishing_fist_final")
                }
            }

        label("fishing_fist_teach")
            npc("First you must know more of harpoons through special","study of fish that are usually caught with such a device.")
            player(ChatAnim.ASKING, "What do I need to know?")
            npc("You must catch fish which are usually harpooned,","without a harpoon. You will be using your skill and","strength.")
            player(ChatAnim.ASKING, "How do you expect me to do that?")
            npc("Use your arm as bait. Wriggle your fingers as if they","are a tasty snack and hungry tuna and swordfish will","throng to be caught by you.")
            player(ChatAnim.ASKING, "That sounds rather insanely dangerous. I'm glad you","didn't mention shark too.")
            npc("Oh, my mind slipped for a moment, this method does","indeed work with shark - though in this case the action","must be more of a frenzied thrashing of the arm than a","wriggle.")
            player("...and I thought Fishing was a safe way to pass the time.")
            exec { player, _ ->
                setAttribute(player, BarbarianTraining.attributeFist, 1)
            }
            goto("return_menu")

        label("fishing_fist_remind") // Sourced from RS3 transcript
            npc("I see you need encouragement in learning the ways of Fishing without a harpoon.")
            player("It's not the most safe-sounding task I have ever been given.")
            npc("Neither is it the most deadly. Go now and return when you have drawn a fish from the sea, with sheer strength and knowledge of our scaled prey.")
            goto("return_menu")

        label("fishing_fist_final")
            npc("I sense you have more understanding of spears through your studies.")
            player(ChatAnim.ASKING, "Well it was interesting, can I move on to more sane","hobbies now?")
            npc("Nothing is sane when one is born knowing only certain","death. The spirits approve of you however, so death is","on your side.")
            player(ChatAnim.ASKING, "What is that supposed to mean?")
            npc("I mean that when you eventually die and find peace, at","least the spirits you encounter will be your friends. Alas","for you adventurous sort, for whom this natural passing","is impossible.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributeFist, 0) == 2) {
                    setAttribute(player, BarbarianTraining.attributeFist, 3)
                }
            }
            goto("return_menu")

        label("fishing_rod")
            exec { player, _ ->
                when (rodStage) {
                    0 -> loadLabel(player, "fishing_rod_teach")
                    1 -> loadLabel(player, "fishing_rod_remind")
                    2 -> loadLabel(player, "fishing_rod_final")
                }
            }

        label("fishing_rod_teach")
            npc("While you civilised folk use small, weak fishing rods, we","barbarians are skilled with heavier tackle. We fish in the","lake nearby.")
            player(ChatAnim.ASKING, "So can you teach me of this Fishing method?")
            npc("Certainly. Take the rod from under the bed in my","dwelling and fish in the lake. When you have caught a","few fish I am sure you will be ready to talk more with","me.")
            npc("You will know when you are ready, since inspiration will","fill your mind.")
            player(ChatAnim.ASKING, "So I can obtain new foods from these Fishing spots?")
            npc("We do not use these fish quite as you might expect.","When you return from Fishing we can speak more of","this matter.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributeRod, 0) == 0) {
                    setAttribute(player, BarbarianTraining.attributeRod, 1)
                    setVarbit(player, 3757, 1, true)
                }
                player.achievementDiaryManager.finishTask(player, DiaryType.FREMENNIK,0, EasyTasks.OTTO_GODBLESSED_LEARN_BARBARIAN_FISHING)
            }
            goto("return_menu")

        label("fishing_rod_remind") // Sourced from RS3 transcript
            npc("Alas, I do not sense that you have been successful in your Fishing yet. The look in your eyes is not that of the osprey.")
            player(ChatAnim.ASKING, "Osprey?")
            npc("Legendary birds, which the ignorant call eagles. They prey upon fish. Do as they do to gain inspiration.")
            goto("return_menu")

        label("fishing_rod_final") // Sourced from book in video and RS3 transcript
            npc("Your mind is as clear as the waters you have fished. This is good.")
            player(ChatAnim.ASKING, "So what do I do with these fish? They are all bloated and so nasty looking that even a starving vulture wouldn't eat one.")
            npc("Patience young one. These are fish which are fat with eggs rather than fat of flesh; this is what we will make use of.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributeRod, 0) == 2) {
                    setAttribute(player, BarbarianTraining.attributeRod, 3)
                }
            }
            goto("return_menu")


        label("herblore")
            exec { player, _ ->
                when {
                    herbloreStage == 0 -> loadLabel(player, "herblore_teach")
                    herbloreStage == 1 -> loadLabel(player, "herblore_remind")
                    herbloreStage == 2 && inInventory(player, Items.ATTACK_MIX2_11429) -> loadLabel(player, "herblore_final")
                    else -> loadLabel(player, "herblore_remind")
                }
            }

        label("herblore_teach") // Otto lines sourced from book in video, player lines sourced from OSRS transcript
            npc("If you use your knife upon the fat fish, several new fishy items will be produced. Fish parts can be used as bait; the roe or caviar is more useful for us.")
            player(ChatAnim.ASKING, "Do I cook them?")
            npc("Mixing these items with two dose potions is what should be performed. This results in a nutritious slop, perfect for healing as well as imparting the effect of the potion.")
            npc("Roe can only be used for some of the more easily combined mixes, while caviar may be used for any of the mixes of which I am aware.")
            player(ChatAnim.ASKING, "What if I only have four dose potions?")
            npc("You will discover that you are able to decant a four dose potion into an empty vial, this giving two potions of two doses. This will aid you in the process.")
            player(ChatAnim.ASKING, "I guess you just want me to go away and make one of these potions?")
            npc("In this case I in fact require a potion, for my own stocks. Bring me a lesser attack potion combined with fish roe.")
            exec { player, _ ->
                setAttribute(player, BarbarianTraining.attributeHerblore, 1)
                setVarbit(player, 3761, 1, true)
            }
            goto("return_menu")

        label("herblore_remind")
            npc(ChatAnim.ASKING, "Do you have my potion?")
            player(ChatAnim.ASKING, "What was it you needed again?")
            npc("Bring me a lesser attack potion combined with fish roe.","There is more importance in this than you will ever","know.")
            goto("return_menu")

        label("herblore_final")
            npc(ChatAnim.HAPPY, "I see you have my potion. I will say no more than that","I am eternally grateful.")
            exec { player, _ ->
                if (removeItem(player, Item(Items.ATTACK_MIX2_11429, 1))) {
                    setAttribute(player, BarbarianTraining.attributeHerblore, 3)
                }
            }
            player(ChatAnim.ASKING, "I feel I am missing some vital information about your","need for this potion, though I often have this suspicion.")
            npc("I will not reveal all of my private matters to you. Some","secrets are best kept rather than revealed.")
            goto("return_menu")


        label("smithing_spear")
            exec { player, _ -> // TODO: convert to a TBWT stage requirement once TBWT is implemented
                if (!hasRequirement(player, Quests.TAI_BWO_WANNAI_TRIO)) loadLabel(player, "smithing_spear_rejection")
                else when (spearStage) {
                    0 -> loadLabel(player, "smithing_spear_teach")
                    1 -> loadLabel(player, "smithing_spear_remind")
                    2 -> loadLabel(player, "smithing_spear_final")
                }
            }

        label("smithing_spear_rejection") // Sourced from book in video and RS3 transcript
            npc("The next step is to manufacture a spear, suitable for combat. Our distant cousins on Karamja are in need of help, however, and you must aid them before I can aid you.")
            npc("You must go and complete the Tai Bwo Wannai Trio quest.")
            player("Couldn't you just take a bribe or something? I am sure you could do with some cash.")
            npc("I am afraid this is a vital step; the spirits foresee that your understanding of spears will increase through this quest. You may not progress in the use of spears until you have completed this mission.")
            exec { player, _ ->
                setAttribute(player, BarbarianTraining.attributeSpearRejected, 1)
            }
            goto("return_menu")

        label("smithing_spear_teach")
            npc("Many warriors complain that spears are difficult to find,","we barbarians thus forge our own.")
            player(ChatAnim.ASKING, "Can I just use some metal on an anvil, or is there","something else involved?")
            npc("If you use our special barbarian anvil here, you will","find it ideal. Other anvils are not sturdy enough or","shaped appropriately for the forging work involved.","Make any of our spears and return.")
            npc("Note well that you will require wood for the spear","shafts; the quality of wood must be similar to that of the","metal involved.")
            exec { player, _ ->
                removeAttribute(player, BarbarianTraining.attributeSpearRejected)
                setAttribute(player, BarbarianTraining.attributeSpear, 1)
                setVarbit(player, 3763, 2, true)
                }
            goto("return_menu")

        label("smithing_spear_remind") // Sourced from OSRS transcript
            npc("You do not exude the presence of one who has poured his soul into manufacturing spears.")
            player(ChatAnim.ASKING, "The spirits told you?")
            npc("Indeed they did. There are no secrets from those who live eternally.")
            goto("return_menu")

        label("smithing_spear_final")
            npc("The manufacture of spears is now yours as a specialty.","Use your skill well. In addition I am ready to reveal","one-handed spear crafts.")
            player("This sounds interesting indeed.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributeSpear, 0) == 2) {
                    setAttribute(player, BarbarianTraining.attributeSpear, 3)
                }
            }
            goto("return_menu")


        label("smithing_hasta")
            exec { player, _ ->
                when (hastaStage) {
                    0 -> loadLabel(player, "smithing_hasta_teach")
                    1 -> loadLabel(player, "smithing_hasta_remind")
                    2 -> loadLabel(player, "smithing_hasta_final")
                }
            }

        label("smithing_hasta_teach")
            npc("The next step is to manufacture a one-handed version","of a spear, suitable for combat. Such a spear is known","to us as a hasta.")
            player(ChatAnim.ASKING, "Can't I just pick one up and use it in one hand?")
            npc("As you might suspect, our ways require greater","understanding than is gained by simply looking at a","weapon.","It is also the case that the process involves a differently")
            npc("balanced spear.","Before you may use such a weapon in anger, you","must make an example. Only then will you fully","understand the poise and techniques involved.")
            player(ChatAnim.ASKING, "So I must manufacture one? I see. This makes sense.")
            npc("Indeed. You may use our special anvil for this spear","type too. The ways of black and dragon spears are","beyond our knowledge, however; these spears may not","be created.")
            exec { player, _ ->
                setAttribute(player, BarbarianTraining.attributeHasta, 1)
                }
            goto("return_menu")

        label("smithing_hasta_remind") // Sourced from OSRS transcript
            npc("Take some wood and metal and make a spear upon the nearby anvil, then you may return to me. As an example, you may use bronze bars with normal logs or iron bars with oak logs. If you perform this task I will")
            npc("be freed from the spirits complaining of your lack of dedication.")
            player("I'll be back.")
            goto("return_menu")

        label("smithing_hasta_final") // Sourced from book, may be incomplete
            npc("I see you have constructed your hasta, and are approaching readiness to live life to its fullest - that you may be a peaceful spirit when your time ends.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributeHasta, 0) == 2) {
                    setAttribute(player, BarbarianTraining.attributeHasta, 3)
                }
            }
            goto("return_menu")


        label("bow_fire")
            exec { player, _ ->
                when (bowfireStage) {
                    0 -> loadLabel(player, "bow_fire_teach")
                    1 -> loadLabel(player, "bow_fire_remind")
                    2 -> loadLabel(player, "bow_fire_final")
                }
            }

        label("bow_fire_teach")
            npc("The first point in your progression is that of lighting","fires without a tinderbox.")
            player(ChatAnim.ASKING, "That sounds pretty useful, tell me more.")
            npc("For this process you will require a strung bow. You","use the bow to quickly rotate pieces of wood against one","another. As you rub the wood becomes hot, eventually","springing into flame.")
            player(ChatAnim.ASKING, "No more secret details?")
            npc("The spirits will aid you, the power they supply will guide","your hands. Go and benefit from their guidance upon","an oaken log.")
            exec { player, _ ->
                setAttribute(player, BarbarianTraining.attributeBowFire, 1)
                setVarbit(player, 3764, 1, true)
            }
            goto("return_menu")

        label("bow_fire_remind")
            npc("By now you know my response.")
            player(ChatAnim.ASKING, "Go and light some oak logs using a strung bow?")
            npc("Exactly.")
            goto("return_menu")

        label("bow_fire_final")
            player(ChatAnim.AMAZED, "The Firemaking with my bow worked!")
            npc("Fine news indeed, secrets of our spirit boats now await","your attention.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributeBowFire, 0) == 2) {
                    setAttribute(player, BarbarianTraining.attributeBowFire, 3)
                }
            }
            goto("return_menu")


        label("pyre_ship")
            exec { player, _ ->
                when (pyreshipStage) {
                    0 -> loadLabel(player, "pyre_ship_teach")
                    1 -> loadLabel(player, "pyre_ship_remind")
                    2 -> loadLabel(player, "pyre_ship_final")
                }
            }

        label("pyre_ship_teach")
            npc("The next stage is quite complex, so listen well. In order","to send our ancestors into the spirit world, their mortal","remains must be burned with due ceremony. This can","only be performed close to the water on the shore of")
            npc("the lake, just to our north-east. You will recognise the","correct places by the ashes to be seen there.")
            npc("You will need to construct a small ship by using an axe","upon logs in this area, then add the bones of a long","dead barbarian hero.")
            player(ChatAnim.ASKING, "Where do I obtain these bones?")
            npc("From the caverns beneath this lake. Many of our","ancestors travelled to these caverns in order to hunt","for glory and found only death. Their bones must still","lie inside, their spirits trapped in torment.")
            player(ChatAnim.ASKING, "So I make sure I am in the right place, then throw","these bones on a ceremonial ship before I set light to it?")
            npc("Correct. The spirit will ascend to glory; the pyre will","send the earthly remains to the depths. You will also","obtain a closer link to the spirit world. During this","heightened contact, any bones you bury will have")
            npc("increased importance to the gods. The number of bones","that may be buried, before the link fades, is increased","with the difficulty of obtaining the wood which you use.")
            player(ChatAnim.ASKING, "Do you foresee any problems?")
            npc("I have little knowledge of the caverns - they are blocked","from the sight of the spirits with whom I commune. I","can only suspect that whatever slew barbarian heroes is","indeed mighty. I would also suggest these bones")
            npc("might well be very uncommon, since heroes are not","found in vast numbers. Good luck.")
            player(ChatAnim.ASKING, "How do I enter these caverns then?")
            npc("Dive into the whirlpool in the lake to the east. The","spirits will use their abilities to ensure you arrive in the","correct location, though their influence fades, so you","must find your own way out.")
            exec { player, _ ->
                setAttribute(player, BarbarianTraining.attributePyreShip, 1)
            }
            goto("return_menu")

        label("pyre_ship_remind")
        // Cannot source dialogue for this scenario; it might not exist, but it would be breaking the pattern if it didn't
        // Leaving the empty label here in case someone finds it someday
            goto("pyre_ship_teach")

        label("pyre_ship_final") // Sourced from OSRS transcript
            npc("Hail to you, saviour of my ancestors. The spirits herald your presence with a spectral fanfare.")
            player("It's good to be appreciated.")
            npc("On this great day you have my eternal thanks. May you find riches while rescuing my spiritual ancestors in the caverns for many moons to come.")
            exec { player, _ ->
                if (getAttribute(player, BarbarianTraining.attributePyreShip, 0) == 2) {
                    setAttribute(player, BarbarianTraining.attributePyreShip, 3)
                }
            }
            goto("return_menu")

        label("spirits")
            npc("Certainly, though it is quite simple - they are the","barbarians of the past, who have died in the passing of","the years. There are three categories which learned","sages claim may describe all spirits encountered.")
            npc("The majority are at peace, having died with their","ambitions sated, even if this ambition was as simple as a","glorious death. They can contact those in our world in","order to spur us on to glory. Of course, they cannot")
            npc("or will not contact all inhabitants of these lands. All are","not worthy.")
            player(ChatAnim.ASKING, "So those are the spirits who give us guidance. What of","the other sorts?")
            npc("A second category of spirit is made up of those who are","not yet at peace. However, all that is needed is for their","mortal remains to be laid to rest and they will join the","peaceful majority.")
            player(ChatAnim.ASKING, "I can see the pyres serve this purpose. The final","category is also restless?")
            npc(ChatAnim.SAD, "Alas, there is a small minority who will never be at rest.","These have been slain in moments of insanity by their","brothers or by adventurers. They are not at rest and","will attack those they blame for their tortured state.")
            npc(ChatAnim.SAD, "They are not known, however, for being able to","determine this blame with any great accuracy, so may","be considered as dangerous to all.")
            player("I thank you Otto, it makes a bit more sense now.")
            exec { player, _ ->
                if (allDone) {
                    loadLabel(player, "end")
                } else {
                    loadLabel(player, "return_menu")
                }
            }


        label("return_menu")
            exec { player, _ ->
                if (allDone) {
                    loadLabel(player, "return_menu_done")
                } else {
                    loadLabel(player, "return_menu_not_done")
                }
            }

        label("return_menu_done")
            npc("And with that, your training is done. You have gone from apprentice, to master. Seek to inspire all with your daring deeds and legendary feats.") // Sourced from OSRS transcript
            goto("end")

        label("return_menu_not_done")
            options(
                DialogueOption("training_menu", "I seek more answers."),
                DialogueOption("book_check_end", "I have no more questions at this time.")
            )


        label("book_check_end")
            exec { player, _ ->
                if (inInventory(player, Items.BARBARIAN_SKILLS_11340) || inBank(player, Items.BARBARIAN_SKILLS_11340)) {
                    loadLabel(player, "goodbye")
                } else {
                    loadLabel(player, "need_book_end")
                }
            }

        label("goodbye")
            npc("In that case, farewell.")
            goto("end")

        label("need_book_end")
            npc(ChatAnim.ASKING, "I see you have free space and no record of the tasks I","have set you. Please take this book as a record of your","progress - between the spirits and your diligence I","expect it will always be up to date.")
            exec { player, _ ->
                addItemOrDrop(player, Items.BARBARIAN_SKILLS_11340)
            }
            goto("end")


        label("miniquest_complete") // Sourced from RS3 transcript, no source for how book return is supposed to look after you finish all training so it's just jammed in here wordlessly
            npc("Welcome back oh friend of the spirits, I am humbled in your presence.")
            player("That's very kind, but I was wondering whether you have any more information for me?")
            npc("You are no longer the apprentice, but the master. Seek to inspire us with your daring deeds and legendary feats. Aspire to join the spirits when your time comes.")
            exec { player, _ ->
                if (!inInventory(player, Items.BARBARIAN_SKILLS_11340) && !inBank(player, Items.BARBARIAN_SKILLS_11340)) {
                    addItemOrDrop(player, Items.BARBARIAN_SKILLS_11340)
                }
                if (!inInventory(player, Items.MY_NOTES_11339) && !inBank(player, Items.MY_NOTES_11339)) {
                    addItemOrDrop(player, Items.MY_NOTES_11339)
                }
            }
            options(
                DialogueOption("spirits", "Could you tell me more of these spirits you continually refer to?", expression = ChatAnim.ASKING),
                DialogueOption("goodbye", "I have no more questions at this time.")
            )


        label("end")
    }
}
