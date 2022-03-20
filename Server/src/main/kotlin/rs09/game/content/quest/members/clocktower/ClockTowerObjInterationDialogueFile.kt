package rs09.game.content.quest.members.clocktower

import api.*
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.tools.END_DIALOGUE

/**
 * @author qmqz
 */

class ClockTowerObjInterationDialogueFile(val it: Int) : DialogueFile() {

    override fun handle(interfaceId: Int, buttonId: Int) {
        when (it) {
            0 -> when (stage) {
                0 -> sendDialogue(player!!, "The death throes of the rats seem to have shaken the door loose of its hinges. You pick it up and go through.").also { stage++ }
                1 -> {
                    end()
                    //forceWalk(player!!, location(2578, 9656, 0), "clip")
                    //teleport(player!!, location(2578, 9656, 0), TeleportManager.TeleportType.INSTANT)
                }
            }
        }
    }
}