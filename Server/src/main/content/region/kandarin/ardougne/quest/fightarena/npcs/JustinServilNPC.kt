package content.region.kandarin.ardougne.quest.fightarena.npcs

import core.api.questStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class JustinServilDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {

        val questName = "Fight Arena"
        val questStage = questStage(player!!, questName)

        npc = NPC(NPCs.JUSTIN_SERVIL_267)

        when {

            // Talking to Justin in the arena during the scorpion fight.
            (questStage == 88) -> {
                when (stage) {
                    0 -> playerl(FacialExpression.FRIENDLY, "Don't worry, I'll get us out of here.").also { stage = 1 }
                    1 -> npcl(FacialExpression.NEUTRAL, "You've incurred the anger of Khazard yourself now. He won't let any of us go easily.").also { stage = END_DIALOGUE }
                }
            }

            // Talking to Justin after saving his life.
            (questStage == 100) -> {
                when (stage) {
                    0 -> npcl(FacialExpression.NEUTRAL, " You saved my life and my son's, I am eternally in your debt brave taveller.").also { stage = END_DIALOGUE }
                }
            }
        }
    }
}