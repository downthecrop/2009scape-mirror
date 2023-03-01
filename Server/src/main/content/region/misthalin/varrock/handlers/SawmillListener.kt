package content.region.misthalin.varrock.handlers

import core.game.component.Component
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.NPCs

class SawmillListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.SAWMILL_OPERATOR_4250, IntType.NPC, "talk-to") { player, node ->
            player.dialogueInterpreter.open(4250, node)
            return@on true
        }

        on(NPCs.SAWMILL_OPERATOR_4250, IntType.NPC, "buy-plank") { player, _ ->
            player.interfaceManager.open( Component(403))
            return@on true
        }

        on(NPCs.SAWMILL_OPERATOR_4250, IntType.NPC, "trade") { player, node ->
            node.asNpc().openShop(player)
            return@on true
        }

        setDest(IntType.NPC, intArrayOf(NPCs.SAWMILL_OPERATOR_4250), "talk-to", "buy-plank", "trade") { _, _ ->
            return@setDest Location.create(3302, 3491 , 0)
        }

    }
}