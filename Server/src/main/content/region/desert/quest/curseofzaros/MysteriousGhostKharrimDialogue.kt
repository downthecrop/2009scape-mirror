package content.region.desert.quest.curseofzaros

import core.api.*
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.dialogue.DialoguePlugin
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class MysteriousGhostKharrimDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MysteriousGhostKharrimDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MysteriousGhostKharrimDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(2388, 2389, 2390)
    }
}

class MysteriousGhostKharrimDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)){
                if (2387 + getAttribute(player, CurseOfZaros.attributePathNumber, 0) == npc.id) {
                    if (getAttribute(player, CurseOfZaros.attributeKharrimSpoke, false)) {
                        if (inInventory(player, Items.GHOSTLY_BOOTS_6106) || inEquipment(player, Items.GHOSTLY_BOOTS_6106)) {
                            loadLabel(player, "subsequenttime")
                        } else {
                            loadLabel(player, "lostghostlything")
                        }
                    } else {
                        loadLabel(player, "firsttime")
                    }
                } else {
                    loadLabel(player, "wrongpath")
                }
            } else {
                loadLabel(player, "noghostspeak")
            }
        }

        CurseOfZaros.withoutGhostspeak(this)

        CurseOfZaros.wrongPath(this)

        label("firsttime")
            player("Hello. So you must be Kharrim the messenger.")
            npc("How do you know my name, stranger?")
            player("Well now... I had a very interesting chat with Rennard the thief.")
            player("It seems you redirected his message regarding a certain god-weapon for your own ends.")
            npc("So THAT is what this is about... I should have known the deal was too good to have no repercussions...")
            player("It seems as though you might be responsible for this curse that has befallen you by not delivering Rennard's message to the correct person.")
            npc("Ha! That is not a truthful assessment of the story... You might think differently if you had heard my side of events.")
            options(
                DialogueOption("tellmeyourstory", "Tell me your story", skipPlayer = true),
                DialogueOption("goodbye", "Goodbye then", skipPlayer = true),
            )

        label("goodbye")
            player("Well, that's all fascinating, but I just don't particularly care. Bye-bye.")

        label("tellmeyourstory")
            player("Please let me hear your side of the story then...")
            npc("Well, if you have spoken to Rennard, then you will know that he had somehow managed to obtain a very valuable weapon, and was looking for buyers.")
            npc("What he probably didn't tell you, was that he met me in a drunken stupor in some smoke filled tavern, and I offered to arrange a purchaser for his item, in exchange for a small finders fee.")
            player("So you knew what the staff was?")
            npc("The god-staff of Armadyl? Well, of course I did.")
            npc("Honestly, you would have to be pretty slow-witted to not recognise a legendary artefact such as that.")
            player("Wait, I don't understand, Rennard said that he had a buyer already in mind, and that you diverted his message to a General Zamorak instead?")
            npc("Ha! Here is a word of advice for you adventurer; Never trust the words of a drunk.")
            npc("Whatever he might have thought he was doing with it, in the end all that happened was he left me to arrange a purchaser for the item.")
            player("So you thought you would offer it to General Zamorak?")
            npc("Ah yes, Lord Zamorak. He was merely a mortal back then, you know?")
            npc("Yet I could see great things in store for him even then. He had a kind of brilliant ruthlessness... And that special kind of vicious streak you see so rarely...")
            npc("Well anyway, when given the task of selling a weapon forged by the very gods themselves, I naturally thought of Zamorak as a potential buyer.")
            npc("I was a messenger in his employ anyway, so it was a mere trifle to find him and deliver the news, and I knew of his particular interest in armour and weaponry of all kinds.")
            npc("Yes, he was always quite the connoisseur when it came to weaponry...")
            npc("But I digress. I let Lord Zamorak know that there was some drunken fool with an artefact of incredible power that could probably be bought off with a few jewels and trinkets,")
            npc("and he escorted me to the tavern and made the purchase there and then.")
            npc("It was a satisfactory deal all around, I got a share of the sale price from Rennard, and I greatly increased my prestige amongst Zamorak and his followers.")
            npc("But maybe... Perhaps the events that followed were responsible for my cursed state...")
            player("Events that followed?")
            npc("I can not tell you of them precisely, for I myself was not there to witness them.")
            npc("I am after all, simply a messenger. When... 'it' happened, I was busy elsewhere delivering a message from Zamorak to the rest of his Mahjarrat ilk.")
            player("When 'it' happened? What was 'it'?")
            npc("As I have explained, I was not there, and I do not know what Zamorak did with the staff, but whatever it was resulted in his banishment by the other gods for many years.")
            npc("The very strange thing was that Saradomin must have known about it, whatever it was, before it even happened...")
            player("Really? Why do you say that?")
            npc("Well, it was the contents of the message I was returning to Zamorak;")
            npc("Lucien seemed quite certain that there was a spy for Saradomin somewhere amongst his followers named Lennissa.")
            npc("If whatever happened to the staff caused this curse to befall me, then it is certain that she too would have been afflicted, because she would have been in the very heart of the action.")
            npc("Hmmm....")
            npc("You have given me much to think on adventurer. I would like to reward you with my sturdy messenger boots, may they aid you in your travels.")
            exec { player, npc ->
                setAttribute(player, CurseOfZaros.attributeKharrimSpoke, true)
                addItemOrDrop(player, Items.GHOSTLY_BOOTS_6106)
            }
            player("But where can I find this Lennissa?")
            npc("Ah yes, the whereabouts of the treacherous Lennissa...")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("subsequenttime")
            player("Hello again Kharrim.")
            player("Can you remind me where to find Lennissa?")
            npc("Ah yes, the whereabouts of the treacherous Lennissa...")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("curseofzaros1")
            npc("Well, she was always sickeningly obedient to Saradomin, so I would expect her to have run to some great place of worship of him if she was affected by the curse to try and gain his blessing.")
            player("Okay, well I'll try and find her for you then.")

        label("curseofzaros2")
            npc("According to Lucien's intelligence report, she had been uncovered as a spy by her constant use of ships to ferry information...")
            npc("It is entirely possible she would be located somewhere coastal to this day!")
            player("Okay, well I'll try and find her for you then.")

        label("curseofzaros3")
            npc("Well, we knew little about her or she would have been caught and exposed as a traitor and a spy, but Lucien did mention that he had evidence that she was a great fan of ball games...")
            player("Okay, well I'll try and find her for you then.")


        label("lostghostlything")
            player(ChatAnim.SAD, "I lost those boots you gave me...", "Can I have some more please?")
            exec { player, npc ->
                addItemOrDrop(player, Items.GHOSTLY_BOOTS_6106)
            }
            npc(ChatAnim.SAD, "How strange...", "They seemed to return to me when you lost them...")

    }
}