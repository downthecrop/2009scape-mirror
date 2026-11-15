package content.region.misthalin.lumbridge.quest.tearsofguthix

import content.data.Quests
import content.global.skill.runecrafting.abyss.ZamorakMageDialogue
import content.region.kandarin.ardougne.quest.hazeelcult.HazeelCult
import content.region.kandarin.barcrawl.BarcrawlManager
import content.region.kandarin.quest.templeofikov.TempleOfIkov
import core.api.finishQuest
import core.api.getAttribute
import core.api.getQuestStage
import core.api.getVarp
import core.api.inInventory
import core.api.isQuestComplete
import core.api.openDialogue
import core.api.removeItem
import core.api.setAttribute
import core.api.setQuestStage
import core.game.dialogue.ChatAnim
import core.game.dialogue.DialogueLabeller
import core.game.dialogue.DialogueOption
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * Anim 2056 - Juna puts head into itself
 * Anim 2055 - Juna lifts tail
 * @author Ovenbread
 * @author Bishop (JunaStories object)
 */

class JunaDialogue : InteractionListener {
    override fun defineListeners() {
        // Juna is a scenery.
        on(Scenery.JUNA_31302, SCENERY, "talk-to") { player, _ ->
            openDialogue(player, JunaDialogueFile(false), NPC(NPCs.JUNA_2023))
            return@on true
        }
        // This is quest varbit 451 controlled. When it is set to 2, JUNA changes in ID
        on(Scenery.JUNA_31303, SCENERY, "talk-to") { player, _ ->
            openDialogue(player, JunaDialogueFile(false), NPC(NPCs.JUNA_2023))
            return@on true
        }
        on(Scenery.JUNA_31303, SCENERY, "tell-story") { player, _ ->
            openDialogue(player, JunaDialogueFile(true), NPC(NPCs.JUNA_2023))
            return@on true
        }
    }
}

class JunaDialogueFile(val fastStory: Boolean) : DialogueLabeller() {
    override fun addConversation() {
        exec { player, _ ->
            if (fastStory) {
                loadLabel(player, "storytime")
                return@exec
            }
            when (getQuestStage(player, Quests.TEARS_OF_GUTHIX)) {
                0   -> loadLabel(player, "quest_not_started")
                1   -> loadLabel(player, "quest_need_bowl")
                100 -> loadLabel(player, "minigame_init")
            }
        }

        label("quest_not_started")
            exec { player, _ ->
                if (TearsOfGuthix.hasRequirements(player)) {
                    loadLabel(player, "has_requirements")
                } else {
                    loadLabel(player, "no_requirements")
                }
                return@exec
            }

        label("no_requirements")
            // TODO: Inauthentic, but absolutely no source on this... -Oven
            npc(ChatAnim.OLD_NORMAL, "You are not strong enough of an adventurer to undertake this quest. Come back when you are stronger.")
            line("You do not meet the quest requirements for Tears of Guthix.")

        label("has_requirements")
            npc(ChatAnim.OLD_NORMAL, "Tell me... a story...")
            player(ChatAnim.THINKING, "A story?")
            npc(ChatAnim.OLD_NORMAL, "I have been waiting here three thousand years, guarding the Tears of Guthix. I serve my master faithfully, but I am bored.")
            npc(ChatAnim.OLD_NORMAL, "An adventurer such as yourself must have many tales to tell. If you can entertain me, I will let you into the cave for a time.")
            npc(ChatAnim.OLD_NORMAL, "The more I enjoy your story, the more time I will give you in the cave.")
            npc(ChatAnim.OLD_NORMAL, "Then you can drink of the power of balance, which will make you stronger in whatever area you are weakest.")
            goto("pre_quest_options")

        label("pre_quest_options")
            options(
                    DialogueOption("storytime", "Okay..."),
                    DialogueOption("end", "Not now."),
                    DialogueOption("tears_lore", "What are the Tears of Guthix?"),
            )

        label("quest_start")
            npc(ChatAnim.OLD_NORMAL, "Your stories have entertained me. I will let you into the cave for a short time.")
            npc(ChatAnim.OLD_NORMAL, "But first you will need to make a bowl in which to collect the tears.")
            // TODO: The camera pans south to the part of the cave containing guthix-infused rocks. -Oven
            npc(ChatAnim.OLD_NORMAL, "There is a cave on the south side of the chasm that is similarly infused with the power of Guthix. The stone in that cave is the only substance that can catch the Tears of Guthix.")
            npc(ChatAnim.OLD_NORMAL, "Mine some stone from that cave, make it into a bowl, and bring it to me, and then I will let you catch the Tears.")
            exec { player, _ ->
                if (getQuestStage(player, Quests.TEARS_OF_GUTHIX) == 0) {
                    setQuestStage(player, Quests.TEARS_OF_GUTHIX, 1)
                }
            }

        label("quest_need_bowl")
            npc(ChatAnim.OLD_NORMAL, "Before you can collect the Tears of Guthix you must", "make a bowl out of the stone in the cave on the south", "of the chasm.")
            exec { player, _ ->
                if (inInventory(player, Items.STONE_BOWL_4704)) {
                    loadLabel(player, "has_bowl")
                } else {
                    loadLabel(player, "no_bowl_options")
                }
            }

        label("no_bowl_options")
            options(
                    DialogueOption("light_creatures_lore", "But I don't know how to reach the cave!"),
                    DialogueOption("tears_lore", "What are the Tears of Guthix?"),
                    DialogueOption("end", "Not now."),
            )

        label("light_creatures_lore")
            npc(ChatAnim.OLD_NORMAL, "I will tell you the story of the light-creatures.")
            npc(ChatAnim.OLD_NORMAL, "Myriad and beautiful were the creatures and civilizations of the early ages of the world. Gielinor was a work of art, shaped lovingly over the millennia by the creative mind of Guthix.")
            npc(ChatAnim.OLD_NORMAL, "Only the sturdiest races survived the Godwars, and even then only by abandoning their high culture and gearing their societies towards war. Of the more delicate races there is now no trace, and almost no memory.")
            npc(ChatAnim.OLD_NORMAL, "One such race had bodies as fragile as snowflakes, yet they built crystal cities that stood for a thousand years.")
            npc(ChatAnim.OLD_NORMAL, "The wind would whisper through the spires and fill them with sweet harmonies, and the rising sun would shine through the precious gems that studded the towers and create inter plays of light as if rainbows were dancing.")
            npc(ChatAnim.OLD_NORMAL, "Indeed, so marvellous was this light-show at its height that the patterns of light themselves became alive, and great flocks of luminous creatures rode along the gem- cast beams, each drawn to its own colour.")
            npc(ChatAnim.OLD_NORMAL, "The creatures you see floating in this chasm are the last sorry remnants of that age. I do not know how they made their way here and survived to this time, but I am grateful for their company.")

        label("tears_lore")
            npc(ChatAnim.OLD_NORMAL, "The Third Age of the world was a time of great conflict, of destruction never seen before or since, when all the gods save Guthix warred for control.")
            npc(ChatAnim.OLD_NORMAL, "The colossal Wyrms, of whom today's dragons are a pale reflection, turned all the sky to fire, while on the ground armies of foot soldiers, goblins and trolls and humans, filled the valleys and plains with blood.")
            npc(ChatAnim.OLD_NORMAL, "In time the noise of the conflict woke Guthix from His deep slumber, and He rose and stood in the centre of the battlefield so that the splendour of His wrath filled the world, and He called for the conflict to cease!")
            npc(ChatAnim.OLD_NORMAL, "Silence fell, for the gods knew that none could challenge the power of the mighty Guthix -- for His power is that of nature itself, to which all other things are subject, in the end.")
            npc(ChatAnim.OLD_NORMAL, "Guthix reclaimed that which had been stolen from Him, and went back underground to return to His sleep and continue to draw the world's power into Himself.")
            npc(ChatAnim.OLD_NORMAL, "But on His way into the depths of the earth He sat and rested in this cave; and, thinking of the battle-scarred desert that now stretched from one side of His world to the other, He wept.")
            npc(ChatAnim.OLD_NORMAL, "And so great was His sorrow, and so great was His life- giving power, that the rocks themselves began to weep with Him.")
            npc(ChatAnim.OLD_NORMAL, "Later, Guthix noticed that the rocks continued to weep, and that their tears were infused with a small part of His power.")
            npc(ChatAnim.OLD_NORMAL, "So He set me, His servant, to guard the cave, and He entrusted to me the task of judging who was and was not worthy to access the tears.")
            exec { player, _ ->
                if (getQuestStage(player, Quests.TEARS_OF_GUTHIX) > 0) {
                    loadLabel(player, "end")
                }
            }
            npc(ChatAnim.OLD_NORMAL, "Tell me... a story...")
            goto("pre_quest_options")

        label("has_bowl")
            player("I have a bowl.")
            npc(ChatAnim.OLD_NORMAL, "I will keep your bowl for you, so that you may collect the tears many times in the future.")
            npc(ChatAnim.OLD_NORMAL, "Now... tell me another story, and I will let you collect the tears for the first time.")
            exec { player, _ ->
                if (removeItem(player, Items.STONE_BOWL_4704)) {
                    finishQuest(player, Quests.TEARS_OF_GUTHIX)
                }
            }

        label("minigame_init")
            npc(ChatAnim.OLD_NORMAL, "Tell me... a story...")
            options(
                    DialogueOption("storytime", "Okay..."),
                    DialogueOption("end", "Not now."),
            )

        label("storytime")
            exec { player, _ ->
                getStory(player)
            }
            line("You tell Juna some stories of your adventures.")
            player(getAttribute(player!!, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).playerFace1,
                getAttribute(player!!, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).playerLine1)
            exec { player, _ ->
                if (getAttribute(player, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).junaLine.isNotEmpty()) {
                    loadLabel(player, "storytime_juna_response")
                } else {
                    loadLabel(player, "storytime_directory")
                }
            }

        label("storytime_juna_response")
            npc(ChatAnim.OLD_NORMAL, getAttribute(player!!, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).junaLine)
            exec { player, _ ->
                if (getAttribute(player, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).playerLine2.isNotEmpty()) {
                    loadLabel(player, "storytime_long_story")
                } else {
                    loadLabel(player, "storytime_directory")
                }
            }

        label("storytime_long_story")
            player(getAttribute(player!!, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).playerFace2,
                getAttribute(player!!, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory).playerLine2)
            goto("storytime_directory")

        label("storytime_directory")
            exec { player, _ ->
                val questFinished = getQuestStage(player, Quests.TEARS_OF_GUTHIX) == 100
                val daysLeft = TearsOfGuthix.daysLeft(player) > 0
                val xpLeft = TearsOfGuthix.xpLeft(player) > 0
                val qpLeft = TearsOfGuthix.questPointsLeft(player) > 0
                when {
                    !questFinished               -> loadLabel(player, "quest_start")
                    daysLeft && xpLeft && qpLeft -> loadLabel(player, "not_ready_all")
                    xpLeft && qpLeft             -> loadLabel(player, "not_ready_xp_qp")
                    daysLeft                     -> loadLabel(player, "not_ready_days")
                    else                         -> loadLabel(player, "ready_to_play")
                }
            }

        // https://www.youtube.com/watch?v=X3M9CiS_BeU&t=3m26s if not enough time has passed.
        label("not_ready_all")
            npc(ChatAnim.OLD_NORMAL, "Your stories have entertained me. But I will not permit any adventurer to access the tears more than once a week. Come back in ${TearsOfGuthix.daysLeft(player!!)} days.")
            npc(ChatAnim.OLD_NORMAL, "You should use that time to have more adventures! You may not re-enter the cave until you have more stories to tell.")
            line("You cannot enter the cave again until you have gained either <col=8A0808>one quest</col>", "<col=8A0808>point</col> or <col=8A0808>${TearsOfGuthix.xpLeft(player!!)} total XP</col>.")

        label("not_ready_xp_qp")
            npc(ChatAnim.OLD_NORMAL, "Your story has entertained me. But it is a poor sort", "of adventurer who only tells stories of the past and", "does not find new stories to tell. I will not let you", "into the cave again until you have had more adventures.")
            line("You cannot enter the cave again until you have gained either <col=8A0808>one quest</col>", "<col=8A0808>point</col> or <col=8A0808>${TearsOfGuthix.xpLeft(player!!)} total XP</col>.")

        label("not_ready_days")
            npc(ChatAnim.OLD_NORMAL, "Your stories have entertained me. But I will not permit any adventurer to access the tears more than once a week. Come back in ${TearsOfGuthix.daysLeft(player!!)} days.")

        label("ready_to_play")
            npc(ChatAnim.OLD_NORMAL, "Your stories have entertained me. I will let you into the cave for a short time.")
            exec { player, _ ->
                if (TearsOfGuthix.isHandsFree(player)) {
                    loadLabel(player, "hands_free")
                } else {
                    loadLabel(player, "hands_not_free")
                }
            }

        // https://www.youtube.com/watch?v=J76OGo-hHlA your hands must be free.
        label("hands_not_free")
            npc(ChatAnim.OLD_NORMAL, "But you must have both hands free to carry the bowl.", "Speak to me again when your hands are free.")

        // https://www.youtube.com/watch?v=Mj3pW-Brv9c
        label("hands_free")
            npc(ChatAnim.OLD_NORMAL, "Collect as much as you can from the blue streams. If", "you let in water from the green streams, it will take", "away from the blue. For Guthix is god of balance, and", "balance lies in the juxtaposition of opposites.")
            exec { player, _ ->
                TearsOfGuthixMinigame.startGame(player)
            }

        label("end")
    }

    companion object JunaStories {

        private const val ATTRIBUTE_JUNA_DIALOGUE = "juna-dialogue"

        private data class JunaStory(
            val playerLine1: String,
            val junaLine:    String = "",
            val playerLine2: String = "",
            val playerFace1: ChatAnim = ChatAnim.NEUTRAL,
            val playerFace2: ChatAnim = ChatAnim.NEUTRAL
        )

        private val barCrawlMiniQuest = JunaStory(
            "I completed my bar crawl card and earned access to the Barbarian Outpost agility course."
        )
        private val abyssMiniQuest = JunaStory(
            "I gained access to the Abyss."
        )
        private val hazeelAgainstCeril = JunaStory(
            "...So that was how I helped return Hazeel to his followers.",
            "So the vile Mahjarrat still walk the earth above? Then Zamorak's defeat was not as thorough as I had thought."
        )
        private val hazeelForCeril = JunaStory(
            "...that was how I foiled the cultists in their plan to resurrect Hazeel.",
            "The Mahjarrat are a vile race. You would do well to steer clear of them."
        )
        private val ikovAgainstLucien = JunaStory(
            "I refused to give Lucien the Staff of Armadyl and had to fight him."
        )
        private val ikovForLucien = JunaStory(
            "...Lucien said that the Staff of Armadyl I gave him had made him more powerful."
        )
        private fun getFremTrials(player: Player): JunaStory {
            return JunaStory("...And that was how I became an honourary member of the Fremennik, and was given my Fremennik name, ${getAttribute(player, "fremennikname", "Charles Barkley")}.")
        }

        private val Quests.junaStory: JunaStory
            get() = when (this) {
                Quests.MYTHS_OF_THE_WHITE_LANDS -> JunaStory(
                    "...I went through the snow imps' prank. Explorer Jack had no idea the 'riches' were really frozen yeti dung!",
                    "It is pleasant to hear that the snow imps are still up to their old tricks."
                )
                Quests.BLACK_KNIGHTS_FORTRESS -> JunaStory(
                    "...in the end the Black Knights were defeated by a cabbage!",
                    "One should never underestimate the vegetables of Guthix."
                )
                Quests.COOKS_ASSISTANT -> JunaStory(
                    "...And, in the end, I found all the ingredients, so the Duke of Lumbridge had a birthday cake after all.",
                    "Ah, a happy ending. It would not be good for such an anniversary to go unmarked."
                )
                Quests.DEMON_SLAYER -> JunaStory(
                    "...so I destroyed the demon Delrith and saved Varrock!",
                    "I remember Delrith. A most unpleasant character; I am glad he has been dispatched."
                )
                Quests.DORICS_QUEST -> JunaStory(
                    "...so once I had got all the ores he wanted, Doric let me use his anvils.",
                    "Such a small task hardly seems worthy of the term 'quest'."
                )
                Quests.DRAGON_SLAYER -> JunaStory(
                    "...so with Elvarg the dragon dead, the master of the Champions' Guild let me in, and I was able to wear rune plate armour!"
                )
                Quests.ERNEST_THE_CHICKEN -> JunaStory(
                    "...So, once I had found all the parts for the machine, poor Ernest could be himself once more.",
                    "That was a good deed. It is a terrible thing to be locked out of one's natural form."
                )
                Quests.GOBLIN_DIPLOMACY -> JunaStory(
                    "...and so the goblins ended up wearing the armour colour they had to start off with!",
                    "Poor, silly goblins! Their race had such potential; if only they could rise above their petty squabbles."
                )
                Quests.IMP_CATCHER -> JunaStory(
                    "...took some time, but I finally got all four beads back, and Mizgog gave me my reward.",
                    "Imps! I remember the age of great war, when armies of Zamorak's imps bloodied the ankles of the other gods' creatures."
                )
                Quests.THE_KNIGHTS_SWORD -> JunaStory(
                    "...that was how I found the Imcando Dwarves and got the knight a new sword."
                )
                Quests.PIRATES_TREASURE -> JunaStory(
                    "...And when I dug in the middle of the park in Falador, I found the pirate's treasure!",
                    "Such is ever the folly of pirates, to bury their loot in the ground so that another can dig it up."
                )
                Quests.PRINCE_ALI_RESCUE -> JunaStory(
                    "...And I had to disguise Prince Ali as a woman in order to smuggle him out!"
                )
                Quests.THE_RESTLESS_GHOST -> JunaStory(
                    "...and once I returned the skull, the ghost was able to rest.",
                    "A strange attachment to an item that has no use after one's death."
                )
                Quests.ROMEO_JULIET -> JunaStory(
                    "...I gave the message to Romeo, but he misunderstood, so they never were together.",
                    "Never was there a story of more woe that this of Juliet and her Romeo."
                )
                Quests.RUNE_MYSTERIES -> JunaStory(
                    "...So, I brought Aubury's notes to Sedridor the head wizard and, from then on, I was able to mine rune essence.",
                    "So, the mortals above have discovered magic once more? Very interesting."
                )
                Quests.SHEEP_SHEARER -> JunaStory(
                    "Out of sheer hard work, I managed to shear some sheep for farmer Fred. Then I got in a spin and created some balls of wool."
                )
                Quests.SHIELD_OF_ARRAV -> JunaStory(
                    "...so that's how I helped return the Shield of Arrav."
                )
                Quests.VAMPIRE_SLAYER -> JunaStory(
                    "...And once the vampire was dead, the people of Draynor no longer lived in fear."
                )
                Quests.WITCHS_POTION -> JunaStory(
                    "...And once I got her all the ingredients, Hetty's potion increased my magical power!",
                    "I see you are on your way to becoming strong in the magical arts."
                )
                Quests.ANIMAL_MAGNETISM -> JunaStory(
                    "...So the attractor works using an undead chicken and a magnet.",
                    "How odd."
                )
                Quests.BETWEEN_A_ROCK -> JunaStory(
                    "...I had to be shot out of a cannon into a rock! But finally I banished the spirit of the rock and Dondakan could mine his gold.",
                )
                Quests.BIG_CHOMPY_BIRD_HUNTING -> JunaStory(
                    "...poor Rantz was so clumsy he couldn't shoot anything, but I managed to kill the Chompy bird."
                )
                Quests.BIOHAZARD -> JunaStory(
                    "...So it turned out there was no plague after all!",
                    "Deception of the people by their rulers is a terrible thing."
                )
                Quests.CABIN_FEVER -> JunaStory(
                    "...so I holed the enemy ship with another cannon shot, and the Adventurous sailed triumphantly into Mos Le'Harmless!"
                )
                Quests.CLOCK_TOWER -> JunaStory(
                    "...So it was I who repaired the clock tower."
                )
                Quests.CONTACT -> JunaStory(
                    "...And when I emerged from the temple I found the place hugely changed. The deserted food stalls and empty streets were replaced with a healthy city filled with life, colour and commerce.",
                    playerFace1 = ChatAnim.HAPPY
                )
                Quests.ZOGRE_FLESH_EATERS -> JunaStory(
                    "...Those Zogres were disgusting! But I learned how to fight them with brutal arrows and disease balm.",
                    "Zombie Ogres!? Some of the foulest creatures of Zamorak. In the great wars, the zogre armies would repel the forces of Saradomin with their stench alone."
                )
                Quests.CREATURE_OF_FENKENSTRAIN -> JunaStory(
                    "...But in the end I stopped Fenkenstrain from continuing his horrible experiments."
                )
                Quests.DARKNESS_OF_HALLOWVALE -> JunaStory(
                    "...I gave Safalaan the book on Haemalchemy, and he recommended me for a position in the Myreque.",
                    "I'm sure you will have many more adventures on behalf of the Myreque. Then, perhaps, you will have even more stories to tell me."
                )
                Quests.DEATH_TO_THE_DORGESHUUN -> JunaStory(
                    "...Sigmund escaped again, but Zanik and I destroyed the machine and Dorgesh-Kaan was saved!"
                )
                Quests.DEATH_PLATEAU -> JunaStory(
                    "...So from then on the Imperial Guard was able to access Death Plateau safely and take the trolls by surprise."
                )
                Quests.DESERT_TREASURE -> JunaStory(
                    "...In the pyramid I discovered a whole new set of magic spells!",
                    "I suggest you think about the origin of those spells. Ancient magicks are not to be toyed with."
                )
                Quests.DEVIOUS_MINDS -> JunaStory(
                    "...So it was me who accidentally brought a teleport beacon onto Entrana! But the Temple Knights are investigating so it should be all right."
                )
                Quests.THE_DIG_SITE -> JunaStory(
                    "...And the examiner was very impressed that I had discovered an ancient altar of Zaros.",
                    "Zaros? I had not heard that name for a thousand years, even before the start of my sojourn here."
                )
                Quests.DRUIDIC_RITUAL -> JunaStory(
                    "...so Kaqemeex taught me how to use the Herblore skill.",
                    "A generous reward indeed."
                )
                Quests.DWARF_CANNON -> JunaStory(
                    "...and that was how I fixed the dwarf multicannon.",
                    "So, war still rages in the world above? Will you never tire of creating machines of destruction?"
                )
                Quests.EADGARS_RUSE -> JunaStory(
                    "...Eadgar's ruse was absurd, but it worked! The trolls ate a fake human, and I got the herb for the druids' ritual."
                )
                Quests.EAGLES_PEAK -> JunaStory(
                    "...in the end I had to dress up as an eagle in order to get the zoo a new ferret!",
                    "You should not make light of your tampering with the balance of nature. Always treat the natural world with respect."
                )
                Quests.ELEMENTAL_WORKSHOP_I -> JunaStory(
                    "...And once I had repaired the Elemental Workshop I was able to make an Elemental Shield."
                )
                Quests.ELEMENTAL_WORKSHOP_II -> JunaStory(
                    "...So I instilled the power of my mind into the primed bar and smithed it into an Elemental Mind Helm!",
                    "You should be careful about meddling with your own head."
                )
                Quests.ENAKHRAS_LAMENT -> JunaStory(
                    "...And then Akthanakos and Enakhra went away to the north to continue fighting!",
                    "A battle between two such powerful beings will take much time and unleash much energy. I hope that the natural world is not damaged in this conflict!"
                )
                Quests.ENLIGHTENED_JOURNEY -> JunaStory(
                    "...we landed the balloon in Taverley and Auguste thanked me for helping him get his idea off the ground."
                )
                Quests.THE_EYES_OF_GLOUPHRIE -> JunaStory(
                    "...The machine revealed that the cute creatures were actually Arposandran spies! I destroyed them, and King Narnode gave me a crystal seed that belonged to Oaknock the Engineer."
                )
                Quests.FAIRYTALE_I_GROWING_PAINS -> JunaStory(
                    "...So I defeated the tanglefoot, and returned the Fairy Queen's Enchanted Secateurs to the Fairy Godfather.",
                    "These fairies seem to be well versed in the powers of nature."
                )
                Quests.FAIRYTALE_II_CURE_A_QUEEN -> JunaStory(
                    "...the Fairy Queen awoke, and we realized that the Godfather had betrayed her!",
                    "Politics are never straightforward."
                )
                Quests.FAMILY_CREST -> JunaStory(
                    "...So all three parts of the family crest were reunited."
                )
                Quests.THE_FEUD -> JunaStory(
                    "...So despite the failure of my original task, Ali Morrisane was very happy about his nephew's fortunes."
                )
                Quests.FIGHT_ARENA -> JunaStory(
                    "...And I defeated General Khazard, and won the Servils their freedom!"
                )
                Quests.FISHING_CONTEST -> JunaStory(
                    "...And after I had won the fishing contest, the Dwarves let me go under White Wolf Mountain.",
                    "Fishing? A strange test of worthiness to pass through an underground tunnel!"
                )
                Quests.FORGETTABLE_TALE -> JunaStory(
                    "...I think I did something with a drunken dwarf... but I can't remember what.",
                    "You must be careful not to let drink muddle your brain!"
                )
                Quests.THE_FREMENNIK_TRIALS -> JunaStory("!FREMENNIK TRIALS! SPECIAL HANDLING")
                Quests.WATERFALL_QUEST -> JunaStory(
                    "...And that was how I retrieved the Chalice of Eternity from the waterfall."
                )
                Quests.GARDEN_OF_TRANQUILITY -> JunaStory(
                    "...so the garden at Varrock palace is all my work!",
                    "It is good that you can take time from slaying and adventuring to grow a beautiful garden."
                )
                Quests.GERTRUDES_CAT -> JunaStory(
                    "...I returned Fluffs safely to Gertrude and she gave me a cat of my own!",
                    "Cats are one of the most mysterious creatures of Guthix. I hope you take your responsibility seriously."
                )
                Quests.GHOSTS_AHOY -> JunaStory(
                    "...so the people of Port Phasmatys were finally able to rest."
                )
                Quests.THE_GIANT_DWARF -> JunaStory(
                    "...So the statue in Keldagrim was rebuilt, and I witnessed a meeting of the Consortium of mining companies.",
                    "I have a feeling that Keldagrim will be the setting of many great adventures for you."
                )
                Quests.THE_GOLEM -> JunaStory(
                    "...And I had to reprogram the golem before it would believe that the demon was dead.",
                    "I remember well the battle of Uzer."
                )
                Quests.THE_GRAND_TREE -> JunaStory(
                    "...Glough fled like the coward he really is, and the Grand Tree was saved!"
                )
                Quests.THE_HAND_IN_THE_SAND -> JunaStory(
                    "...So that was how I gave Bert a hand solving the murder and returned the wizard's head to solve the riddle of the ever full sand pits!",
                    "A victim dies and a murderer is punished - that is balance."
                )
                Quests.HAUNTED_MINE -> JunaStory(
                    "...And from the legendary crystals I cut a salve shard to fight the undead."
                )
                Quests.HAZEEL_CULT -> JunaStory("!HAZEEL CULT! SPECIAL HANDLING")
                Quests.HEROES_QUEST -> JunaStory(
                    "...So after I had retrieved all the items, I became a member of the Heroes' Guild!"
                )
                Quests.HOLY_GRAIL -> JunaStory(
                    "...And out of all the Knights of the Round Table, it was I who found the Holy Grail."
                )
                Quests.HORROR_FROM_THE_DEEP -> JunaStory(
                    "...And that was the end of the horror from the deep!"
                )
                Quests.ICTHLARINS_LITTLE_HELPER -> JunaStory(
                    "...And with the amulet I received, I could understand the language of cats!",
                    "It's a rare privilege to understand the language of those beings. You should listen carefully to all they have to say - but do not believe too much of it."
                )
                Quests.IN_AID_OF_THE_MYREQUE -> JunaStory(
                    "...Veliaf was very impressed by the 'Rod of Ivandis', and I can use it to kill Juves and the Juvinates!"
                )
                Quests.IN_SEARCH_OF_THE_MYREQUE -> JunaStory(
                    "...So I suppose it was my fault that the young members of the Myreque were killed by Vanstrom."
                )
                Quests.JUNGLE_POTION -> JunaStory(
                    "...and once I had gathered all the herbs, Trufitus Shakaya was able to commune with his gods."
                )
                Quests.LEGENDS_QUEST -> JunaStory(
                    "...And when I had completed all the tasks, I became a member of the Legends' Guild!"
                )
                Quests.LOST_CITY -> JunaStory(
                    "...And when I entered the door carrying the Dramen Staff, I was transported to a whole new world - a world populated by magical fairies!"
                )
                Quests.THE_LOST_TRIBE -> JunaStory(
                    "...Sigmund was dismissed, and the duke and the ruler of the cave goblins signed a peace treaty.",
                    "The Dorgueshuun goblins have been good neighbours during my vigil here. They are a timid race, but not cowardly, and I am glad they have the peace they desire."
                )
                Quests.LUNAR_DIPLOMACY -> JunaStory(
                    "...So I defeated my mirror image, won the respect of the Moon Clan and persuaded them to communicate with the Fremenniks!",
                    "The Moon Clan's reliance on magic is no more balanced than the Fremennik's hatred of it."
                )
                Quests.MAKING_HISTORY -> JunaStory(
                    "...so the outpost became a museum.",
                    "History is the greatest story of all, and it is very good that you have helped to preserve it."
                )
                Quests.MERLINS_CRYSTAL -> JunaStory(
                    "...and when I told King Arthur that I had single-handedly freed Merlin from his crystal prison, he made me a knight of the Round Table!"
                )
                Quests.MONKEY_MADNESS -> JunaStory(
                    "...So I defeated the Jungle Demon and stopped the Monkeys' plot to take over Karamja.",
                    "I see they couldn't make a monkey out of you!",
                    "Well, actually, I was a monkey for a bit."
                )
                Quests.MONKS_FRIEND -> JunaStory(
                    "...so the monks had a drunken party to celebrate the child's birthday.",
                    "Mirth should not be taken to excess. These Saradominists have never understood the importance of balance."
                )
                Quests.MOUNTAIN_DAUGHTER -> JunaStory(
                    "...I was too late to save the chieftain's daughter, but at least I gave her a proper burial.",
                    "Brief is life, but more precious if its passing is properly honoured."
                )
                Quests.MOURNINGS_END_PART_I -> JunaStory(
                    "...So after I had told the Head Mourner I had poisoned the city, he told me about the lost temple in the mountains that they're searching for."
                )
                Quests.MOURNINGS_END_PART_II -> JunaStory(
                    "...What kind of twisted mind would come up with the light-beam puzzle in that temple?"
                )
                Quests.MURDER_MYSTERY -> JunaStory(
                    "...And that's how I solved the murder of Lord Sinclair."
                )
                Quests.MY_ARMS_BIG_ADVENTURE -> JunaStory(
                    "...My Arm is still up on that rooftop growing goutweed in his little farming patch!",
                    "It is good that a troll wishes to study the complex skill that is Farming."
                )
                Quests.NATURE_SPIRIT -> JunaStory(
                    "...and that was how I helped Filliman Tarlock transform into a nature spirit.",
                    "It is good that you helped a servant of Guthix to his rest."
                )
                Quests.OBSERVATORY_QUEST -> JunaStory(
                    "And once I fixed the telescope, I saw the stars!",
                    "It is long since I have seen the stars."
                )
                Quests.ONE_SMALL_FAVOUR -> JunaStory(
                    "...I still can't believe how long that 'one small favour' took me!",
                )
                Quests.PLAGUE_CITY -> JunaStory(
                    "...And that was how I rescued Elena from West Ardougne."
                )
                Quests.PRIEST_IN_PERIL -> JunaStory(
                    "...but with Drezel's help I was able to purify the River Salve."
                )
                Quests.RAG_AND_BONE_MAN -> JunaStory(
                    "...So I fetched all the bones the Odd Old Man wanted, and he gave me a reward!",
                    "What could that man have wanted all those bones for? Collecting that many bones cannot be a balanced activity."
                )
                Quests.RATCATCHERS -> JunaStory(
                    "...Those rat-catchers are odd people. But by the end of it I had caught a huge number of rats all over @servername!",
                    "Rats are a highly unbalanced creature and it is right that their numbers be controlled."
                )
                Quests.RECIPE_FOR_DISASTER -> JunaStory(
                    "...So after I had freed all the dinner guests I was finally able to face the Culinaromancer and put an end to his mad schemes!",
                    "That evil wizard's time spells sent a ripple through the fabric of balance. I am glad he is no longer at large!"
                )
                Quests.RECRUITMENT_DRIVE -> JunaStory(
                    "...So I figured out their weird puzzles, and the Temple Knights asked me to join as an initiate member!",
                    "I have heard tales of these 'Temple Knights' from many years past. Be wary, their motivations are not what they might lead you to believe."
                )
                Quests.REGICIDE -> JunaStory(
                    "...I returned to tell King Lathas that the assassination was successful, and he rewarded me.",
                )
                Quests.ROVING_ELVES -> JunaStory(
                    "...and when I planted the shard it grew into a crystal tree!"
                )
                Quests.ROYAL_TROUBLE -> JunaStory(
                    "...So I showed that the war was based on a misunderstanding, and now Miscellania and Etceteria were at peace again!",
                    "War? What is it good for?",
                    "Absolutely nothing!"
                )
                Quests.RUM_DEAL -> JunaStory(
                    "...So that was how I helped keep the zombie pirates drunk!",
                    "Drunken zombie pirates? What is the world coming to?"
                )
                Quests.SCORPION_CATCHER -> JunaStory(
                    "...I would never have thought that three scorpions would take that long to find!"
                )
                Quests.SEA_SLUG -> JunaStory(
                    "...And that was how I rescued Caroline's family from the sea slugs."
                )
                Quests.THE_SLUG_MENACE -> JunaStory(
                    "...When I told Sir Tiffy that I had accidentally freed the Mother Mallum, he promoted me to the rank of Proselyte within the Temple Knights.",
                    "That's a strange response to such an unfortunate event. But the motives of the temple knights have always been incomprehensively unbalanced."
                )
                Quests.SHADES_OF_MORTTON -> JunaStory(
                    "...So Mort'ton returned pretty much to normal, and I was able to put the shades at rest."
                )
                Quests.SHADOW_OF_THE_STORM -> JunaStory(
                    "...So Denath was Agrith-Naar all along! But I managed to summon the demon again and slay him.",
                    "Agrith-Naar was one of the demons my master banished at the end of the great war. It seems he found a way to work around the ban on direct intervention."
                )
                Quests.SHEEP_HERDER -> JunaStory(
                    "...so the sheep were safely killed and incinerated."
                )
                Quests.SHILO_VILLAGE -> JunaStory(
                    "...So what Rashiliyia wanted all along was to be reunited with her son in the afterlife."
                )
                Quests.A_SOULS_BANE -> JunaStory(
                    "...I trapped the spirit beast and the family could talk again, but they said I had only made the beast more powerful.",
                    "The most fearsome monsters are to be found in the darkness of our own minds."
                )
                Quests.SPIRITS_OF_THE_ELID -> JunaStory(
                    "...So it turned out that the people of Nardah were dying of thirst because the spirits of the Elid had cursed them for turning away from their goddess.",
                    "It is a terrible thing for people to turn away from their religion. I think the spirits taught the town a lesson."
                )
                Quests.SWAN_SONG -> JunaStory(
                    "...So the Wise Old Man said it was time for him to retire, and he left the Colony and went home. He gave me loads of xp, but he wouldn't let me have his hat!",
                    "A hat is just a hat, @name.",
                    "Grrr!"
                )
                Quests.TAI_BWO_WANNAI_TRIO -> JunaStory(
                    "...So that was how I helped the people of Tai Bwo Wannai village return."
                )
                Quests.A_TAIL_OF_TWO_CATS -> JunaStory(
                    "...So now Bob and Neite are very happy together!",
                    "Bob the cat? I have heard of that strange creature, but have never been able to fathom him. It is no small achievement for you to be able to."
                )
                Quests.TEARS_OF_GUTHIX -> JunaStory("") // Juna knows already
                Quests.TEMPLE_OF_IKOV -> JunaStory("!TEMPLE OF IKOV! SPECIAL HANDLING")
                Quests.THRONE_OF_MISCELLANIA -> JunaStory(
                    "...So that was how I became @g[King,Queen] of Miscellania!"
                )
                Quests.THE_TOURIST_TRAP -> JunaStory(
                    "...Ana wasn't too happy to be cooped up in that barrel! But at least I got her out of the mining camp."
                )
                Quests.WITCHS_HOUSE -> JunaStory(
                    "...All that trouble just to get a ball out of someone's garden!",
                    "It is often hard to know how long a task will take when we begin it.",
                    playerFace1 = ChatAnim.LAUGH
                )
                Quests.TREE_GNOME_VILLAGE -> JunaStory(
                    "...And King Bolren thanked me for defeating the warlord and returning the orbs to the gnome people."
                )
                Quests.TRIBAL_TOTEM -> JunaStory(
                    "...So I returned the totem, and the Rantuki tribe rewarded me for my help."
                )
                Quests.TROLL_ROMANCE -> JunaStory(
                    "...So Ug and Aga lived happily ever after."
                )
                Quests.TROLL_STRONGHOLD -> JunaStory(
                    "...And after I rescued Godric, Dunstan gave me two magic lamps as a reward!"
                )
                Quests.UNDERGROUND_PASS -> JunaStory(
                    "...I don't know who put all those traps in the underground pass!"
                )
                Quests.WANTED -> JunaStory(
                    "...And after I finally managed to track him down, let me tell you, that Solus Dellagar was almost as powerful as he was insane!",
                    "Yes, it seems he sacrificed his sanity for power. Power often comes with a cost, and the highest cost is often the one you do not immediately see."
                )
                Quests.WATCHTOWER -> JunaStory(
                    "...And with the shield generator working, the ogres could never threaten Yanille again."
                )
                Quests.COLD_WAR -> JunaStory(
                    "...It was a close call, but I managed to discover the penguins' plans and escape from their base.",
                    "Penguins? Those creatures are not to be underestimated."
                )
                Quests.THE_FREMENNIK_ISLES -> JunaStory(
                    "...I slew many trolls and brought the Troll King's head back to Neitiznot as a trophy!"
                )
                Quests.TOWER_OF_LIFE -> JunaStory(
                    "...So, it turned out that the alchemists had created a Homunculus - a mixture of logic and magic.",
                    "Ah, that is good. New forms of life should always be created according to a balance between different principles."
                )
                Quests.THE_GREAT_BRAIN_ROBBERY -> JunaStory(
                    "...But after the monks' brains had been restored, I had to fight an enormous abomination called Barrelchest!",
                    "Zombies and brain-swapping? It is good that you stopped this disrespect for nature."
                )
                Quests.WHAT_LIES_BELOW -> JunaStory(
                    "..but after I defeated King Roald, Rat was able to remove Surok's mind control spell from him.",
                    "I see the servants of Zamorak still play their part in balancing the world between good and evil.",
                    playerFace1 = ChatAnim.NEUTRAL
                )
                Quests.OLAFS_QUEST -> JunaStory(
                    "...It wasn't easy, but I managed to convince Olaf's family that he was a good navigator!"
                )
                Quests.ANOTHER_SLICE_OF_HAM -> JunaStory(
                    "...so I rescued Zanik from Sigmund and the railway was opened."
                )
                Quests.DREAM_MENTOR -> JunaStory(
                    "...So in the end I travelled into Cyrisus's Dreamland and defeated his fear of combat."
                )
                Quests.GRIM_TALES -> JunaStory(
                    "...So I found the golden goblin, cut down the beanstalk, and that was the end of that grim tale!"
                )
                Quests.KINGS_RANSOM -> JunaStory(
                    "...And after I had used the spell to free King Arthur, I disguised him as a guard so he could escape."
                )
                Quests.THE_PATH_OF_GLOUPHRIE -> JunaStory(
                    "...The room filled up with gas and I nearly died, but Hazelmere came to my rescue.",
                    "A truly exciting tail of failing and needing to be rescued."
                )
                Quests.BACK_TO_MY_ROOTS -> JunaStory(
                    "...So I defeated the wild vine and learned how to care for my own Jade Vine.",
                    "The care of nature is important, but wild plants are creatures of Guthix just as much as cultivated ones."
                )
                Quests.LAND_OF_THE_GOBLINS -> JunaStory(
                    "...Oldak and I escaped from Yu'biusk before the portal closed, but Zanik was trapped in the box."
                )
                Quests.DEALING_WITH_SCABARAS -> JunaStory(
                    "...And that was how I dealt with the High Priest of Scabaras.",
                    "The desert gods are strange, but you must be sure to pay them the proper respect."
                )
                Quests.WOLF_WHISTLE -> JunaStory(
                    "...And that was how I learned the secrets of summoning."
                )
                Quests.AS_A_FIRST_RESORT -> JunaStory(
                    "...So now the Oo'glog spa is open!"
                )
                Quests.CATAPULT_CONSTRUCTION -> JunaStory(
                    "...And that was how I repaired the catapult in the Tyras camp.",
                    "Catapults are terrible devices. They move rocks from their proper Guthix-assigned positions."
                )
                Quests.KENNITHS_CONCERNS -> JunaStory(
                    "...And my last step was to bring Kennith his toy train."
                )
                Quests.LEGACY_OF_SEERGAZE -> JunaStory(
                    "...I slew a Vyrewatch, as Safalaan had asked, and took the corpse to the Paterdomus Columbarium to cremate it. In gratitude the spirit left a key which allowed me to gain access to their worldly wealth. My first prize was a blood rune talisman!",
                    "With your mind so focused on material wealth, I worry whether the intrigues overshadowing your discoveries are being properly looked into. Drakan, I fear, is a whole world of trouble for which you are ill-prepared."
                )
                Quests.PERILS_OF_ICE_MOUNTAIN -> JunaStory(
                    "...So now Nurmof's pickaxe machine is powered by a windmill.",
                    "You acted as a true servant of Guthix in this matter."
                )
                Quests.TOKTZ_KET_DILL -> JunaStory(
                    "...And that's how I defeated the TokTz-Ket-Dill and helped to write a Tzhaar play.",
                    "I wish I could have seen your play, @name. I am always glad to hear your stories."
                )
                Quests.SMOKING_KILLS -> JunaStory(
                    "...Who would have thought there were so many monsters down the well in Pollnivneach?",
                    "You will find that monsters can hide in the most surprising of places. Indeed, those with friendly faces can be the closest things to a monster. Be most careful in your desert adventures."
                )
                Quests.ROCKING_OUT -> JunaStory(
                    "...And that was how I heard the tale of the death of Rabid Jack the pirate.",
                    "The lives of pirates often make for good tales."
                )
                Quests.SPIRIT_OF_SUMMER -> JunaStory(
                    "...I trapped the Spirit Beast and the family could talk again, but they said I had only made it more powerful.",
                    "I have heard of that Spirit Beast. You did very well to even survive your encounter with it."
                )
                Quests.MEETING_HISTORY -> JunaStory(
                    "...And that was how I travelled into the past and met the first family to arrive in @servername.",
                    "You have been truly blessed by Guthix. I can only dream of such an honour."
                )
                Quests.ALL_FIRED_UP -> JunaStory(
                    "...And that was how I learned to maintain King Roald's watchtower beacons."
                )
                Quests.SUMMERS_END -> JunaStory(
                    "...And after I defeated the Spirit Beast, Summer and her family were finally able to die.",
                    "The balance of life and death have been restored. It is good."
                )
                Quests.DEFENDER_OF_VARROCK -> JunaStory(
                    "...The Shield of Arrav glowed with lightning, and Dimintheis used it to defeat the zombie army."
                )
                Quests.SWEPT_AWAY -> JunaStory(
                    "...And that was how I helped the witches prepare their goulash."
                )
                Quests.WHILE_GUTHIX_SLEEPS -> JunaStory(
                    "...But Lucien took the Stone of Jas and teleported away!",
                    "You should know better than to interfere. Without your involvement, the stone would still rest where my master had put it. But good may yet come of this. We shall see."
                )
                Quests.IN_PYRE_NEED -> JunaStory(
                    "...And that was how I helped the phoenix to be reborn!",
                    "You have performed a service to one of the creatures of Guthix, and for that I thank you."
                )
                Quests.TEST_QUEST -> JunaStory(
                    "Blah blah blah something about recent quest I did that I'm forced to talk about, but looks like you want me to shut up.",
                    "Blah blah blah that's so cool, I'm totally interested in what you are saying and am totally not falling asleep at this point."
                )
            }

        private fun getStory(player: Player) {
            val recentStory = getAttribute(player, ATTRIBUTE_JUNA_DIALOGUE, Quests.TEST_QUEST.junaStory)
            val eligibleStories = Quests.values().filter { isQuestComplete(player, it) }
                .mapTo(mutableListOf<JunaStory>()) { it.junaStory }
            if (BarcrawlManager.getInstance(player).isFinished) {
                eligibleStories.add(barCrawlMiniQuest)
            }
            if (getVarp(player, ZamorakMageDialogue.INTO_THE_ABYSS_VARP) == ZamorakMageDialogue.INTO_THE_ABYSS_COMPLETE_STAGE) {
                eligibleStories.add(abyssMiniQuest)
            }
            if (eligibleStories.remove(Quests.THE_FREMENNIK_TRIALS.junaStory)) {
                eligibleStories.add(getFremTrials(player))
            }
            if (eligibleStories.remove(Quests.HAZEEL_CULT.junaStory)) {
                when {
                    HazeelCult.mahjarratArc(player) -> eligibleStories.add(hazeelAgainstCeril)
                    HazeelCult.carnilleanArc(player) -> eligibleStories.add(hazeelForCeril)
                }
            }
            if (eligibleStories.remove(Quests.TEMPLE_OF_IKOV.junaStory)) {
                val ikovSide = getAttribute(player, TempleOfIkov.attributeChosenEnding, 0)
                when (ikovSide) {
                    1 -> eligibleStories.add(ikovAgainstLucien)
                    2 -> eligibleStories.add(ikovForLucien)
                }
            }
            eligibleStories.removeAll { it.playerLine1.isEmpty() }
            eligibleStories.remove(recentStory)
            if (eligibleStories.isEmpty()) {
                eligibleStories.add(Quests.TEST_QUEST.junaStory)
            }
            setAttribute(player, ATTRIBUTE_JUNA_DIALOGUE, eligibleStories.random())
        }

    }

}