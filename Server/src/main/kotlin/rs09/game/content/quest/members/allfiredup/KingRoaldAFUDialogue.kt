package rs09.game.content.quest.members.allfiredup

import core.game.node.entity.skill.Skills
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE
import rs09.tools.START_DIALOGUE


class KingRoaldAFUDialogue(val questStage: Int) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {

        if(questStage == 0){
            when(stage){
                0 -> npc("Ah, it's you again. Hello there.").also { stage++ }

                1 -> player(
                    "Hello, Your Majesty. I am happy to report that the",
                    "situation at the Temple of Paterdomus has been sorted.",
                    "Misthalin's borders should once again be fully protected",
                    "against the threats of Morytania."
                ).also { stage++ }

                2 -> npc("Ah, thank you! The kingdom is forever in your", "debt.").also{stage++}
                3 -> player("In your debt? Does that mean you're going to give me", "fabulous rewards for my efforts?").also { stage++ }
                4 -> npc(
                    "Of course not; however, if it's rewards you're after, it",
                    "occurs to me that you could be of even more service to",
                    "the kingdom...and this time, there's payment in it for",
                    "you."
                ).also { stage++ }

                5 -> options("Oh, tell me more!", "Sorry, not interested.").also { stage++ }
                6 -> when(buttonID){
                    1 -> player("Oh, tell me more!").also { stage++ }
                    2 -> player("Sorry, not interested.").also { stage = END_DIALOGUE }
                }

                7 -> npc(
                    "Well, you see, because of the mounting threats from",
                    "Morytania and the Wilderness, the southern kingdoms",
                    "have banded together to take action."
                ).also { stage++ }

                8 -> npc(
                    "We have constructed a network of beacons that stretch",
                    "all the way from the source of the River Salve to the",
                    "northwestern-most edge of the Wilderness."
                ).also { stage++ }

                9 -> npc(
                    "Should there be any threat from these uncivilized lands,",
                    "we'll be able to spread the word as fast as the light of",
                    "the flames can travel."
                ).also { stage++ }

                10 -> player("So how could I be of help?").also { stage++ }
                11 -> npc(
                    "The task itself should be rather straightforward: I need",
                    "you to help us test the network of beacons to make",
                    "sure everything is in order, in case the worst",
                    "should occur."
                ).also { stage++ }

                12 -> options("I'd be happy to help.", "I'm a bit busy right now.").also { stage++ }
                13 -> when(buttonID){
                    1 -> player("I'd be happy to help.").also { stage++ }
                    2 -> player("I'm a bit busy right now.").also { stage = END_DIALOGUE }
                }

                14 -> if (player!!.skills.getLevel(Skills.FIREMAKING) < 43) {
                    npc("I'd love for you to help, but you need", "to get better at lighting fires first.")
                    stage = END_DIALOGUE
                } else {
                    npc("Excellent! The kingdom of Misthalin is eternally", "grateful.")
                    stage++
                }

                15 -> player("So what do I need to do?").also { stage++ }
                16 -> npc(
                    "Talk to the head fire tender, Blaze Sharpeye - he'll",
                    "explain everything. He is stationed just south of the",
                    "Temple of Paterdomus, on the cliffs by the River Salve."
                ).also { stage++ }

                17 -> {
                    player("Thank you, Your Majesty. I'll seek out Blaze", "right away.")
                    stage = END_DIALOGUE
                    player!!.questRepository.getQuest("All Fired Up").start(player)
                }

                END_DIALOGUE -> end()
            }
        }

        else if(questStage == 90){
            when(stage){
                START_DIALOGUE -> player("I'm happy to report that the beacon network seems to", "be working as expected.").also { stage++ }
                1 -> npc("Excellent! I'm delighted to hear it.").also { stage++ }
                2 -> player("So, about that reward you promised?").also { stage++ }
                3 -> npc("What happened to the days when adventurers felt", "rewarded in full by the knowledge of a job well done?").also { stage++ }
                4 -> player("Well before my time, I'm afraid.").also { stage++ }
                5 -> npc("Hmph. Well, I suppose a king must stick to his word.", "Mind you, let me stress how grateful we are - and how", "grateful we'd be if you could continue helping us test", "the beacons.").also { stage++ }
                6 -> npc("There is much more to be done and this is but a", "pittance compared to what I'm willing to offer for", "further assistance!").also { stage++ }
                7 -> {
                    end()
                    player!!.questRepository.getQuest("All Fired Up").finish(player)
                }
            }
        }

        else {
            abandonFile()
        }

    }
}