package content.global.skill.construction.decoration.bedroom

import core.cache.def.impl.SceneryDefinition
import core.game.component.Component
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

@Initializable
class WardrobeHandler : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(13155).handlers["option:change-clothes"] = this
        SceneryDefinition.forId(13156).handlers["option:change-clothes"] = this
        SceneryDefinition.forId(13161).handlers["option:change-clothes"] = this
        return this
    }

    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        player ?: return false
        node ?: return false
        if(player.appearance.isMale){
            player.interfaceManager.open(Component(591))
        } else {
            player.interfaceManager.open(Component(594))
        }
        return true
    }

}