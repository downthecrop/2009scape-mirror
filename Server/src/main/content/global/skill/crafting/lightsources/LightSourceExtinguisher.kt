package content.global.skill.crafting.lightsources

import core.api.log
import core.cache.def.impl.ItemDefinition
import core.game.container.Container
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
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

        player.inventory.replace(node.asItem(), Item(lightSource.fullID))
        return true
    }

    fun Container.replace(item: Item, with: Item){
		if(remove(item)) {
			add(with)
		}
    }
}
