package rs09.game.content.ame.events.certer

import api.*
import core.game.component.Component
import core.game.node.item.GroundItemManager
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

class CerterDialogue(val initial: Boolean) : DialogueFile() {
    val CERTER_INTERFACE = 184
    override fun handle(componentID: Int, buttonID: Int) {
        if(initial){
            when(stage){
                0 -> npc("Ah, hello, ${player!!.username.capitalize()}. Could you","please help me identify this?").also { stage++ }
                1 -> {
                    end()
                    player!!.interfaceManager.open(Component(CERTER_INTERFACE))
                }
            }
        } else {
            val isCorrect = player!!.getAttribute("certer:correct",false)
            when(stage){
                0 -> if(!isCorrect) npc("Sorry, I don't think so.").also { stage = END_DIALOGUE; player!!.antiMacroHandler.event?.terminate() } else npc("Oh yes! That's right.").also { stage++ }
                1 -> {
                    npc("Please, take this as a thanks.")
                    player!!.antiMacroHandler.event?.loot!!.roll().forEach { addItemOrDrop(player!!, it.id, it.amount) }
                    player!!.antiMacroHandler.event?.terminate()
                    stage = END_DIALOGUE
                }
            }
        }
    }
}