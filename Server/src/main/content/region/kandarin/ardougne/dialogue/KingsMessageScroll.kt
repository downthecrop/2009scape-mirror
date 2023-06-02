package content.region.kandarin.ardougne.dialogue

import core.api.openInterface
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.Components
import org.rs09.consts.Items

class KingsMessageScroll : InteractionListener {
    // Receive from the King's messenger some time after completing the Underground Pass quest.
    override fun defineListeners() {
        val kingsmessage = Components.KINGS_LETTER_V2_463
        val kingsscroll = Items.KINGS_MESSAGE_3206
        on(kingsscroll, IntType.ITEM, "read") { player, _ ->
            openInterface(player, kingsmessage)
            player.packetDispatch.sendString("Squire ${player.username}<br>You are needed to serve the Kingdom of Kandarin. At last our magi have reopened the well of voyage. We must discuss your next commission.", kingsmessage, 1)
            player.packetDispatch.sendString("<col=8A0808>His Majesty King Lathas</col>", kingsmessage, 2)
            return@on true
        }
    }
}