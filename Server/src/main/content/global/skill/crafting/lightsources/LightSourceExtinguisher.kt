package content.global.skill.crafting.lightsources

import content.data.LightSource
import core.api.log
import core.api.*
import core.cache.def.impl.ItemDefinition
import core.game.container.Container
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.tools.SystemLogger
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Log


/**
 * Extinguishes light sources
 * @author Ceikry
 */
@Initializable
class LightSourceExtinguisher : OptionHandler(){
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.setOptionHandler("extinguish",this)
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return false
        player ?: return false

        val lightSource = LightSources.forId(node.id)

        lightSource ?: return false.also { log(this::class.java, Log.WARN,  "UNHANDLED EXTINGUISH OPTION: ID = ${node.id}") }

        // For Temple of Ikov - if you are in the dark basement, do not let light source extinguish.
        if(player.location.isInRegion(10648)) {
            sendMessage(player, "Extinguishing the " + LightSource.getActiveLightSource(player).product.name.lowercase() + " would leave you without a light source.")
            return true
        }

        player.inventory.replace(node.asItem(), Item(lightSource.fullID))
        return true
    }

    fun Container.replace(item: Item, with: Item){
		if(remove(item)) {
			add(with)
		}
    }
}
