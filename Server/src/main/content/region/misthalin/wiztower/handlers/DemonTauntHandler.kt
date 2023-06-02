package content.region.misthalin.wiztower.handlers

import core.api.*
import core.game.node.entity.player.link.emote.Emotes
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

/**
 * Handles taunting of the demon in the wizard's tower
 * @author Ceikry
 * @author vddCore
 */
class DemonTauntHandler : InteractionListener {

    override fun defineListeners() {
        on(Scenery.RAILING_37668, IntType.SCENERY, "taunt-through") { player, _ ->
            val demon = findLocalNPC(player, NPCs.LESSER_DEMON_82) ?: return@on true
            forceWalk(demon, player.location, "smart")
            face(player, demon, 3)
            sendMessage(player, "You taunt the demon, making it growl.")
            sendChat(demon, "Graaaagh!")
            face(demon, player, 3)
            emote(player, Emotes.RASPBERRY)
            return@on true
        }
    }
}