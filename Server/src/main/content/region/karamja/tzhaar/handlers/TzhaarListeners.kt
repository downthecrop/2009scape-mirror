package content.region.karamja.tzhaar.handlers

import core.api.sendNPCDialogueLines
import core.api.teleport
import core.game.activity.ActivityManager
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.NPCs

class TzhaarListeners : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(31284, 9359, 9356), IntType.SCENERY, "enter") { player, node ->
            when (node.id) {
                31284 -> teleport(player, Location.create(2480, 5175, 0))
                9359 -> teleport(player, Location.create(2866, 9571, 0))
                9356 -> {
                    if (player.familiarManager.hasFamiliar()) {
                        sendNPCDialogueLines(player, NPCs.TZHAAR_MEJ_JAL_2617, FacialExpression.ANGRY, false, "No Kimit-Zil in the cave! This is a fight for YOU,",  "not your friends!")
                    } else ActivityManager.start(player, "fight caves", false)
                }
            }
            return@on true
        }

        on(9369, IntType.SCENERY, "pass") { player, _ ->
            ActivityManager.start(player, "fight pits", false)
            return@on true
        }

        on(31292, IntType.SCENERY, "go-through") { _, _ ->
            return@on false
        }
    }
}