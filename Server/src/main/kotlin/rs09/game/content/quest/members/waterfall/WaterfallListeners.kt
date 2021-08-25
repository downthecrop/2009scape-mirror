package rs09.game.content.quest.members.waterfall

import core.game.world.map.Location
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class WaterfallListeners : InteractionListener(){

    val HUDON = NPCs.HUDON_305
    override fun defineListeners() {
        on(HUDON,NPC,"talk-to"){player, node ->
            player.dialogueInterpreter.open(HUDON,node.asNpc())
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(NPC,HUDON){_,_ ->
            return@setDest Location.create(2512, 3481, 0)
        }
    }
}