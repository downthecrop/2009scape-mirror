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
class MysteriousGhostDhalakDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MysteriousGhostDhalakDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MysteriousGhostDhalakDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(2385, 2386, 2387)
    }
}

class MysteriousGhostDhalakDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)){
                if (2384 + getAttribute(player, CurseOfZaros.attributePathNumber, 0) == npc.id) {
                    if (getAttribute(player, CurseOfZaros.attributeDhalakSpoke, false)) {
                        if (inInventory(player, Items.GHOSTLY_HOOD_6109) || inEquipment(player, Items.GHOSTLY_HOOD_6109)) {
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
            player("Hello Dhalak.")
            npc("You see my form, hear my words, and know my name, yet your face I recognise not...")
            npc("Be you some mighty sorcerer to bind me so?")
            player("Um... Well, not really...") // player("Well, I don't mean to brag, but I guess I am with my level 99 magic...") if you are 99 lvl magic but calm the fuck down showoff
            player("But that is besides the point. It is not I who has trapped you here as a ghost.")
            npc("Then how comes it to be that you know my name stranger?")
            player("Lennissa told me about you, and where to find you?")
            npc("Lennissa? Oh that poor sweet girl... Has my foolishness cursed her as well as myself???")
            player("Your foolishness?")
            npc("The story shames me stranger, I wouldst rather keep it unto myself.")
            options(
                DialogueOption("tellmeyourstory", "Tell me your story", skipPlayer = true),
                DialogueOption("goodbye", "Goodbye then", skipPlayer = true),
            )

        label("goodbye")
            player("Well, that's all fascinating, but I just don't particularly care. Bye-bye.")

        label("tellmeyourstory")
            player("Look, I don't want to force you into telling me, but perhaps sharing it with someone might relieve your guilt?")
            npc("Aye... Perhaps it might at that.")
            npc("So what has Lennissa told you of the events of the day this curse befell me?")
            player("Well, she told me that she was working as an undercover agent of Saradomin amongst the followers of some 'Empty Lord', and when news of the theft of a god-weapon reached her, she passed the message on to")
            player("you instead of taking it to Saradomin because she was scared her cover might be blown.")
            npc("Aye, that is a fair account of events...")
            player("But I don't understand why you didn't take her message to Saradomin?")
            npc("Stranger, my foolishness was a result of my respect for Saradomin, not as a result of any attempted treachery!")
            player("So why didn't you pass on Lennissa's message? As I understand it something happened with that staff that could have been avoided if you had passed her message on!")
            npc("(sigh) I know not what occurred with that god-weapon, but I have my suspicions...")
            npc("Let me explain myself. I was Lennissa's immediate superior, and I was often her contact for missions.")
            npc("Because of this role, I had access to a larger picture of what was happening than she herself did, and I was not only well aware that her presence amongst the enemy camp had been detected, but I was also well aware that")
            npc("there was a growing faction amongst them who were plotting to overthrow their master.")
            player("Their master being...?")
            npc("That I will not tell you. I will tempt the fates no more than I already have done.")
            npc("But anyway, it had come to my attention that the Mahjarrat who had been liberated from the control of Icthlarin did not much appreciate one form of slavery to another, and under the leadership of the mighty")
            npc("Zamorak were making plans to overthrow their master and take his power for themselves.")
            npc("Now, as powerful, long-lived and evil as they were, they were still just mortal, and I made the decision that it would be of benefit to my Lord Saradomin for his mightiest rival to be distracted by such internal conflicts.")
            player("So that is why you decided not to pass the report from Lennissa on?")
            npc("Yes, but my guilt is more than simply inaction...")
            npc("I knew that with such a weapon, Zamorak would be capable of launching an attack that could actually stand a chance of success, but I also knew that he would never be able to get a chance to use it in a battle for")
            npc("being a god-weapon its very presence would have sung out to their leader.")
            player("I'm guessing you did something about that, then?")
            npc("Indeed I did. To my eternal shame, I decided that I would assist Zamorak and his henchmen in their battle, by secretly casting a spell of concealment upon the staff so that")
            npc("they might use it secretly against their master.")
            player("So Zamorak knew about this?")
            npc("No, nobody except myself, and now you, knew that I cast such a spell....")
            npc("Had I known what a threat to my Lord Saradomin Zamorak would later become, I wouldst have taken the message to Saradomin immediately! Alas, it is all too easy to see your mistakes after you")
            npc("have made them...")
            player("I'm confused. What exactly happened with this staff anyway? And why have all these various random people been cursed because of it?")
            npc("I cannot answer your question with anything other than my own suppositions, but I do know of one who might be able to, and if any man deserved to be cursed for their actions that day, it was he!")
            player("Who are you speaking of?")
            npc("His name was Viggora. He was an evil man, brutal and vicious, and deadly with a blade.")
            npc("He was one of the few humans Zamorak allowed to rise to a position of power amongst his rebels, possibly because he imitated those same qualities of Zamorak.")
            npc("If anyone knows what Zamorak did with that god- weapon to have caused this curse to have befallen us, it would have been he, for he would have been fighting on Zamorak's very right hand side in their rebellion.")
            npc("Please, if this curse can be lifted, find Viggora and find out what he has wrought upon us! I have no wealth nor magic to aid you, but take my hood as reward;")
            exec { player, npc ->
                setAttribute(player, CurseOfZaros.attributeDhalakSpoke, true)
                addItemOrDrop(player, Items.GHOSTLY_HOOD_6109)
            }
            npc("it has served me well these centuries past, and may bring you luck.")
            player("Where would you suggest I look for Viggora?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("subsequenttime")
            player("Dhalak, where can I find the swordsman Viggora?")
            exec { player, npc ->
                // 1 of 3 paths.
                loadLabel(player, "curseofzaros" + getAttribute(player, CurseOfZaros.attributePathNumber, 0))
            }

        label("curseofzaros1")
            npc("Ah, the evil swordsman Viggora... A rogue like him would probably flock to his own kind.")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros2")
            npc("Ah, the evil swordsman Viggora... Perhaps he has returned to his castle in the dark lands?")
            player("Okay, well I'll try and find him for you then.")

        label("curseofzaros3")
            npc("Ah, the evil swordsman Viggora... Paddewwa was where he fought many battles, perhaps he has returned to one of his old haunts?")
            player("Okay, well I'll try and find him for you then.")


        label("lostghostlything")

        player(ChatAnim.SAD, "Could I have that hat again? I seem to have misplaced it", "somewhere...")
        exec { player, npc ->
            addItemOrDrop(player, Items.GHOSTLY_HOOD_6109)
        }
        npc("Certainly, I am not sure how, but it returned to me by", "some magic or other.")

    }
}