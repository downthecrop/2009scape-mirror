package rs09.game.interaction.region.rellekka

import api.ContentAPI
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.NPCs
import rs09.game.interaction.InteractionListener

class NeitiznotListeners : InteractionListener() {
    override fun defineListeners() {
        val zone = object : MapZone("Yakzone", true){
            override fun handleUseWith(player: Player, used: Item?, with: Node?): Boolean {
                if(with is NPC && with.id == NPCs.YAK_5529){
                    ContentAPI.sendMessage(player, "The cow doesn't want that.")
                    return true
                }
                return false
            }
        }

        ContentAPI.registerMapZone(zone, ZoneBorders(2313,3786,2331,3802))
        ContentAPI.addClimbDest(Location.create(2363, 3799, 0), Location.create(2364, 3799, 2))
        ContentAPI.addClimbDest(Location.create(2363, 3799, 2), Location.create(2362, 3799, 0))
    }
}