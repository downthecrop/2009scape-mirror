package rs09.game.interaction.`object`

import api.*
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.emote.Emotes
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType

/**
 * Handles taunting of the demon in the wizard's tower
 * @author Ceikry
 * @author vddCore
 */
class DemonTauntHandler : InteractionListener {

    override fun defineListeners() {
        on(Scenery.RAILING_37668, IntType.SCENERY, "taunt-through") { player, _ ->
            val demon = findLocalNPC(player, NPCs.LESSER_DEMON_82) ?: return@on true

            sendMessage(player, "You taunt the demon, making it growl.")
            sendChat(demon, "Graaagh!")
            face(demon, player, 3)
            emote(player, Emotes.RASPBERRY)
            return@on true
        }
    }
}