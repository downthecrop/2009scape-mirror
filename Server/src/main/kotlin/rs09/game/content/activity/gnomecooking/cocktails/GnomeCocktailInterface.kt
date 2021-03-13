package rs09.game.content.activity.gnomecooking.cocktails

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

private const val WIZARD_BLIZZARD = 2054
private const val SHORT_GREEN_GUY = 2080
private const val FRUIT_BLAST = 2084
private const val PINEAPPLE_PUNCH = 2048
private const val DRUNK_DRAGON = 2092
private const val CHOC_SATURDAY = 2074
private const val BLURBERRY_SPECIAL = 2064

/**
 * Handles the gnome cocktail interface
 * @author Ceikry
 */
@Initializable
class GnomeCocktailInterface : ComponentPlugin() {

    override fun open(player: Player?, component: Component?) {
        player ?: return
        component ?: return
        super.open(player, component)
        player.packetDispatch.sendItemOnInterface(WIZARD_BLIZZARD,1,component.id,3)
        player.packetDispatch.sendItemOnInterface(SHORT_GREEN_GUY,1,component.id,16)
        player.packetDispatch.sendItemOnInterface(FRUIT_BLAST,1,component.id,23)
        player.packetDispatch.sendItemOnInterface(PINEAPPLE_PUNCH,1,component.id,32)
        player.packetDispatch.sendItemOnInterface(DRUNK_DRAGON,1,component.id,41)
        player.packetDispatch.sendItemOnInterface(CHOC_SATURDAY,1,component.id,50)
        player.packetDispatch.sendItemOnInterface(BLURBERRY_SPECIAL,1,component.id,61)
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when(button){
            3 ->  attemptMake(FruitCocktail.WIZARD_BLIZZARD,player)
            16 -> attemptMake(FruitCocktail.SHORT_GREEN_GUY, player)
            23 -> attemptMake(FruitCocktail.FRUIT_BLAST, player)
            32 -> attemptMake(FruitCocktail.PINEAPPLE_PUNCH,player)
            41 -> attemptMake(FruitCocktail.DRUNK_DRAGON,player)
            50 -> attemptMake(FruitCocktail.CHOC_SATURDAY,player)
            61 -> attemptMake(FruitCocktail.BLURBERRY_SPEC,player)
        }
        return true
    }

    private fun attemptMake(cocktail: FruitCocktail, player: Player){
        var hasAll = true
        val cookingLevel = player.skills.getLevel(Skills.COOKING)

        if(cookingLevel < cocktail.levelReq){
            player.dialogueInterpreter.sendDialogue("You don't have the necessary level to make that.")
            return
        }

        for(ingredient in cocktail.requiredItems){
            if(!player.inventory.containsItem(ingredient)){
                hasAll = false
                break
            }
        }

        if(!hasAll){
            player.dialogueInterpreter.sendDialogue("You don't have the ingredients to make that.")
            return
        }

        player.inventory.remove(*cocktail.requiredItems)
        player.inventory.remove(Item(Items.COCKTAIL_SHAKER_2025))
        player.inventory.add(Item(cocktail.product))
        player.skills.addExperience(Skills.COOKING,cocktail.experience)
        player.interfaceManager.close()
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(436,this)
        return this
    }

    internal enum class FruitCocktail(val levelReq: Int, val experience: Double, val product: Int, val requiredItems: Array<Item>){
        FRUIT_BLAST(6,50.0,9568, arrayOf(Item(Items.PINEAPPLE_2114),Item(Items.LEMON_2102),Item(Items.ORANGE_2108))),
        PINEAPPLE_PUNCH(8,70.0,9569, arrayOf(Item(Items.PINEAPPLE_2114,2),Item(Items.LEMON_2102),Item(Items.ORANGE_2108))),
        WIZARD_BLIZZARD(18,110.0, 9566, arrayOf(Item(Items.VODKA_2015,2),Item(Items.GIN_2019),Item(Items.LIME_2120),Item(Items.LEMON_2102),Item(Items.ORANGE_2108))),
        SHORT_GREEN_GUY(20,120.0,9567, arrayOf(Item(Items.VODKA_2015),Item(Items.LIME_2120,3))),
        DRUNK_DRAGON(32,160.0,9574, arrayOf(Item(Items.VODKA_2015),Item(Items.GIN_2019),Item(Items.DWELLBERRIES_2126))),
        CHOC_SATURDAY(33,170.0,9571, arrayOf(Item(Items.WHISKY_2017),Item(Items.CHOCOLATE_BAR_1973),Item(Items.EQUA_LEAVES_2128),Item(Items.BUCKET_OF_MILK_1927))),
        BLURBERRY_SPEC(37,180.0,9570, arrayOf(Item(Items.VODKA_2015),Item(Items.BRANDY_2021),Item(Items.GIN_2019),Item(Items.LEMON_2102,2),Item(Items.ORANGE_2108)))
    }

}