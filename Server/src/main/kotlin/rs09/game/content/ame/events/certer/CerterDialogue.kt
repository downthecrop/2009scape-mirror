package rs09.game.content.ame.events.certer

import api.addItemOrDrop
import core.game.component.Component
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.link.emote.Emotes
import rs09.game.content.ame.RandomEventManager
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class CerterDialogue(val initial: Boolean) : DialogueFile() {
    val CERTER_INTERFACE = 184
    override fun handle(componentID: Int, buttonID: Int) {
        if (initial && !player!!.getAttribute("certer:reward", false)) {
            when (stage) {
                0 -> npc("Ah, hello, ${player!!.username.capitalize()}. Could you", "please help me identify this?").also { stage++ }
                1 -> {
                    end()
                    player!!.interfaceManager.open(Component(CERTER_INTERFACE))
                }
            }
        } else {
            player!!.setAttribute("random:pause", true)
            val isCorrect = player!!.getAttribute("certer:correct", false)
            val receivedReward = player!!.getAttribute("certer:reward", false)
            if (receivedReward == true) {
                stage = 1
            }
            when (stage) {
                0 -> if (!isCorrect) {
                    npc("Sorry, I don't think so.").also {
                        player!!.setAttribute("certer:reward", true)
                        stage = END_DIALOGUE
                        RandomEventManager.getInstance(player!!)?.event?.terminate()
                    }
                } else {
                    npc("Thank you, I hope you like your present. I must be", "leaving now though.").also {
                        player!!.setAttribute("certer:reward", true)
                        stage = END_DIALOGUE
                        RandomEventManager.getInstance(player!!)!!.event!!.loot!!.roll().forEach { addItemOrDrop(player!!, it.id, it.amount) }
                    }
                }
            }
        }
    }

    override fun end() {
        super.end()
        if (player!!.getAttribute("certer:reward", false)) {
            // Remove movement pulse to stop following player
            npc!!.pulseManager.clear(PulseType.STANDARD)
            // Wave goodbye
            npc!!.animate(Emotes.WAVE.animation)
            // Terminate the event
            RandomEventManager.getInstance(player!!)?.event?.terminate()
        } else {
            player!!.setAttribute("random:pause", false)
        }
    }
}