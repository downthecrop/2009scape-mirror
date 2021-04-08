package rs09.game.content.activity.communityevents

import core.game.content.global.action.DigAction
import core.game.content.global.action.DigSpadeHandler
import core.game.content.ttrail.MapClueScroll
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.*
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items
import rs09.game.interaction.InteractionListener

class SuperEggEaster2020 : InteractionListener() {
    override fun defineListeners() {
        onDig(Location.create(2188, 3281, 0)){player ->
            val hasKey = player.getAttribute("easter2020:key",false)
            if(!hasKey){
                player.inventory.add(Item(Items.KEY_11039))
                player.dialogueInterpreter.sendDialogue("You dig and find an ancient key!")
                player.setAttribute("/save:easter2020:key",true)
            } else {
                player.sendMessage("You dig and find nothing.")
            }
        }
    }
}


