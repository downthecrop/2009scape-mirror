package content.global.handlers.item

import core.api.*
import content.global.skill.slayer.Tasks
import org.rs09.consts.Items
import core.game.dialogue.DialogueFile
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.interaction.InteractionListener
import core.game.interaction.IntType
import core.tools.END_DIALOGUE

class EnchantedGemListener : InteractionListener {
    override fun defineListeners() {
        on(Items.ENCHANTED_GEM_4155, IntType.ITEM, "activate") { player, _ ->
            openDialogue(player, EnchantedGemDialogue())
            return@on true
        }
    }
}

class EnchantedGemDialogue() : DialogueFile() {
    var firstRun = true
    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = getSlayerMaster(player!!)
        when(stage) {
            0 -> npcl(core.game.dialogue.FacialExpression.FRIENDLY, "Hello there ${player!!.username}, what can I help you with?").also { stage++ }
            1 -> showTopics(
                Topic(core.game.dialogue.FacialExpression.ASKING, "How am I doing so far?", 100),
                Topic(core.game.dialogue.FacialExpression.HALF_ASKING, "Who are you?", 200),
                Topic(core.game.dialogue.FacialExpression.HALF_ASKING, "Where are you?", 300),
                Topic(core.game.dialogue.FacialExpression.FRIENDLY, "Got any tips for me?", 400),
                IfTopic(core.game.dialogue.FacialExpression.FRIENDLY, "Nothing really.", END_DIALOGUE, firstRun),
                IfTopic(core.game.dialogue.FacialExpression.HAPPY, "That's all thanks.", END_DIALOGUE, !firstRun)
            )
            100 -> {
                firstRun = false
                if(!hasSlayerTask(player!!)) {
                    npcl(core.game.dialogue.FacialExpression.HALF_THINKING, "You need something new to hunt. Come and see me when you can and I'll give you a new task.").also { stage = 1 }
                } else {
                    if(getSlayerTask(player!!) == Tasks.JAD) {
                        npcl(core.game.dialogue.FacialExpression.FRIENDLY, "You're currently assigned to kill TzTok-Jad!")
                    } else {
                        npcl(core.game.dialogue.FacialExpression.FRIENDLY, "You're currently assigned to kill ${getSlayerTaskName(player!!)}s; only ${getSlayerTaskKillsRemaining(player!!)} more to go.")
                    }
                    setVarp(player!!, 2502, getSlayerTaskFlags(player!!) shr 4)
                    stage = 1
                }
            }
            200 -> {
                firstRun = false
                npcl(core.game.dialogue.FacialExpression.HAPPY, "My name's ${getSlayerMaster(player!!).name}, I'm the Slayer Master best able to train you.").also { stage = 1 }
            }
            300 -> {
                firstRun = false
                npcl(core.game.dialogue.FacialExpression.HAPPY, "You'll find me in ${getSlayerMasterLocation(player!!)}, I'll be here when you need a new task.").also { stage = 1 }
            }
            400 -> {
                firstRun = false
                npc(core.game.dialogue.FacialExpression.FRIENDLY, *getSlayerTip(player!!))
                stage++
            }
            401 -> player(core.game.dialogue.FacialExpression.HAPPY, "Great, thanks!").also { stage = 1 }
        }
    }
}
