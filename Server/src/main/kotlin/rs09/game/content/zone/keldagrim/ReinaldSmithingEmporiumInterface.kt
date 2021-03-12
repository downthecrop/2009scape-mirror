package rs09.game.content.zone.keldagrim

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

const val REINALD_COMPONENT_ID = 593
private const val BRACELET_INTERFACE_CHILD_ID = 69

//Models to send for various wristguards
private const val SILVER_WRISTGUARDS = 27703
private const val SILVER_CLASPS = 27704
private const val SILVER_BANGLES = 27706
private const val SILVER_BANDING = 27707
private const val SILVER_BANDS = 27697
private const val SILVER_ARMGUARDS = 27699

private const val GOLD_WRISTGUARDS = 27698
private const val GOLD_CLASPS = 27708
private const val GOLD_BANGLES = 27702
private const val GOLD_BANDING = 27705
private const val GOLD_BANDS = 27700
private const val GOLD_ARMGUARDS = 27709

private const val NONE = 0

private val COINS = Item(995,500)

//Appearance Indexes for Various Wristguards
//117S 118G 119S 120G 121S 122G 123S 124G 125S 126G M
//159S 160G 161S 162G 163S 164G 165S 166G 167S 168G F
//67SF 68NF 127GF 33SM 34NM 84GM
@Initializable
/**
 * Handle's Reinald's bracelet shop
 * @author Ceikry
 */
class ReinaldSmithingEmporiumInterface : ComponentPlugin(){
    override fun open(player: Player?, component: Component?) {
        super.open(player, component)
        //make sure the player can't just close the interface and get it for free (lol)
        player?.setAttribute("wrists-look",player.appearance.wrists.look)
        player?.toggleWardrobe(true)
        component?.setCloseEvent(){pl,_ ->
            player?.toggleWardrobe(false)
            val orindex = pl.getAttribute("wrists-look", if(pl.isMale) 34 else 68)
            val paid = pl.getAttribute("bracelet-paid",false)
            if(!paid) {
                pl.appearance.wrists.changeLook(orindex)
                pl.appearance.sync()
            }
            pl.removeAttribute("bracelet-paid")
            true
        }
    }
    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when(button){
            122,
            129 -> sendModel(NONE,player)
            123 -> sendModel(SILVER_WRISTGUARDS,player)
            124 -> sendModel(SILVER_CLASPS,player)
            125 -> sendModel(SILVER_BANGLES,player)
            126 -> sendModel(SILVER_BANDING,player)
            127 -> sendModel(SILVER_BANDS,player)
            128 -> sendModel(SILVER_ARMGUARDS,player)
            130 -> sendModel(GOLD_WRISTGUARDS,player)
            131 -> sendModel(GOLD_CLASPS, player)
            132 -> sendModel(GOLD_BANGLES,player)
            133 -> sendModel(GOLD_BANDING,player)
            134 -> sendModel(GOLD_BANDS,player)
            135 -> sendModel(GOLD_ARMGUARDS,player)
            117 -> confirm(player)
        }
        return true
    }

    fun confirm(player: Player){
        if(player.inventory.containsItem(COINS)){
            player.inventory.remove(COINS)
            player.setAttribute("bracelet-paid",true)
            player.interfaceManager.close()
        } else {
            player.dialogueInterpreter.sendDialogue("You can not afford that.")
        }
    }

    fun sendModel(id: Int, player: Player){
        var appearanceIndex = when(id){
            SILVER_CLASPS -> 117
            GOLD_CLASPS -> 118
            SILVER_BANDS -> 119
            GOLD_BANDS -> 120
            SILVER_ARMGUARDS -> 123
            GOLD_ARMGUARDS -> 124
            SILVER_BANDING -> 121
            GOLD_BANDING -> 122
            SILVER_BANGLES -> 125
            GOLD_BANGLES -> 126
            SILVER_WRISTGUARDS -> if(player.isMale) 33 else 67
            GOLD_WRISTGUARDS -> if(player.isMale) 84 else 127
            NONE -> if(player.isMale) 34 else 68
            else -> 0
        }
        if(!player.isMale && id != SILVER_WRISTGUARDS && id != GOLD_WRISTGUARDS && id != NONE){
            appearanceIndex += 42 //Female is almost always 42 higher than male, except for the above cases in the if
        }
        player.packetDispatch.sendModelOnInterface(id, REINALD_COMPONENT_ID, BRACELET_INTERFACE_CHILD_ID, 1)
        player.packetDispatch.sendInterfaceConfig(REINALD_COMPONENT_ID, BRACELET_INTERFACE_CHILD_ID,id == 0) //hide/show bracelet model depending on if 0 or not
        player.appearance.wrists.changeLook(appearanceIndex)
        player.debug("USING WRIST APPEARANCE ID $appearanceIndex")
        player.appearance.sync()
        player.packetDispatch.sendPlayerOnInterface(REINALD_COMPONENT_ID,60)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(REINALD_COMPONENT_ID,this)
        return this
    }
}