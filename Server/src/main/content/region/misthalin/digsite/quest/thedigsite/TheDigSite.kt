package content.region.misthalin.digsite.quest.thedigsite

import content.region.morytania.quest.creatureoffenkenstrain.CreatureOfFenkenstrain
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import org.rs09.consts.Items

/**
 * The Dig Site Quest
 * Funny enough, the crossed out quest log doesn't change into the past tense like other quests do.
 * I guess this quest is so old, no one was bothered to change the tenses.
 * This is working off the low quality quest log in 2008, which is very much different from the current quest log.

 * 1 - Speak to an examiner to start the quest
 * 2 - Talked to the curator at Varrock
 * 3 - Passed the signed letter back to the examiner
 * 4 - Passed Level 1 Exam
 * 5 - Passed Level 2 Exam
 * 6 - Passed Level 3 Exam
 * 7 - Found ANCIENT_TALISMAN_681 to impress expert
 * 8 - Gave expert's recommendation letter to a workman
 * 9 - Went down any of the digshafts
 * 10 - Talked to Doug Deeping to get a key to open a chest
 * 11 - Covered rocks with explosives
 * 12 - Blew up the rocks (after this, climbing down a digshaft will end up in a different area)
 * 100 - Talked to expert after showing him the STONE_TABLET_699
 */
@Initializable
class TheDigSite : Quest("The Dig Site", 47, 46, 2, 131, 0, 1, 9) {
    companion object {
        const val questName = "The Dig Site"
        const val attributeStudentGreenExam1Talked = "/save:quest:thedigsite-studentgreenexam1talked"
        const val attributeStudentGreenExam1ObtainAnswer = "/save:quest:thedigsite-studentgreenexam1obtainanswer"
        const val attributeStudentPurpleExam1Talked = "/save:quest:thedigsite-studentpurpleexam1talked"
        const val attributeStudentPurpleExam1ObtainAnswer = "/save:quest:thedigsite-studentpurplexam1obtainanswer"
        const val attributeStudentBrownExam1Talked = "/save:quest:thedigsite-studentbrownexam1talked"
        const val attributeStudentBrownExam1ObtainAnswer = "/save:quest:thedigsite-studentbrownexam1obtainanswer"
        const val attributePanningGuideTea = "/save:quest:thedigsite-panningguidetea"
        const val attributeStudentGreenExam2ObtainAnswer = "/save:quest:thedigsite-studentgreenexam2obtainanswer"
        const val attributeStudentPurpleExam2ObtainAnswer = "/save:quest:thedigsite-studentpurplexam2obtainanswer"
        const val attributeStudentBrownExam2ObtainAnswer = "/save:quest:thedigsite-studentbrownexam2obtainanswer"
        const val attributeStudentGreenExam3ObtainAnswer = "/save:quest:thedigsite-studentgreenexam3obtainanswer"
        const val attributeStudentPurpleExam3ObtainAnswer = "/save:quest:thedigsite-studentpurplexam3obtainanswer"
        const val attributeStudentPurpleExam3Talked = "/save:quest:thedigsite-studentpurpleexam3talked"
        const val attributeStudentBrownExam3ObtainAnswer = "/save:quest:thedigsite-studentbrownexam3obtainanswer"

        const val attributeRopeNorthEastWinch = "/save:quest:thedigsite-ropenortheastwinch"
        const val attributeRopeWestWinch = "/save:quest:thedigsite-ropewestwinch"

        const val attributeFirstQuestion = "quest:thedigsite-firstquestion"
        const val attributeSecondQuestion = "quest:thedigsite-secondquestion"
        const val attributeThirdQuestion = "quest:thedigsite-thirdquestion"

        const val barrelVarbit = 2547
        const val tabletVarbit = 2548
    }

    override fun reset(player: Player) {
        removeAttribute(player, attributeStudentGreenExam1Talked)
        removeAttribute(player, attributeStudentGreenExam1ObtainAnswer)
        removeAttribute(player, attributeStudentPurpleExam1Talked)
        removeAttribute(player, attributeStudentPurpleExam1ObtainAnswer)
        removeAttribute(player, attributeStudentBrownExam1Talked)
        removeAttribute(player, attributeStudentBrownExam1ObtainAnswer)
        removeAttribute(player, attributePanningGuideTea)
        removeAttribute(player, attributeStudentGreenExam2ObtainAnswer)
        removeAttribute(player, attributeStudentPurpleExam2ObtainAnswer)
        removeAttribute(player, attributeStudentBrownExam2ObtainAnswer)
        removeAttribute(player, attributeStudentGreenExam3ObtainAnswer)
        removeAttribute(player, attributeStudentPurpleExam3Talked)
        removeAttribute(player, attributeStudentPurpleExam3ObtainAnswer)
        removeAttribute(player, attributeStudentBrownExam3ObtainAnswer)

        removeAttribute(player, attributeRopeNorthEastWinch)
        removeAttribute(player, attributeRopeWestWinch)
    }

    override fun drawJournal(player: Player, stage: Int) {
        super.drawJournal(player, stage)
        var line = 11
        var stage = getStage(player)

        var started = getQuestStage(player, questName) > 0

        if(!started){
            line++
            line(player, "I can start this quest by speaking to the !!Examiner?? at the", line++, false)
            line(player, "!!Digsite Exam Centre.??", line++, false)
            line(player, "I need the following skill levels:", line++, false)
            line(player, "Level 10 Agility", line++, hasLevelStat(player, Skills.AGILITY, 10))
            line(player, "Level 10 Herblore", line++, hasLevelStat(player, Skills.HERBLORE, 10))
            line(player, "Level 25 Thieving", line++, hasLevelStat(player, Skills.THIEVING, 25))
        } else {
            line(player, "I should speak to an examiner about taking Earth Science", line++, true)
            line(player, "Exams.", line++, true)

            if (stage >= 2) {
                line(player, "I should take the letter the Examiner has given me to the", line++, true)
                line(player, "Curator of Varrock Museum, for his approval.", line++, true)
            } else if (stage >= 1) {
                line(player, "I should take the !!letter?? the !!Examiner?? has given me to the", line++)
                line(player, "!!Curator?? of !!Varrock Museum??, for his approval.", line++)
            }

            if (stage >= 3) {
                line(player, "I need to return the letter of recommendation from the", line++, true)
                line(player, "Curator of Varrock Museum to the Examiner at the Exam", line++, true)
                line(player, "Centre for inspection.", line++, true)
            } else if (stage >= 2) {
                line(player, "I need to return the !!letter of recommendation?? from the", line++)
                line(player, "!!Curator?? of !!Varrock Museum?? to the !!Examiner?? at the !!Exam??", line++)
                line(player, "!!Centre?? for inspection.", line++)
            }
            if (stage >= 4) {
                line(player, "I need to study for my first exam. Perhaps the students", line++, true)
                line(player, "on the site can help?", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to study for my first exam. Perhaps the students", line++)
                line(player, "on the site can help?", line++)
            }
            if (stage >= 4 || getAttribute(player, attributeStudentGreenExam1Talked, false)) {
                line(player, "I need to speak to the student in the green top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to speak to the student in the green top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 4 || getAttribute(player, attributeStudentPurpleExam1Talked, false)) {
                line(player, "I need to speak to the student in the purple shirt about", line++, true)
                line(player, "the exams.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to speak to the student in the purple shirt about", line++)
                line(player, "the exams.", line++)
            }
            if (stage >= 4 || getAttribute(player, attributeStudentBrownExam1Talked, false)) {
                line(player, "I need to speak to the student in the orange top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 3) {
                line(player, "I need to speak to the student in the orange top about the", line++)
                line(player, "exams.", line++)
            }

            if (stage >= 4 || getAttribute(player, attributeStudentGreenExam1ObtainAnswer, false)) {
                line(player, "I have agreed to help the student in the green top.", line++, true)
                line(player, "He has lost his Animal Skull and thinks he may have", line++, true)
                line(player, "dropped it around the digsite. I need to find it and return it", line++, true)
                line(player, "to him. Maybe one of the workmen has picked it up?", line++, true)
            } else if (stage >= 3 && getAttribute(player, attributeStudentGreenExam1Talked, false)) {
                line(player, "I have agreed to help the student in the green top.", line++)
                line(player, "He has lost his !!Animal Skull?? and thinks he may have", line++)
                line(player, "dropped it around the digsite. I need to find it and return it", line++)
                line(player, "to him. Maybe one of the workmen has picked it up?", line++)
            }
            if (stage >= 4) {
                line(player, "I should talk to him to see if he can help with my exams.", line++, true)
                line(player, "He gave me an answer to one of the questions on the first", line++, true)
                line(player, "exam.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attributeStudentGreenExam1ObtainAnswer, false)) {
                line(player, "I should talk to him to see if he can help with my exams.", line++)
                line(player, "He gave me an answer to one of the questions on the first", line++)
                line(player, "exam.", line++)
            }

            if (stage >= 4 || getAttribute(player, attributeStudentPurpleExam1ObtainAnswer, false)) {
                line(player, "I have agreed to help the student in the purple skirt.", line++, true)
                line(player, "She has lost her Lucky Mascot and thinks she may have", line++, true)
                line(player, "dropped it around the large urns on the digsite. I need to", line++, true)
                line(player, "find it and return it to her.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attributeStudentPurpleExam1Talked, false)) {
                line(player, "I have agreed to help the student in the purple skirt.", line++)
                line(player, "She has lost her !!Lucky Mascot?? and thinks she may have", line++)
                line(player, "dropped it around the large urns on the digsite. I need to", line++)
                line(player, "find it and return it to her.", line++)
            }
            if (stage >= 4) {
                line(player, "I should talk to her to see if she can help with my exams.", line++, true)
                line(player, "She gave me an answer to one of the questions on the", line++, true)
                line(player, "first exam.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attributeStudentPurpleExam1ObtainAnswer, false)) {
                line(player, "I should talk to her to see if she can help with my exams.", line++)
                line(player, "She gave me an answer to one of the questions on the", line++)
                line(player, "first exam.", line++)
            }


            if (stage >= 4 || getAttribute(player, attributeStudentBrownExam1ObtainAnswer, false)) {
                line(player, "I have agreed to help the student in the orange top.", line++, true)
                line(player, "He has lost his Special Cup and thinks he may have", line++, true)
                line(player, "dropped it around the tents near the panning site. I need", line++, true)
                line(player, "to find it and return it.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attributeStudentBrownExam1Talked, false)) {
                line(player, "I have agreed to help the student in the orange top.", line++)
                line(player, "He has lost his !!Special Cup?? and thinks he may have", line++)
                line(player, "dropped it around the tents near the panning site. I need", line++)
                line(player, "to find it and return it.", line++)
            }
            if (stage >= 4) {
                line(player, "I should talk to him to see if he can help with my exams.", line++, true)
                line(player, "He gave me an answer to one of the questions on the first", line++, true)
                line(player, "exam.", line++, true)
            } else if (stage >= 3 && getAttribute(player, attributeStudentBrownExam1ObtainAnswer, false)) {
                line(player, "I should talk to him to see if he can help with my exams.", line++)
                line(player, "He gave me an answer to one of the questions on the first", line++)
                line(player, "exam.", line++)
            }


            if (stage >= 4) {
                line(player, "I should talk to an examiner to take my first exam. If I", line++, true)
                line(player, "have forgotten anything, I can always ask the students", line++, true)
                line(player, "again.", line++, true)
                line(player, "I have passed my first Earth Science exam.", line++, true)
            } else if (stage >= 3
                    && getAttribute(player, attributeStudentGreenExam1ObtainAnswer, false)
                    && getAttribute(player, attributeStudentPurpleExam1ObtainAnswer, false)
                    && getAttribute(player, attributeStudentBrownExam1ObtainAnswer, false)
                    ) {
                line(player, "I should talk to an examiner to take my first exam. If I", line++)
                line(player, "have forgotten anything, I can always ask the students", line++)
                line(player, "again.", line++)
            }

            if (stage >= 5) {
                line(player, "I need to study for my second exam. Perhaps the three", line++, true)
                line(player, "students on the digsite can help me again?", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to study for my second exam. Perhaps the three", line++)
                line(player, "students on the digsite can help me again?", line++)
            }
            if (stage >= 5 || getAttribute(player, attributeStudentGreenExam2ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the green top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to speak to the student in the green top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 5 || getAttribute(player, attributeStudentPurpleExam2ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the purple skirt about", line++, true)
                line(player, "the exams.", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to speak to the student in the purple skirt about", line++)
                line(player, "the exams.", line++)
            }
            if (stage >= 5 || getAttribute(player, attributeStudentBrownExam2ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the orange top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 4) {
                line(player, "I need to speak to the student in the orange top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 5) {
                line(player, "I should talk to an examiner to take my second exam. If I", line++, true)
                line(player, "have forgotten anything, I can always ask the students", line++, true)
                line(player, "again.", line++, true)
                line(player, "I have passed my second Earth Science exam.", line++, true)
            } else if (stage >= 4
                    && getAttribute(player, attributeStudentGreenExam2ObtainAnswer, false)
                    && getAttribute(player, attributeStudentPurpleExam2ObtainAnswer, false)
                    && getAttribute(player, attributeStudentBrownExam2ObtainAnswer, false)
            ) {
                line(player, "I should talk to an examiner to take my second exam. If I", line++)
                line(player, "have forgotten anything, I can always ask the students", line++)
                line(player, "again.", line++)
            }

            if (stage >= 6) {
                line(player, "I should research for my third exam. Perhaps the students", line++, true)
                line(player, "can help me again?", line++, true)
            } else if (stage >= 5) {
                line(player, "I should research for my third exam. Perhaps the students", line++)
                line(player, "can help me again?", line++)
            }
            if (stage >= 6 || getAttribute(player, attributeStudentGreenExam3ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the green top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to speak to the student in the green top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 6 || getAttribute(player, attributeStudentPurpleExam3Talked, false)) {
                line(player, "I need to speak to the student in the purple skirt about", line++, true)
                line(player, "the exams.", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to speak to the student in the purple skirt about", line++)
                line(player, "the exams.", line++)
            }
            if (stage >= 6 || getAttribute(player, attributeStudentPurpleExam3ObtainAnswer, false)) {
                line(player, "I need to bring her an Opal.", line++, true) // What a cunt
            } else if (stage >= 5 && getAttribute(player, attributeStudentPurpleExam3Talked, false)) {
                line(player, "I need to bring her an Opal.", line++)
            }
            if (stage >= 6 || getAttribute(player, attributeStudentBrownExam3ObtainAnswer, false)) {
                line(player, "I need to speak to the student in the orange top about the", line++, true)
                line(player, "exams.", line++, true)
            } else if (stage >= 5) {
                line(player, "I need to speak to the student in the orange top about the", line++)
                line(player, "exams.", line++)
            }
            if (stage >= 6) {
                line(player, "I should talk to an examiner to take my third exam. If I", line++, true)
                line(player, "have forgotten anything, I can always ask the students", line++, true)
                line(player, "again.", line++, true)
                line(player, "I have passed my third and final Earth Science exam.", line++, true)
            } else if (stage >= 5
                    && getAttribute(player, attributeStudentPurpleExam3ObtainAnswer, false)
                    && getAttribute(player, attributeStudentGreenExam3ObtainAnswer, false)
                    && getAttribute(player, attributeStudentBrownExam3ObtainAnswer, false)
            ) {
                line(player, "I should talk to an examiner to take my third exam. If I", line++)
                line(player, "have forgotten anything, I can always ask the students", line++)
                line(player, "again.", line++)
            }

            if (stage >= 7) {
                line(player, "I need a find from the digsite to impress the Expert.", line++, true)
            } else if (stage >= 6) {
                line(player, "I need a find from the digsite to impress the Expert.", line++)
            }
            if (stage >= 8) {
                line(player, "I need to take the letter to a workman near a winch.", line++, true)
            } else if (stage >= 7) {
                line(player, "I need to take the letter to a workman near a winch.", line++)
            }
            if (stage >= 9) {
                line(player, "I need to investigate the dig shafts.", line++, true)
                line(player, "I found a secret passageway under the site.", line++, true)
            } else if (stage >= 8) {
                line(player, "I need to !!investigate the dig shafts??.", line++)
            }
            if (stage >= 10) {
                line(player, "I need to find a way to move the rocks blocking the way in", line++, true)
                line(player, "the shaft. Perhaps someone can help me.", line++, true)
                line(player, "I covered the rocks in the cave with an explosive", line++, true)
                line(player, "compound.", line++, true)
            } else if (stage >= 9) {
                line(player, "I need to find a way to move the rocks blocking the way in", line++)
                line(player, "the shaft. Perhaps someone can help me.", line++)
            }
            if (stage >= 11) {
                line(player, "I need to ignite the explosive compound and blow up the", line++, true)
                line(player, "rocks blocking the way.", line++, true)
            } else if (stage >= 10) {
                line(player, "I need to ignite the explosive compound and blow up the", line++)
                line(player, "rocks blocking the way.", line++)
            }
            if (stage >= 12) {
                line(player, "I should look for something interesting in the secret room I", line++, true)
                line(player, "found, and show it to the Expert at the Exam Centre.", line++, true)
                line(player, "The expert was impressed with the Zarosian tablet that I", line++, true)
                line(player, "found, and I also discovered an ancient altar!", line++, true)
                line(player, "I was rewarded for my findings.", line++, true)
                line(player, "My work here is done.", line++, true)
                line(player, "I should also talk to the expert about any other finds.", line++, true)
            } else if (stage >= 11) {
                line(player, "I should look for something interesting in the secret room I", line++)
                line(player, "found, and show it to the !!Expert?? at the Exam Centre.", line++)
            }
            if (stage >= 100) {
                line++
                line(player,"<col=FF0000>QUEST COMPLETE!</col>", line)
            }
        }
    }

    override fun finish(player: Player) {
        var ln = 10
        super.finish(player)
        player.packetDispatch.sendString("You have completed Digsite Quest!", 277, 4)
        // This image is special since it isn't an item, but a standalone model.
        player.packetDispatch.sendModelOnInterface(17343, 277, 5, 0)
        player.packetDispatch.sendAngleOnInterface(277, 5, 1020, 0, 0)

        drawReward(player, "2 Quest Points", ln++)
        drawReward(player, "15,300 Mining XP", ln++)
        drawReward(player, "2,000 Herblore XP", ln++)
        drawReward(player, "2 Gold Bars", ln)

        rewardXP(player, Skills.MINING, 15300.0)
        rewardXP(player, Skills.HERBLORE, 2000.0)
        addItemOrDrop(player, Items.GOLD_BAR_2357, 2)
    }

    override fun newInstance(`object`: Any?): Quest {
        return this
    }
}