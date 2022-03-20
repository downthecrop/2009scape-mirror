package rs09.game.content.quest.members.deathplateau

import api.getScenery
import api.sendDialogue
import core.game.content.dialogue.FacialExpression
import core.game.content.global.action.DoorActionHandler
import core.game.node.entity.npc.NPC
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile

/**
 * @author qmqz
 */

class Death_Plateau_Door_Dialogue_File() : DialogueFile() {
    override fun handle(interfaceId: Int, buttonId: Int) {
        npc = NPC(NPCs.HAROLD_1078)
        when (stage) {
            0 -> sendDialogue(player!!,"You knock on the door.").also { stage++ }
            1 -> npc(FacialExpression.FRIENDLY, "Come on in!").also { stage++ }
            2 -> {
                end()
                DoorActionHandler.handleAutowalkDoor(player, getScenery(2906, 3543, 1))
            }
        }

    }
}

