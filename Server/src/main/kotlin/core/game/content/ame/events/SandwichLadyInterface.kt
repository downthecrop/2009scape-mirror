package core.game.content.ame.events

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.Items

@Initializable
class SandwichLadyInterface  : ComponentPlugin(){
    val baguette = Items.BAGUETTE_6961
    val triangle_sandwich = Items.TRIANGLE_SANDWICH_6962
    val sandwich = Items.SQUARE_SANDWICH_6965
    val roll = Items.ROLL_6963
    val pie = Items.MEAT_PIE_2327
    val kebab = Items.KEBAB_1971
    val chocobar = Items.CHOCOLATE_BAR_1973

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        var item = Item(0)
        when(button) {
            7 -> {item = Item(baguette)}
            8 -> {item = Item(triangle_sandwich)}
            9 -> {item = Item(sandwich)}
            10 -> {item = Item(roll)}
            11 -> {item = Item(pie)}
            12 -> {item = Item(kebab)}
            13 -> {item = Item(chocobar)}
        }
        if(item.id != 0){
            player?.let { if(it.inventory.add(item)) else GroundItemManager.create(item,it) }
            player?.interfaceManager?.close()
            player?.antiMacroHandler?.event?.terminate()
            return true
        }
        return false
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(297,this)
        return this
    }

}