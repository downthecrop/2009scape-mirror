package content.region.misc.keldagrim.dialogue

import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class FactoryWorkerDialogue1(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "I'm sorry, I'm looking for the blast furnace?").also { stage++ }
            1 -> npcl(FacialExpression.OLD_ANGRY1, "Do I look like a guide to you?").also { stage++ }
            2 -> playerl(FacialExpression.FRIENDLY, "No, you look like a hard-working dwarf, but can you please tell me where the blast furnace is?").also { stage++ }
            3 -> npcl(FacialExpression.OLD_DEFAULT, "Alright, just head down the stairs, it's easy to find.").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "Thanks.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FactoryWorkerDialogue1(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_WORKER_2172)
    }
}

@Initializable
class FactoryWorkerDialogue2(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Are you okay?").also { stage++ }
            1 -> npcl(FacialExpression.OLD_ANGRY1, "Don't I look okay?").also { stage++ }
            2 -> playerl(FacialExpression.FRIENDLY, "If you were any shorter you wouldn't exist.").also { stage++ }
            3 -> npcl(FacialExpression.OLD_ANGRY1, "Very funny, human.").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FactoryWorkerDialogue2(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_WORKER_2173)
    }
}

@Initializable
class FactoryWorkerDialogue3(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "What are you dwarves doing in this factory?").also { stage++ }
            1 -> npcl(FacialExpression.OLD_ANGRY1, "Working of course, can't you see that?").also { stage++ }
            2 -> playerl(FacialExpression.FRIENDLY, "But working on what?").also { stage++ }
            3 -> npcl(FacialExpression.OLD_DEFAULT, "Refining the ore that is being brought into the factory, of course.").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "And what does that mean?").also { stage++ }
            5 -> npcl(FacialExpression.OLD_ANGRY1, "It means you should stop asking so many questions and get back to work!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FactoryWorkerDialogue3(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_WORKER_2174)
    }
}

@Initializable
class FactoryWorkerDialogue4(player: Player? = null) : core.game.dialogue.DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> playerl(FacialExpression.FRIENDLY, "Who owns this factory?").also { stage++ }
            1 -> npcl(FacialExpression.OLD_DEFAULT, "The Consortium does and that's all you need to know.").also { stage++ }
            2 -> playerl(FacialExpression.FRIENDLY, "But what company? I thought there were all these different companies?").also { stage++ }
            3 -> npcl(FacialExpression.OLD_DEFAULT, "Oh yes, all the major companies own this plant. It's too vital to be in the hands of one company alone.").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "And what exactly are you doing here?").also { stage++ }
            5 -> npcl(FacialExpression.OLD_ANGRY1, "I tire of these questions. Let me get back to work!").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return FactoryWorkerDialogue4(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FACTORY_WORKER_2175)
    }
}