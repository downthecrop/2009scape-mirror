package content.region.misthalin.draynor.dialogue

import content.data.Quests
import content.region.misthalin.lumbridge.diary.LumbridgeAchivementDiary.Companion.EasyTasks.DRAYNOR_WISEOLDMAN_CHECK_JUNK
import content.region.misthalin.varrock.quest.gardenoftranquility.GardenQuizInterface.Companion.QUESTION_NUMBER_VALUE
import content.region.misthalin.varrock.quest.gardenoftranquility.GardenQuizInterface.Companion.WISE_OLD_MAN_QUIZ_INTERFACE
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.diary.DiaryLevel
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * Represents the dialogue plugin used for the wise old man.
 *
 * @author Vexia, Syndromeramo, Oftheshire
 * @version 1.0
 */

class WiseOldManDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.WISE_OLD_MAN_2253), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, WiseOldManDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class WiseOldManDialogueFile : DialogueLabeller() {

    // attributes that are set if the wise old man steals your cape
    companion object {
        const val ATTR_QP_CAPE_RECLAIM = "/save:reclaim-qp-cape"
        const val ATTR_QP_HOOD_RECLAIM = "/save:reclaim-qp-hood"
    }

    private val UNIQUE_BOOKS = intArrayOf(Items.STRANGE_BOOK_5507, Items.BOOK_OF_FOLKLORE_5508, Items.BOOK_ON_CHICKENS_7464)

    override fun addConversation() {
        npc(ChatAnim.HAPPY, "Greetings, @name.")

        exec { player, _ ->
            val cape = getAttribute(player, ATTR_QP_CAPE_RECLAIM, false)
            val hood = getAttribute(player, ATTR_QP_HOOD_RECLAIM, false)
            val allQuests = player.questRepository.hasCompletedAll()
            val gotStage = getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)

            if (cape && hood) {
                loadLabel(player, "reclaim_both")
            } else if (cape) {
                loadLabel(player, "reclaim_cape")
            } else if (hood) {
                loadLabel(player, "reclaim_hood")
            } else if (allQuests) {
                loadLabel(player, "completed_all")
            } else if (gotStage == 10) {
                loadLabel(player, "got_start")
            } else {
                loadLabel(player, "main_options")
            }
        }

        // Reclaim Quest Cape
        label("reclaim_both")
        npc("I assume you're looking for your items? I've placed them ${if (freeSlots(player!!) < 2) "at your feet." else "in your inventory."}")
        exec { player, _ ->
            removeAttribute(player, ATTR_QP_CAPE_RECLAIM)
            removeAttribute(player, ATTR_QP_HOOD_RECLAIM)
            addItemOrDrop(player, Items.QUEST_POINT_CAPE_9813)
            addItemOrDrop(player, Items.QUEST_POINT_HOOD_9814)
        }
        goto("end")

        label("reclaim_cape")
        npc("I assume you're looking for your Quest Point Cape? I've placed it ${if (freeSlots(player!!) < 1) "at your feet." else "in your inventory."}")
        exec { player, _ ->
            removeAttribute(player, ATTR_QP_CAPE_RECLAIM)
            addItemOrDrop(player, Items.QUEST_POINT_CAPE_9813)
        }
        goto("end")

        label("reclaim_hood")
        npc("I assume you're looking for your Quest Point Hood? I've placed it ${if (freeSlots(player!!) < 1) "at your feet." else "in your inventory."}")
        exec { player, _ ->
            removeAttribute(player, ATTR_QP_HOOD_RECLAIM)
            addItemOrDrop(player, Items.QUEST_POINT_HOOD_9814)
        }
        goto("end")

        // Quest Point Cape
        label("completed_all")
        options(
            DialogueOption("qpc_start", "Quest Point Cape.", skipPlayer = true),
            DialogueOption("main_options", "Something else.", skipPlayer = true)
        )

        label("qpc_start")
        player("I believe you are the person to talk to if I want to buy", "a Quest Point Cape?")
        npc("Indeed you believe rightly, @name, and if you know that", "then you'll also know that they cost 99000 coins.")
        options(
            DialogueOption("qpc_no", "No, I hadn't heard that!"),
            DialogueOption("qpc_yes", "Yes, so I was lead to believe.")
        )

        label("qpc_no")
        npc("Well that's the cost, and it's not changing.")
        goto("end")

        label("qpc_yes")
        exec { player, _ ->
            if (freeSlots(player) < 2) {
                loadLabel(player, "qpc_no_space")
            } else if (removeItem(player, Item(Items.COINS_995, 99000))) {
                loadLabel(player, "qpc_buy")
            } else {
                loadLabel(player, "qpc_no_coins")
            }
        }

        label("qpc_no_space")
        player("I don't seem to have enough inventory space.")
        goto("end")

        label("qpc_no_coins")
        player("I don't seem to have enough coins with", "me at this time.")
        goto("end")

        label("qpc_buy")
        npc("Have fun with it.")
        exec { player, _ ->
            addItem(player, Items.QUEST_POINT_CAPE_9813)
            addItem(player, Items.QUEST_POINT_HOOD_9814)
        }
        goto("end")

        // Garden of Tranquility
        label("got_start")
        options(
            DialogueOption("got_chat", "Queen Ellamaria has sent me to seek your guidance.", skipPlayer = true),
            DialogueOption("main_options", "(Talk about something else)", skipPlayer = true)
        )

        label("got_chat")
        player("I have been sent by Queen Ellamaria of Varrock to seek your guidance.")
        npc("Ellamaria? Well, if you're in her service my guidance is this: do the fastest run that you can off the shortest pier you can find.")
        player(ChatAnim.ASKING, "You know Queen Ellamaria?")
        npc("Oh yes, we go way back, long before she married that fool the King of Varrock. She wasn't always royal you know.")
        player(ChatAnim.ASKING, "Really?")
        npc("No, in her youth she was a barmaid at the Blue Moon Inn. All that fancy talk, that's just for show. No, the Ellamaria I knew was as common as muck, until she lucked onto good King Roald.")
        player(ChatAnim.LAUGH, "I'll certainly look at her in a different light when I return to Varrock.")
        npc(ChatAnim.ASKING, "So what sort of guidance has Little-Miss-Hoity-Toity sent you to seek?")
        player("I am supposed to gain some knowledge of diplomacy and persuasion from you.")
        npc("That is an area that - luckily for you, young @name - I have studied. Have you heard of the diplomat known as Charos?")
        player("Charos? I must say the name rings a bell...")
        player("Charos...")
        player(ChatAnim.HAPPY, "Yes, I remember! Once, during my travels to Morytania, I came upon an item of magical jewellery called the Ring of Charos.")
        npc("You have found the Ring of Charos?!? Tell me - do you still have it in your possession?")
        exec { player, _ ->
            if (inInventory(player, Items.RING_OF_CHAROS_4202)) {
                loadLabel(player, "got_ring_inv")
            } else if (inEquipment(player, Items.RING_OF_CHAROS_4202)) {
                loadLabel(player, "got_ring_equip")
            } else if (amountInBank(player, Items.RING_OF_CHAROS_4202, includeSecondary = true) > 0) {
                loadLabel(player, "got_ring_bank")
            } else {
                loadLabel(player, "got_ring_lost")
            }
        }

        label("got_ring_inv")
        player("Yes, I have it in my bag. Is it important, then?")
        goto("got_ring_found")

        label("got_ring_equip")
        player("Yes, I'm wearing it at the moment. Is it important, then?")
        goto("got_ring_found")

        label("got_ring_bank")
        player(ChatAnim.ASKING, "Yes, I have it stored in the bank. Is it important, then?")
        npc("Important?!? The ring possesses hidden power beyond belief! Go and get it, and I will tell you its secrets.")
        goto("end")

        label("got_ring_lost")
        player("No, I'm afraid that I lost it at some point.")
        npc("Lost the Ring of Charos?!? That is indeed a disastrous turn of events. This ring that you have so casually thrown away possesses hidden power beyond belief.")
        player("How can I re-obtain the Ring of Charos?")
        npc("If everything I have heard about Charos's ring is true, it will not stay lost forever. Where did you originally find it?")
        player("I ... um ... 'borrowed' it from a rather shady character called Fenkenstrain.")
        npc("In order to find the ring it may be wise to return to Fenkenstrain - he may have found a way to regain it. If you manage to reobtain the ring, return to me and I will reveal its secrets to you.")
        goto("end")

        label("got_ring_found")
        npc("Charos rose to power under the rule of Lord Drakan of Morytania; Charos was his only living servant.")
        npc("The vampyre lord suffered the existence of Charos because of the talents that this sophisticated diplomat had to offer.")
        player(ChatAnim.ASKING, "The art of persuasion?")
        npc("Indeed. Charos had many uses - spy, assassin,", "negotiator - because he could convince anyone to do his", "wishes. These talents were in some part Charos's own,", "but greatly enhanced by the ring that he fashioned.")
        player("The Ring of Charos.")
        npc("Exactly. One who wears the ring must possess some innate ability of their own, which the ring enhances so that the wearer can convince all but the most strong-minded to do their bidding.")
        player(ChatAnim.ASKING, "What happened to Charos? And why did Fenkenstrain have the ring?")
        npc("Charos had a castle built on the northern coast of Morytania, where he dwelt for many years in service to Drakan. None really know what became of him, but some say that he became too certain of his abilities,")
        npc("and turned them on his master. This would have been his ultimate downfall, for the ring's power would have had little or no effect upon Drakan.")
        npc("Fenkenstrain probably found the ring many years later", "buried somewhere within the depths of Charos's castle.")
        player(ChatAnim.SCARED, "So what you are telling me is that nobody really knows what happened to Charos? That he could still be alive? And if he is still alive...")
        npc("...he would naturally come looking for the ring that is his by right. But I don't believe that you should worry about that - it has been many years since his disappearance, and the likelyhood is that he is dead.")
        player(ChatAnim.SCARED, "Still - it doesn't inspire me with much confidence.")
        npc("I could activate the power within this ring for you, but for me to do that I must know that you will use it wisely. To do that I must ask you a few questions, to see how good a diplomat you can be.")
        player(ChatAnim.SCARED, "Okay, go ahead...")
        exec { player, _ ->
            QUESTION_NUMBER_VALUE = 1
            end()
            openInterface(player, WISE_OLD_MAN_QUIZ_INTERFACE)
        }

        // Main Options
        label("main_options")
        options(
            DialogueOption("tasks", "Is there anything I can do for you?"),
            DialogueOption("junk_check", "Could you check my things for junk, please?"),
            DialogueOption("look_at", "I've got something I'd like you to look at."),
            DialogueOption("lore_start", "I'd just like to ask you something."),
            DialogueOption("charmed", "[Charm] I think you wanted to give me a thousand coins...", "I think you wanted to give me a thousand coins...",
                optionIf = { player, _ -> inEquipment(player, Items.RING_OF_CHAROSA_6465) }
            )
        )

        label("charmed")
        npc("Ha ha! I thought you might try that!! But remember, @name - the power of the Ring of Charos will not work with everybody, especially not a wise old bird like me!")
        goto("end")

        label("tasks")
        npc("Thanks, but I don't have anything I need.")
        goto("end")

        label("junk_check")
        npc("Certainly, but I should warn you that I don't know about", "all items.")
        options(
            DialogueOption("junk_bank", "Could you check my bank for junk, please?"),
            DialogueOption("junk_inv", "Could you check my inventory for junk, please?")
        )

        label("junk_bank")
        exec { player, _ ->
            if (player.bank.containsAtLeastOneItem(UNIQUE_BOOKS) && player.bank.removeAll(UNIQUE_BOOKS)) {
                loadLabel(player, "junk_found_bank")
            } else {
                loadLabel(player, "junk_not_found_bank")
            }
        }

        label("junk_found_bank")
        npc(ChatAnim.DISGUSTED, "That's my book! What's it doing in your bank?")
        exec { player, _ -> finishTask(player, DiaryType.LUMBRIDGE, DiaryLevel.EASY, DRAYNOR_WISEOLDMAN_CHECK_JUNK) }
        goto("end")

        label("junk_not_found_bank")
        npc("You seem to have no junk in your bank, sorry.")
        exec { player, _ -> finishTask(player, DiaryType.LUMBRIDGE, DiaryLevel.EASY, DRAYNOR_WISEOLDMAN_CHECK_JUNK) }
        goto("end")

        label("junk_inv")
        exec { player, _ ->
            if (player.inventory.containsAtLeastOneItem(UNIQUE_BOOKS) && player.inventory.removeAll(UNIQUE_BOOKS)) {
                loadLabel(player, "junk_found_inv")
            } else {
                loadLabel(player, "junk_not_found_inv")
            }
        }

        label("junk_found_inv")
        npc(ChatAnim.DISGUSTED, "That's my book! What's it doing in your inventory?")
        exec { player, _ -> finishTask(player, DiaryType.LUMBRIDGE, DiaryLevel.EASY, DRAYNOR_WISEOLDMAN_CHECK_JUNK) }
        goto("end")

        label("junk_not_found_inv")
        npc("You seem to have no junk in your inventory, sorry.")
        exec { player, _ -> finishTask(player, DiaryType.LUMBRIDGE, DiaryLevel.EASY, DRAYNOR_WISEOLDMAN_CHECK_JUNK) }
        goto("end")

        label("look_at")
        npc("Jolly good. Give it to me, and I'll tell you anything I know", "about it.")
        goto("end")

        // Lore Options
        label("lore_start")
        npc("Please do!")
        options(
            DialogueOption("lore_distant", "Distant lands", skipPlayer = true),
            DialogueOption("lore_beasts", "Strange beasts", skipPlayer = true),
            DialogueOption("lore_history", "Days gone by", skipPlayer = true),
            DialogueOption("lore_gods", "Gods and demons", skipPlayer = true),
            DialogueOption("lore_hat", "Something to do with you...", skipPlayer = true)
        )

        label("lore_distant")
        options(
            DialogueOption("lore_wilderness", "The Wilderness", skipPlayer = true),
            DialogueOption("lore_jungles", "Misty jungles", skipPlayer = true),
            DialogueOption("lore_underground", "Underground domains", skipPlayer = true),
            DialogueOption("lore_mystical", "Mystical realms", skipPlayer = true)
        )

        label("lore_wilderness")
        player("Could you tell me about the Wilderness, please?")
        npc("Many years ago, when the foul Zamorak was doing battle with the forces of Saradomin, the northern reaches of this continent were blasted by his evil magic, transforming a vast area into a barren wasteland of rocks and lava.")
        npc("The spirits of the monsters that perished in this cataclysm are still roaming the Wilderness, even today, hunting for people to slaughter in their endless desire for revenge. They are incredibly dangerous, far more deadly than other monsters.")
        npc("However, it's still often worth the risk of entering the Wilderness. There are treasures in the Wilderness that are scarce elsewhere, and some of the other monsters are popular for combat training.")
        npc("There are even arenas where you can fight other people like yourself! If you dare to go to the far north-west of the Wilderness, there's a building called the Mage Arena where you can learn to summon the power of Saradomin himself!")
        goto("lore_ask_more")

        label("lore_jungles")
        player("What can you tell me about jungles?")
        npc("If it's a jungle you want, look no further than the southern regions of Karamja.")
        npc("Once you get south of Brimhaven, the whole island is pretty much covered in exotic trees, creepers and shrubs.")
        npc("There's a small settlement called Tai Bwo Wannai Village in the middle of the island. It's a funny place; the chieftain's an unfriendly chap and his sons are barking mad.")
        npc("Honestly, one of them asked me to stuff a dead monkey with seaweed so he could EAT it!")
        npc("Further south you'll find Shilo Village. It's been under attack by terrifying zombies in recent months, if my sources are correct.")
        npc("The jungle's filled with nasty creatures. There are vicious spiders that you can hardly see before they try to bite your legs off, and great big jungle ogres.")
        goto("lore_ask_more")

        label("lore_underground")
        player("Tell me about what's underground.")
        npc("Oh, the dwarven realms?")
        npc("Yes, there was a time, back in the Fourth Age, when we humans wouldn't have been able to venture underground. That was before we had magic; the dwarves were quite a threat.")
        npc("Still, it's much more friendly now. You can visit the vast dwarven mine if you like; the entrance is on the mountain north of Falador.")
        npc("If you go further west you may be able to visit the dwarven city of Keldagrim. But they were a bit cautious about letting humans in, last time I asked.")
        npc("On the other hand, if you go west of Brimhaven, you'll find a huge underground labyrinth full of giants, demons, dogs and dragons to fight. It's even bigger than the caves under Taverley, although the Taverley dungeon's pretty good for training your combat skills.")
        goto("lore_ask_more")

        label("lore_mystical")
        player("What mystical realms can I visit?")
        npc("The fabled Lost City of Zanaris has an entrance somewhere near here. Perhaps some day you'll go there.")
        npc("Also, in my research I came across ancient references to some kind of Abyss. Demons from the Abyss have already escaped into this land; Saradomin be thanked that they are very rare!")
        goto("lore_ask_more")

        label("lore_beasts")
        options(
            DialogueOption("lore_mighty", "Biggest & Baddest", skipPlayer = true),
            DialogueOption("lore_poison", "Poison and how to survive it", skipPlayer = true),
            DialogueOption("lore_drops", "Wealth through slaughter", skipPlayer = true)
        )

        label("lore_mighty")
        player("Tell me about mighty monsters.")
        npc("There's a mighty fire-breathing dragon living underground in the deep Wilderness, known as the King Black Dragon. It's a fearsome beast, with a breath that can poison you, freeze you to the ground or incinerate you where you stand.")
        npc("But even more deadly is the Queen of the Kalphites. As if her giant mandibles of death were not enough, she also throws her spines at her foes with deadly force. She can even cast rudimentary spells.")
        npc("Some dark power must be protecting her, for she can block attacks using prayer just as humans do.")
        npc("Another beast that's worthy of a special mention is the Shaikahan. It dwells in the eastern reaches of Karamja, and is almost impossible to kill except with specially prepared weapons.")
        npc("In the north, beyond the troll lands, there is a cavern where an endless battle rages between the forces of Saradomin and those of Zamorak.")
        npc("Saradomin's forces are led by the icyene warrior Commander Zilyana, Keeper of the Faith. According to legend, she takes the form of a colossal woman with wings.")
        npc("Zamorak's forces are led by K'ril Tsutsaroth, a demon of devastating power. Around their encampment is a mystical darkness that no lantern can alleviate. Two minor armies are loosely allied with the Saradominist force.")
        npc("One consists of bird-like creatures, the Aviansie. Another consists of mindless monsters such as ogres and orks. The leaders of those armies are terrifying creatures in their own right, although inferior by far to Zilyana.")
        npc("Beyond the Fremennik lands dwell the fearsome Dagannoth Kings, reptilian creatures of the deep. There are three of them, fighting with melee, magic and ranged attacks respectively, on a small island at the bottom of a labyrinthine cavern.")
        goto("lore_ask_more")

        label("lore_poison")
        player("What does poison do?")
        npc("Many monsters use poison against their foes. If you get poisoned, you will not feel it at the time, but later you will begin to suffer its effects, and your life will drain slowly from you.")
        npc("Over time the effects dwindle to nothing, but if you had already been wounded you might die before they wear off completely.")
        npc("Fortunately, followers of Guthix have devised potions that can cure the poison or even give immunity to it.")
        npc("Should you wish to use poison against your own enemies and those of our Lord Saradomin, there is a potion that you can smear on your daggers, arrows, spears, javelins and throwing knives.")
        goto("lore_ask_more")

        label("lore_drops")
        player("What monsters drop good items?")
        npc("As a general rule, tougher monsters drop more valuable items. But even a lowly hobgoblin can drop valuable gems; it just does this extremely rarely.")
        npc("If you can persuade the Slayer Masters to train you as a Slayer, you will be able to fight certain monsters that drop valuable items far more often.")
        npc("You might care to invest in an enchanted dragonstone ring. These are said to make a monster drop its most valuable items a little more often.")
        goto("lore_ask_more")

        label("lore_history")
        options(
            DialogueOption("lore_heroes", "Heroic figures", skipPlayer = true),
            DialogueOption("lore_magic", "The origin of magic", skipPlayer = true),
            DialogueOption("lore_settlements", "Settlements", skipPlayer = true),
            DialogueOption("lore_old_man", "The Wise Old Man of Draynor Village", skipPlayer = true)
        )

        label("lore_heroes")
        player("Tell me about valiant heroes!")
        npc("Ha ha ha... There are plenty of heroes. Always have been, always will be, until the fall of the world.")
        npc("You're quite a noted adventurer yourself!")
        npc("But I suppose I could tell you of a couple...")
        npc("Yes, there was a man named Arrav. No-one knew where he came from, but he was a fearsome fighter, a skilful hunter and a remarkable farmer.")
        npc("He lived in the ancient settlement of Avarrocka, defending it from goblins, until he went forth in search of some strange artifact long desired by the dreaded Mahjarrat.")
        npc("But do not let your head be turned by heroics. Randas was another great man, but he let himself be beguiled into turning to serve Zamorak, and they say he is now a mindless creature deep in the Underground Pass that leads to Isafdar.")
        goto("lore_ask_more")

        label("lore_magic")
        player("Where did humans learn to use magic?")
        npc("Ah, that was quite a discovery! It revolutionised our way of life and jolted us into the Fifth Age of the world.")
        npc("They say a traveller in the north discovered the key, although no records state exactly what he found. From this he was able to summon the magic of four elements, using magic as a tool and a weapon. He and his followers learnt how to bind the power into little stones so that others could use it.")
        npc("In the land south of here they constructed an immense tower where the power could be studied, but followers of Zamorak destroyed it with fire many years ago, and most of the knowledge was lost.")
        goto("lore_ask_more")

        label("lore_settlements")
        player("I suppose you'd know about the history of today's cities?")
        npc("Yes, there are fairly good records of the formation of the cities from primitive settlements.")
        npc("In the early part of the Fourth Age, of course, there were no permanent settlements. Tribes wandered the lands, staying where they could until the resources were exhausted.")
        npc("This changed as people learnt to grow crops and breed animals, and now there are very few of the old nomadic tribes. There's at least one tribe roaming between the Troll Stronghold and Rellekka, though.")
        npc("One settlement was Avarrocka, a popular trading centre.")
        npc("In the west, Ardougne gradually formed under the leadership of the Carnillean family, despite the threat of the Mahjarrat warlord Hazeel who dwelt in that area until his downfall.")
        goto("lore_ask_more")

        label("lore_old_man")
        player("Tell me about yourself, old man.")
        npc("Ah, so you want to know about me, eh?")
        npc("Mmm... what could I say about myself? Let's see what I've done...")
        npc("I've delved into the dungeon west of Brimhaven and heard the terrifying CRASH of the steel dragons battling each other for territory.")
        npc("I spent some years on Entrana, where I learnt the techniques of pure meditation.")
        npc("I've wandered through the vast desert that lies south of Al Kharid and seen the great walls of Menaphos and Sophanem.")
        npc("Apart from all that, I've spent many a happy hour in dusty libraries, searching through ancient scrolls and texts for the wisdom of those who have passed on.")
        npc("Plus plenty of other adventures, quests, journeys...")
        goto("lore_ask_more")

        label("lore_gods")
        options(
            DialogueOption("lore_three_gods", "Three gods?", skipPlayer = true),
            DialogueOption("lore_wars", "The wars of the gods", skipPlayer = true),
            DialogueOption("lore_mahjarrat", "The Mahjarrat", skipPlayer = true),
            DialogueOption("lore_wielding", "Wielding the power of the gods", skipPlayer = true)
        )

        label("lore_three_gods")
        player("I heard that the world used to have three gods.")
        npc("Indeed. That was correct: Saradomin, Guthix and Zamorak.")
        npc("Saradomin, the great and glorious, gives life to this world.")
        npc("Zamorak craves only death and destruction.")
        npc("Guthix, calling itself a god of 'balance', held no allegiance, but simply aided whatever cause suits its shifting purpose.")
        goto("lore_ask_more")

        label("lore_wars")
        player("I wanna know about the wars of the gods!")
        npc("Ah, the first war was terrible time. The armies of Saradomin fought gloriously against the minions of Zamorak, but many brave warriors and noble cities were overthrown and destroyed utterly.")
        player("How did it end?")
        npc("Before the Zamorakian forces could be utterly routed, Lord Saradomin took pity on them and the battle-scarred world, and allowed a truce.")
        goto("lore_ask_more")

        label("lore_mahjarrat")
        player("What are the Mahjarrat?")
        npc("Very little is written about the tribe of the Mahjarrat. They are believed to be from a realm of Freneskae, or Frenaskrae - the spelling in this tongue is only approximate.")
        npc("One of them, the foul Zamorak, had achieved godhood, although none know how this came about.")
        npc("Other Mahjarrat who have been particularly active upon this plane are Hazeel, Lucien, Azzanadra and Zemouregal.")
        goto("lore_ask_more")

        label("lore_wielding")
        player("Can I wield the power of Saradomin myself?")
        npc("On completing the trials to use the Mage Arena, you can wield the cloak and staff of Saradomin. You will then be able to cast the Saradomin Strike spell, which summons his power against your foe.")
        goto("lore_ask_more")

        label("lore_hat")
        player("Your hat!")
        npc("Why, thank you! I rather like it myself.")
        player("How can I get a hat like that? ")
        npc("Ahh, sadly these hats are now very rare. Maybe some other player would be willing to sell you theirs, but it would be incredibly expensive.")
        player("Can I buy your hat?")
        npc("Ohhh no, I don't intend to part with this.")
        goto("lore_ask_more")

        label("lore_ask_more")
        npc("Is there anything else you'd like to ask?")
        options(
            DialogueOption("lore_start", "Yes please."),
            DialogueOption("lore_end", "Thanks, maybe some other time.")
        )

        label("lore_end")
        npc("As you wish. Farewell, @name.")
        goto("end")
    }
}