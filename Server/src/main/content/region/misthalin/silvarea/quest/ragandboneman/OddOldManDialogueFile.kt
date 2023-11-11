package content.region.misthalin.silvarea.quest.ragandboneman

import content.region.asgarnia.burthorpe.quest.deathplateau.DeathPlateau
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

class OddOldManDialogueFile : DialogueFile() {
    // BONES_3674 is the Sack on ODD_OLD_MAN_3670
    // There are probably FacialExpressions for the bone sack, but that's too much work.
    override fun handle(componentID: Int, buttonID: Int) {
        when (getQuestStage(player!!, RagAndBoneMan.questName)) {
            0 -> {
                when (stage) {
                    START_DIALOGUE -> npcl(FacialExpression.FRIENDLY, "Can I help you with something?").also { stage++ }
                    1 -> playerl(FacialExpression.FRIENDLY, "Well, err...who are you, and what are all these bones doing here?").also { stage++ }
                    2 -> npcl(FacialExpression.FRIENDLY, "Errr...").also { stage++ }
                    3 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    4 -> npcl(FacialExpression.FRIENDLY, "I'm an archaeologist. I work with the museum.").also { stage++ }
                    5 -> playerl(FacialExpression.FRIENDLY, "An archaeologist?").also { stage++ }
                    6 -> npcl(FacialExpression.FRIENDLY, "Yes.").also { stage++ }
                    7 -> playerl(FacialExpression.FRIENDLY, "Well that explains the bones...sort of...but what are you doing all the way out here?").also { stage++ }
                    8 -> npcl(FacialExpression.FRIENDLY, "Errr...").also { stage++ }
                    9 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble. Mumblemumblemumble.").also { stage++ }
                    10 -> npcl(FacialExpression.FRIENDLY, "I'm collecting bones for the museum.").also { stage++ }
                    11 -> npcl(FacialExpression.FRIENDLY, "They have asked me to rig up some displays of second and third age creatures using their bones, so that people can come and...well, look at them.").also { stage++ }
                    12 -> npcl(FacialExpression.FRIENDLY, "I need to get them into some sort of order before I begin, but I've run into a bit of a snag.").also { stage++ }
                    13 -> playerl(FacialExpression.FRIENDLY, "What sort of a snag?").also { stage++ }
                    14 -> npcl(FacialExpression.FRIENDLY, "Well, I need to have all the bones I'm going to use here, and placed into some sort of order.").also { stage++ }
                    15 -> npcl(FacialExpression.FRIENDLY, "However, I seem to have discovered I am a few short.").also { stage++ }
                    16 -> showTopics(
                            Topic("Anything I can do to help?", 20, true),
                            Topic("Well, good luck with that.", 70),
                            Topic("Where is that mumbling coming from?", 80)
                    )
                    20 -> playerl(FacialExpression.FRIENDLY, "Anything I can do to help?").also { stage++ }
                    21 -> npcl(FacialExpression.FRIENDLY, "Errr...").also { stage++ }
                    22 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    23 -> npcl(FacialExpression.FRIENDLY, "There is something you could do for me. I'm going to be busy...err...").also { stage++ }
                    24 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumble").also { stage++ }
                    25 -> npcl(FacialExpression.FRIENDLY, "Sorting, yes, sorting these bones out ready for the museum, but I need a few more.").also { stage++ }
                    26 -> npcl(FacialExpression.FRIENDLY, "Will you help me out by grabbing some?").also { stage++ }
                    27 -> showTopics(
                            Topic(FacialExpression.FRIENDLY, "Yes", 28, true),
                            Topic(FacialExpression.FRIENDLY, "No", 60),
                            Topic("Where is that mumbling coming from?", 80)
                    )
                    28 -> playerl(FacialExpression.FRIENDLY, "Yes. I'll give you a hand.").also { stage++ }
                    29 -> npcl(FacialExpression.FRIENDLY, "You will? Excellent!").also { stage++ }
                    30 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Sniggersnigger").also { stage++ }
                    31 -> playerl(FacialExpression.FRIENDLY, "Where do you need me to dig?").also { stage++ }
                    32 -> npcl(FacialExpression.FRIENDLY, "Dig?").also { stage++ }
                    33 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    34 -> npcl(FacialExpression.FRIENDLY, "Oh, you must have got the wrong end of the stick.").also { stage++ }
                    35 -> npcl(FacialExpression.FRIENDLY, "I need some fresh, whole bones to replace ones that have become damaged.").also { stage++ }
                    36 -> playerl(FacialExpression.FRIENDLY, "What?").also { stage++ }
                    37 -> npcl(FacialExpression.FRIENDLY, "Err...").also { stage++ }
                    38 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumblemumblemumblemumble. Mumblemumblemumblemumble. Mumble.").also { stage++ }
                    39 -> playerl(FacialExpression.FRIENDLY, "Excuse me...").also { stage++ }
                    40 -> npcl(FacialExpression.FRIENDLY, "Shhh!").also { stage++ }
                    41 -> playerl(FacialExpression.FRIENDLY, "Sorry...").also { stage++ }
                    42 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble. Mumblemumblemumble.").also { stage++ }
                    43 -> npcl(FacialExpression.FRIENDLY, "Ok, got it.").also { stage++ }
                    44 -> npcl(FacialExpression.FRIENDLY, "Ok, here is the thing. While sorting out what bones I do have I managed to lose or damage a few. If you can get me some fresh, unbroken bones to use as replacements then I can get on with things.").also { stage++ }
                    45 -> npcl(FacialExpression.FRIENDLY, "That make things clearer?").also { stage++ }
                    46 -> playerl(FacialExpression.FRIENDLY, "Well, it makes some sense I suppose...").also { stage++ }
                    47 -> npcl(FacialExpression.FRIENDLY, "Great. If you can get me a bone from a Goblin, a Bear, a Big Frog, a Ram, a Unicorn, a Monkey, a Giant rat and a Giant Bat then I'll be able to move on with the...").also { stage++ }
                    48 -> npcl(FacialExpression.FRIENDLY, "Displays...").also { stage++ }
                    49 -> playerl(FacialExpression.FRIENDLY, "So you just want me to bring you these bones and that will be that?").also { stage++ }
                    50 -> npcl(FacialExpression.FRIENDLY, "Well, I wouldn't mind you sticking them in a pot and boiling them in vinegar first, if you don't mind.").also { stage++ }
                    51 -> npcl(FacialExpression.FRIENDLY, "There is a Wine Merchant in Draynor called Fortunato that sells the stuff you'll need.").also { stage++ }
                    52 -> npcl(FacialExpression.FRIENDLY, "You can even use my pot-boiler if you want.").also { stage++ }
                    53 -> playerl(FacialExpression.FRIENDLY, "Why do I need to boil them in vinegar?").also { stage++ }
                    54 -> npcl(FacialExpression.FRIENDLY, "It gets them bright and sparking white.").also { stage++ }
                    55 -> npcl(FacialExpression.FRIENDLY, "It's an archaeologist thing.").also { stage++ }
                    56 -> npcl(FacialExpression.FRIENDLY, "Just put the bone in a pot of vinegar, throw some logs on the fire, put the pot in the holder and light the logs.").also { stage++ }
                    57 -> npcl(FacialExpression.FRIENDLY, "It takes a while for the vinegar to evaporate, but the bone will be nice and clean in the end.").also { stage++ }
                    58 -> playerl(FacialExpression.FRIENDLY, "All right, I'll be back later.").also { stage++ }
                    59 -> npcl(FacialExpression.FRIENDLY, "Bye!").also {
                        setQuestStage(player!!, RagAndBoneMan.questName, 1)
                        stage = END_DIALOGUE
                    }

                    60 -> npcl(FacialExpression.FRIENDLY, "Oh...I see.").also { stage++ }
                    61 -> npcl(FacialExpression.FRIENDLY, "Well, never mind me young man, I'll just stagger over here under my massive burden, and continue my thankless task.").also { stage++ }
                    62 -> npcl(FacialExpression.FRIENDLY, "Unaided and alone...").also { stage++ }
                    63 -> playerl(FacialExpression.FRIENDLY, "Wow, trying a guilt trip much?").also {
                        stage = END_DIALOGUE
                    }

                    70 -> npcl(FacialExpression.FRIENDLY, "Thanks stranger!").also { stage++ }
                    71 -> npcl(FacialExpression.FRIENDLY, "What a polite young man...").also { stage++ }
                    72 -> npcl(FacialExpression.FRIENDLY, "Well, back to work!").also {
                        stage = END_DIALOGUE
                    }

                    80 -> npcl(FacialExpression.FRIENDLY, "Errr...").also { stage++ }
                    81 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    82 -> npcl(FacialExpression.FRIENDLY, "What mumbling?").also { stage++ }
                    83 -> npcl(FacialExpression.FRIENDLY, "Anyway, I have enough problems of my own without dealing with a delusional adventurer.").also {
                        stage = 16
                    }
                }
            }
            in 1 .. 3 -> {
                when(stage) {
                    START_DIALOGUE -> {
                        if (checkBonesInInventory(player!!)) {
                            playerl(FacialExpression.FRIENDLY, "I have some bones for you...").also { stage = 1 }
                        } else {
                            npcl(FacialExpression.FRIENDLY, "Have you brought me any bones?").also { stage = 20 }
                        }
                    }
                    1 -> npcl(FacialExpression.FRIENDLY, "Great! Let me take a look at them.").also {
                        stage++
                    }
                    2 -> {
                        submitBonesInInventory(player!!)
                        if (hasAllBones(player!!)) {
                            npcl(FacialExpression.FRIENDLY, "That's the last of them!").also { stage = 4 }
                        } else {
                            npcl(FacialExpression.FRIENDLY, "Thanks the museum will be so pleased.").also { stage = 3 }
                        }
                    }
                    3 -> npcl(FacialExpression.FRIENDLY, "Come and see me when you have the rest.").also {
                        stage = END_DIALOGUE
                    }
                    4 -> npcl(FacialExpression.FRIENDLY, "The museum will be thrilled to know I've completed the collection.").also { stage++ }
                    5 -> playerl(FacialExpression.FRIENDLY, "Well I'm just glad I could help.").also { stage++ }
                    6 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    7 -> npcl(FacialExpression.FRIENDLY, "Well you've been a big help and no mistake.").also { stage++ }
                    8 -> npcl(FacialExpression.FRIENDLY, "I'm always on the lookout for fresh bones, so if you see some bring them right over.").also { stage++ }
                    9 -> playerl(FacialExpression.FRIENDLY, "No problem, I'll be sure to bring anything you might like over if I find something.").also { stage++ }
                    10 -> playerl(FacialExpression.FRIENDLY, "I can't wait to see the displays once they are finished.").also { stage++ }
                    11 -> finishQuest(player!!, RagAndBoneMan.questName).also {
                        end()
                    }
                    20 -> playerl(FacialExpression.FRIENDLY, "Not at the moment. Can you just give me a run down on which bones I have left to get?").also { stage++ }
                    21 -> npcl(FacialExpression.FRIENDLY, "Sure.").also {stage = 30 }
                    30, 40, 50, 60, 70, 80, 90, 100, 110 -> {
                        if (!getAttribute(player!!, RagAndBoneMan.attributeGoblinBone, false) && stage <= 30) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 31 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeBearBone, false) && stage <= 40) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 41 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeBigFrogBone, false) && stage <= 50) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 51 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeRamBone, false) && stage <= 60) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 61 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeUnicornBone, false) && stage <= 70) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 71 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeMonkeyBone, false) && stage <= 80) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 81 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeGiantRatBone, false) && stage <= 90) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 91 }
                        else if (!getAttribute(player!!, RagAndBoneMan.attributeGiantBatBone, false) && stage <= 100) { npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble"); stage = 101 }
                        else {
                            npcl(FacialExpression.FRIENDLY, "Did you get all that?")
                            stage = 111
                        }
                    }
                    31 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Goblin bone.").also { stage++ }
                    32 -> npcl(FacialExpression.FRIENDLY, "Goblins are relatively common. I hear there is a house full of them around Lumbridge in fact.").also { stage = 40 }

                    41 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Bear bone.").also { stage++ }
                    42 -> npcl(FacialExpression.FRIENDLY, "I heard that there are some Bears over by the Legends' Guild, near Ardougne.").also { stage = 50 }

                    51 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Big Frog bone.").also { stage++ }
                    52 -> npcl(FacialExpression.FRIENDLY, "This might be a little tricky as you will need to go into the Lumbridge Swamp caves. You will need a light source! Never forget your light source down there!").also { stage = 60 }

                    61 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Ram bone.").also { stage++ }
                    62 -> npcl(FacialExpression.FRIENDLY, "I'm sure you will be able to find a ram wherever there are sheep.").also { stage = 70 }

                    71 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Unicorn bone.").also { stage++ }
                    72 -> npcl(FacialExpression.FRIENDLY, "I seem to remember that there were Unicorns south of Varrock, I think they might be there still.").also { stage = 80 }

                    81 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Monkey bone.").also { stage++ }
                    82 -> npcl(FacialExpression.FRIENDLY, "Monkeys tend to live in Jungle areas, like Karamja. I think they are pretty plentiful there if I remember correctly.").also { stage = 90 }

                    91 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Giant Rat bone.").also { stage++ }
                    92 -> npcl(FacialExpression.FRIENDLY, "If you can't find one in a sewer, then you might want to try looking in some caves.").also { stage = 100 }

                    101 -> npcl(FacialExpression.FRIENDLY, "You still need to bring me a Giant Bat bone.").also { stage++ }
                    102 -> npcl(FacialExpression.FRIENDLY, "Giant bats tend to live underground, but I have heard there are a few near the Coal Pits.").also { stage = 110 }

                    111 -> playerl(FacialExpression.FRIENDLY, "Yes. I'll get right on it.").also { stage++ }
                    112 -> npcl(NPCs.BONES_3674, "Sack", FacialExpression.OLD_HAPPY, "Mumblemumble").also { stage++ }
                    113 -> npcl(FacialExpression.FRIENDLY, "Don't forget to boil them in vinegar first.").also { stage++ }
                    114 -> npcl(FacialExpression.FRIENDLY, "Just chuck some logs into the pit, put the bone in the pot of vinegar and drop it onto the pot-boiler. Then light the logs and wait for the the vinegar to boil away.").also { stage++ }
                    115 -> playerl(FacialExpression.FRIENDLY, "Ok, I'll remember.").also {
                        stage = END_DIALOGUE
                    }
                }
            }
        }
        /*
        Unfortunately from OSRS 2016: https://www.youtube.com/watch?v=31QNg1E0qf0
        Wonderful! I'll put this with the rest.
        No problem. I'll be back if I find some more.

        This one is closest RS 2012: https://www.youtube.com/watch?v=0I8fNTeAwA8
        I have some bones for you...
        Great! Let me take a look at them.
        Thanks the museum will be so pleased.
        Come and see me when you have the rest.
         */
    }

    private fun checkBonesInInventory(player: Player) : Boolean {
        var hasBone = false
        RagAndBoneMan.requiredBonesMap.forEach {
            if (inInventory(player, it.key)) {
                hasBone = true
            }
        }
        return hasBone
    }

    private fun submitBonesInInventory(player: Player) {
        RagAndBoneMan.requiredBonesMap.forEach {
            if (!getAttribute(player, it.value, false) && removeItem(player, it.key)) {
                setAttribute(player, it.value, true)
            }
        }
    }

    private fun hasAllBones(player: Player) : Boolean {
        var hasBoneAllBones = true
        RagAndBoneMan.requiredBonesMap.values.forEach {
            if (!getAttribute(player, it, false)) {
                hasBoneAllBones = false
            }
        }
        return hasBoneAllBones
    }
}