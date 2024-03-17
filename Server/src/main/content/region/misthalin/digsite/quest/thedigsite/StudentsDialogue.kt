package content.region.misthalin.digsite.quest.thedigsite

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs

@Initializable
class StudentGreenDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StudentGreenDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return StudentGreenDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STUDENT_615)
    }
}
class StudentGreenDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {


        b.onQuestStages(TheDigSite.questName, 6,7,8,9,10,11,12,13,100)
                .npcl(FacialExpression.FRIENDLY, " Oh, hi again. News of your find has spread fast; you are quite famous around here now.")

        b.onQuestStages(TheDigSite.questName, 5)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "I need more help with the exam.")
                .npcl(FacialExpression.FRIENDLY, "Well, okay, this is what I have learned since I last spoke to you...")
                .npcl(FacialExpression.FRIENDLY, "Specimen brush use: Brush carefully and slowly using short strokes.")
                .playerl(FacialExpression.FRIENDLY, "Okay, I'll remember that. Thanks for all your help.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentGreenExam3ObtainAnswer, true)
                }

        b.onQuestStages(TheDigSite.questName, 4)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "I need more help with the exam.")
                .npcl(FacialExpression.FRIENDLY, "Well, okay, this is what I have learned since I last spoke to you...")
                .npcl(FacialExpression.FRIENDLY, "Correct rock pick usage: Always handle with care; strike the rock cleanly on its cleaving point.")
                .playerl(FacialExpression.FRIENDLY, "Okay, I'll remember that.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentGreenExam2ObtainAnswer, true)
                }
        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 3 && getAttribute(player, TheDigSite.attributeStudentGreenExam1ObtainAnswer, false)}
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "I need more help with the exam.")
                .npcl(FacialExpression.FRIENDLY, "Well, okay, this is what I have learned since I last spoke to you...")
                .npcl(FacialExpression.FRIENDLY, "The study of Earth Sciences is: The study of the earth, its contents and history.")
                .playerl(FacialExpression.FRIENDLY, "Okay, I'll remember that.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentGreenExam1ObtainAnswer, true)
                }
        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 3 && getAttribute(player, TheDigSite.attributeStudentGreenExam1Talked, false)}
                .branch { player ->
                    return@branch if (inInventory(player, Items.ANIMAL_SKULL_671)) { 1 } else { 0 }
                }.let{ branch ->
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "Hello there. How's the study going?")
                            .npcl(FacialExpression.FRIENDLY, "Very well, thanks. Have you found my animal skull yet?")
                            .playerl(FacialExpression.FRIENDLY, "No, sorry, not yet.")
                            .npcl(FacialExpression.FRIENDLY, "Oh well, I am sure it's been picked up. Couldn't you try looking through some pockets?")
                            .end()
                    branch.onValue(1)
                            .playerl(FacialExpression.FRIENDLY, "Hello there. How's the study going?")
                            .npcl(FacialExpression.FRIENDLY, "Very well, thanks. Have you found my animal skull yet?")
                            .betweenStage { _, player, _, _ ->
                                if (inInventory(player, Items.ANIMAL_SKULL_671)){
                                    removeItem(player, Items.ANIMAL_SKULL_671)
                                }
                            }
                            .npcl("Oh wow! You've found it! Thank you so much. I'll be glad to tell you what I know about the exam. The study of Earth Sciences is: The study of the earth, its contents and history.")
                            .endWith { _, player ->
                                setAttribute(player, TheDigSite.attributeStudentGreenExam1ObtainAnswer, true)
                            }
                    return@let branch
                }
        b.onQuestStages(TheDigSite.questName, 3)
                .playerl(FacialExpression.FRIENDLY, "Hello there. Can you help me with the Earth Sciences exams at all?")
                .npcl(FacialExpression.FRIENDLY, "Well... Maybe I will if you help me with something.")
                .playerl(FacialExpression.FRIENDLY, "What's that?")
                .npcl(FacialExpression.FRIENDLY, "I have lost my recent good find.")
                .playerl(FacialExpression.FRIENDLY, "What does it look like?")
                .npcl(FacialExpression.FRIENDLY, "Err... Like an animal skull!")
                .playerl(FacialExpression.FRIENDLY, "Well, that's not too helpful, there are lots of those around here. Can you remember where you last had it?")
                .npcl(FacialExpression.FRIENDLY, "It was around here for sure. Maybe one of the workmen picked it up?")
                .playerl(FacialExpression.FRIENDLY, "Okay, I'll have a look for you.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentGreenExam1Talked, true)
                }
        b.onPredicate { _ -> true }
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "Oh, hi. I'm studying hard for an exam.")
                .playerl(FacialExpression.FRIENDLY, "What exam is that?")
                .npcl(FacialExpression.FRIENDLY, "It's the Earth Sciences exam.")
                .playerl(FacialExpression.FRIENDLY, "Interesting....")
                .end()
    }
}


@Initializable
class StudentPurpleDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StudentPurpleDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return StudentPurpleDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STUDENT_617)
    }
}
class StudentPurpleDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {

        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 5 && getAttribute(player, TheDigSite.attributeStudentPurpleExam3ObtainAnswer, false)}
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "I am stuck on some more exam questions.")
                .npcl(FacialExpression.FRIENDLY, "Okay, I'll tell you my latest notes...")
                .npcl(FacialExpression.FRIENDLY, "Sample preparation: Samples cleaned, and carried only in specimen jars.")
                .playerl(FacialExpression.FRIENDLY, "Great, thanks for your advice.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentPurpleExam3ObtainAnswer, true)
                }

        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 5 && getAttribute(player, TheDigSite.attributeStudentPurpleExam3Talked, false)}
                .branch { player ->
                    return@branch if (inInventory(player, Items.OPAL_1609) || inInventory(player, Items.UNCUT_OPAL_1625)) { 1 } else { 0 }
                }.let{ branch ->
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "Hello there.")
                            .npcl(FacialExpression.FRIENDLY, "Oh, hi again. Did you bring me the opal?")
                            .playerl(FacialExpression.FRIENDLY, "I haven't found one yet.")
                            .npcl(FacialExpression.FRIENDLY, "Oh, well, tell me when you do. Remember that they can be found around the site; perhaps try panning the river.")
                            .end()
                    branch.onValue(1)
                            .playerl(FacialExpression.FRIENDLY, "Hello there.")
                            .npcl(FacialExpression.FRIENDLY, "Oh, hi again. Did you bring me the opal?")
                            .betweenStage { _, player, _, _ ->
                                if (inInventory(player, Items.UNCUT_OPAL_1625)){
                                    removeItem(player, Items.UNCUT_OPAL_1625)
                                    return@betweenStage
                                }
                                if (inInventory(player, Items.OPAL_1609)){
                                    removeItem(player, Items.OPAL_1609)
                                    return@betweenStage
                                }
                            }
                            .playerl(FacialExpression.FRIENDLY, "Would an opal look like this by any chance?")
                            .npcl(FacialExpression.FRIENDLY, "Wow, great, you've found one. This will look beautiful set in my necklace. Thanks for that; now I'll tell you what I know... Sample preparation: Samples cleaned, and carried only in specimen jars.")
                            .playerl(FacialExpression.FRIENDLY, "Great, thanks for your advice.")
                            .endWith { _, player ->
                                setAttribute(player, TheDigSite.attributeStudentPurpleExam3ObtainAnswer, true)
                            }
                    return@let branch
                }

        b.onQuestStages(TheDigSite.questName, 5)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "What, you want more help?")
                .playerl(FacialExpression.FRIENDLY, "Err... Yes please!")
                .npcl(FacialExpression.FRIENDLY, "Well... it's going to cost you...")
                .playerl(FacialExpression.FRIENDLY, "Oh, well how much?")
                .npcl(FacialExpression.FRIENDLY, "I'll tell you what I would like: a precious stone. I don't find many of them. My favorite are opals; they are beautiful. Just like me! Tee hee hee!")
                .playerl(FacialExpression.FRIENDLY, "Err... OK I'll see what I can do, but I'm not sure where I'd get one.")
                .npcl(FacialExpression.FRIENDLY, "Well, I have seen people get them from panning occasionally.")
                .playerl(FacialExpression.FRIENDLY, "OK, I'll see what I can turn up for you.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentPurpleExam3Talked, true)
                }

        b.onQuestStages(TheDigSite.questName, 4)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "I am stuck on some more exam questions.")
                .npcl(FacialExpression.FRIENDLY, "Okay, I'll tell you my latest notes...")
                .npcl(FacialExpression.FRIENDLY, "Finds handling: Finds must be carefully handled.")
                .playerl(FacialExpression.FRIENDLY, "Great, thanks for your advice.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentPurpleExam2ObtainAnswer, true)
                }
        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 3 && getAttribute(player, TheDigSite.attributeStudentPurpleExam1ObtainAnswer, false)}
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "I am stuck on some more exam questions.")
                .npcl(FacialExpression.FRIENDLY, "Okay, I'll tell you my latest notes...")
                .npcl(FacialExpression.FRIENDLY, "The proper health and safety points are: Leather gloves and boots to be warn at all times; proper tools must be used.")
                .playerl(FacialExpression.FRIENDLY, "Great, thanks for your advice.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentPurpleExam1ObtainAnswer, true)
                }
        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 3 && getAttribute(player, TheDigSite.attributeStudentPurpleExam1Talked, false)}
                .branch { player ->
                    return@branch if (inInventory(player, Items.TEDDY_673)) { 1 } else { 0 }
                }.let{ branch ->
                    branch.onValue(0)
                            .npcl(FacialExpression.FRIENDLY, "Very well thanks. Have you found my lucky mascot yet?")
                            .playerl(FacialExpression.FRIENDLY, "No sorry, not yet.")
                            .npcl(FacialExpression.FRIENDLY, "I'm sure it's just outside the site somewhere...")
                            .end()
                    branch.onValue(1)
                            .playerl(FacialExpression.FRIENDLY, "Hello there.")
                            .betweenStage { _, player, _, _ ->
                                if (inInventory(player, Items.TEDDY_673)){
                                    removeItem(player, Items.TEDDY_673)
                                }
                            }
                            .playerl(FacialExpression.FRIENDLY, "Guess what I found.")
                            .npcl(FacialExpression.FRIENDLY, "Hey! My lucky mascot! Thanks ever so much. Let me help you with those questions now.")
                            .npcl(FacialExpression.FRIENDLY, "The proper health and safety points are: Leather gloves and boots to be warn at all times; proper tools must be used.")
                            .playerl(FacialExpression.FRIENDLY, "Great, thanks for your advice.")
                            .endWith { _, player ->
                                setAttribute(player, TheDigSite.attributeStudentPurpleExam1ObtainAnswer, true)
                            }
                    return@let branch
                }
        b.onQuestStages(TheDigSite.questName, 3)
                .playerl(FacialExpression.FRIENDLY, "Hello there. Can you help me with the Earth Sciences exams at all?")
                .npcl(FacialExpression.FRIENDLY, "I can if you help me...")
                .playerl(FacialExpression.FRIENDLY, "How can I do that?")
                .npcl(FacialExpression.FRIENDLY, "I have lost my teddy bear. He was my lucky mascot.")
                .playerl(FacialExpression.FRIENDLY, "Do you know where you dropped him?")
                .npcl(FacialExpression.FRIENDLY, "Well, I was doing a lot of walking that day... Oh yes, that's right - a few of us were studying that funny looking relic in the centre of the campus. Maybe I lost my lucky mascot around there, perhaps in a bush?")
                .playerl(FacialExpression.FRIENDLY, "Leave it to me, I'll find it.")
                .npcl(FacialExpression.FRIENDLY, "Oh, great! Thanks!")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentPurpleExam1Talked, true)
                }
        b.onPredicate { _ -> true }
                .playerl("Hello there.")
                .npcl("Hi there. I'm studying for the Earth Sciences exam.")
                .playerl("Interesting... This exam seems to be a popular one!")
    }
}


@Initializable
class StudentBrownDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, StudentBrownDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return StudentBrownDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.STUDENT_616)
    }
}
class StudentBrownDialogueFile : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {

        b.onQuestStages(TheDigSite.questName, 5)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "There are more exam questions I'm stuck on.")
                .npcl(FacialExpression.FRIENDLY, "Hey, I'll tell you what I've learned. That may help.")
                .npcl(FacialExpression.FRIENDLY, "The proper technique for handling bones is: Handle bones carefully and keep them away from other samples.")
                .playerl(FacialExpression.FRIENDLY, "Thanks for the information.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentBrownExam3ObtainAnswer, true)
                }

        b.onQuestStages(TheDigSite.questName, 4)
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "There are more exam questions I'm stuck on.")
                .npcl(FacialExpression.FRIENDLY, "Hey, I'll tell you what I've learned. That may help.")
                .npcl(FacialExpression.FRIENDLY, "Correct sample transportation: Samples taken in rough form; kept only in sealed containers.")
                .playerl(FacialExpression.FRIENDLY, "Thanks for the information.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentBrownExam2ObtainAnswer, true)
                }
        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 3 && getAttribute(player, TheDigSite.attributeStudentBrownExam1ObtainAnswer, false)}
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "How's it going?")
                .playerl(FacialExpression.FRIENDLY, "There are more exam questions I'm stuck on.")
                .npcl(FacialExpression.FRIENDLY, "Hey, I'll tell you what I've learned. That may help.")
                .npcl(FacialExpression.FRIENDLY, "Correct sample transportation: Samples taken in rough form; kept only in sealed containers.")
                .playerl(FacialExpression.FRIENDLY, "Thanks for the information.")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentBrownExam1ObtainAnswer, true)
                }
        b.onPredicate { player -> getQuestStage(player, TheDigSite.questName) == 3 && getAttribute(player, TheDigSite.attributeStudentBrownExam1Talked, false)}
                .playerl(FacialExpression.FRIENDLY, "Hello there. How's the study going?")
                .npcl(FacialExpression.FRIENDLY, "I'm getting there. Have you found my special cup yet?")
                .branch { player ->
                    if (inInventory(player, Items.SPECIAL_CUP_672)){
                        removeItem(player, Items.SPECIAL_CUP_672)
                        return@branch 1
                    } else {
                        return@branch 0
                    }
                }.let{ branch ->
                    branch.onValue(0)
                            .playerl(FacialExpression.FRIENDLY, "No, sorry, not yet.")
                            .npcl(FacialExpression.FRIENDLY, "Oh dear, I hope it didn't fall into the river. I might never find it again.")
                            .end()
                    branch.onValue(1)
                            .npcl("Oh wow! You've found it! Thank you so much. I'll be glad to tell you what I know about the exam. The study of Earth Sciences is: The study of the earth, its contents and history.")
                            .endWith { _, player ->
                                setAttribute(player, TheDigSite.attributeStudentBrownExam1ObtainAnswer, true)
                            }
                    return@let branch
                }
        b.onQuestStages(TheDigSite.questName, 3)
                .playerl(FacialExpression.FRIENDLY, "Hello there. Can you help me with the Earth Sciences exams at all?")
                .npcl(FacialExpression.FRIENDLY, "I can't do anything unless I find my special cup.")
                .playerl(FacialExpression.FRIENDLY, "Your what?")
                .npcl(FacialExpression.FRIENDLY, "My special cup. I won it for a particularly good find last month.")
                .playerl(FacialExpression.FRIENDLY, "Oh, right. So if I find it, you'll help me?")
                .npcl(FacialExpression.FRIENDLY, "I sure will!")
                .playerl(FacialExpression.FRIENDLY, "Any ideas where it may be?")
                .npcl(FacialExpression.FRIENDLY, "All I remember is that I was working near the panning area when I lost it.")
                .playerl(FacialExpression.FRIENDLY, "Okay, I'll see what I can do.")
                .npcl(FacialExpression.FRIENDLY, "Yeah, maybe the panning guide saw it? I hope I didn't lose it in the water!")
                .endWith { _, player ->
                    setAttribute(player, TheDigSite.attributeStudentBrownExam1Talked, true)
                }
        b.onPredicate { _ -> true }
                .playerl(FacialExpression.FRIENDLY, "Hello there.")
                .npcl(FacialExpression.FRIENDLY, "Hello there. As you can see, I am a student.")
                .playerl(FacialExpression.FRIENDLY, "What are you doing here?")
                .npcl(FacialExpression.FRIENDLY, "I'm studying for the Earth Sciences exam.")
                .playerl(FacialExpression.FRIENDLY, "Interesting... Perhaps I should study for it as well.")
    }
}
