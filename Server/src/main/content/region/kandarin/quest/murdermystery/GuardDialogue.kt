package content.region.kandarin.quest.murdermystery

import content.data.Quests
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributePoisonClue
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.attributeRandomMurderer
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.clueCount
import content.region.kandarin.quest.murdermystery.MurderMystery.Companion.solvedMystery
import core.ServerConstants.Companion.SERVER_NAME
import core.api.*
import core.game.dialogue.*
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class GuardDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, GuardDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return GuardDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_812, NPCs.GUARD_6191)
    }
}

class GuardDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val KILLER = when (getAttribute(player!!, attributeRandomMurderer, 0)) {
            0 -> "Anna"
            1 -> "Bob"
            2 -> "Carol"
            3 -> "David"
            4 -> "Elizabeth"
            5 -> "Frank"
            else -> "Anna"
        }
        val POISONSPOT = when (getAttribute(player!!, attributeRandomMurderer, 0)) {
            0 -> "compost heap"
            1 -> "beehive"
            2 -> "drain"
            3 -> "spiders nest"
            4 -> "fountain"
            5 -> "Sinclair Crest"
            else -> "compost heap"
        }
        val KILLERMALE = when (getAttribute(player!!, attributeRandomMurderer, 0)) {
            1, 3, 5 -> true
            else -> false
        }
        val EVIDENCE = (1796..1822).toList().toIntArray() //All Murder Mystery related items are between these item ids

        when (getQuestStage(player!!, Quests.MURDER_MYSTERY)){
            0 -> when (stage) {
                0 -> playerl(FacialExpression.NEUTRAL, "What's going on here?").also { stage++ }
                1 -> npcl(FacialExpression.SAD, "Oh, it's terrible! Lord Sinclair has been murdered and we don't have any clues as to who or why. We're totally baffled!").also { stage++ }
                2 -> npcl(FacialExpression.SAD, "If you can help us we will be very grateful.").also { stage++ }
                3 -> options("Sure, I'll help.", "You should do your own dirty work.").also { stage++ }
                4 -> when (buttonID) {
                    1 -> playerl(FacialExpression.HAPPY, "Sure, I'll help.").also { stage++ }
                    2 -> playerl("You should do your own dirty work.").also { stage = 8 }
                }
                5 -> npcl(FacialExpression.HAPPY, "Thanks a lot!").also { stage++ }
                6 -> playerl(FacialExpression.NEUTRAL, "What should I be doing to help?").also { stage++ }
                7 -> npcl(FacialExpression.NEUTRAL, "Look around and investigate who might be responsible. The Sarge said every murder leaves clues to who done it, but frankly we're out of our depth here.")
                    .also { setAttribute(player!!, attributeRandomMurderer, RandomFunction.random(5))}
                    .also { setQuestStage(player!!, Quests.MURDER_MYSTERY, 1) }
                    .also { stage = END_DIALOGUE }

                8 -> npcl("Get lost then, this is private property! ...Unless you'd like to be taken in for questioning yourself?").also { stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                0 -> options("What should I be doing to help again?", "How did Lord Sinclair die?", "I know who did it!").also { stage++ }
                1 -> when(buttonID) {
                    1 -> playerl("What should I be doing to help?").also { stage = 2 }
                    2 -> playerl("How did Lord Sinclair die?").also { stage = 3 }
                    3 -> playerl(FacialExpression.HAPPY, "I know who did it!")
                        .also {
                            stage = if (solvedMystery(player!!)) 46
                            else if (clueCount(player!!) == 1) {
                                if (inInventory(player!!, Items.KILLERS_PRINT_1815)) {
                                    41
                                }
                                else if (getAttribute(player!!, attributePoisonClue,0) == 2) {
                                    37
                                }
                                else 33
                            }
                            else if (clueCount(player!!) > 1) 32
                            else 6
                        }
                }
                2 -> npcl("Look around and investigate who might be responsible. The Sarge said every murder leaves clues to who done it, but frankly we're out of our depth here.").also { stage = END_DIALOGUE }

                3 -> npcl("Well, it's all very mysterious. Mary, the maid, found the body in the study next to his bedroom on the east wing of the ground floor.").also { stage++ }
                4 -> npcl("The door was found locked from the inside, and he seemed to have been stabbed, but there was an odd smell in the room. Frankly, I'm stumped.").also { stage = END_DIALOGUE }

                5 -> npcl("Really? That was quick work! Who?").also { stage++ }
                6 -> options("It was an intruder!", "The butler did it!", "It was one of the servants!", "It was one of his family!").also { stage++ }
                7 -> when (buttonID) {
                    1 -> playerl("It was an intruder!").also { stage = 10 }
                    2 -> playerl("The butler did it!").also { stage = 8 }
                    3 -> playerl("It was one of the servants!").also { stage = 13 }
                    4 -> playerl("It was one of his family!").also { stage = 20 }
                }
                8 -> npcl("I hope you have proof to that effect. We have to arrest someone for this and it seems to me that only the actual murderer would gain by falsely accusing someone.").also { stage++ }
                9 -> npcl("Although having said that the butler is kind of shifty looking...").also { stage = END_DIALOGUE }

                10 -> npcl("That's what we were thinking too. That someone broke in to steal something, was discovered by Lord Sinclair, stabbed him and ran.").also { stage++ }
                11 -> npcl("It's odd that apparently nothing was stolen though... Find out something has been stolen,").also { stage++ }
                12 -> npcl("and the case is closed, but the murdered man was a friend of the King and it's more than my job's worth not to investigate fully.").also { stage = END_DIALOGUE }

                13 -> npcl("Oh really? Which one?").also { stage++ }
                14 -> options("It was one of the women.", "It was one of the men.").also { stage++ }
                15 -> when (buttonID) {
                    1 -> playerl("It was one of the women.").also { stage = 16 }
                    2 -> playerl("It was one of the men.").also { stage = 18 }
                }
                16 -> options("It was so obviously Louisa The Cook.", "It must have been Mary The Maid.").also { stage++ }
                17 -> when (buttonID) {
                    1 -> playerl("It was so obviously Louisa The Cook.").also { stage = 27 }
                    2 -> playerl("It must have been Mary The Maid.").also { stage = 27 }
                }

                18 -> options("It can only be Donovan the Handyman.", "Pierre the Dog Handler. No question", "Hobbes the Butler. The butler *always* did it.", "You must know it was Stanford The Gardener.").also { stage++ }
                19-> when (buttonID) {
                        1 -> playerl("It can only be Donovan the Handyman.").also { stage = 27 }
                        2 -> playerl("Pierre the Dog Handler. No question.").also { stage = 27 }
                        3 -> playerl("Hobbes the Butler. The butler *always* did it.").also { stage = 8 }
                        4 -> playerl("You must know it was Stanford The Gardener.").also { stage = 27 }
                }

                20 -> npcl("Oh really? Which one?").also { stage++ }
                21 -> options("It was one of the women.", "It was one of the men.").also { stage++ }
                22 -> when (buttonID) {
                    1 -> playerl("It was one of the women.").also { stage = 23 }
                    2 -> playerl("It was one of the men.").also { stage = 25 }
                }
                23 -> options("I know it was Anna.", "I am so sure it was Carol.", "I'll bet you anything it was Elizabeth.").also { stage++ }
                24 -> when (buttonID) {
                    1 -> playerl("I know it was Anna.").also { stage = 27 }
                    2 -> playerl("I am so sure it was Carol.").also { stage = 27 }
                    3 -> playerl("I'll bet you anything it was Elizabeth.").also { stage = 27 }
                }
                25 -> options("I'm certain it was Bob.", "It was David. No doubt about it.", "If it wasn't Frank I'll eat my shoes.").also { stage++ }
                26 -> when (buttonID) {
                    1 -> playerl("I'm certain it was Bob.").also { stage = 27 }
                    2 -> playerl("It was David. No doubt about it.").also { stage = 27 }
                    3 -> playerl("If it wasn't Frank I'll eat my shoes.").also { stage = 27 }
                }
                27 -> sendDialogue(player!!, "You tell the guard who you suspect of the crime.").also { stage++ }
                28 -> npcl("Great work, show me the evidence and we'll take them to the dungeons.").also { stage++ }
                29 -> npcl("You *DO* have evidence of their crime, right?").also { stage++ }
                30 -> playerl("Uh...").also { stage++ }
                31 -> npcl("Tch. You wouldn't last a day in the guards with sloppy thinking like that. Come see me when you have some proof of your accusations.").also { stage = END_DIALOGUE }

                32 -> showTopics(
                    IfTopic("I have proof that it wasn't any of the servants.", 33, inInventory(player!!, Items.CRIMINALS_THREAD_1808) || inInventory(player!!, Items.CRIMINALS_THREAD_1809) || inInventory(player!!, Items.CRIMINALS_THREAD_1810), true),
                    IfTopic("I have proof one of the family lied about the poison.", 37, getAttribute(player!!, attributePoisonClue, 0) == 2, true),
                    IfTopic("I have the finger prints of the culprit.", 41, inInventory(player!!, Items.KILLERS_PRINT_1815), true)
                )
                33 -> playerl("I have proof that it wasn't any of the servants!").also { stage++ }
                34 -> sendDialogue(player!!, "You show the guard the thread you found on the window.").also { stage ++ }
                35 -> playerl("All the servants dress in black so it couldn't have been one of them.").also { stage++ }
                36 -> npcl("That's some good work there. I guess it wasn't a servant. You still haven't proved who did do it though.").also { stage = END_DIALOGUE }

                37 -> playerl("I have proof that $KILLER is lying about the poison.").also { stage++ }
                38 -> npcl("Oh really? How did you get that?").also { stage++ }
                39 -> sendDialogue(player!!, "You tell the guard about the " +
                    when (getAttribute(player!!, attributeRandomMurderer, 0)) {
                        4 -> "mosquitos at the fountain."
                        5 -> "tarnished family crest."
                        else -> "$POISONSPOT."
                    }).also { stage++ }
                40 -> npcl("Hmm. That's some good detective work there, we need more evidence before we can close the case though. Keep up the good work.").also { stage = END_DIALOGUE }

                41 -> playerl("I have the fingerprints of the culprit!").also { stage++ }
                42 -> playerl("I have $KILLER's fingerprints here, you can see for yourself they match the fingerprints on the murder weapon exactly.").also { stage++ }
                43 -> sendDialogue(player!!,"You show the guard the finger prints evidence.").also { stage++ }
                44 -> npcl("... I'm impressed. How on earth did you think of something like that? I've never heard of such a technique for finding criminals before!").also { stage++ }
                45 -> npcl("This will come in very handy in the future but we can't arrest someone on just this. I'm afraid you'll still need to find more evidence before we can close this case completely.").also { stage = END_DIALOGUE }

                46 -> playerl(FacialExpression.HAPPY, "I have conclusive proof who the killer was.").also { stage++ }
                47 -> npcl(FacialExpression.HAPPY, "You do? That's excellent work. Let's hear it then.").also { stage++ }
                48 -> playerl("I don't think it was an intruder, and I don't think Lord Sinclair was killed by being stabbed.").also { stage++ }
                49 -> npcl("Hmmm? Really? Why not?").also { stage++ }
                50 -> playerl(FacialExpression.HAPPY, "Nobody heard the guard dog barking, which it would have if it had been an intruder who was responsible.").also { stage++ }
                51 -> playerl("Nobody heard any signs of a struggle either. I think the knife was there to throw suspicion away from the real culprit.").also { stage++ }
                52 -> npcl("Yes, that makes sense. But who did do it then?").also { stage++ }
                53 -> sendDialogue(player!!, "You prove to the guard the thread matches $KILLER's clothes.").also { stage++ }
                54 -> npcl("Yes, I'd have to agree with that... but we need more evidence!").also { stage++ }
                55 -> sendDialogue(player!!, "You prove to the guard $KILLER did not use the poison on the $POISONSPOT.").also { stage++ }
                56 -> npcl("Excellent work - have you considered a career as a detective? But I'm afraid it's still not quite enough...").also { stage++ }
                57 -> sendDialogue(player!!, "You match $KILLER's finger prints with those on the dagger found in the body of Lord Sinclair.").also { stage++ }
                58 -> npcl(FacialExpression.HAPPY, "Yes. There's no doubt about it. It must have been $KILLER who killed " + if (KILLERMALE) {"his"} else {"her"} + " father. All of the guards must congratulate you on your excellent work in helping us to solve this case.").also { stage++ }
                59 -> npcl("We don't have many murders here in $SERVER_NAME and I'm afraid we wouldn't have been able to solve it by ourselves. We will hold " + if (KILLERMALE) {"him"} else {"her"} + " here under house arrest until such time as we bring " + if (KILLERMALE) {"him"} else {"her"} + " to trial.").also { stage++ }
                60 -> npcl("You have our gratitude, and I'm sure the rest of the family's as well, in helping to apprehend the murderer. I'll just take the evidence from you now.").also { stage++ }
                61 -> sendDialogue(player!!, "You hand over all the evidence.").also { stage++ }
                62 -> npcl(FacialExpression.HAPPY, "Please accept this reward from the family!").also { stage++ }
                63 -> finishQuest(player!!, Quests.MURDER_MYSTERY)
                    .also {
                        player!!.inventory.removeAll(EVIDENCE)
                        player!!.bank.removeAll(EVIDENCE)
                        player!!.equipment.removeAll(EVIDENCE)
                    }
                    .also { stage = END_DIALOGUE }
            }
            100 -> when (stage) {
                0 -> npcl("Excellent work on solving the murder! All of the guards I know are very impressed, and don't worry, we have the murderer under guard until they can be taken to trial.").also { stage++ }
                1 -> playerl("Is there anything else I can do? Seems awfully quiet up at the house, considering their sibling has just been arrested.").also { stage++ }
                2 -> npcl("Nothing right now, we have it all under control.").also { stage = END_DIALOGUE }
            }
        }
    }
}