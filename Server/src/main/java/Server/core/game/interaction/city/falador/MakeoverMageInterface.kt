/*
package core.game.interaction.city.falador

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.PlayerDetails
import core.game.node.entity.player.link.appearance.Gender
import core.plugin.InitializablePlugin
import core.plugin.Plugin
import core.game.interaction.inter.FEMALE_HAIR_STYLES
import core.game.interaction.inter.MALE_HAIR_STYLES

const val MAKEOVER_COMPONENT_ID = 205

private const val MALE_CHILD_ID = 90
private const val FEMALE_CHILD_ID = 92

@InitializablePlugin
class MakeoverMageInterface : ComponentPlugin(){
    override fun open(player: Player?, component: Component?) {
        player ?: return
        super.open(player, component)
        player.packetDispatch.sendModelOnInterface(114,MAKEOVER_COMPONENT_ID, MALE_CHILD_ID,100)
        player.packetDispatch.sendModelOnInterface(118,MAKEOVER_COMPONENT_ID, FEMALE_CHILD_ID,100)
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        player.appearance.hair.changeLook(MALE_HAIR_STYLES.random())
        player.appearance.sync()
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(MAKEOVER_COMPONENT_ID,this)
        return this
    }
}*/
