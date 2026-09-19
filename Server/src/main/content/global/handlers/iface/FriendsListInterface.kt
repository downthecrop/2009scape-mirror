package content.global.handlers.iface

import core.ServerConstants
import core.api.setInterfaceText
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import org.rs09.consts.Components

/**
 * Makes sure the server name is always applied to the Friends List interface.
 */

class FriendsListInterface : Component(Components.FRIENDS2_550) {
    companion object {
        private const val TITLE_ELEMENT = 3
    }

    override fun open(player: Player) {
        super.open(player)
        setInterfaceText(player, "Friends List - ${ServerConstants.SERVER_NAME} ${GameWorld.settings!!.worldId}", Components.FRIENDS2_550, TITLE_ELEMENT)
    }
}