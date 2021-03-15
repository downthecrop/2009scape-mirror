package rs09.game.content.activity.mta

import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class MTAListeners : InteractionListener() {
    override fun defineListeners() {
        on(NPCs.MAZE_GUARDIAN_3102,NPC,"talk-to"){player,node ->
            player.dialogueInterpreter.open(node.id, node)
            return@on true
        }
    }
}