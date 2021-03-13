package rs09.game.content.activity.gnomecooking.crunchies

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

private const val TOAD_CRUNCHIES = 9538
private const val SPICE_CRUNCHIES = 9540
private const val CHOC_CHIP_CRUNCHIES = 9544
private const val WORM_CRUNCHIES = 9542
private const val HALF_BAKED_CRUNCHY = 2201

/**
 * Handles the gnome crunchy interface
 * @author Ceikry
 */
@Initializable
class GnomeCrunchyInterface : ComponentPlugin() {

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when(button){
            3 -> attemptMake(HalfMadeCrunchy.TOAD,player)
            10 -> attemptMake(HalfMadeCrunchy.SPICY,player)
            17 -> attemptMake(HalfMadeCrunchy.WORM,player)
            26 -> attemptMake(HalfMadeCrunchy.CHOCCHIP,player)
        }
        return true
    }

    private fun attemptMake(crunchy: HalfMadeCrunchy, player: Player){
        var hasAll = true

        if(player.skills.getLevel(Skills.COOKING) < crunchy.reqLevel){
            player.dialogueInterpreter.sendDialogue("You don't have the required level to make these.")
            return
        }

        if(!player.inventory.containsItem(Item(Items.GNOME_SPICE_2169))){
            player.dialogueInterpreter.sendDialogue("You need some gnome spice to make these.")
            return
        }

        for(item in crunchy.requiredItems){
            if(!player.inventory.containsItem(item)){
                hasAll = false
                break;
            }
        }

        if(!hasAll){
            player.dialogueInterpreter.sendDialogue("You don't have the required ingredients to make these.")
            return
        }

        player.inventory.remove(*crunchy.requiredItems)
        player.inventory.remove(Item(HALF_BAKED_CRUNCHY))
        player.inventory.add(Item(crunchy.product))
        player.skills.addExperience(Skills.COOKING,30.0)
        player.interfaceManager.close()
    }

    override fun open(player: Player?, component: Component?) {
        player    ?: return
        component ?: return
        super.open(player, component)

        player.packetDispatch.sendItemOnInterface(TOAD_CRUNCHIES,1,component.id,3)
        player.packetDispatch.sendItemOnInterface(SPICE_CRUNCHIES,1,component.id,10)
        player.packetDispatch.sendItemOnInterface(WORM_CRUNCHIES,1,component.id,17)
        player.packetDispatch.sendItemOnInterface(CHOC_CHIP_CRUNCHIES,1,component.id,26)
    }
    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(437,this)
        return this
    }

    internal enum class HalfMadeCrunchy(val product: Int,val reqLevel: Int, val requiredItems: Array<Item>){
        CHOCCHIP(9577, 16, arrayOf(Item(Items.CHOCOLATE_BAR_1973,2))),
        SPICY(9579, 12, arrayOf(Item(Items.EQUA_LEAVES_2128,2))),
        TOAD(9581, 10, arrayOf(Item(Items.TOADS_LEGS_2152,2))),
        WORM(9583, 14, arrayOf(Item(Items.EQUA_LEAVES_2128),Item(Items.KING_WORM_2162,2)))
    }

}