package content.region.misthalin.lumbridge.quest.tearsofguthix

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FacialExpression
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

/**
 * Anim 2056 - Juna puts head into itself
 * Anim 2055 - Juna lifts tail
 */
class JunaDialogue : InteractionListener {
    override fun defineListeners() {
        // Juna is a scenery.
        on(Scenery.JUNA_31302, SCENERY, "talk-to") { player, _ ->
            openDialogue(player, JunaDialogueFile(), NPC(NPCs.JUNA_2023))
            return@on true
        }
        // This is quest varbit 451 controlled. When it is set to 2, JUNA changes in ID
        on(Scenery.JUNA_31303, SCENERY, "talk-to") { player, node ->
            openDialogue(player, JunaDialogueFile(), NPC(NPCs.JUNA_2023))
            return@on true
        }
    }
}

class JunaDialogueFile : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {

        b.onQuestStages(TearsOfGuthix.questName, 0)
                .branch { player -> if(TearsOfGuthix.hasRequirements(player)) { 1 } else { 0 } }
                .let { branch ->
                    branch.onValue(0)
                            // Inauthentic, but absolutely no source on this...
                            .npcl(FacialExpression.OLD_NORMAL, "You are not strong enough of an adventurer to partake this quest. Come back when you are stronger.")
                            .linel("You do not meet the quest requirements for Tears of Guthix.")
                            .end()
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1)
                .npcl(FacialExpression.OLD_NORMAL, "Tell me... a story...")
                .playerl(FacialExpression.THINKING, "A story?")
                .npcl(FacialExpression.OLD_NORMAL, "I have been waiting here three thousand years, guarding the Tears of Guthix. I serve my master faithfully, but I am bored.")
                .npcl(FacialExpression.OLD_NORMAL, "An adventurer such as yourself must have many tales to tell. If you can entertain me, I will let you into the cave for a time.")
                .npcl(FacialExpression.OLD_NORMAL, "The more I enjoy your story, the more time I will give you in the cave.")
                .npcl(FacialExpression.OLD_NORMAL, "Then you can drink of the power of balance, which will make you stronger in whatever area you are weakest.")

                .let { builder ->
                    val returnJoin = b.placeholder()
                    returnJoin.builder()
                            .options()
                            .let { optionBuilder ->

                                optionBuilder.option_playerl("Okay...")
                                        .linel("You tell Juna some stories of your adventures.")
                                        // Yes I know.
                                        .playerl("Blah blah blah something about recent quest I did that I'm forced to talk about, but looks like you want me to shut up.")
                                        .npcl(FacialExpression.OLD_NORMAL,"Blah blah blah that's so cool, I'm totally interested in what you are saying and am totally not falling asleep at this point.")
                                        // ^ I'm not going to type out 120 fucking quest dialogue for shit you wouldn't care reading.
                                        .npcl(FacialExpression.OLD_NORMAL,"Your stories have entertained me. I will let you into the cave for a short time.")
                                        .npcl(FacialExpression.OLD_NORMAL,"But first you will need to make a bowl in which to collect the tears.")
                                        // The camera pans south to the part of the cave containing guthix-infused rocks.
                                        .npcl(FacialExpression.OLD_NORMAL,"There is a cave on the south side of the chasm that is similarly infused with the power of Guthix. The stone in that cave is the only substance that can catch the Tears of Guthix.")
                                        .npcl(FacialExpression.OLD_NORMAL,"Mine some stone from that cave, make it into a bowl, and bring it to me, and then I will let you catch the Tears.")
                                        .endWith { _, player ->
                                            if(getQuestStage(player, TearsOfGuthix.questName) == 0) {
                                                setQuestStage(player, TearsOfGuthix.questName, 1)
                                            }
                                        }

                                optionBuilder.option_playerl("Not now.")
                                        .end()

                                optionBuilder.option_playerl("What are the Tears of Guthix?")
                                        .npcl(FacialExpression.OLD_NORMAL, "The Third Age of the world was a time of great conflict, of destruction never seen before or since, when all the gods save Guthix warred for control.")
                                        .npcl(FacialExpression.OLD_NORMAL, "The colossal Wyrms, of whom today's dragons are a pale reflection, turned all the sky to fire, while on the ground armies of foot soldiers, goblins and trolls and humans, filled the valleys and plains with blood.")
                                        .npcl(FacialExpression.OLD_NORMAL, "In time the noise of the conflict woke Guthix from His deep slumber, and He rose and stood in the centre of the battlefield so that the splendour of His wrath filled the world, and He called for the conflict to cease!")
                                        .npcl(FacialExpression.OLD_NORMAL, "Silence fell, for the gods knew that none could challenge the power of the mighty Guthix -- for His power is that of nature itself, to which all other things are subject, in the end.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Guthix reclaimed that which had been stolen from Him, and went back underground to return to His sleep and continue to draw the world's power into Himself.")
                                        .npcl(FacialExpression.OLD_NORMAL, "But on His way into the depths of the earth He sat and rested in this cave; and, thinking of the battle-scarred desert that now stretched from one side of His world to the other, He wept.")
                                        .npcl(FacialExpression.OLD_NORMAL, "And so great was His sorrow, and so great was His life- giving power, that the rocks themselves began to weep with Him.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Later, Guthix noticed that the rocks continued to weep, and that their tears were infused with a small part of His power.")
                                        .npcl(FacialExpression.OLD_NORMAL, "So He set me, His servant, to guard the cave, and He entrusted to me the task of judging who was and was not worthy to access the tears.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Tell me... a story...")
                                        .goto(returnJoin)
                            }
                    return@let builder.goto(returnJoin)
                }


        b.onQuestStages(TearsOfGuthix.questName, 1)
                .npc(FacialExpression.OLD_NORMAL, "Before you can collect the Tears of Guthix you must", "make a bowl out of the stone in the cave on the south", "of the chasm.")
                .branch { player -> if(inInventory(player, Items.STONE_BOWL_4704)) { 1 } else { 0 } }
                .let{ branch ->
                    branch.onValue(0)
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("But I don't know how to reach the cave!")
                                        .npcl(FacialExpression.OLD_NORMAL, "I will tell you the story of the light-creatures.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Myriad and beautiful were the creatures and civilizations of the early ages of the world. Gielinor was a work of art, shaped lovingly over the millennia by the creative mind of Guthix.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Only the sturdiest races survived the Godwars, and even then only by abandoning their high culture and gearing their societies towards war. Of the more delicate races there is now no trace, and almost no memory.")
                                        .npcl(FacialExpression.OLD_NORMAL, "One such race had bodies as fragile as snowflakes, yet they built crystal cities that stood for a thousand years.")
                                        .npcl(FacialExpression.OLD_NORMAL, "The wind would whisper through the spires and fill them with sweet harmonies, and the rising sun would shine through the precious gems that studded the towers and create inter plays of light as if rainbows were dancing.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Indeed, so marvellous was this light-show at its height that the patterns of light themselves became alive, and great flocks of luminous creatures rode along the gem- cast beams, each drawn to its own colour.")
                                        .npcl(FacialExpression.OLD_NORMAL, "The creatures you see floating in this chasm are the last sorry remnants of that age. I do not know how they made their way here and survived to this time, but I am grateful for their company.")
                                        .end()

                                optionBuilder.option_playerl("What are the Tears of Guthix?")
                                        .npcl(FacialExpression.OLD_NORMAL, "The Third Age of the world was a time of great conflict, of destruction never seen before or since, when all the gods save Guthix warred for control.")
                                        .npcl(FacialExpression.OLD_NORMAL, "The colossal Wyrms, of whom today's dragons are a pale reflection, turned all the sky to fire, while on the ground armies of foot soldiers, goblins and trolls and humans, filled the valleys and plains with blood.")
                                        .npcl(FacialExpression.OLD_NORMAL, "In time the noise of the conflict woke Guthix from His deep slumber, and He rose and stood in the centre of the battlefield so that the splendour of His wrath filled the world, and He called for the conflict to cease!")
                                        .npcl(FacialExpression.OLD_NORMAL, "Silence fell, for the gods knew that none could challenge the power of the mighty Guthix -- for His power is that of nature itself, to which all other things are subject, in the end.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Guthix reclaimed that which had been stolen from Him, and went back underground to return to His sleep and continue to draw the world's power into Himself.")
                                        .npcl(FacialExpression.OLD_NORMAL, "But on His way into the depths of the earth He sat and rested in this cave; and, thinking of the battle-scarred desert that now stretched from one side of His world to the other, He wept.")
                                        .npcl(FacialExpression.OLD_NORMAL, "And so great was His sorrow, and so great was His life- giving power, that the rocks themselves began to weep with Him.")
                                        .npcl(FacialExpression.OLD_NORMAL, "Later, Guthix noticed that the rocks continued to weep, and that their tears were infused with a small part of His power.")
                                        .npcl(FacialExpression.OLD_NORMAL, "So He set me, His servant, to guard the cave, and He entrusted to me the task of judging who was and was not worthy to access the tears.")
                                        .end()

                                optionBuilder.option_playerl("Not now.")
                                        .end()
                            }
                    return@let branch // Return DialogueBranchBuilder instead of DialogueBuilder to forward the success branch.
                }.onValue(1)
                .playerl("I have a bowl.")
                .npcl(FacialExpression.OLD_NORMAL, "I will keep your bowl for you, so that you may collect the tears many times in the future.")
                .npcl(FacialExpression.OLD_NORMAL, "Now... tell me another story, and I will let you collect the tears for the first time.")
                .endWith { _, player ->
                    if (removeItem(player, Items.STONE_BOWL_4704)) {
                        finishQuest(player, TearsOfGuthix.questName)
                    }
                }

        b.onQuestStages(TearsOfGuthix.questName, 100)
                .npcl(FacialExpression.OLD_NORMAL, "Tell me... a story...")
                .let { builder ->
                    val returnJoin = b.placeholder()
                    returnJoin.builder()
                            .options()
                            .let { optionBuilder ->
                                optionBuilder.option_playerl("Okay...")
                                        .linel("You tell Juna some stories of your adventures.")
                                        // Yes I know.
                                        .playerl("Blah blah blah something about recent quest I did that I'm forced to talk about, but looks like you want me to shut up.")
                                        .npcl(FacialExpression.OLD_NORMAL,"Blah blah blah that's so cool, I'm totally interested in what you are saying and am totally not falling asleep at this point.")
                                        // ^ I'm not going to type out 120 fucking quest dialogue for shit you wouldn't care reading.
                                        .branch { player ->
                                            if(TearsOfGuthix.daysLeft(player) > 0 && TearsOfGuthix.xpLeft(player) > 0 && TearsOfGuthix.questPointsLeft(player) > 0) {
                                                3
                                            } else if(TearsOfGuthix.xpLeft(player) > 0 && TearsOfGuthix.questPointsLeft(player) > 0) {
                                                2
                                            } else if(TearsOfGuthix.daysLeft(player) > 0) {
                                                1
                                            } else {
                                                0 // Success branch
                                            }
                                        }
                                        .let{ branch ->
                                            // https://www.youtube.com/watch?v=X3M9CiS_BeU if not enough time has passed.
                                            branch.onValue(3)
                                                    .manualStage { df, player, _, _ -> npcl(FacialExpression.OLD_NORMAL,"Your stories have entertained me. But I will not permit any adventurer to access the tears more than once a week. Come back in " + TearsOfGuthix.daysLeft(player).toString() + " days.") }
                                                    .npcl(FacialExpression.OLD_NORMAL,"You should use that time to have more adventures! You may not re-enter the cave until you have more stories to tell.")
                                                    .manualStage { df, player, _, _ -> sendDialogue(player, "You cannot enter the cave again until you have gained either <col=8A0808>one quest point</col> or <col=8A0808>" + TearsOfGuthix.xpLeft(player) + " total XP</col>.") }
                                                    .end()

                                            branch.onValue(2)
                                                    .npc(FacialExpression.OLD_NORMAL,"Your story has entertained me. But it is a poor sort", "of adventurer who only tells stories of the past and", "does not find new stories to tell. I will not let you", "into the cave again until you have had more adventures.")
                                                    .manualStage { df, player, _, _ -> sendDialogue(player, "You cannot enter the cave again until you have gained either <col=8A0808>one quest point</col> or <col=8A0808>" + TearsOfGuthix.xpLeft(player) + " total XP</col>.") }
                                                    .end()

                                            branch.onValue(1)
                                                    .manualStage { df, player, _, _ -> npcl(FacialExpression.OLD_NORMAL,"Your stories have entertained me. But I will not permit any adventurer to access the tears more than once a week. Come back in " + TearsOfGuthix.daysLeft(player).toString() + " days.") }
                                                    .end()

                                            return@let branch
                                        }.onValue(0)
                                        .npcl(FacialExpression.OLD_NORMAL,"Your stories have entertained me. I will let you into the cave for a short time.")
                                        .branch { player -> if(TearsOfGuthix.isHandsFree(player)) { 1 } else { 0 } }
                                        .let{ branch ->
                                            // https://www.youtube.com/watch?v=J76OGo-hHlA your hands must be free.
                                            branch.onValue(0)
                                                    .npc(FacialExpression.OLD_NORMAL,"But you must have both hands free to carry the bowl.", "Speak to me again when your hands are free.")
                                                    .end()
                                            return@let branch
                                        }.onValue(1)
                                        // https://www.youtube.com/watch?v=Mj3pW-Brv9c
                                        .npc(FacialExpression.OLD_NORMAL,"Collect as much as you can from the blue streams. If", "you let in water from the green streams, it will take", "away from the blue. For Guthix is god of balance, and", "balance lies in the juxtaposition of opposites.")
                                        .endWith { _, player ->
                                            TearsOfGuthixMinigame.startGame(player)
                                        }

                                optionBuilder.option_playerl("Not now.")
                                        .end()
                            }
                    return@let builder.goto(returnJoin)
                }

    }
}