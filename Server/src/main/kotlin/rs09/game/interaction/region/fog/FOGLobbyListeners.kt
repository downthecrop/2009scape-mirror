package rs09.game.interaction.region.fog

import api.ContentAPI
import core.game.world.map.Location
import rs09.game.content.activity.fog.FOGWaitingArea
import rs09.game.interaction.InteractionListener

class FOGLobbyListeners : InteractionListener(){

    val FOG_WAITING_ROOM = Location.create(1653, 5600, 0)
    val FOG_ARENA = Location.create(1663, 5695, 0)

    val WAITING_ROOM_ENTRANCE = 30224

    override fun defineListeners() {
        on(WAITING_ROOM_ENTRANCE, SCENERY, "go-through"){player, node ->
            if(FOGWaitingArea.isFull()){
                ContentAPI.sendMessage(player, "The game is currently full. Please wait.")
                return@on true
            }
            ContentAPI.teleport(player, FOG_WAITING_ROOM)
            FOGWaitingArea.register(player)
            return@on true
        }
    }

}