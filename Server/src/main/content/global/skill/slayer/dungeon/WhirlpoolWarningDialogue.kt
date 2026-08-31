package content.global.skill.slayer.dungeon

import content.region.kandarin.quest.barbariantraining.BarbarianTraining
import core.api.*
import core.game.dialogue.DialogueFile
import core.tools.START_DIALOGUE

/**
 * Dialogue to warn the player the first time they try to enter the Ancient Cavern.
 * @author Bishop
 */

class WhirlpoolWarningDialogue(private val onConfirm: Runnable) : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            START_DIALOGUE -> sendDialogueLines(player!!, "You realise that you are about to attempt entry an unknown and", "potentially hideously dangerous place, with no known exits. Doughty", "barbarian warriors have entered, never to return.").also { stage++ }
            1 -> sendDialogueOptions(player!!, "Are you really sure you wish to enter?", "Yes, I wish to enter the unknown.", "No, I fear the unknown perils.").also { stage++ }
            2 -> when (buttonID) {
                1 -> {
                    setAttribute(player!!, BarbarianTraining.attributeWhirlpool, true)
                    end()
                    onConfirm.run()
                }
                2 -> {
                    sendMessage(player!!, "You decide that discretion is the better part of valour.")
                    end()
                }
            }
        }
    }
}