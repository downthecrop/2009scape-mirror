package content.region.kandarin.ardougne.quest.arena


import core.api.lockInteractions
import core.api.questStage
import core.api.sendPlayerDialogue
import core.api.setQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class JeremyServilRescuedDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {

        val questName = "Fight Arena"
        val questStage = questStage(player!!, questName)
        npc = NPC(NPCs.JEREMY_SERVIL_266)

        when (questStage) {

            75 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Come on Jeremy, we have to get out of here.").also { stage = 1 }
                    1 -> npcl("Thank you for saving my father.").also { stage = END_DIALOGUE }
                }
            }

            85 -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Don't worry, I'll get us out of here.").also { stage = 1 }
                    1 -> npcl("Thanks traveller. I'm sorry that you too are now a subject of this arena.").also { stage = END_DIALOGUE }
                }
            }

            97 -> {
                when (stage) {
                    0 -> lockInteractions(player!!,1).also{ playerl(FacialExpression.FRIENDLY, "You and you father can return to Lady Servil.")}.also { stage = 1 }
                    1 -> npcl(FacialExpression.FRIENDLY, "Thank you, we are truly indebted to you.").also { stage = 2 }
                    2 -> {
                        end()
                        setQuestStage(player!!, FightArena.FightArenaQuest, 99)
                    }
                }
            }

            98 -> {
                when (stage) {
                    0 -> sendPlayerDialogue(player!!, "Khazard is dead, you and you father can return to Lady Servil.").also { stage = 1 }
                    1 -> npcl(FacialExpression.FRIENDLY, "Thank you, we are truly indebted to you.").also { stage = 2 }
                    2 -> {
                        end()
                        setQuestStage(player!!, FightArena.FightArenaQuest, 99)
                    }
                }
            }
        }
    }
}