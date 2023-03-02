package content.region.kandarin.ardougne.quest.arena

import content.region.kandarin.ardougne.quest.arena.FightArena.Companion.FightArenaQuest
import core.api.questStage
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class LadyServilDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val questName = "Fight Arena"
        val questStage = questStage(player!!, questName)
        npc = NPC(NPCs.LADY_SERVIL_264)

        when {

            (questStage == 0) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hi there, looks like you're in some trouble.").also { stage++ }
                    1 -> npcl(FacialExpression.SAD, "Oh I wish this broken cart was my only problem. *sob* I've got to find my family.. **sob**").also { stage++ }
                    2 -> options("I hope you can. good luck.", "Can I help you?").also { stage++ }
                    3 -> when (buttonID) {
                        1 -> playerl(FacialExpression.NEUTRAL, "I hope you can, good luck.").also { stage = END_DIALOGUE }
                        2 -> playerl(FacialExpression.FRIENDLY, "Can I help you?").also { stage++ }
                    }
                    4 -> npcl(FacialExpression.SAD, "Would you? Please?").also { stage++ }
                    5 -> npcl(FacialExpression.SAD, "I'm Lady Servil, and my husband is Sir Servil. We were travelling north together with our son Jeremy when we were ambushed by General Khazard's men.").also { stage++ }
                    6 -> playerl(FacialExpression.HALF_ASKING, "General Khazard?").also { stage++ }
                    7 -> npcl(FacialExpression.SAD, "He's been after me even since I declined his hand in marriage.").also { stage++ }
                    8 -> npcl(FacialExpression.SAD, "Now he's kidnapped my husband and son to fight in his battle arena to the south of here. I hate to think what he'll do to them. He's a sick and twisted man.").also { stage++ }
                    9 -> playerl(FacialExpression.FRIENDLY, "I'll try my best to return your family.").also { stage++ }
                    10 -> npcl(FacialExpression.SAD, "Please do. My family is wealthy and can reward you handsomely. I'll be waiting here for you.").also { stage = END_DIALOGUE }.also { setQuestStage(player!!, FightArenaQuest, 10) }
                }
            }

            (questStage == 10) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello Lady Servil.").also { stage++ }
                    1 -> npcl(FacialExpression.SAD, "Brave traveller, please... bring back my family.").also { stage = END_DIALOGUE }
                }
            }

            (questStage == 20) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello Lady Servil.").also { stage++ }
                    1 -> npcl(FacialExpression.WORRIED, "Have you had any luck with freeing my family?").also { stage++ }
                    2 -> playerl(FacialExpression.HAPPY, "I've managed to get a guard's uniform, hopefully I can infiltrate the arena.").also { stage++ }
                    3 -> npcl(FacialExpression.SAD, "Please hurry.").also { stage = END_DIALOGUE }
                }
            }

            (questStage == 30) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Lady Servil, I have managed to infiltrate General Khazard's arena.").also { stage++ }
                    1 -> npcl(FacialExpression.ASKING, "And my family?").also { stage++ }
                    2 -> playerl(FacialExpression.NEUTRAL, "I'm working on it.").also { stage++ }
                    3 -> npcl(FacialExpression.SAD, "Please hurry.").also { stage = END_DIALOGUE }
                }
            }

            (questStage == 70) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Lady Servil. I freed your son, however he has returned to the arena to help your husband.").also { stage++ }
                    1 -> npcl(FacialExpression.WORRIED, "Oh no, they won't stand a chance. Please go back and help.").also { stage = END_DIALOGUE }
                }
            }

            (questStage == 99) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Lady Servil.").also { stage++ }
                    1 -> npcl(FacialExpression.AMAZED, "You're alive, I thought Khazard's men had taken you.").also { stage++ }
                    2 -> npcl(FacialExpression.HAPPY, "My son and husband are safe and recovering at home. Without you they would certainly be dead. I am truly grateful for your service.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "All I can offer in return is material wealth. Please take these coins as a sign of my gratitude.").also {
                        player!!.questRepository.getQuest("Fight Arena").finish(player).also { setQuestStage(player!!, FightArenaQuest, 100) }
                    }
                }
            }

            (questStage == 100) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Hello Servil.").also { stage++ }
                    1 -> npcl(FacialExpression.FRIENDLY, "Oh hello my dear. My husband and son are resting while I wait for the cart fixer.").also { stage++ }
                    2 -> playerl(FacialExpression.FRIENDLY, "I hope he's not too long.").also { stage++ }
                    3 -> npcl(FacialExpression.FRIENDLY, "Thanks again for everything.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}