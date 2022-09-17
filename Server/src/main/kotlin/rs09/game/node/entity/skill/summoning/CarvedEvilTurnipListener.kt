import api.*
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener
import rs09.game.interaction.IntType
import core.tools.RandomFunction

class CarvedEvilTurnipListener : InteractionListener {
    val knife = Items.KNIFE_946
    val evilTurnip = Items.EVIL_TURNIP_12134
    val carvedEvilTurnip = Items.CARVED_EVIL_TURNIP_12153

    override fun defineListeners() {
        onUseWith(IntType.ITEM, evilTurnip, knife) { player, used, with ->
            if(removeItem(player, used.asItem())) {
                sendMessage(player, "You carve a scary face into the evil turnip.")
                sendMessage(player, "Wooo! It's enough to give you nightmares.")
                return@onUseWith addItem(player, carvedEvilTurnip)
            }
            return@onUseWith false
        }
    }
}