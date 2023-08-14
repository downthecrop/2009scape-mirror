package content.global.ame.events.genie

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.dialogue.DialogueFile
import core.game.system.timer.impl.AntiMacro
import core.tools.END_DIALOGUE

class GenieDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        val assigned = player!!.getAttribute("genie:item",0)
        npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Ah, so you are there, ${player!!.name.capitalize()}. I'm so glad you summoned me. Please take this lamp and make your wish.")
		addItemOrDrop(player!!, assigned)
        AntiMacro.terminateEventNpc(player!!)
        stage = END_DIALOGUE
    }
}