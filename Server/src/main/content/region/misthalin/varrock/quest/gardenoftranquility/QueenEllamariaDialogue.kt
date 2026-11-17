package content.region.misthalin.varrock.quest.gardenoftranquility

import content.data.Quests
import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class QueenEllamariaDialogue : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(NPCs.ELLAMARIA_2581), IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, QueenEllamariaDialogueFile(), node as NPC)
            return@on true
        }
    }
}

class QueenEllamariaDialogueFile : DialogueLabeller() {

    override fun addConversation() {
        exec { player, _ ->
            when (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY)) {
                // 0: quest not started
                0 -> loadLabel(player, "stage_0")
                
                // 10, 20, 30, 40: getting/got charos and assembling the garden
                in 10..40 -> {
                    if (isGrowing(player) || isGrown(player)) {
                        // check if the garden is done. if so, this will advance stage to 50
                        loadLabel(player, "check_garden")
                    } else {
                        // label where you ask what's going on and get trolley etc.
                        loadLabel(player, "ask_shit")
                    }
                }
                
                // 50: all the plants are grown
                50 -> loadLabel(player, "stage_50")
                
                // 60: cutscene has played, ready to finish quest
                60 -> loadLabel(player, "stage_60")

                // 100: post-quest
                100 -> loadLabel(player, "stage_100")

                else -> loadLabel(player, "end")
            }
        }

        label("stage_0")
        player(ChatAnim.ASKING, "What's going on here? I see a lot of farming patches with nothing growing in them.")
        npc("No, one has just had them installed. One has had the most marvellous idea to bring renewed happiness to one's own dearest husband.")
        player(ChatAnim.ASKING, "One? I'm not sure I understand you-")
        npc("Oh dear - the common classes, oh how they fill one with intolerable levels of exasperation, I swear to be most true.")
        player(ChatAnim.ASKING, "Ri-ight ... if you say so, my lady. I'll be off then, if you don't mind-")
        npc("Yes, be off with you, before I call the guards.")
        player(ChatAnim.ANGRY, "No problem, sorry to have bothered you.")
        exec { player, _ ->
            if (GardenOfTranquility.hasRequirements(player)) {
                loadLabel(player, "stage_0_continue")
            } else {
                loadLabel(player, "end")
            }
        }

        label("stage_0_continue")
        npc("Peasant - WAIT!!!")
        player(ChatAnim.SUSPICIOUS, "Are you speaking to me?")
        npc("Who else would I be speaking to, you funny little @g[man, woman]? Of course you.")
        player(ChatAnim.ANGRY, "Well for a moment there I thought there was a dog around here somewhere...")
        npc("Oh yes, sarcasm - how amusing. No, no, peasant - I have a question to ask of you, and you are required to answer.")
        player(ChatAnim.SUSPICIOUS, "Well, seeing as you put it so nicely...")
        npc("Do you know of anyone who might have some degree of horticultural experience?")
        player("If you are asking me do I know a gardener, then yes I do. I happen to be quite handy at farming myself, as it happens.")
        npc("Oh, marvellous! Peasant, I hereby order you to assist me with a little project I have planned.")
        player(ChatAnim.ANGRY, "You'll have to ask me nicely first.")
        npc("Oh, what I have to deal with these days - to think this is what being a Queen has become. Very well then: would you ... good @g[sir, lady] help me with a little project of mine?")
        options(
            DialogueOption("stage_0_accept", "I would be happy to help someone who is so in touch with the people."),
            DialogueOption("stage_0_decline", "I would rather jump off the docks at Port Sarim.")
        )

        label("stage_0_accept")
        npc("Ah, more sarcasm - wonderful. Well, it is nice to have a master of the fine art of gardening to assist me; in the absence of any such person, I gladly accept your service.")
        player("I'm not sure if that's a compliment or not. Anyway, what would you like me to do, and what do I get out of it?")
        npc("To answer your first question, you have noticed the new farming patches that we have had installed around us. We wish to grow many varied and exotic plants in them, to bring happiness back to my husband.")
        player(ChatAnim.ASKING, "King Roald?")
        npc("The very same, and that's 'His Most Royal Majesty' to you. Now we come to the plants that one wishes you to grow in these patches. I would tell you all at once, but I fear that your small mind would not cope.")
        player("Thanks, I'm sure. So tell me Queeny, how do I know which plants I am to get for you?")
        npc("I have given you a list of the items that I need in each patch. One is sure that you will know how to use the list with each patch to find out what needs to go in each one. There is one more thing, though.")
        player("One listens with bated breath...")
        npc("Obtaining the seeds may prove difficult if you go about it by hacking and slashing everything that moves. No, to gather these items you will need to employ a finer tactic; the art of persuasion.")
        npc("You must go at once and converse with the wise old man who is said to dwell in Draynor Village; he may guide you in mastering this delicate and sophisticated ability.")
        exec { player, _ ->
            if (getQuestStage(player, Quests.GARDEN_OF_TRANQUILITY) == 0) {
                addItemOrDrop(player, Items.LIST_6479)
                setQuestStage(player, Quests.GARDEN_OF_TRANQUILITY, 10)
            }
        }
        goto("end")

        label("stage_0_decline")
        npc("Well don't allow one to delay you - it is probably the most constructive thing you could do with your life, to be honest.")
        goto("end")

        // ask what is going on
        label("ask_shit")
        options(
            DialogueOption("another_list", "Can I have another list?") { player, _ ->
                !hasAnItem(player, Items.LIST_6479).exists()
            },
            DialogueOption("move_statues", "How am I supposed to move statues all the way here?"),
            DialogueOption("what_getting", "What am I supposed to be getting again?"),
            DialogueOption("watering", "Will these patches need much in the way of watering?")
        )

        label("another_list")
        npc("Take better care with this one.")
        exec { player, _ ->
            addItemOrDrop(player, Items.LIST_6479)
        }
        goto("end")

        label("move_statues")
        npc("You use the dormant object that lies between your ears, that's how. A statue may be far too heavy to carry, but simplicity itself to push.")
        player("Push?")
        exec { player, _ ->
            if (hasAnItem(player, Items.TROLLEY_6478).exists()) {
                loadLabel(player, "has_trolley")
            } else {
                loadLabel(player, "needs_trolley")
            }
        }

        label("has_trolley")
        npc("What are you talking about?!? You still have the trolley I gave you!")
        goto("end")

        label("needs_trolley")
        npc("Yes peasant, push. Here - take this trolley. You can use it to push the statues here.")
        exec { player, _ ->
            addItemOrDrop(player, Items.TROLLEY_6478)
            if (getVarbit(player, GardenOfTranquility.VB_SARA_STATUE) == 1) {
                setVarbit(player, GardenOfTranquility.VB_SARA_STATUE, 0, true)
            }
            if (getVarbit(player, GardenOfTranquility.VB_KING_STATUE) == 1) {
                setVarbit(player, GardenOfTranquility.VB_KING_STATUE, 0, true)
            }
        }
        goto("end")

        label("what_getting")
        npc("Haven't you got that list I gave you?")
        exec { player, _ ->
            if (hasAnItem(player, Items.LIST_6479).exists()) {
                loadLabel(player, "has_list")
            } else {
                loadLabel(player, "needs_list")
            }
        }

        label("has_list")
        player("Er ... yes I do have a list...")
        npc("Well read it then! That should tell you everything you need to know!!")
        goto("end")

        label("needs_list")
        player("Er ... no ... I seem to have lost it...")
        npc("Oh dear, what a pathetic creature you are. Here, have another one.")
        exec { player, _ ->
            addItemOrDrop(player, Items.LIST_6479)
        }
        goto("end")

        label("watering")
        npc("Well, you are supposed to be the gardener here, but I would say not. One had the soil imported from an extremely fertile area at great cost - watering should not be necessary one would have thought.")
        goto("end")

        // check if garden is complete
        label("check_garden")
        player("Well, I think that's everything...")
        npc("One finds that hard to believe. Out of my way, let me see for myself.")
        exec { player, _ ->
            if (isGrown(player)) {
                loadLabel(player, "grown")
            } else {
                loadLabel(player, "planted")
            }
        }

        label("planted")
        npc("One was right not to trust your word, common as it is. The garden is not yet complete.")
        player("Really? What haven't I finished yet?")
        npc("One suggest you learn a little patience. These plants should not take long to grow - these patches have been filled with extremely fertile soil that was very expensive to import.")
        goto("end")

        label("grown")
        npc("Oh yes, very nice, if a little yobbish. Rollie will be so pleased-")
        player(ChatAnim.HAPPY, "Rollie?!?")
        npc("You will address my husband by his proper title!!!!! Speaking of whom, I think it is time that we brought him to see his new garden. And when one says we, one means you.")
        player(ChatAnim.ROLLING_EYES, "Oh, you want me to go and get Roald - sorry ... King Roald ... and bring him here. Fair enough, wait here then, I'll be back in a mo.")
        exec { player, _ ->
            setQuestStage(player, Quests.GARDEN_OF_TRANQUILITY, 50)
        }
        goto("end")

        // go get the king
        label("stage_50")
        npc("So are you going to get Rollie then?")
        player("No, I haven't spoken to Roll ... King Roald ... yet.")
        npc("Well hurry along peasant. This garden will be such a wonderful surprise!")
        goto("end")

        // after cutscene plays
        label("stage_60")
        npc("Excuse my husband, he can be so rude sometimes.")
        player(ChatAnim.ASKING, "Only sometimes?")
        npc("Are you being rude again ... ?")
        npc(ChatAnim.LAUGH, "Yes, and that's all he deserves! Here you go, your help here has proved invaluable.")
        exec { player, _ ->
            finishQuest(player, Quests.GARDEN_OF_TRANQUILITY)
        }
        goto("end")

        // post-quest
        label("stage_100")
        options(
            DialogueOption("supercompost", "What's the supercompost potion for?"),
            DialogueOption("farming", "Can I use this garden for my own farming?")
        )

        label("supercompost")
        npc("Oh, let one see peasant - perhaps it is a potion for making supercompost?")
        player("No, I didn't mean that-")
        npc("You wish to know how to use it?")
        npc("Pour it on some normal compost and it will transform into supercompost. You can do this with a bucket of compost, although it is possibly wiser to use it on a full compost bin.")
        goto("end")

        label("farming")
        npc("What, you want to dig up the plants that took one so long to grow? Don't be ridiculous, you silly little @g[man, woman].")
        player("All right then, sorry I asked.")
        npc("Impertinent peasant! Oh, if you promise to leave one alone then feel free to pick fruit from the White Tree. One is told that they are a great natural energy restorative.")
        goto("end")
    }
}

class QueenEllamariaDontTouchMyFuckingPlantsDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        line("There's nothing in this patch to dig up.")
        npc(ChatAnim.ANGRY, "Hey, why are you digging that up? Leave it alone!")
        player(ChatAnim.SCARED, "Oh, sorry, didn't mean to...")
    }
}

// checks if all the plants are at least growing in the patches
private fun isGrowing(player: Player): Boolean {
    // true if all plants are planted (varbits at least 4) and statues are placed (varbits at 2)
    return (minOf(getVarbit(player, GardenOfTranquility.VB_VINE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_WHITE_ROSE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_RED_ROSE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_PINK_ROSE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_SNOWDROP_PATCH),
        getVarbit(player, GardenOfTranquility.VB_DELPHINIUM_PATCH),
        getVarbit(player, GardenOfTranquility.VB_WHITE_TREE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_YELLOW_ORCHID_PATCH),
        getVarbit(player, GardenOfTranquility.VB_PINK_ORCHID_PATCH)) == 4
            && minOf(getVarbit(player, GardenOfTranquility.VB_KING_STATUE),
        getVarbit(player, GardenOfTranquility.VB_SARA_STATUE)) == 2)
}

// checks if all the patches are fully grown
private fun isGrown(player: Player): Boolean {
    // true if all plants are grown (varbits at 7, tree at least 8) and statues are placed (varbits at 2)
    return (minOf(getVarbit(player, GardenOfTranquility.VB_VINE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_WHITE_ROSE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_RED_ROSE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_PINK_ROSE_PATCH),
        getVarbit(player, GardenOfTranquility.VB_SNOWDROP_PATCH),
        getVarbit(player, GardenOfTranquility.VB_DELPHINIUM_PATCH),
        getVarbit(player, GardenOfTranquility.VB_YELLOW_ORCHID_PATCH),
        getVarbit(player, GardenOfTranquility.VB_PINK_ORCHID_PATCH)) == 7
            && getVarbit(player, GardenOfTranquility.VB_WHITE_TREE_PATCH) >= 8
            && minOf(getVarbit(player, GardenOfTranquility.VB_KING_STATUE),
        getVarbit(player, GardenOfTranquility.VB_SARA_STATUE)) == 2)
}