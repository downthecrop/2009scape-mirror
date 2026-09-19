package content.region.kandarin.gnomestronghold.dialogue

import core.api.addItemOrDrop
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class GnomeChildDialogue : InteractionListener {
    override fun defineListeners() {
        val gnomeChildren = intArrayOf(NPCs.GNOME_CHILD_159, NPCs.GNOME_CHILD_160, NPCs.GNOME_CHILD_161)
        on(gnomeChildren, IntType.NPC, "talk-to") { player, node ->
            DialogueLabeller.open(player, GnomeChildDialogueFile("init_gnome_child_${gnomeChildren.indexOf(node.id) + 1}"), node as NPC)
            return@on true
        }
    }
}

class GnomeChildDialogueFile(val startLabel: String) : DialogueLabeller() {
    override fun addConversation() {
        // Source: https://runescape.wiki/w/Transcript:Gnome_child?oldid=13970310
        exec { player, _ ->
            loadLabel(player, startLabel)
            return@exec
        }

        label("init_gnome_child_1")
            player(ChatAnim.NEUTRAL, "Hello.")
            exec { player, _ ->
                val conversationIndex = RandomFunction.random(13) + 1
                loadLabel(player, "gnome_child_1_conversation_$conversationIndex")
                return@exec
            }

        label("gnome_child_1_conversation_1")
            npc(ChatAnim.OLD_DEFAULT, "I have a riddle for you.")
            player(ChatAnim.NEUTRAL, "OK.")
            npc(ChatAnim.OLD_DEFAULT, "A tree which is planted on Mondaay " /*sic*/+ "and doubles in size each day is fully grown on the following Sunday. On what day is it half grown?")
            options(
                DialogueOption("gnome_child_1_conversation_1_fail", "Wednesday", "Wednesday.", expression = ChatAnim.NEUTRAL),
                DialogueOption("gnome_child_1_conversation_1_fail", "Thursday",  "Thursday.",  expression = ChatAnim.NEUTRAL),
                DialogueOption("gnome_child_1_conversation_1_fail", "Friday",    "Friday.",    expression = ChatAnim.NEUTRAL),
                DialogueOption("gnome_child_1_conversation_1_win",  "Saturday",  "Saturday.",  expression = ChatAnim.NEUTRAL),
                DialogueOption("gnome_child_1_conversation_1_fail", "I don't know.",           expression = ChatAnim.NEUTRAL),
            )
        label("gnome_child_1_conversation_1_fail")
            npc(ChatAnim.OLD_DEFAULT, "Saturday. You big folk really aren't the quickest!")
        label("gnome_child_1_conversation_1_win")
            npc(ChatAnim.OLD_DEFAULT, "Correct. You can go now.")

        label("gnome_child_1_conversation_2")
            npc(ChatAnim.OLD_DEFAULT, "To be or not to be.")
            player(ChatAnim.NEUTRAL, "Hey I know that. Where's it from?")
            npc(ChatAnim.OLD_DEFAULT, "Existentialism for insects.")

        label("gnome_child_1_conversation_3")
            npc(ChatAnim.OLD_DEFAULT, "My mum says: A friendly look, a kindly smile", "one good act, and life's worthwhile!")
            player(ChatAnim.NEUTRAL, "Sweet.")

        label("gnome_child_1_conversation_4")
            npc(ChatAnim.OLD_DEFAULT, "I wrote a few children's books.")
            player(ChatAnim.NEUTRAL, "Really.")
            npc(ChatAnim.OLD_DEFAULT, "Yep... but not on purpose!")

        label("gnome_child_1_conversation_5")
            npc(ChatAnim.OLD_DEFAULT, "The human mind is a tremendous thing.")

        label("gnome_child_1_conversation_6")
            npc(ChatAnim.OLD_DEFAULT, "Bla bla bla!")
            player(ChatAnim.NEUTRAL, "What?")
            npc(ChatAnim.OLD_DEFAULT, "Bla bla bla!")
            line("Rude little gnome!")

        label("gnome_child_1_conversation_7")
            npc(ChatAnim.OLD_DEFAULT, "Real generosity is doing something nice for someone who willl " /*sic*/+ "never find it out.")
            player(ChatAnim.NEUTRAL, "Thanks for the quote.")

        label("gnome_child_1_conversation_8")
            npc(ChatAnim.OLD_DEFAULT, "Nice weather we're having today.")
            npc(ChatAnim.OLD_DEFAULT, "But then it doesn't tend to rain much around here.")

        label("gnome_child_1_conversation_9")
            npc(ChatAnim.OLD_DEFAULT, "Hardy, ha ha! Hee, hee hee!")
            player(ChatAnim.NEUTRAL, "Are you OK?")
            npc(ChatAnim.OLD_DEFAULT, "I'm a little tree gnome. That is me!")
            player(ChatAnim.NEUTRAL, "I've heard better.")

        label("gnome_child_1_conversation_10")
            npc(ChatAnim.OLD_DEFAULT, "I have a riddle for you.")
            player(ChatAnim.NEUTRAL, "OK.")
            npc(ChatAnim.OLD_DEFAULT, "I am the beginning of eternity and the end of time and space. I am the beginning of every end and the end of every place. What am I?")
            player(ChatAnim.NEUTRAL, "Err...?")
            player(ChatAnim.NEUTRAL, "Erm...not sure...annoying.")
            npc(ChatAnim.OLD_DEFAULT, "I'm 'e'! Hee hee! Do you get it?")

        label("gnome_child_1_conversation_11")
            npc(ChatAnim.OLD_DEFAULT, "I worship Guthix, the god of balance.")
            npc(ChatAnim.OLD_DEFAULT, "He really does have exceptional co-ordination.")

        label("gnome_child_1_conversation_12")
            player(ChatAnim.NEUTRAL, "Hello, are you alright?")
            npc(ChatAnim.OLD_DEFAULT, "Not really, I just saw a man with a wooden leg.")
            player(ChatAnim.NEUTRAL, "So?!")
            npc(ChatAnim.OLD_DEFAULT, "He had a real foot!")

        label("gnome_child_1_conversation_13")
            player(ChatAnim.NEUTRAL, "What are you doing?")
            npc(ChatAnim.OLD_DEFAULT, "I'm making wine out of raisins.")
            player(ChatAnim.NEUTRAL, "Why?")
            npc(ChatAnim.OLD_DEFAULT, "So I don't have to wait for it to age.")
            npc(ChatAnim.OLD_DEFAULT, "Hee hee!")


        label("init_gnome_child_2")
            player(ChatAnim.NEUTRAL, "Hi.")
            exec { player, _ ->
                val conversationIndex = RandomFunction.random(13) + 1
                loadLabel(player, "gnome_child_2_conversation_$conversationIndex")
                return@exec
            }

        label("gnome_child_2_conversation_1")
            npc(ChatAnim.OLD_DEFAULT, "She loves me!")
            player(ChatAnim.NEUTRAL, "Really.")
            npc(ChatAnim.OLD_DEFAULT, "She does I tell you. She really loves me!")

        label("gnome_child_2_conversation_2")
            npc(ChatAnim.OLD_DEFAULT, "Hello.")
            player(ChatAnim.NEUTRAL, "Are you alright?")
            npc(ChatAnim.OLD_DEFAULT, "I just want something to happen!")
            player(ChatAnim.NEUTRAL, "Like what?")
            npc(ChatAnim.OLD_DEFAULT, "Something, anything, I don't know what!")

        label("gnome_child_2_conversation_3")
            npc(ChatAnim.OLD_DEFAULT, "A little inaccuracy sometimes saves tons of explanation!")
            player(ChatAnim.NEUTRAL, "True.")

        label("gnome_child_2_conversation_4")
            npc(ChatAnim.OLD_DEFAULT, "Hello. Why are you so tall?")
            player(ChatAnim.NEUTRAL, "Um... I've always been this height.")
            npc(ChatAnim.OLD_DEFAULT, "Maybe you should eat more worms.")

        label("gnome_child_2_conversation_5")
            npc(ChatAnim.OLD_DEFAULT, "Low.")
            player(ChatAnim.NEUTRAL, "What?")
            npc(ChatAnim.OLD_DEFAULT, "When?")
            player(ChatAnim.NEUTRAL, "Cheeky!")
            npc(ChatAnim.OLD_DEFAULT, "Hee hee!")

        label("gnome_child_2_conversation_6")
            line("The gnome is praying.")
            npc(ChatAnim.OLD_DEFAULT, "Guthix's angels fly so high as to be beyond our sight but they are always looking down upon us.")

        label("gnome_child_2_conversation_7")
            npc(ChatAnim.OLD_DEFAULT, "Hello, would you like a worm?")
            player(ChatAnim.NEUTRAL, "Erm... OK.")
            exec { player, _ ->
                addItemOrDrop(player, Items.KING_WORM_2162)
            }
            item(Item(Items.KING_WORM_2162), "The gnome gives you a worm"/*sic*/)
            player(ChatAnim.NEUTRAL, "Thanks.")
            npc(ChatAnim.OLD_DEFAULT, "In the gnome village those who are needy receive what they need, and those who are able give what they can.")

        label("gnome_child_2_conversation_8")
            npc(ChatAnim.OLD_DEFAULT, "Dimensionality is a function of consciousness.")
            player(ChatAnim.NEUTRAL, "What?")
            npc(ChatAnim.OLD_DEFAULT, "That's all you need to know.")

        label("gnome_child_2_conversation_9")
            npc(ChatAnim.OLD_DEFAULT, "I've been thinking.")
            player(ChatAnim.NEUTRAL, "OK.")
            npc(ChatAnim.OLD_DEFAULT, "Surely, if you're scared to die...")
            npc(ChatAnim.OLD_DEFAULT, "...you're not living, right?")
            player(ChatAnim.NEUTRAL, "Maybe!")

        label("gnome_child_2_conversation_10")
            player(ChatAnim.NEUTRAL, "How are you?")
            npc(ChatAnim.OLD_DEFAULT, "A warning traveller! The new world will rise from the underground!")
            player(ChatAnim.NEUTRAL, "What do you mean, underground?")
            npc(ChatAnim.OLD_DEFAULT, "Just a warning!")

        label("gnome_child_2_conversation_11")
            npc(ChatAnim.OLD_DEFAULT, "Some advice traveller. We can walk, run, row or fly but never lose sight of the reason for the journey or miss the chance to see a rainbow on the way.")
            player(ChatAnim.NEUTRAL, "I like that.")

        label("gnome_child_2_conversation_12")
            player(ChatAnim.NEUTRAL, "You look happy.")
            npc(ChatAnim.OLD_DEFAULT, "I'm always at peace with myself.")
            player(ChatAnim.NEUTRAL, "How do you manage that?")
            npc(ChatAnim.OLD_DEFAULT, "I know, therefore I am.")

        label("gnome_child_2_conversation_13")
            line("The gnome appears to be singing.")
            npc(ChatAnim.OLD_DEFAULT, "Oh baby, oh my sweet.")
            player(ChatAnim.NEUTRAL, "Are you talking to me?")
            npc(ChatAnim.OLD_DEFAULT, "No, I'm just singing.")
            npc(ChatAnim.OLD_DEFAULT, "I'm gonna sweep you off your feet.")


        label("init_gnome_child_3")
            player(ChatAnim.NEUTRAL, "Hi there.")
            exec { player, _ ->
                val conversationIndex = RandomFunction.random(12) + 1
                loadLabel(player, "gnome_child_3_conversation_$conversationIndex")
                return@exec
            }

        label("gnome_child_3_conversation_1")
            npc(ChatAnim.OLD_DEFAULT, "Hey big man, watch where you're standing.")
            npc(ChatAnim.OLD_DEFAULT, "I wouldn't want to make a mess of your shoes.")

        label("gnome_child_3_conversation_2")
            npc(ChatAnim.OLD_DEFAULT, "Have you seen our big tree?")
            player(ChatAnim.NEUTRAL, "Yes I have!")
            npc(ChatAnim.OLD_DEFAULT, "It's not small!")

        label("gnome_child_3_conversation_3")
            npc(ChatAnim.OLD_DEFAULT, "You are the contained centre of an O.")
            player(ChatAnim.NEUTRAL, "Pardon?")
            npc(ChatAnim.OLD_DEFAULT, "While me, I'm much more. I am the pyramidic containment of an A.")
            player(ChatAnim.NEUTRAL, "You're strange.")

        label("gnome_child_3_conversation_4")
            npc(ChatAnim.OLD_DEFAULT, "I'm trying to make a comeback!")
            player(ChatAnim.NEUTRAL, "Really?")
            npc(ChatAnim.OLD_DEFAULT, "It's hard when you haven't been anywhere.")

        label("gnome_child_3_conversation_5")
            npc(ChatAnim.OLD_DEFAULT, "Aaaaarrggghh... humans! Run for your lives!")
            line("The gnome backs away.")

        label("gnome_child_3_conversation_6")
            npc(ChatAnim.OLD_DEFAULT, "Hello brave adventurer. A warrior like you should go a long way.")
            player(ChatAnim.NEUTRAL, "Thanks.")
            npc(ChatAnim.OLD_DEFAULT, "The further, the better! Hee hee!")

        label("gnome_child_3_conversation_7")
            npc(ChatAnim.OLD_DEFAULT, "Es ataris eto meriz ip prit es gutus!")
            player(ChatAnim.NEUTRAL, "Pardon?")
            npc(ChatAnim.OLD_DEFAULT, "Es ataris eto meriz ip prit es gutus!")

        label("gnome_child_3_conversation_8")
            npc(ChatAnim.OLD_DEFAULT, "Oh hello.")
            player(ChatAnim.NEUTRAL, "How are you?")
            npc(ChatAnim.OLD_DEFAULT, "Not great, it's just... well... people just don't seem to take me seriously!")

        label("gnome_child_3_conversation_9")
            npc(ChatAnim.OLD_DEFAULT, "How are you?")
            player(ChatAnim.NEUTRAL, "I'm fine thanks.")

        label("gnome_child_3_conversation_10")
            npc(ChatAnim.OLD_DEFAULT, "Why don't you make like a tree...")
            npc(ChatAnim.OLD_DEFAULT, "...and leave!")
            player(ChatAnim.NEUTRAL, "Funny!")
            npc(ChatAnim.OLD_DEFAULT, "Hee hee!")

        label("gnome_child_3_conversation_11")
            npc(ChatAnim.OLD_DEFAULT, "Top of the morning to you.")
            player(ChatAnim.NEUTRAL, "Thanks.")
            npc(ChatAnim.OLD_DEFAULT, "And bottom of the afternoon.")

        label("gnome_child_3_conversation_12")
            npc(ChatAnim.OLD_DEFAULT, "The only thing that is constant...")
            npc(ChatAnim.OLD_DEFAULT, "...is change!")

    }
}