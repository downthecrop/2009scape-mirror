package content.region.kandarin.quest.waterfall

import core.game.world.map.Location
import org.rs09.consts.NPCs
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

class WaterfallListeners : InteractionListener {

    val HUDON = NPCs.HUDON_305
    override fun defineListeners() {
        on(HUDON, IntType.NPC, "talk-to"){ player, node ->
            player.dialogueInterpreter.open(HUDON,node.asNpc())
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.NPC,HUDON){ _, _ ->
            return@setDest Location.create(2512, 3481, 0)
        }
    }
}