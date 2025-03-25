package content.region.kandarin.quest.tree

import content.data.Quests
import core.api.*
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import content.region.kandarin.quest.tree.TreeGnomeVillage.Companion.mazeEntrance
import core.game.dialogue.FacialExpression
import core.game.world.GameWorld
import core.tools.END_DIALOGUE

class KingBolrenDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val questStage = getQuestStage(player!!, Quests.TREE_GNOME_VILLAGE)
        when {
            questStage < 10 -> {
                when (stage) {
                    0 -> playerl("Hello.").also { stage++ }
                    1 -> npcl(FacialExpression.OLD_NORMAL, "Well hello stranger. My name's Bolren, I'm the king of the tree gnomes.").also { stage++ }
                    2 -> npcl(FacialExpression.OLD_NORMAL, "I'm surprised you made it in, maybe I made the maze too easy.").also { stage++ }
                    3 -> playerl("Maybe.").also { stage++ }
                    4 -> npcl(FacialExpression.OLD_NORMAL, "I'm afraid I have more serious concerns at the moment. Very serious.").also { stage++ }
                    5 -> options("I'll leave you to it then.", "Can I help at all?").also { stage++ }
                    6 -> when (buttonID) {
                        1 -> playerl("I'll leave you to it then.").also { stage = 7 }
                        2 -> playerl("Can I help at all?").also { stage = 8 }
                    }
                    7 -> npcl(FacialExpression.OLD_NORMAL, "Ok, take care.").also { stage = END_DIALOGUE }
                    8 -> npcl(FacialExpression.OLD_NORMAL, "I'm glad you asked.").also { stage++ }
                    9 -> npcl(FacialExpression.OLD_SAD, "The truth is my people are in grave danger. We have always been protected by the Spirit Tree. No creature of dark can harm us while its three orbs are in place.").also { stage++ }
                    10 -> npcl(FacialExpression.OLD_SAD, "We are not a violent race, but we fight when we must. Many gnomes have fallen battling the dark forces of Khazard to the North.").also { stage++ }
                    11 -> npcl(FacialExpression.OLD_SAD, "We became desperate, so we took one orb of protection to the battlefield. It was a foolish move.").also { stage++ }
                    12 -> npcl(FacialExpression.OLD_NORMAL, "Khazard troops seized the orb. Now we are completely defenceless.").also { stage++ }
                    13 -> playerl("How can I help?").also { stage++ }
                    14 -> npcl(FacialExpression.OLD_NORMAL,"You would be a huge benefit on the battlefield. If you would go there and try to retrieve the orb, my people and I will be forever grateful.").also { stage++ }
                    15 -> options("I would be glad to help.", "I'm sorry but I won't be involved.").also { stage++ }
                    16 -> when (buttonID) {
                        1 -> playerl("I would be glad to help.").also { stage = 18 }
                        2 -> playerl("I'm sorry but I won't be involved.").also { stage = 17 }
                    }
                    17 -> npcl(FacialExpression.OLD_NORMAL, "Ok then, travel safe.").also { stage = END_DIALOGUE }
                    18 -> npcl(FacialExpression.OLD_NORMAL, "Thank you. The battlefield is to the north of the maze. Commander Montai will inform you of their current situation.").also { stage++ }
                    19 -> npcl(FacialExpression.OLD_NORMAL, "That is if he's still alive.").also { stage++ }
                    20 -> npcl(FacialExpression.OLD_NORMAL, "My assistant shall guide you out. Good luck friend, try your best to return the orb.").also {
                        stage++
                    }
                    21 -> {
                        teleport(player!!, mazeEntrance)
                        sendNPCDialogue(player!!, NPCs.ELKOY_5179, "We're out of the maze now. Please hurry, we must have the orb if we are to survive.", FacialExpression.OLD_NORMAL)
                        setQuestStage(player!!, Quests.TREE_GNOME_VILLAGE, 10)
                        stage = END_DIALOGUE
                    }
                }
            }
            questStage < 31 -> {
                when (stage) {
                    0 -> playerl("Hello Bolren.").also { stage++ }
                    1 -> npcl(FacialExpression.OLD_NORMAL, "Hello traveller, we must retrieve the orb. It's being held by Khazard troops north of here.").also { stage++ }
                    2 -> playerl("Ok, I'll try my best.").also { stage = END_DIALOGUE }
                }
            }
            questStage == 31 -> {
                if(inInventory(player!!,Items.ORB_OF_PROTECTION_587)){
                    when(stage) {
                        0 -> playerl("I have the orb.").also { stage++ }
                        1 -> npcl(FacialExpression.OLD_DISTRESSED, "Oh my... The misery, the horror!").also { stage++ }
                        2 -> playerl("King Bolren, are you OK?").also { stage++ }
                        3 -> npcl(FacialExpression.OLD_DISTRESSED, "Thank you traveller, but it's too late. We're all doomed.").also { stage++ }
                        4 -> playerl("What happened?").also { stage++ }
                        5 -> npcl(FacialExpression.OLD_DISTRESSED, "They came in the night. I don't know how many, but enough.").also { stage++ }
                        6 -> playerl("Who?").also { stage++ }
                        7 -> npcl(FacialExpression.OLD_DISTRESSED, "Khazard troops. They slaughtered anyone who got in their way. Women, children, my wife.").also { stage++ }
                        8 -> playerl("I'm sorry.").also { stage++ }
                        9 -> npcl(FacialExpression.OLD_BOWS_HEAD_SAD, "They took the other orbs, now we are defenceless.").also { stage++ }
                        10 -> playerl("Where did they take them?").also { stage++ }
                        11 -> npcl(FacialExpression.OLD_NORMAL, "They headed north of the stronghold. A warlord carries the orbs.").also { stage++ }
                        12 -> options("I will find the warlord and bring back the orbs.", "I'm sorry but I can't help.").also { stage++ }
                        13 -> when(buttonID) {
                            1 -> playerl("I will find the warlord and bring back the orbs.").also { stage = 15 }
                            2 -> playerl("I'm sorry but I can't help.").also { stage = 14 }
                        }
                        14 -> npcl(FacialExpression.OLD_NORMAL, "I understand, this isn't your battle.").also { stage = END_DIALOGUE }
                        15 -> npcl(FacialExpression.OLD_NORMAL, "You are brave, but this task will be tough even for you. I wish you the best of luck. Once again you are our only hope.").also { stage++ }
                        16 -> npcl(FacialExpression.OLD_NORMAL, "I will safeguard this orb and pray for your safe return. My assistant will guide you out.").also {
                            stage++
                        }
                        17 -> {
                            if(removeItem(player!!,Items.ORB_OF_PROTECTION_587)){
                                teleport(player!!,mazeEntrance)
                                setQuestStage(player!!, Quests.TREE_GNOME_VILLAGE, 40)
                                sendNPCDialogue(player!!, NPCs.ELKOY_5179, "Good luck friend.", FacialExpression.OLD_NORMAL)
                            }
                            stage = END_DIALOGUE
                        }
                    }
                } else {
                    when(stage) {
                        0 -> playerl("Hello Bolren.").also { stage++ }
                        1 -> npcl(FacialExpression.OLD_NORMAL, "Do you have the orb?").also { stage++ }
                        2 -> playerl("No, I'm afraid not.").also { stage++ }
                        3 -> npcl(FacialExpression.OLD_NORMAL, "Please, we must have the orb if we are to survive.").also { stage = END_DIALOGUE }
                    }
                }
            }
            questStage == 40 -> {
                if(inInventory(player!!,Items.ORBS_OF_PROTECTION_588)){
                    when(stage) {
                        0 -> playerl("Bolren, I have returned.").also { stage++ }
                        1 -> npcl(FacialExpression.OLD_NORMAL, "You made it back! Do you have the orbs?").also { stage++ }
                        2 -> playerl("I have them here.").also { stage++ }
                        3 -> npcl(FacialExpression.OLD_HAPPY, "Hooray, you're amazing. I didn't think it was possible but you've saved us.").also { stage++ }
                        4 -> npcl(FacialExpression.OLD_NORMAL, "Once the orbs are replaced we will be safe once more. We must begin the ceremony immediately.").also { stage++ }
                        5 -> playerl("What does the ceremony involve?").also { stage++ }
                        6 -> npcl(FacialExpression.OLD_NORMAL, "The spirit tree has looked over us for centuries. Now we must pay our respects.").also { stage++ }
                        7 -> sendDialogue(player!!,"The gnomes begin to chant. Meanwhile, King Bolren holds the orbs of protection out in front of him.").also { stage++ }
                        8 -> {
                            // Orbs fly up, gnomes chant, spirit tree animates
                            GameWorld.Pulser.submit(object : Pulse(0) {
                                var count = 0
                                val orbsOfProtection = 79
                                val spiritTreeWithOrbs = Animation(333)
                                val gnomeSpots = listOf(Location(2542, 3172, 0),Location(2541, 3171, 0),Location(2542, 3167, 0),Location(2541, 3168, 0))
                                val spiritTree = getScenery(2543,3168,0)
                                override fun pulse(): Boolean {
                                    when (count) {
                                        0 -> {
                                            forceWalk(player!!,Location(2542, 3170, 0),"DUMB")
                                        }
                                        2 -> {
                                            player!!.faceLocation(Location(2543,3170,0))
                                            for((i, spot) in gnomeSpots.withIndex()){
                                                val gnome = RegionManager.getNpc(spot, NPCs.LOCAL_GNOME_484, 1)
                                                gnome!!.faceLocation(Location(2543,3169,0))
                                                gnome.animate(Animation(197))
                                                if(i < 2)
                                                    gnome.sendChat("En tanai.")
                                                else
                                                    gnome.sendChat("Su tana.")
                                            }

                                        }
                                        3 -> {
                                            player!!.packetDispatch.sendGraphic(orbsOfProtection)
                                        }
                                        5 -> {
                                            player!!.packetDispatch.sendSceneryAnimation(spiritTree, spiritTreeWithOrbs, false)
                                            sendDialogue(player!!,"The orbs of protection come to rest gently in the branches of the ancient spirit tree.")
                                            return true
                                        }
                                    }
                                    count++
                                    return false
                                }
                            })
                            // This loops back to the start of the handle..
                            if(removeItem(player!!,Items.ORBS_OF_PROTECTION_588)){
                                setQuestStage(player!!, Quests.TREE_GNOME_VILLAGE, 99)
                            }
                            stage = 0
                        }
                    }
                } else {
                    when(stage) {
                        0 -> playerl("Bolren, I have returned.").also { stage++ }
                        1 -> npcl(FacialExpression.OLD_NORMAL, "You made it back! Do you have the orbs?").also { stage++ }
                        2 -> playerl("No, I'm afraid not.").also { stage++ }
                        3 -> npcl(FacialExpression.OLD_NORMAL, "Please, we must have the orb if we are to survive.").also { stage = END_DIALOGUE }
                    }
                }
            }
            questStage == 99 -> {
                when(stage){
                    0 -> npcl(FacialExpression.OLD_NORMAL, "Now at last my people are safe once more. We can live in peace again.").also { stage++ }
                    1 -> playerl("I'm pleased I could help.").also { stage++ }
                    2 -> npcl(FacialExpression.OLD_NORMAL, "You are modest brave traveller.").also { stage++ }
                    3 -> npcl(FacialExpression.OLD_NORMAL, "Please, for your efforts take this amulet. It's made from the same sacred stone as the orbs of protection. It will help keep you safe on your journeys.").also { stage++ }
                    4 -> playerl("Thank you King Bolren.").also { stage++ }
                    5 -> npcl(FacialExpression.OLD_NORMAL, "The tree has many other powers, some of which I cannot reveal. As a friend of the gnome people, I can now allow you to use the tree's magic to teleport to other trees grown from related seeds.").also {
                        finishQuest(player!!, Quests.TREE_GNOME_VILLAGE)
                        stage = END_DIALOGUE
                    }
                }
            }
            isQuestComplete(player!!, Quests.TREE_GNOME_VILLAGE) -> {
                when(stage) {
                    0 -> playerl("Hello again Bolren.").also { stage++ }
                    1 -> npcl(FacialExpression.OLD_NORMAL, "Well hello, it's good to see you again.").also { stage = if (hasAnItem(player!!, Items.GNOME_AMULET_589).container != null) END_DIALOGUE else 2 }
                    2 -> playerl("I've lost my amulet.").also { stage++ }
                    3 -> npcl(FacialExpression.OLD_NORMAL, "Oh dear. Here, take another. We are truly indebted to you.").also {
                        addItemOrDrop(player!!, Items.GNOME_AMULET_589)
                        stage = END_DIALOGUE
                    }
                }
            }
        }
    }
}
