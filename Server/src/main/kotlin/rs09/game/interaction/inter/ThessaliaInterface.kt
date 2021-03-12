package rs09.game.interaction.inter

import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Fully accurate and functional thessalia interface handling
 * @author Ceikry
 */

private const val THESSALIA_MALE_COMPONENT = 591
private const val THESSALIA_FEMALE_COMPONENT = 594
private const val PLAYER_MODEL_CHILD_ID = 59

private val legColors =   intArrayOf(24, 23, 3, 22, 13, 12, 7, 19, 5, 1, 10, 14, 25, 9, 0, 26, 21, 8, 20, 15, 11, 28, 27, 4, 6, 18, 17, 2, 16)
private val torsoColors = intArrayOf(24, 23, 2, 22, 12, 11, 6, 19, 4, 0, 9, 13, 25, 8, 15, 26, 21, 7, 20, 14, 10, 28, 27, 3, 5, 18, 17, 1, 16)

private val maleTorsoButtonRange = (185..198)
private val maleArmsButtonRange = (199..210)
private val maleLegsButtonRange = (211..221)
private val maleColorButtonRange = (252..280)

private val femaleTorsoButtonRange = (186..196)
private val femaleArmsButtonRange = (197..207)
private val femaleLegsButtonRange = (208..222)
private val femaleColorButtonRange = (253..281)

//Male Torso
private const val M_STRIPED_SWEATER = 111
private const val M_WOOLLEN_VEST = 113
private const val M_PRINCELY_VEST = 114
private const val M_TATTERED_WAISTCOAT = 115
private const val M_FINE_SHIRT = 21
private const val M_WAISTCOAT = 116
private const val M_PLAIN_TOP = 18
private const val M_LIGHT_BUTTONS = 19
private const val M_DARK_BUTTONS = 20
private const val M_JACKET = 112
private const val M_SHIRT = 24
private const val M_STITCHING = 23
private const val M_RAGGED_TOP = 24
private const val M_TWO_TONED = 25

private val maleTorsoIDs = intArrayOf(M_STRIPED_SWEATER, M_WOOLLEN_VEST, M_PRINCELY_VEST, M_TATTERED_WAISTCOAT, M_FINE_SHIRT, M_WAISTCOAT, M_PLAIN_TOP, M_LIGHT_BUTTONS, M_DARK_BUTTONS, M_JACKET, M_SHIRT, M_STITCHING, M_RAGGED_TOP, M_TWO_TONED)

//Male Sleeves
private const val M_STRIPED_ARMS = 107
private const val M_PRINCELY_SLEEVES = 108
private const val M_FINE_CUFFS = 29
private const val M_WOOLLEN_SLEEVES = 106
private const val M_RAGGED_ARMS = 110
private const val M_TATTERED_SLEEVES = 109
private const val M_LOOSE_SLEEVED = 28
private const val M_REGULAR = 26
private const val M_MUSCLEBOUND = 27
private const val M_LARGE_CUFFED = 105
private const val M_THIN_SLEEVED = 30
private const val M_SHOULDER_PADS = 31

private val maleSleeveIDs = intArrayOf(M_STRIPED_ARMS, M_PRINCELY_SLEEVES, M_FINE_CUFFS, M_WOOLLEN_SLEEVES, M_RAGGED_ARMS, M_TATTERED_SLEEVES, M_LOOSE_SLEEVED, M_REGULAR, M_MUSCLEBOUND, M_LARGE_CUFFED, M_THIN_SLEEVED, M_SHOULDER_PADS)

//Male Leg IDs
private const val M_PLAIN_TROUSERS = 36
private const val M_PRINCELY_BREECHES = 85
private const val M_SHORTS = 37
private const val M_RAGGED_BREECHES = 40
private const val M_TATTERED_BREECHES = 89
private const val M_TORN_TROUSERS = 90
private const val M_BREECHES = 86
private const val M_STRIPED_TROUSERS = 88
private const val M_TURN_UPS = 39
private const val M_FLARES = 38
private const val M_FINE_BREECHES = 87

private val maleLegIDs = intArrayOf(M_PLAIN_TROUSERS, M_PRINCELY_BREECHES, M_SHORTS, M_RAGGED_BREECHES, M_TATTERED_BREECHES, M_TORN_TROUSERS, M_BREECHES, M_STRIPED_TROUSERS, M_TURN_UPS, M_FLARES, M_FINE_BREECHES)

//Female Torso
private const val F_STRIPED_SWEATER = 153
private const val F_WOOLLEN_VEST = 155
private const val F_FRILLED_BLOUSE = 156
private const val F_BODICE = 157
private const val F_FINE_SHIRT = 154
private const val F_RAGGED_TOP = 158
private const val F_PLAIN_TOP = 56
private const val F_CROP_TOP = 57
private const val F_SIMPLE = 58
private const val F_POLO_NECK = 59
private const val F_TORN = 60

private val femaleTopIDs = intArrayOf(F_STRIPED_SWEATER, F_WOOLLEN_VEST, F_FRILLED_BLOUSE, F_BODICE, F_FINE_SHIRT, F_RAGGED_TOP, F_PLAIN_TOP, F_CROP_TOP, F_SIMPLE, F_POLO_NECK, F_TORN)

//Female Arms
private const val F_STRIPED_ARMS = 149
private const val F_FRILLED_SLEEVES = 150
private const val F_FINE_CUFFS = 65
private const val F_WOOLLEN_ARMS = 148
private const val F_RAGGED_SLEEVES = 151
private const val F_TATTERED_SLEEVES = 152
private const val F_LONG_SLEEVES = 64
private const val F_SHORT_SLEEVES = 61
private const val F_MUSCLY = 63
private const val F_LARGE_CUFFS = 147
private const val F_BARE_ARMS = 62

private val femaleArmIDs = intArrayOf(F_STRIPED_ARMS, F_FRILLED_SLEEVES, F_FINE_CUFFS, F_WOOLLEN_ARMS, F_RAGGED_SLEEVES, F_TATTERED_SLEEVES, F_LONG_SLEEVES, F_SHORT_SLEEVES, F_MUSCLY, F_LARGE_CUFFS, F_BARE_ARMS)

//Female Legs
private const val F_FINE_SKIRT = 129
private const val F_FRILLED_SKIRT = 130
private const val F_LAYERED_SKIRT = 128
private const val F_LONG_NARROW_SKIRT = 74
private const val F_RAGGED_SKIRT = 133
private const val F_TATTERED_SKIRT = 134
private const val F_SHORT_SKIRT = 71
private const val F_SASHED_SKIRT = 131
private const val F_FITTED_SKIRT = 132
private const val F_TORN_TROUSERS = 75
private const val F_LONG_SKIRT = 73
private const val F_TURN_UPS = 76
private const val F_FLARES = 72
private const val F_PLAIN_TROUSERS = 70
private const val F_SHORTS = 77

private val femaleLegIDs = intArrayOf(F_FINE_SKIRT, F_FRILLED_SKIRT, F_LAYERED_SKIRT, F_LONG_NARROW_SKIRT, F_RAGGED_SKIRT, F_TATTERED_SKIRT, F_SHORT_SKIRT, F_SASHED_SKIRT, F_FITTED_SKIRT, F_TORN_TROUSERS, F_LONG_SKIRT, F_TURN_UPS, F_FLARES, F_FLARES, F_PLAIN_TROUSERS, F_SHORTS)

private val COINS = Item(995,1000)

@Initializable
class ThessaliaInterface : ComponentPlugin(){
    enum class colorType{
        TORSO,
        ARMS,
        LEGS
    }

    override fun open(player: Player?, component: Component?) {
        super.open(player, component)
        component ?: return
        player?.toggleWardrobe(true)
        player?.setAttribute("orig-torso",player.appearance.torso.look)
        player?.setAttribute("orig-torso-color",player.appearance.torso.color)
        player?.setAttribute("orig-arms",player.appearance.arms.look)
        player?.setAttribute("orig-arms-color",player.appearance.arms.color)
        player?.setAttribute("orig-legs",player.appearance.legs.look)
        player?.setAttribute("orig-legs-color",player.appearance.legs.color)
        player?.packetDispatch?.sendPlayerOnInterface(component.id, PLAYER_MODEL_CHILD_ID)


        component.setCloseEvent{pl,_ ->
            pl?.toggleWardrobe(false)
            pl.attributes.remove("thes-type")
            if(!pl.getAttribute("thes-paid",false)){
                pl.appearance.torso.changeLook(pl.getAttribute("orig-torso",0))
                pl.appearance.torso.changeColor(pl.getAttribute("orig-torso-color",0))
                pl.appearance.arms.changeLook(pl.getAttribute("orig-arms",0))
                pl.appearance.arms.changeColor(pl.getAttribute("orig-arms-color",0))
                pl.appearance.legs.changeLook(pl.getAttribute("orig-legs",0))
                pl.appearance.legs.changeColor(pl.getAttribute("orig-legs-color",0))
                pl.appearance.sync()
            }
            pl.removeAttribute("thes-paid")
            true
        }
    }
    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false
        when(button){
            181,180 -> pay(player)
            else -> when(component?.id){
                THESSALIA_MALE_COMPONENT -> {
                    when(button){
                        182 -> player.setAttribute("thes-type",colorType.TORSO)
                        183 -> player.setAttribute("thes-type",colorType.ARMS)
                        184 -> player.setAttribute("thes-type",colorType.LEGS)
                    }
                    if(maleArmsButtonRange.contains(button))  updateArms(player,button,true)
                    if(maleTorsoButtonRange.contains(button)) updateTop(player,button,true)
                    if(maleLegsButtonRange.contains(button))  updateLegs(player,button,true)
                    if(maleColorButtonRange.contains(button)) updateColor(player,button,true,player.getAttribute("thes-type",colorType.TORSO))
                }
                THESSALIA_FEMALE_COMPONENT -> {
                    when(button){
                        183 -> player.setAttribute("thes-type",colorType.TORSO)
                        184 -> player.setAttribute("thes-type",colorType.ARMS)
                        185 -> player.setAttribute("thes-type",colorType.LEGS)
                    }
                    if(femaleArmsButtonRange.contains(button))  updateArms(player,button,false)
                    if(femaleTorsoButtonRange.contains(button)) updateTop(player,button,false)
                    if(femaleLegsButtonRange.contains(button))  updateLegs(player,button,false)
                    if(femaleColorButtonRange.contains(button)) updateColor(player,button,false,player.getAttribute("thes-type",colorType.TORSO))
                }
            }
        }
        return true
    }

    fun pay(player: Player){
        if(player.inventory.containsItem(COINS)){
            player.inventory.remove(COINS)
            player.setAttribute("thes-paid",true)
            player.interfaceManager.close()
        } else {
            player.dialogueInterpreter.sendDialogue("You can not afford that.")
        }
    }

    fun updateTop(player: Player,button: Int,male: Boolean){
        val usedArray = if(male) maleTorsoIDs else femaleTopIDs
        val subtractor = if(male) maleTorsoButtonRange.first else femaleTorsoButtonRange.first
        player.appearance.torso.changeLook(usedArray[button - subtractor])
        player.appearance.sync()
    }

    fun updateLegs(player: Player, button: Int, male: Boolean){
        val usedArray = if(male) maleLegIDs else femaleLegIDs
        val subtractor = if(male) maleLegsButtonRange.first else femaleLegsButtonRange.first
        player.appearance.legs.changeLook(usedArray[button - subtractor])
        player.appearance.sync()
    }

    fun updateArms(player: Player, button: Int, male: Boolean){
        val usedArray = if(male) maleSleeveIDs else femaleArmIDs
        val subtractor = if(male) maleArmsButtonRange.first else femaleArmsButtonRange.first
        player.appearance.arms.changeLook(usedArray[button - subtractor])
        player.appearance.sync()
    }

    fun updateColor(player: Player, button: Int, male: Boolean, type: colorType){
        val subtractor = if(male) maleColorButtonRange.first else femaleColorButtonRange.first
        when(type){
            colorType.ARMS -> player.appearance.torso.changeColor(torsoColors[button - subtractor])
            colorType.LEGS -> player.appearance.legs.changeColor(legColors[button - subtractor])
            colorType.TORSO -> player.appearance.torso.changeColor(torsoColors[button - subtractor])
        }
        player.appearance.sync()
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(THESSALIA_MALE_COMPONENT,this)
        ComponentDefinition.put(THESSALIA_FEMALE_COMPONENT,this)
        return this
    }
}