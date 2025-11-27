package content.region.kandarin.quest.observatoryquest

import content.data.Quests
import content.region.kandarin.quest.observatoryquest.ObservatoryQuest.Companion.attributeReceivedWine
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class ObservatoryAssistantDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return ObservatoryAssistantDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, ObservatoryAssistantDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.OBSERVATORY_ASSISTANT_6118, 10022)
    }
}

class ObservatoryAssistantDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        assignToIds(NPCs.OBSERVATORY_ASSISTANT_6118)

        exec { player, npc ->
            if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 0) {
                loadLabel(player, "observatoryQuest0")
            } else if (getAttribute(player, attributeReceivedWine, false)) {
                loadLabel(player, "receivedwine")
            } else if (getQuestStage(player, Quests.OBSERVATORY_QUEST) == 100) {
                loadLabel(player, "observatoryqueststage100")
            } else {
                loadLabel(player, "randomStart" + (1..4).random())
            }
        }

        label("observatoryQuest0")
            player(FacialExpression.ASKING, "Hi, are you busy?")
            npc("Me? I'm always busy.")
            npc("See that man there? That's the professor. If he had his way, I think he'd never let me sleep!")
            npc("Anyway, how might I help you?")
            options(
                DialogueOption("wonderingwhat", "I was wondering what you do here."),
                DialogueOption("justlooking", "Just looking around, thanks."),
                DialogueOption("looktelescope", "Can I have a look through that telescope?", expression = FacialExpression.ASKING)
            )

        label("wonderingwhat")
            npc("Glad you ask. This is the Observatory reception. Up on the cliff is the Observatory dome, from which you can view the heavens. Usually...")
            player(FacialExpression.ASKING, "What do you mean, 'usually'?")
            npc("*Ahem*. Back to work, please.")
            npc("I'd speak with the professor. He'll explain.")

        label("justlooking")
            npc("Okay, just don't break anything. If you need any help, let me know.")
            line("The assistant continues with his work.")

        label("looktelescope")
            npc("You can. You won't see much though.")
            player(FacialExpression.ASKING, "And that's because?")
            npc("Just talk to the professor. He'll fill you in.")

        label("randomStart1")
            player(FacialExpression.ASKING, "Can I interrupt you?")
            npc("I suppose so. Please be quick though.")
            goto("afterRandomStart")

        label("randomStart2")
            player(FacialExpression.ASKING, "Might I have a word?")
            npc("Sure, how can I help you?")
            goto("afterRandomStart")

        label("randomStart3")
            player("Hello there.")
            npc("Yes?")
            goto("afterRandomStart")

        label("randomStart4")
            player(FacialExpression.ASKING, "Can I speak with you?")
            npc("Why, of course. What is it?")
            goto("afterRandomStart")


        label("afterRandomStart")
            exec { player, npc ->
                loadLabel(player, "observatoryqueststage" + getQuestStage(player, Quests.OBSERVATORY_QUEST))
            }

        label("observatoryqueststage1")
            exec { player, _ ->
                if (inInventory(player, Items.PLANK_960, 3)) {
                    loadLabel(player, "enoughplanks")
                }
                else if (inInventory(player, Items.PLANK_960)) {
                    loadLabel(player, "someplanks")
                }
                else {
                    loadLabel(player, "notenoughplanks")
                }
            }

        label("notenoughplanks")
            player(FacialExpression.ASKING,"Where can I find planks of wood? I need some for the telescope's base.")
            npc("I understand planks can be found at Port Khazard, to the east of here. There are some at the Barbarian Outpost, too.")
            npc("Failing that, you could always ask the Sawmill Operator. He's to the north-east of Varrock, by the Lumber Yard.")

        label("enoughplanks")
            player("I've got some planks for the telescope's base.")
            npc("Good work! The professor has been eagerly awaiting them.")

        label("someplanks")
            player("I've got a plank.")
            npc("That's nice.")
            player("You know, for the telescope's base.")
            npc("Well done. Remember that you'll need three in total.")

        label("observatoryqueststage2")
            exec { player, _ ->
                if(inInventory(player, Items.BRONZE_BAR_2349)) {
                    loadLabel(player, "hasbronzebar")
                } else {
                    loadLabel(player, "nohasbronzebar")
                }
            }

        label("nohasbronzebar")
            player(FacialExpression.ASKING, "Can you help me? How do I go about getting a bronze bar?")
            npc("You'll need to use tin and copper ore on a furnace to produce this metal.")
            player("Right you are.")

        label("hasbronzebar")
            player("The bronze bar is ready, and waiting for the professor.")
            npc("He'll surely be pleased. Go ahead and give it to him.")

        label("observatoryqueststage3")
            exec { player, _ ->
                if(inInventory(player, Items.MOLTEN_GLASS_1775)) {
                    loadLabel(player, "hasmoltenglass")
                } else {
                    loadLabel(player, "nohasmoltenglass")
                }
            }

        label("nohasmoltenglass")
            player(FacialExpression.ASKING, "What's the best way for me to get molten glass?")
            npc("There are many ways, but I'd suggest making it yourself. Get yourself a bucket of sand and some soda ash, which you can get from using seaweed with a furnace. Use the soda ash and sand together in a")
            npc("furnace and bang - molten glass is all yours. There's a book about it on the table if you want to know more.")
            player("Thank you!")

        label("hasmoltenglass")
            player("I've managed to get hold of some molten glass.")
            npc("I suggest you have a word with the professor, in that case.")

        label("observatoryqueststage4")
            exec { player, _ ->
                if(inInventory(player, Items.LENS_MOULD_602)) {
                    loadLabel(player, "haslensmould")
                } else {
                    loadLabel(player, "nohaslensmould")
                }
            }

        label("nohaslensmould")
            player(FacialExpression.ASKING, "Where can I find this lens mould you mentioned?")
            npc("I'm sure I heard one of those goblins talking about it. I bet they've hidden it somewhere. Probably using it for some strange purpose, I'm sure.")
            player(FacialExpression.ASKING, "What makes you say that?")
            npc("I had a nice new star chart, until recently. I went out to do an errand for the professor the other day, only to see a goblin using it...")
            npc("...as some kind of makeshift hankey to blow his nose!")
            player("Oh dear.")
            npc("You may want to look through the dungeon they have under their little village.")
            player("Thanks for the advice.")

        label("haslensmould")
            player("I have the lens mould.")
            npc("Well done on finding that! I am honestly quite impressed. Make sure you take it straight to the professor.")
            player("Will do.")

        label("observatoryqueststage5")
            exec { player, _ ->
                if(inInventory(player, Items.OBSERVATORY_LENS_603)) {
                    loadLabel(player, "haslens")
                } else {
                    loadLabel(player, "nohaslens")
                }
            }

        label("nohaslens")
            player(FacialExpression.ASKING, "How should I make this lens?")
            npc("Just use the molten glass with the mould. Simple.")
            player("Thanks!")

        label("haslens")
            player("Do you like this lens? Good, huh?")
            npc("Nice. What's that scratch?")
            player("Oh, erm, that's a feature.")
            player("Yes, that's it! Indubitably, it facilitates the triangulation of photonic illumination to the correct...")
            npc("Stop! You can't confuse me with big words. Just pray the professor doesn't notice.")

        label("observatoryqueststage6")
            player("Hello again.")
            npc("Ah, it's the telescope repairman! The professor is waiting for you in the Observatory.")
            player("How can I get to the Observatory?")
            npc("Well, since the bridge was ruined, you will have to travel through the dungeon under the goblins' settlement.")

        label("observatoryqueststage100")
            player("Hi assistant.")
            player("Wait a minute.")
            player(FacialExpression.ASKING, "What's your real name?")
            npc(FacialExpression.ASKING, "My real name?")
            player(FacialExpression.ASKING, "I only know you as the professor's assistant. What's your actual name?")
            npc("Patrick.")
            player("Hi Patrick, I'm @name.")
            npc("Well, hello again, and thanks for helping out the professor.")
            npc(" Oh, and, believe it or not, his name is Mambo-duna-roona, but don't tell him I told you.")
            player(FacialExpression.AMAZED, "You made that up!")
            npc("No, honest! Anyways, you've made my life much easier. Have a drink on me!")
            item(Item(Items.JUG_OF_WINE_1993), "The assistant gives you some wine.")
            exec { player, _ ->
                addItemOrDrop(player, Items.JUG_OF_WINE_1993)
                setAttribute(player, attributeReceivedWine, true)
            }
            player("Thanks very much.")
            npc("No problem. Scorpius would be proud.")
            player(FacialExpression.ASKING, "Sorry?")
            npc("You may want to check out that book on the table, and perhaps look around for a grave...")

        label("receivedwine")
            npc(FacialExpression.ASKING, "How was the wine?")
            player(FacialExpression.DRUNK, "That was good stuff, *hic*! Wheresh the professher?")
            npc(FacialExpression.ASKING, "The professor? He's up in the Observatory. Since the bridge was ruined, you will have to travel through the dungeon under the goblins' settlement.")
    }
}