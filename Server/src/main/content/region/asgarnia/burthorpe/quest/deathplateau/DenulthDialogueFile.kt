package content.region.asgarnia.burthorpe.quest.deathplateau

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs


/**
 * Denulth sub dialogue file for death plateau.
 * Called by EohricDialogue
 * @author bushtail
 * @author ovenbread
 */

class DenulthDialogueFile : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, DeathPlateau.questName)) {
            in 0..4 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello citizen, how can I help you?").also { stage++ }
                    2 -> showTopics(
                        Topic("Do you have any quests for me?", 100),
                        Topic("What is this place?", 200),
                        Topic(FacialExpression.ANNOYED, "You can't, thanks.", END_DIALOGUE)
                    )
                    100 -> npcl(FacialExpression.FRIENDLY, "I don't know if you can help us!").also { stage++ }
                    101 -> npcl(FacialExpression.FRIENDLY, "The trolls have taken up camp on the Death Plateau! They are using it to launch raids at night on the village. We have tried to attack the camp, but the main path is heavily guarded!").also { stage++ }
                    102 -> playerl(FacialExpression.ASKING, "Perhaps there is a way you can sneak up at night?").also { stage++ }
                    103 -> npcl(FacialExpression.FRIENDLY, "If there is another way, I do not know of it.").also { stage++ }
                    104 -> npcl(FacialExpression.FRIENDLY, "Do you know of such a path?").also { stage++ }
                    105 -> showTopics(
                        Topic(FacialExpression.THINKING,"No, but perhaps I could try and find one?", 300),
                        Topic(FacialExpression.ANNOYED, "No, sorry.", END_DIALOGUE)
                    )

                    200 -> npcl(FacialExpression.HAPPY, "Welcome to the Principality of Burthorpe! We are the Imperial Guard for his Royal Highness Prince Anlaf of Burthorpe. Can I assist you with anything else?").also { stage = 2 }

                    300 -> npc(FacialExpression.FRIENDLY, "Citizen you would be well rewarded!").also { stage++ }
                    301 -> npcl(FacialExpression.FRIENDLY, "If you go up to Death Plateau, be very careful as the trolls will attack you on sight!").also { stage++ }
                    302 -> playerl(FacialExpression.FRIENDLY, "I'll be careful.").also { stage++ }
                    303 -> npcl(FacialExpression.FRIENDLY, "One other thing.").also { stage++ }
                    304 -> playerl(FacialExpression.FRIENDLY, "What's that?").also { stage++ }
                    305 -> npcl(FacialExpression.FRIENDLY, "All of our equipment is kept in the castle on the hill.").also { stage++ }
                    306 -> npcl(FacialExpression.FRIENDLY, "The stupid guard that was on duty last night lost the combination to the lock! I told the Prince that the Imperial Guard should've been in charge of security!").also { stage++ }
                    307 -> playerl(FacialExpression.ASKING, "No problem, what does the combination look like?").also { stage++ }
                    308 -> npcl(FacialExpression.FRIENDLY, "The equipment room is unlocked when the stone balls are placed in the correct order on the stone mechanism outside it. The right order is written on a piece of paper the guard had.").also { stage++ }
                    309 -> playerl(FacialExpression.FRIENDLY, "A stone what...?!").also { stage++ }
                    310 -> npcl(FacialExpression.FRIENDLY, "Well citizen, the Prince is fond of puzzles. Why we couldn't just have a key is beyond me!").also { stage++ }
                    311 -> playerl(FacialExpression.SUSPICIOUS, "I'll get on it right away!").also {
                        setQuestStage(player!!, DeathPlateau.questName, 5)
                        stage = END_DIALOGUE
                    }
                }
            }
            in 5..21 -> {
                when(stage) {
                    0 -> npcl(FacialExpression.HAPPY, "Hello citizen, is there anything you'd like to know?").also { stage++ }
                    1 -> showTopics(
                        Topic("Can you remind me of the quest I am on?", 100),
                        Topic("I thought the White Knights controlled Asgarnia?", 200),
                        Topic("What is this place?", 300),
                        Topic(FacialExpression.HAPPY, "That's all, thanks.", 10)
                    )
                    10 -> npcl(FacialExpression.HAPPY, "God speed citizen.").also { stage = END_DIALOGUE }

                    100 -> npcl(FacialExpression.HALF_THINKING, "You offered to see if you could find another way up Death Plateau. We could then use it to sneak up and attack the trolls by night.").also { stage++ }
                    101 -> playerl(FacialExpression.THINKING, "Ah yes, and the guard had lost the combination to your equipment rooms in the castle on the hill?").also { stage++ }
                    102 -> npcl(FacialExpression.HAPPY, "That's right citizen, you offered to recover the combination and unlock the door.").also { stage++ }
                    103 -> playerl(FacialExpression.FRIENDLY, "I'll get on it right away!").also { stage++ }
                    104 -> npcl(FacialExpression.FRIENDLY, "Good work citizen!").also { stage++ }
                    105 -> npcl(FacialExpression.ASKING, "Is there anything else you would like to know?").also { stage = 1 }

                    200 -> npcl(FacialExpression.ANGRY, "You are right citizen. The White Knights have taken advantage of the old and weak king, they control most of Asgarnia, including Falador. However they do not control Burthorpe!").also { stage++ }
                    201 -> npcl(FacialExpression.EVIL_LAUGH, "We are the prince's elite troops! We keep Burthorpe secure!").also { stage++ }
                    202 -> npcl(FacialExpression.ANGRY, "The White Knights have overlooked us until now! They are pouring money into their war against the Black Knights, They are looking for an excuse to stop our funding and I'm afraid they may have found it!").also { stage++ }
                    203 -> npcl(FacialExpression.HALF_WORRIED, "If we can not destroy the troll camp on Death Plateau then the Imperial Guard will be disbanded and Burthorpe will come under control of the White Knights. We cannot let this happen!").also { stage++ }
                    204 -> npcl(FacialExpression.ASKING, "Is there anything else you'd like to know?").also { stage = 1 }

                    300 -> npcl(FacialExpression.HAPPY, "Welcome to the Principality of Burthorpe! We are the Imperial Guard for his Royal Highness Prince Anlaf of Burthorpe. Can I assist you with anything else?").also { stage = 1 }
                }
            }
            22 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello citizen, have you found another way up Death Plateau?").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "Yes there is another way up Death Plateau!").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "We are saved!").also { stage++ }
                    4 -> playerl(FacialExpression.FRIENDLY, "There's one thing...").also { stage++ }
                    5 -> npcl(FacialExpression.FRIENDLY, "And what is that citizen?").also { stage++ }
                    6 -> playerl(FacialExpression.FRIENDLY, "There is a Sherpa who will only show me the secret way if I first get some spikes for his climbing boots. The smith will only do this for me if you sign up his son for the Imperial Guard!").also { stage++ }
                    7 -> npcl(FacialExpression.FRIENDLY, "Hmm...this is very irregular.").also { stage++ }
                    8 -> playerl(FacialExpression.FRIENDLY, "Will you not do this?").also { stage++ }
                    9 -> npcl(FacialExpression.FRIENDLY, "I have heard of Dunstan's son, he is a very promising young man. For the sake of your mission we can make an exception!").also { stage++ }
                    10 -> sendItemDialogue(player!!, Items.CERTIFICATE_3114, "Denulth has given you a certificate.").also {
                        addItemOrDrop(player!!, Items.CERTIFICATE_3114)
                        stage++
                    }
                    11 -> npcl(FacialExpression.FRIENDLY, "This certificate proves that we have accepted Dunstan's son for training in the Imperial Guard!").also { stage++ }
                    12 -> playerl(FacialExpression.FRIENDLY, "Thank you Denulth, I shall be back shortly!").also {
                        setQuestStage(player!!, DeathPlateau.questName, 23)
                        stage = END_DIALOGUE
                    }
                }
            }
            23 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello citizen, have you found the secret way up Death Plateau?").also {
                        if (!inInventory(player!!, Items.CERTIFICATE_3114)) {
                            stage = 3
                        } else {
                            stage++
                        }
                    }
                    2 -> playerl(FacialExpression.FRIENDLY, "I'm working on it, I just need to give the certificate to Dunstan.").also { stage = END_DIALOGUE }
                    3 -> playerl(FacialExpression.FRIENDLY, "I'm working on it but I've lost the certificate!").also { stage++ }
                    4 -> npcl(FacialExpression.FRIENDLY, "No problem, I have a duplicate.").also { stage++ }
                    5 -> sendItemDialogue(player!!, Items.CERTIFICATE_3114, "Denulth has given you a certificate.").also {
                        addItemOrDrop(player!!, Items.CERTIFICATE_3114)
                        stage = END_DIALOGUE
                    }
                }
            }
            in 24 .. 26 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello!").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Hello citizen, have you found the secret way up Death Plateau?").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "Yes! There is a path that runs from a Sherpa's hut around the back of Death Plateau. The trolls haven't found it yet. The Sherpa made a map I can give you.").also {
                        if (!inInventory(player!!, Items.SECRET_WAY_MAP_3104)) {
                            stage = 3
                        } else {
                            stage = 4
                        }
                    }
                    3 -> playerl(FacialExpression.FRIENDLY, "I don't have the map on me..").also { stage = END_DIALOGUE }
                    4 -> sendItemDialogue(player!!, Items.SECRET_WAY_MAP_3104, "You give Denulth the map of the secret way.").also {
                        if (removeItem(player!!, Item(Items.SECRET_WAY_MAP_3104))) {
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    }
                    5 -> npcl(FacialExpression.FRIENDLY, "Excellent, this looks perfect. They will never see us coming.").also { stage++ }
                    6 -> npcl(FacialExpression.FRIENDLY, "Have you managed to open the equipment room?").also {
                        if (!inInventory(player!!, Items.COMBINATION_3102)) {
                            stage = 7
                        } else {
                            stage = 8
                        }
                    }
                    7 -> playerl(FacialExpression.FRIENDLY, "I have opened the door but I don't have the combination on me.").also { stage++ }
                    8 -> playerl(FacialExpression.FRIENDLY, "Yes! The door is open and here is the combination.").also { stage++ }
                    9 -> sendItemDialogue(player!!, Items.COMBINATION_3102, "You give Denulth the combination to the equipment room.").also {
                        if (removeItem(player!!, Item(Items.COMBINATION_3102))) {
                            stage++
                        } else {
                            stage = END_DIALOGUE
                        }
                    }
                    10 -> npcl(FacialExpression.FRIENDLY, "Well done citizen! We will reward you by training you in attack!").also { stage++ }
                    11 -> npcl(FacialExpression.FRIENDLY, "I shall present you with some steel fighting claws. In addition I shall show you the knowledge of creating the fighting claws for yourself.").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "You are now an honourary member of the Imperial Guard!").also { stage++ }
                    13 -> {
                        stage = END_DIALOGUE
                        finishQuest(player!!, DeathPlateau.questName)
                    }
                }
            }
        }
    }
}