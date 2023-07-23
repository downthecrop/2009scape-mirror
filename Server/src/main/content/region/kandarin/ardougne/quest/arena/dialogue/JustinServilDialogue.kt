package content.region.kandarin.ardougne.quest.arena.dialogue

import content.region.kandarin.ardougne.quest.arena.FightArena
import core.api.face
import core.api.findNPC
import core.api.getQuestStage
import core.api.sendNPCDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class JustinServilDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.JUSTIN_SERVIL_267)
        when (getQuestStage(player!!, FightArena.FightArenaQuest)) {

            in 1..68 -> when (stage) {
                0 -> playerl(FacialExpression.FRIENDLY, "Hello.").also { stage++ }
                1 -> {
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    npcl(FacialExpression.FRIENDLY, "You've incurred the anger of Khazard himself. He won't let any of us go easily.").also { stage = END_DIALOGUE }
                }

            }

            in 69..71 -> when (stage) {
                0 -> {
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    playerl(FacialExpression.FRIENDLY, "Lady Servil sent me to rescue you and your son. Come on, we have to get out of here.").also { stage++ }
                }
                1 -> npcl(FacialExpression.FRIENDLY, "I'm too old to fight. I'm afraid you'll have to kill that ogre by yourself.").also { stage = END_DIALOGUE }
            }

            72 -> when (stage) {
                0 -> {
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    playerl(FacialExpression.FRIENDLY, "Are you alright").also { stage++ }
                }
                1 -> npcl(FacialExpression.FRIENDLY, "You saved my life and my son's. I am eternally in your debt brave traveller.").also { stage = END_DIALOGUE }
            }

            in 73..89 -> when (stage) {
                0 -> {
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    playerl(FacialExpression.FRIENDLY, "Don't worry, I'll get us out of here.").also { stage++ }
                }
                1 -> npcl(FacialExpression.NEUTRAL, "You've incurred the anger of Khazard yourself now. He won't let any of us go easily.").also { stage = END_DIALOGUE }
            }

            in 90..99 -> when (stage) {
                0 -> {
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    npcl(FacialExpression.NEUTRAL, " You saved my life and my son's, I am eternally in your debt brave taveller.").also { stage = END_DIALOGUE }
                }
            }

            100 -> when (stage) {
                0 -> {
                    face(findNPC(NPCs.JUSTIN_SERVIL_267)!!, player!!, 1)
                    npcl(FacialExpression.FRIENDLY, "Thank you again, ${player!!.username}. You have saved me and my boy from an awful fate.").also { stage++ }
                }
                1 -> sendNPCDialogue(player!!, NPCs.JEREMY_SERVIL_266, "Yeah, you were ace. I wanna be jus' like you.").also { stage++ }
                2 -> npcl(FacialExpression.FRIENDLY, "If you do well in your studies and work hard, maybe you will be.").also { stage++ }
                3 -> sendNPCDialogue(player!!, NPCs.JEREMY_SERVIL_266, "Daaad, studying's so boooooooring! I wanna hit things.").also { stage++ }
                4 -> npcl(FacialExpression.FRIENDLY, "I'm sure ${player!!.username} studied hard when he was younger.").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "That's right, son, you should do as your father says. Anyway, I should go now, have a safe journey home. And you, young lad, work hard and stay out of trouble - at least until you're a little older.").also { stage = END_DIALOGUE }
            }
        }
    }
}