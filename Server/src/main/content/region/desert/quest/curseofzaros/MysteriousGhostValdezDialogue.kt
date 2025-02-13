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
class MysteriousGhostValdezDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MysteriousGhostValdezDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MysteriousGhostValdezDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(2381) // wrong const SICK_LOOKING_SHEEP_4_2381 -> should be correct MYSTERIOUS_GHOST_2381
    }
}

class MysteriousGhostValdezDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)){
                if (getAttribute(player, CurseOfZaros.attributeValdezSpoke, false)) {
                    if (inInventory(player, Items.GHOSTLY_ROBE_6107) || inEquipment(player, Items.GHOSTLY_ROBE_6107)) {
                        loadLabel(player, "subsequenttime")
                    } else {
                        loadLabel(player, "lostghostlything")
                    }
                } else {
                    loadLabel(player, "firsttime")
                }
            } else {
                loadLabel(player, "noghostspeak")
            }
        }

        CurseOfZaros.withoutGhostspeak(this)

        label("firsttime")
            player("Hello.")
            npc(ChatAnim.EXTREMELY_SHOCKED, "H-hello!")
            player(ChatAnim.THINKING, "So what's up?")
            npc(ChatAnim.EXTREMELY_SHOCKED, "I cannot believe it!", "You can see me?", "You understand my words?")
            player("Sure can.", "So why are you hanging around here?")
            npc(ChatAnim.SAD, "My tale is one of woe...", "No doubt you will have little interest in hearing it...")
            npc(ChatAnim.SAD, "Though it has been so many moons since last I had", "company in this endless non-life...")
            options(
                DialogueOption("tellmeyourstory", "Tell me your story", skipPlayer = true),
                DialogueOption("goodbye", "Goodbye then", skipPlayer = true),
            )

        label("goodbye")
            player("Well, that's all fascinating, but I just don't particularly care. Bye-bye.")

        label("tellmeyourstory")
            player("Well, actually I would like to know what happened to you to turn you into an invisible ghost.")
            player(ChatAnim.SUSPICIOUS, "If only so I can make sure it doesn't happen to me...")
            npc("My name is Valdez. I served my Lord Saradomin faithfully for many years, as an explorer of this strange land we had been brought to.")
            npc("I remember the day this curse fell upon me clearly... I had just discovered a huge temple, hidden below the ground, of one of Saradomin's compatriots.")
            npc("I am unsure who had built it, or why they had left it seemingly abandoned, but inside I located a great treasure...")
            npc("It was the godstaff of Armadyl.", "Oh, how I rue my choice that day!")
            player("Choice?")
            npc("Aye, stranger.", "I chose that day to take it so that my Lord Saradomin's", "power and prestige could be increased by its possession.")
            npc("A god-weapon!", "Do you have any comprehension of the difficulty and", "rarity in obtaining such a thing?")
            npc("To find such an artefact of power just lying around, it is almost incomprehensible...")
            npc("So it was there in that deserted temple that I made my", "choice.", "I took the staff, and left that temple for Entrana", "immediately.")
            npc("This was the cause of my cursed state.")
            player("What, you mean you gave it to Saradomin and in return he cursed you???")
            player("Seems kind of ungrateful if you ask me...")
            npc("No stranger, you misunderstand completely...", "Firstly my gracious Lord would never treat anyone in", "such a manner;", "If he felt it was beyond my bounds as a mere mortal")
            npc("to hold such an artefact, he would simply have commanded me to return it to whence I had claimed it, and I being eternally loyal would have obeyed without question...")
            player("And secondly?")
            npc("And secondly, I never managed to pass the artefact on to my Lord...")
            npc("The vile thief Rennard accosted me as I made my way to Entrana, and after defeating me with a sneak attack, plundered the staff from my person, and left me for dead...")
            npc("I do not know what became of the staff, but I can feel in my very bones that whatever its final fate was, it is somehow related to this curse upon me...")
            player("Wow", "Tough break.")
            npc("I am sorry to bore you with my tale stranger, please allow me to compound my rudeness by asking you for one favour, small to perform?")
            player("Eh, I won't make any promises, but if it's nothing too annoying I guess I can help you out.")
            npc("Many thanks stranger, this existence tortures me...")
            npc("I need you to find Rennard and if he has the staff yet reclaim it, or find out what hideous deed he performed to curse me so!")
            npc("I have nothing I may offer you save this piece of clothing, please take it as payment...")
            exec { player, npc ->
                setAttribute(player, CurseOfZaros.attributeValdezSpoke, true)
                // Set the path to follow.
                setAttribute(player, CurseOfZaros.attributePathNumber, (1..3).random())
                addItemOrDrop(player, Items.GHOSTLY_ROBE_6107)
            }
            player("Where can I find this Rennard then?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("subsequenttime")
            npc("Thank you for hearing my tale...", "It has been so lonely here...")
            player("Can you remind me where to find the thief Rennard who caused this curse to befall you again?")
            npc("Of course...")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("curseofzaros1")
            npc("Ah, the infamous Rennard...", "The last I had heard of him, he had sought passage on", "a ship crewed by none but the most dastardly lowly", "pirates...")
            npc("I also heard that this ship had been caught in a violent", "storm, and stranded upon rocks, where the pirates then", "made their home...")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros2")
            npc("Ah, the infamous Rennard...", "The last I had heard of that vile thief, he had joined a", "group of bandits in an evil land to the North-east of", "here, where they had made their home living outside of")
            npc("the reach of the authorities that pursued them...")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros3")
            npc("Ah, the infamous Rennard...", "The last I had heard of that vile thief, he had joined a", "group of bandits in a barren land to the South-east of", "here, where they prey upon the unsuspecting visitors to")
            npc("the desert awaiting the return of their dark master...")
            player("Okay, well I'll try and find him for you then.")


        label("lostghostlything")
            player(ChatAnim.SAD, "I lost that Robe top you gave me...", "Can I have another please?")
            exec { player, npc ->
                addItemOrDrop(player, Items.GHOSTLY_ROBE_6107)
            }
            npc(ChatAnim.SAD, "It seems as though the curse that keeps me here", "extends to my very clothing...")
            npc("Here, take it, the moment you lost it, it returned to", "me...")

    }
}