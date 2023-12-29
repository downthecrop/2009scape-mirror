package content.global.ame.events.evilbob

import core.ServerConstants
import core.api.getAttribute
import core.api.setAttribute
import core.game.dialogue.DialogueFile
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.tools.END_DIALOGUE
import org.rs09.consts.NPCs

class ServantDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.SERVANT_2481)
            when (stage) {
                0 -> {
                    if (getAttribute(player!!, EvilBobUtils.eventComplete, false)) {
                        playerl(FacialExpression.NEUTRAL, "Evil Bob has fallen asleep, come quickly!").also { stage = 2 }
                    } else if (!getAttribute(player!!, EvilBobUtils.servantHelpDialogueSeen, false)) {
                        playerl(FacialExpression.ANGRY, "I need help, I've been kidnapped by an evil cat!").also { stage = 200 }
                    } else if (getAttribute(player!!, EvilBobUtils.attentiveNewSpot, false)) {
                        npcl(FacialExpression.SAD, "Look... over t-t-there! That fishing spot c-c-contains the f-f-f-fish he likes.").also { stage = 1 }
                    } else npcl(FacialExpression.SAD, "F-f-f-fish... give him the f-f-f-fish he likes and he might f- f-f-fall asleep.").also { stage = END_DIALOGUE }

                }
                1 -> {
                    end()
                    setAttribute(player!!, EvilBobUtils.attentiveNewSpot, false)
                    when (getAttribute(player!!, EvilBobUtils.assignedFishingZone, EvilBobUtils.northFishingZone.toString())) {
                        EvilBobUtils.northFishingZone.toString() -> ServantCutsceneN(player!!).start()
                        EvilBobUtils.southFishingZone.toString() -> ServantCutsceneS(player!!).start()
                        EvilBobUtils.eastFishingZone.toString() -> ServantCutsceneE(player!!).start()
                        EvilBobUtils.westFishingZone.toString() -> ServantCutsceneW(player!!).start()
                    }
                }
                2 -> npcl(FacialExpression.SAD, "Come? Come where?").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY, "Away from this place! To ${ServerConstants.SERVER_NAME} proper!").also { stage++ }
                4 -> npcl(FacialExpression.SAD, "You go, ${player!!.username}, I don't belong there... I belong here, in Scape2009. This is the only place I can ever go...").also { stage++ }
                5 -> options("But I love you!", "Oh alright then.").also { stage++ }
                6 -> when(buttonID) {
                    1 -> playerl(FacialExpression.SAD, "But I love you!").also { stage = 100 }
                    2 -> playerl(FacialExpression.NEUTRAL, "Oh, alright then, I'll be off! See you around!").also { stage = END_DIALOGUE}
                }

                100 -> npcl(FacialExpression.NEUTRAL, "Our love can never be, sweet ${player!!.username}.").also { stage++ }
                101 -> npcl(FacialExpression.NEUTRAL, "Go now! Go, and don't look back!").also { stage = END_DIALOGUE }

                200 -> npcl(FacialExpression.SAD, "Meow! Errr... I c-c-c-can't help you... He'll kill us all!").also { stage++ }
                201 -> playerl(FacialExpression.ANGRY, "Now you listen to me! He's just a little cat! There must be something I can do!").also { stage++ }
                202 -> npcl(FacialExpression.SAD, "F-f-f-fish... give him the f-f-f-fish he likes and he might f- f-f-fall asleep.").also { stage++ }
                203 -> {
                    npcl(FacialExpression.SAD, "Look... over t-t-there! That fishing spot c-c-contains the f-f-f-fish he likes.").also { stage = 1 }
                    setAttribute(player!!, EvilBobUtils.servantHelpDialogueSeen, true)
                }
            }
    }
}
