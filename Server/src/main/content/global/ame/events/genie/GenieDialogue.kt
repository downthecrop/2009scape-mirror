package content.global.ame.events.genie

import core.api.*
import core.game.dialogue.FacialExpression
import core.game.dialogue.DialogueFile
import core.game.system.timer.impl.AntiMacro
import org.rs09.consts.Items

class GenieDialogue() : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        when (stage) {
            0 -> npcl(FacialExpression.NEUTRAL, "Ah, so you are there, ${player!!.username.capitalize()}. I'm so glad you summoned me. Please take this lamp and make your wish."). also { stage++ }
            1 -> {
                end()
                addItemOrDrop(player!!, Items.LAMP_2528)
                AntiMacro.terminateEventNpc(player!!)
            }
        }
    }
}
