package content.global.ame.events.strangeplant

import core.api.addItemOrDrop
import core.api.getAttribute
import core.api.sendMessage
import core.api.setAttribute
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.timer.impl.AntiMacro
import org.rs09.consts.Items
import org.rs09.consts.NPCs

class StrangePlantListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.STRANGE_PLANT_407, IntType.NPC, "pick") { player, node ->
            if (AntiMacro.getEventNpc(player) != node) {
                sendMessage(player, "This isn't your strange plant.")
                return@on true
            }

            if (!getAttribute(node.asNpc(), "fruit-grown", false)) {
                sendMessage(player, "The fruit isn't ready to be picked.")
                return@on true
            }

            if (!getAttribute(node.asNpc(), "fruit-picked", false)) {
                setAttribute(node.asNpc(), "fruit-picked", true)
                addItemOrDrop(player, Items.STRANGE_FRUIT_464)
            }
            return@on true
        }
    }
}