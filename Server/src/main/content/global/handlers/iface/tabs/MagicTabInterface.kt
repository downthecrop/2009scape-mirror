package content.global.handlers.iface.tabs

import content.global.skill.magic.SpellListener
import content.global.skill.magic.SpellListeners
import core.api.getAttribute
import core.game.interaction.InterfaceListener
import core.game.node.entity.combat.spell.MagicSpell
import core.game.node.entity.player.link.SpellBookManager.SpellBook
import core.game.world.GameWorld

class MagicTabInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        SpellBook.values().forEach {
            on(it.interfaceId) { player, _, _, buttonID, _, _ ->
                if (GameWorld.ticks < getAttribute(player, "magic:delay", -1)) return@on true

                SpellListeners.run(buttonID, SpellListener.NONE, it.name.lowercase(), player)

                return@on MagicSpell.castSpell(player, it, buttonID, player)
            }
        }
    }
}