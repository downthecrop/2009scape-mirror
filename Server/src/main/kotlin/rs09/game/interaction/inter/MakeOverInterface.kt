package rs09.game.interaction.inter

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.appearance.Gender
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs09.consts.Items

//child IDs for components of interest on the interface
private const val MALE_CHILD_ID = 90
private const val FEMALE_CHILD_ID = 92
private const val TEXT_CHILD = 88

//skin color buttons
private val skincolorButtons = (93..100)

/**
 * Fully authentic and functional makeover interface
 * @author Ceikry
 */
@Initializable
class MakeOverInterface : ComponentPlugin(){
    override fun open(player: Player?, component: Component?) {
        component ?: return
        player ?: return
        super.open(player, component)
        //Send NPC chat heads to interface
        player.packetDispatch.sendNpcOnInterface(1,component.id, MALE_CHILD_ID)
        player.packetDispatch.sendNpcOnInterface(5,component.id, FEMALE_CHILD_ID)
        //Send chathead animations to interface
        player.packetDispatch.sendAnimationInterface(FacialExpression.SILENT.animationId,component.id, MALE_CHILD_ID)
        player.packetDispatch.sendAnimationInterface(FacialExpression.SILENT.animationId,component.id, FEMALE_CHILD_ID)

        //Check for makeover voucher and then change interface text if it's there
        if(player.inventory.containsAtLeastOneItem(Items.MAKEOVER_VOUCHER_5606)){
            player.packetDispatch.sendString("USE MAKEOVER VOUCHER",component.id, TEXT_CHILD)
        }

        //Automatically hide the button representing the player's current skin color
        val currentSkin = player.appearance.skin.color
        player.setAttribute("mm-previous",currentSkin)
        player.packetDispatch.sendInterfaceConfig(component.id, skincolorButtons.first + currentSkin, true)

        player.toggleWardrobe(true)

        component.setCloseEvent{pl,_ ->
            pl.toggleWardrobe(false)
            if(player.getAttribute("mm-paid",false)){
                val newColor = player.getAttribute("mm-previous",-1)
                val newGender = player.getAttribute("mm-gender",-1)
                if(newColor > -1){
                    player.appearance.skin.changeColor(newColor)
                }
                if(newGender > -1){
                    player.appearance.changeGender(Gender.values()[newGender])
                    player.appearance.skin.changeColor(newColor)
                }
                player.appearance.sync()
                player.removeAttribute("mm-paid")
            }
            pl.removeAttribute("mm-previous")
            pl.removeAttribute("mm-gender")
            true
        }
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        if(skincolorButtons.contains(button)){
            updateInterfaceConfigs(player,button)
            return true
        }
        when(button){
            113,101 -> updateGender(player,true)
            114,103 -> updateGender(player,false)
            88 -> pay(player)
        }

        return true
    }

    fun updateGender(player: Player, male: Boolean){
        player.setAttribute("mm-gender",if(male) Gender.MALE.ordinal else Gender.FEMALE.ordinal)
    }

    fun pay(player: Player){
        val newColor = player.getAttribute("mm-previous",player.appearance.skin.color)
        val newGender = player.getAttribute("mm-gender",player.appearance.gender.ordinal)

        //Don't charge if they didn't change anything
        if(newColor == player.appearance.skin.color && Gender.values()[newGender] == player.appearance.gender){
            player.interfaceManager.close()
        } else {
            val currency = if(player.inventory.containsAtLeastOneItem(Items.MAKEOVER_VOUCHER_5606)){
                Item(Items.MAKEOVER_VOUCHER_5606,1)
            } else Item(995,3000)

            if(player.inventory.containsItem(currency)){
                player.setAttribute("mm-paid",true)
                player.inventory.remove(currency)
                player.interfaceManager.close()
            } else {
                player.dialogueInterpreter.sendDialogue("You can not afford that.")
            }
        }
    }

    fun updateInterfaceConfigs(player: Player,button: Int){
        val old = player.getAttribute("mm-previous",0)
        player.setAttribute("mm-previous",button - skincolorButtons.first)
        player.packetDispatch.sendInterfaceConfig(205,old + skincolorButtons.first,false)
        player.packetDispatch.sendInterfaceConfig(205,button,true)

    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(205,this)
        return this
    }

}
