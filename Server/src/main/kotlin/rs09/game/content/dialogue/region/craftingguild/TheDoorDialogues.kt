package rs09.game.content.dialogue.region.craftingguild

import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 */

class TheDoorDialogues(val it: Int) : DialogueFile() {
    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = NPC(NPCs.MASTER_CRAFTER_805)
        when(it) {
            0 -> when(stage) {
                0 -> npcl(FacialExpression.FRIENDLY, "Welcome to the Guild of Master craftsmen.").also{ stage = END_DIALOGUE }
            }
            1 -> when(stage) {
                0 -> npcl(FacialExpression.ASKING, "Where's your brown apron? You can't come in here unless you're wearing one.").also{ stage++ }
                1 -> player(FacialExpression.SAD, "Err... I haven't got one.").also { stage = END_DIALOGUE }
            }
            2 -> when(stage) {
                0 -> npcl(FacialExpression.NEUTRAL, "Sorry, only experienced crafters are allowed in here. You must be level 40 or above to enter.").also{ stage = END_DIALOGUE }
            }
        }

    }


}