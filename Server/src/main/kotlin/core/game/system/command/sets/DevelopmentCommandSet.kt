package core.game.system.command.sets

import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.Items
import core.game.system.command.Command

@Initializable
class DevelopmentCommandSet : CommandSet(Command.Privilege.ADMIN) {
    val farmKitItems = arrayListOf(Items.RAKE_5341, Items.SPADE_952, Items.SEED_DIBBER_5343, Items.WATERING_CAN8_5340, Items.SECATEURS_5329, Items.GARDENING_TROWEL_5325)

    override fun defineCommands() {

        /**
         * Gives the player a set of tools used to test farming stuff.
         */
        define("farmkit"){player,_ ->
            for(item in farmKitItems){
                player.inventory.add(Item(item))
            }
        }


    }
}