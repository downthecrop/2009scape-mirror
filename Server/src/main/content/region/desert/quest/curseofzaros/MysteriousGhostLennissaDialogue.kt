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
class MysteriousGhostLennissaDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MysteriousGhostLennissaDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MysteriousGhostLennissaDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(2391, 2392, 2393)
    }
}

class MysteriousGhostLennissaDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)){
                if (2390 + getAttribute(player, CurseOfZaros.attributePathNumber, 0) == npc.id) {
                    if (getAttribute(player, CurseOfZaros.attributeLennissaSpoke, false)) {
                        if (inInventory(player, Items.GHOSTLY_ROBE_6108) || inEquipment(player, Items.GHOSTLY_ROBE_6108)) {
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
            player("Hello. You would be Lennissa, I take it?")
            npc("Who are you? Where did you hear that name? How comes it that you can see and speak to me?")
            player("Well, a ghost called Kharrim directed me towards you.")
            npc("So that weasel Kharrim has been blighted by this curse too?")
            npc("Ha, a good thing too. If anybody deserved such a fate it would be one such as him.")
            player("I guess you didn't get along then?")
            npc("No, evil scum such as he should never have been allowed to walk this world.")
            npc("What lies has he told you to come here? Have you come to try and kill me?")
            player("Well, I'm not sure I could if I tried, but that is not why I have come to you.")
            npc("Then speak, and speak well, for I may yet be dead, but am still a danger to those who cross me.")
            player("Actually, I'm trying to work out why all of you invisible ghosts seem to have been cursed.")
            player("I'm not sure how exactly, but the trail seems to have led me to you...")
            npc("That makes no sense... I served Saradomin faithfully my entire life, then all of a sudden I find myself reduced to this state!")
            player("Well, it seems as though that may have been the cause...")
            npc("What? Explain yourself.")
            player("As I understand it, it was something to do with the Staff of Armadyl, and your refusal to tell Saradomin that it had been stolen...")
            npc("What? But... But that is not how it happened at all!")
            options(
                DialogueOption("tellmeyourstory", "Tell me your story", skipPlayer = true),
                DialogueOption("goodbye", "Goodbye then", skipPlayer = true),
            )

        label("goodbye")
            player("Well, that's all fascinating, but I just don't particularly care. Bye-bye.")

        label("tellmeyourstory")
            player("Then please, go ahead and tell me the events of that day in your own words...")
            npc("Let me see... I had been working as a spy for my Lord Saradomin, amongst the forces of... the Empty Lord.")
            player("'The Empty Lord'? Who is that?")
            npc("I will not give you his name, for to do so would give him power here.")
            npc("Let us just say that he was a fearsome deity, whose strength was greater than all the gods we knew of on this realm at the time.")
            npc("It is probably worth mentioning that at this time we had no knowledge of the mysterious nature god Guthix.")
            player("So he was stronger than Saradomin?")
            npc("As Saradomin was, yes. As Saradomin is now? Who can say?")
            player("I see... Please, continue.")
            npc("As I say, I was working as a spy within the very camp of my Lord's enemies.")
            npc("I knew that should I have been caught, I risked being killed upon the spot, but my combat skills were always formidable, and if truth be told, there was a fair amount of dissent amongst... 'his' followers anyway.")
            player("How do you mean 'dissent'?")
            npc("Ah, to not understand this, you must have led a sheltered life...")
            npc("Let me tell you this: Evil will always breed more evil, and will never be satisfied with what it has.")
            npc("The Empty Lord chose to ally himself with the dark creatures of this world, fully aware that their own natures would cause them to rally against his rule, and take every opportunity they could to betray him.")
            npc("This has always been the nature of evil. Perhaps he thought his power could prevent such treachery?")
            npc("This allowed me freedom amongst their camp, for it was always easy to point the finger of suspicion at some unsuspecting necromancer or foolish Mahjarrat if it seemed as though my activities had been discovered.")
            npc("Similarly, should I ever be caught in the act of my sabotage, it was all too easy to bribe whoever found me or persuade them into believing it was just some minor treachery of my own, rather than my work for my")
            npc("Lord Saradomin.")
            player("Okay... Well, that makes sense, but I don't understand what the Staff of Armadyl had to do with this...?")
            npc("As I have told you, the Empty Lord was extremely powerful, but not so powerful that he could rule over the other deities of this world without opposition.")
            npc("Should he have made a move against any other god, then he could still have been easily brought down by the combined efforts of the others.")
            npc("The theft of Armadyl's staff changed this however.")
            npc("If he had taken possession of this god-weapon, then his power would have been so great that he could have overthrown all on this world, and made it into his own image!")
            npc("I could not allow such a thing to happen!")
            npc("I went immediately to my comrade Dhalak, the mage, and told him that a message had come to the lair offering this weapon for sale!")
            npc("I knew that as soon as my Lord Saradomin heard this, he would contact Armadyl to inform them of the theft, and the matter would have been resolved quickly and discreetly.")
            player("So you passed this information to Dhalak instead of taking it to Saradomin yourself?")
            npc("To my eternal shame, I indeed failed my Lord Saradomin...")
            npc("I could not risk taking the message directly, for I feared my disguise had been uncovered.")
            npc("Lucien particularly had been taking an unhealthy interest in my activities, and I had a gut feeling that to make any obvious moves against the Empty Lord would have been my undoing.")
            npc("But Dhalak was a noble man! I cannot believe that he would not have taken my message immediately to Lord Saradomin!")
            player("Well, it seems like he didn't, but I don't know why not...")
            npc("Please adventurer, discover what foul fate must have befallen him for him to have neglected his duty!")
            npc("I have not much to offer as reward, but for these spare robes I wore while on assignment...")
            exec { player, npc ->
                setAttribute(player, CurseOfZaros.attributeLennissaSpoke, true)
                addItemOrDrop(player, Items.GHOSTLY_ROBE_6108)
            }
            npc("Please find him and discover why I am cursed like this!")
            player("Where would I be able to find this Dhalak then?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("subsequenttime")
            player("Hello Lennissa. Can you remind me where to find Dhalak?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("curseofzaros1")
            npc("Dhalak? Well, he was always a knowlegeable mage, so if this curse has befallen him as well, I would suspect he would be researching how to free himself of it.")
            npc("I would look for a library to find him if I were you.")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros2")
            npc("Dhalak? He was always a loyal follower of Saradomin... I think he would have found an altar to Saradomin so that he may pray for this curse to be lifted.")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros3")
            npc("Dhalak? I know not where, but he would try and make the most of his situation if he has been cursed, and find a place to lift his spirits!")
            player("Okay, well I'll try and find him for you then.")


        label("lostghostlything")
            player(ChatAnim.SAD, "Could I have that rob bottom again? I seem to have", "misplaced it somewhere...")
            exec { player, npc ->
                addItemOrDrop(player, Items.GHOSTLY_ROBE_6108)
            }
            npc(ChatAnim.SAD, "Certainly, I am not sure how, but it returned to me by", "some magic or other.")

    }
}