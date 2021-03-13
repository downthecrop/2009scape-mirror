package rs09.game.content.activity.gnomecooking.cocktails

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles the mix-cocktail option for the cocktail shaker
 * @author Ceikry
 */
@Initializable
class CocktailShakerHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(2025).handlers["option:mix-cocktail"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        player.interfaceManager.open(Component(436)) //Gnome cocktail interface
        return true
    }

}