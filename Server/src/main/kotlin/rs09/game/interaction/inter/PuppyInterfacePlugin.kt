package rs09.game.interaction.inter

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction

/**
 * Represents the puppy interface plugin.
 * @author Ceikry
 */
@Initializable
class PuppyInterfacePlugin : ComponentPlugin() {
    override fun newInstance(arg: Any?): Plugin<Any>? {
        ComponentDefinition.put(668, this)
        return this
    }

    override fun handle(player: Player, component: Component, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        val index = when(button){
            8 -> 0
            3 -> 1
            4 -> 2
            5 -> 3
            6 -> 4
            7 -> 5
            else -> 0
        }
        player.dialogueInterpreter.open(6893, NAMES[index], PUPPIES[index][RandomFunction.random(PUPPIES[index].size)])
        return true
    }

    companion object {
        /**
         * Represents the names of the puppys.
         */
        private val NAMES = arrayOf("labrador", "bulldog", "dalmatian", "greyhound", "terrier", "sheepdog")

        /**
         * Represents the puppies.
         */
        private val PUPPIES = arrayOf(
                arrayOf(Item(12516), Item(12708), Item(12710)), //Labrador
                arrayOf(Item(12522), Item(12720), Item(12722)), //Bulldog
                arrayOf(Item(12518), Item(12712), Item(12714)), //Dalmatian
                arrayOf(Item(12514), Item(12704), Item(12706)), //Greyhound
                arrayOf(Item(12512), Item(12700), Item(12702)), //Terrier
                arrayOf(Item(12520), Item(12716), Item(12718))) //Sheepdog
    }
}