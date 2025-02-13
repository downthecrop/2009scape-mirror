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
class MysteriousGhostViggoraDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player): DialoguePlugin {
        return MysteriousGhostViggoraDialogue(player)
    }
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MysteriousGhostViggoraDialogueFile(), npc)
        return false
    }
    override fun getIds(): IntArray {
        return intArrayOf(2394, 2395, 2396)
    }
}

class MysteriousGhostViggoraDialogueFile : DialogueLabeller() {
    override fun addConversation() {
        exec { player, npc ->
            if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552) || inEquipment(player, Items.GHOSTSPEAK_AMULET_4250)){
                if (2393 + getAttribute(player, CurseOfZaros.attributePathNumber, 0) == npc.id) {
                    if (getAttribute(player, CurseOfZaros.attributeViggoraSpoke, false)) {
                        if (inInventory(player, Items.GHOSTLY_CLOAK_6111) || inEquipment(player, Items.GHOSTLY_CLOAK_6111)) {
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
            player("So... You must be the infamous Viggora.")
            npc("Hold thy tongue varlet! Speak fast, how come you to find me here, and how doth you understand mine speech?")
            player("You want me to hold my tongue and tell you how I found you?")
            npc("Cease thy chatter and respond to my demand!")
            player("Cease my chatter AND respond to your demand?")
            npc("I warn thee knave, this curse upon me hath not improved my temper, these centuries past...")
            player("Well, it's actually about that curse that I have come to speak to you.")
            npc("Oh, be that so? Then forgive my swift anger, and speak to me of how you plan to break this curse.")
            player("Erm... I didn't actually mention anything about breaking the curse...")
            npc("Then what lets you dare speak to me?")
            player("Well, I heard of your name from a mage called Dhalak, and I'm trying to find out what exactly caused the curse to befall him, and you as well apparently.")
            npc("Ha! So the weak-willed mage was cursed along with me?")
            npc("Well now, that is an interesting turn of events... Then am I to assume that Valdez, Rennard, Kharrim and Lennissa were also cursed along with me?")
            player("...How did you know that?")
            npc("Ha ha ha! Oh, the curse cut deeper than I had previously thought!")
            npc("Stranger, this news has brought me a ray of sunshine in an otherwise dreary millennium! Please, ask any question you wish!")
            options(
                DialogueOption("tellmeyourstory", "Tell me your story", skipPlayer = true),
                DialogueOption("goodbye", "Goodbye then", skipPlayer = true),
            )

        label("goodbye")
            player("Well, that's all fascinating, but I just don't particularly care. Bye-bye.")

        label("tellmeyourstory")
            player("Erm... Thanks, I think. So what exactly happened on that day you were all cursed?")
            player("I know that Valdez discovered the Staff of Armadyl, was robbed by Rennard, who then sent Kharrim to tell Zamorak of it.")
            player("Meanwhile Lennissa heard of the sale, and informed Dhalak who placed an enchantment upon it so that its power would be hidden.")
            player("I still don't know what happened with the staff to cause this curse, or what you had to do with it though...")
            npc("Well stranger, rest yourself awhile, and I will recount a tale of the events of that day, for I was one of the few actually there when it happened...")
            player("When what happened?")
            npc("When my Lord Zamorak first got his taste of godhood!")
            player("Wow. Sounds like quite the dinner party anecdote.")
            npc("You can take the snide venom out of your voice whelp, you came to me; 'twas not the other way round.")
            player("Okay, okay. Please continue.")
            npc("Well now, let us see... As you may have heard tell, my affiliation lay with General Zamorak, a mighty warrior of the Mahjarrat tribe, and my skill on the battlefield had quickly brought")
            npc("me to his attention.")
            npc("So pleased was he with my bloodlust that he promoted me on the battlefield once to serve in his honour guard, and let me tell you, this was a rare honour indeed, for I was the only human chosen to take such a position.")
            player("Really?")
            npc("Oh yes, the dragon riders, Mahjarrat, demons and vampyre warriors made up the bulk of the force, but I wager I was their equal in all ways of combat.")
            npc("Ha, when I think of someone like Lucien struggling to lift a blade, in some ways I was even their better!")
            player("Please continue.")
            npc("Well anyway... Myself and the rest of Zamoraks honour guard were formulating stratagems in our battle-tent, when that sneaky messenger Kharrim came in offering to sell us")
            npc("the god-staff of Armadyl!")
            npc("Naturally, we suspected that this was some trick by our Lord to test our loyalty...")
            player("Yes, who was your lord? Everyone has been very evasive about that...")
            npc("Quiet fool, all things in their course; You are disrupting my train of thought!")
            player("Sorry...")
            npc("Well anyway, we thought it was too good to be true, yet when we visited this scummy tavern we were amazed to discover there was no trick, no test of loyalty, no hidden trap:")
            npc("Somehow this fool had actually managed to obtain the god-staff of Armadyl!")
            npc("Its power was incredible, you could almost feel the energies crackling around it in the air!")
            player("So what happened then?")
            npc("Well, with such a weapon, the plans we had been developing for a rebellion against our lord could finally be put into action, but we knew that we would have to act swiftly, before he heard that we had a weapon")
            npc("capable of defeating him, and we would have to act decisively, for even amongst our group there were still those loyal to the lord - such as that pathetic fool Azzanadra.")
            player("So JUST WHO WAS this lord you speak of?")
            npc("And I tell thee again, I will say when it is appropriate, now do not disrupt my tale!")
            player("Okay, carry on then...")
            npc("So anyway, Lord Zamorak and his most trusted compatriots, namely myself, Hazeel, Drakan, Thammaron and Zemouregal made plans to overthrow our lord using the god-weapon, and by pledging")
            npc("allegiance to Zamorak as our master, were each to be given a large piece of land as our own in return.")
            npc("We decided to move immediately, before anyone got cold feet, or any other parties could interfere in our work, and made haste towards the castle where our Lord lived.")
            npc("If Lucien had not been otherwise occupied, he would have probably accompanied us with his magicks, but it turned out the foolish Dhalak had made his involvement unnecessary with some spells of his own allowing us to")
            npc("get close enough to the castle with the staff without the Empty Lord being able to sense its presence.")
            player("So your lord was the one that cursed you?")
            npc("I am coming to that... So anyway, we made our way to the castle, under the pretense that we had war plans against Saradomin and the other deities to discuss.")
            npc("As usual, our lord was guarded well, but this was why Zamorak had brought his most trusted fighters with him.")
            npc("While we distracted the Empty Lord with our feints and attacks, and kept his bodyguards busy, Lord Zamorak outflanked him, unsheathed the staff and plunged it into his back!")
            npc("Ah, it was a glorious sight... At that moment I was reminded for whom I fought, and why General Zamorak had earned his nickname 'the scourge' upon the battlefield...")
            player("And the next thing you know you were cursed?")
            npc("No, it was not quite that simple... The Empty Lord turned away from our battle, eyes burning with hatred, and towards Zamorak instead. Seeing this, we all fought with extra vigour, so that")
            npc("General Zamorak would not face our lord alone, but we were outnumbered by many hundreds of warriors and demons, and could not reach him to assist him!")
            player("So what then?")
            npc("Why, it was the Empty Lord versus Zamorak, in single combat! And the sight of the battle will be with me forever more...")
            npc("The Empty Lord was a powerful god, stronger than any of the others awake at that time, possibly even as strong as Guthix is, and Zamorak was but a mortal: A Mahjarrat warrior all the same, with all of the")
            npc("strength and power that that entails, but mortal nonetheless, but to see him fight, you would not think of him as a 'mere' anything...")
            npc("He was war itself! Flurry after flurry of blows he rained upon the Empty Lord, and the very castle walls shook and quivered with their power, but the Empty Lord would not fall!")
            npc("Even with the weapon of a god embedded in his back, he fought on, and with each blow our victory seemed less and less certain...")
            player("So what then?")
            npc("Well, then a miracle happened. Or luck. Or natural justice.")
            npc("You can call it what you want, but as the Empty Lords hands wrapped tightly around Zamorak's throat, Lord Zamorak, kicking and screaming defiantly and radiant in his anger until the very last, plunged towards the")
            npc("Empty Lord, who seemed to lose his footing slightly, and fell in such a way so that the staff plunged deeper into his body, but also impaled Lord Zamorak with it at the same time...")
            npc("And then...")
            player("And then what?")
            npc("And then nothing. There was a sudden flash of bright light, and then a sudden blink of cold darkness, and it was over.")
            npc("Zamorak stood over the Empty Lord who was slowly... fading from existence... And as he faded, it seemed as though... It almost seemed as though Zamorak became more real,")
            npc("more solid than he had been before...")
            npc("And as the Empty Lord faded from this world completely, I heard his voice, almost a whisper upon the wind, cursing all who had helped Zamorak in his victory, which as you now tell me seems to have been all who")
            npc("were responsible for the staff ending up in Zamorak's hands at the castle.")
            npc("As I heard it, I saw that I too was fading, just as the Empty Lord had, and I called to my brethren for their assistance, but they could no longer hear my words, nor see my form.")
            npc("It was then that the other gods appeared and banished Zamorak from the world completely for daring to kill one of their kind, although as it turned out it didn't quite work out that way for them, when he returned")
            npc("stronger than ever, a god himself.")
            npc("But the god wars were another story entirely...")
            player("But... I don't understand... If it was Zamorak who used the weapon, then why was it only you who were cursed?")
            player("And the other people who were cursed, why them? Why not the other Mahjarrat for example?")
            player("And just who was this 'Empty Lord' you keep speaking of?")
            npc("Well, in my life I was nothing but a warrior. I had no hidden knowledge, I didn't especially care about the gods or their magics, and I certainly didn't respect them.")
            npc("Now in my... I suppose this is my death. I am but a shade on the wind, unnoticed by all who pass me, until today anyway, and the only answer I can give you is that the others who were with me, the")
            npc("Mahjarrats and the Vampyre, they were beings of magic.")
            npc("It runs through their very veins, and ebbs through their bones.")
            npc("Who knows why the curse fell as it did? Perhaps as a mere human I was more susceptible to it, when they were not. Perhaps they too are cursed, but their life spans are so")
            npc("long that it will be millennia before they feel it upon them. Perhaps because it did not affect them, it extended backwards through time, to the moment the staff was")
            npc("taken from its rightful place, and all who had known of its theft were cursed too. Perhaps there are others also cursed, who played no part in this tale, and were merely unlucky enough to be")
            npc("in the wrong place at the wrong time...")
            npc("I don't know. Things do not always happen for a reason, just as tales do not always end with all of the loose ends neatly tied up and all answers supplied.")
            npc("I have simply told you the events I was witness to, for I can do no more.")
            npc("You have cheered me no end to let me know that this curse that has afflicted me has not left me alone here, in this void between worlds.")
            exec { player, npc ->
                setAttribute(player, CurseOfZaros.attributeViggoraSpoke, true)
                addItemOrDrop(player, Items.GHOSTLY_CLOAK_6111)
            }
            npc("Perhaps I will hunt down these others who have also been cursed as I was, but I feel I must reward you for your efforts; Here, take my cloak, it is drenched in the blood of a")
            npc("thousand foes, and may bring you luck in battle.")
            player("So how can I break this curse?")
            npc("Who knows? If it was the death curse of the Empty Lord, there may be no way to break it.")
            npc("If it was not his death curse, and he is still alive but not on this world, then the only way to break it may be to bring him back here;")
            npc("But I would rather stay cursed than suffer under his rule again...")
            player("But WHO was this 'Empty Lord'? WHAT was his NAME?")
            npc("You do not know? You have not guessed yet?")
            npc("He was Zaros.")

        label("subsequenttime")
            player("Hello.")
            npc("Hello yourself.")
            player("I really liked your little story. Can you tell me another one?")
            player("Preferably something with a big fight and lots of explosions!")
            npc("...You are a very strange young @g[man/woman].")

        label("lostghostlything")
            player(ChatAnim.SAD, "Can I have that cloak back?")
            exec { player, npc ->
                addItemOrDrop(player, Items.GHOSTLY_CLOAK_6111)
            }
            npc("Hmph.", "I suppose.")

    }
}