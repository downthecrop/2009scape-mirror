package rs09.game.interaction.inter

import api.*
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.container.access.InterfaceContainer
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rs09.tools.stringtools.colorize

private const val FUR_CLOTHING_COMPONENT_ID = 477

//Polar Kebbit
private val POLAR_CAMO_TOP = Item(10065)
private val POLAR_CAMO_LEGS = Item(10067)

//COMMON/WOOD CAMO
private val WOOD_CAMO_TOP = Item(10053)
private val WOOD_CAMO_LEGS = Item(10055)

//FELDIP/JUNGLE CAMO
private val JUNGLE_CAMO_TOP = Item(10057)
private val JUNGLE_CAMO_LEGS = Item(10059)

//DESERT CAMO
private val DESERT_CAMO_TOP = Item(10061)
private val DESERT_CAMO_LEGS = Item(10063)


//Larupia
private val LARUPIA_HAT = Item(10045)
private val LARUPIA_TOP = Item(10043)
private val LARUPIA_LEGS = Item(10041)

//Graahk
private val GRAAHK_HEADDRESS = Item(10051)
private val GRAAHK_TOP = Item(10049)
private val GRAAHK_LEGS = Item(10047)

//KYATT
private val KYATT_HAT = Item(10039)
private val KYATT_TOP = Item(10037)
private val KYATT_LEGS = Item(10035)

//SPECIAL
private val GLOVES_OF_SILENCE = Item(10075)
private val SPOTTED_CAPE = Item(10069)
private val SPOTTIER_CAPE = Item(10071)


private val OPTIONS = arrayOf("Buy 10","Buy 5","Buy 1","Value")

//I just want to say that this monolith of fuckery ACTUALLY SEEMS TO BE THE WAY
//J*GEX HANDLED THIS. This is THEIR packet this is being sent in! The spacing lines up 1:1!
//Like holy what the fuck j*gex
private val ITEMS = arrayOf(
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,POLAR_CAMO_TOP, null,null, POLAR_CAMO_LEGS,null,null,null,null,
        null,WOOD_CAMO_TOP, null,null, WOOD_CAMO_LEGS,null,null,null,
        null,JUNGLE_CAMO_TOP,null,null, JUNGLE_CAMO_LEGS,null,null,null,
        null,DESERT_CAMO_TOP,null,null, DESERT_CAMO_LEGS,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        LARUPIA_HAT,null,null,LARUPIA_TOP,null,null,LARUPIA_LEGS,null,null,null,null,
        GRAAHK_HEADDRESS,null,null,GRAAHK_TOP,null,null,GRAAHK_LEGS,null,null,null,null,null,
        KYATT_HAT,null,null,KYATT_TOP,null,null,KYATT_LEGS, null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null, GLOVES_OF_SILENCE,null,null,null,null,null,null,null,
        null,null,null, SPOTTED_CAPE,null,null,null,null,null,null,null,null,
        null,null,null, SPOTTIER_CAPE,null)

/**
 * Interface for custom fur clothing that is a 1:1 matchup in functionality and appearance to OSRS's.
 * It's got a fuckhuge hideous monolith and I'm okay with that, it seems to be AUTHENTIC.
 * @author Ceikry
 */
@Initializable
class FurClothingInterface : ComponentPlugin(){
    override fun open(player: Player?, component: Component?) {
        super.open(player, component)
        player ?: return
        //Send the monolith of fuckery to the client to deal with
        InterfaceContainer.generateItems(player, ITEMS, OPTIONS, FUR_CLOTHING_COMPONENT_ID, 26,12,33,5000)

        //Highlight the names of groups that contain a craftable item
        //Running in a crouton because why the hell not.
        GlobalScope.launch {
            val checkedFurs = arrayListOf<String>()
            for(CLOTHING in FUR_CLOTHING.values()){
                if(checkedFurs.contains(CLOTHING.textContent)) continue
                if(inInventory(player, CLOTHING.requiredFur.id, CLOTHING.requiredFur.amount)){
                    setInterfaceText(player, colorize("%G${CLOTHING.textContent}"), FUR_CLOTHING_COMPONENT_ID, CLOTHING.textChildID)
                    checkedFurs.add(CLOTHING.textContent)
                }
            }
        }
    }

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        player ?: return false

        val clothingItem: FUR_CLOTHING? = when(slot){
            34 -> FUR_CLOTHING.POLAR_KEBBIT_TOP
            37 -> FUR_CLOTHING.POLAR_KEBBIT_BOT
            43 -> FUR_CLOTHING.COMMON_KEBBIT_TOP
            46 -> FUR_CLOTHING.COMMON_KEBBIT_BOT
            51 -> FUR_CLOTHING.FELDIP_WEASEL_TOP
            54 -> FUR_CLOTHING.FELDIP_WEASEL_BOT
            59 -> FUR_CLOTHING.DESERT_DEVIL_TOP
            62 -> FUR_CLOTHING.DESERT_DEVIL_BOT
            166 -> FUR_CLOTHING.LARUPIA_HEAD
            169 -> FUR_CLOTHING.LARUPIA_CHEST
            172 -> FUR_CLOTHING.LARUPIA_BOT
            177 -> FUR_CLOTHING.GRAAHK_HEAD
            180 -> FUR_CLOTHING.GRAAHK_CHEST
            183 -> FUR_CLOTHING.GRAAHK_BOT
            189 -> FUR_CLOTHING.KYATT_HEAD
            192 -> FUR_CLOTHING.KYATT_CHEST
            195 -> FUR_CLOTHING.KYATT_BOT
            334 -> FUR_CLOTHING.GLOVES_SILENCE
            345 -> FUR_CLOTHING.SPOT_CAPE
            357 -> FUR_CLOTHING.DASH_CAPE
            else -> null
        }

        clothingItem ?: return false

        when(opcode){
            196 -> buy(player,clothingItem,1)
            124 -> buy(player,clothingItem,5)
            199 -> buy(player,clothingItem,10)
            155 -> value(player,clothingItem)
        }
        return true
    }

    private fun value(player: Player, clothing: FUR_CLOTHING){
        sendMessage(player,"${clothing.product.name} requires ${clothing.requiredFur.amount} ${clothing.requiredFur.name.toLowerCase()} and costs ${clothing.price} coins.")
    }

    private fun buy(player: Player, clothing: FUR_CLOTHING, amount: Int){
        val coins = Item(995,clothing.price * amount)
        val amtFurRequired = clothing.requiredFur.amount * amount
        val requiredFur = Item(clothing.requiredFur.id,amtFurRequired)

        if(!inInventory(player, requiredFur.id, requiredFur.amount)){
            sendDialogue(player, "You don't have enough fur for that.")
            return
        }

        if(!inInventory(player, coins.id, coins.amount)){
            sendDialogue(player,"You can't afford that.")
            return
        }

        removeItem(player, requiredFur, api.Container.INVENTORY)
        removeItem(player, coins, api.Container.INVENTORY)
        addItem(player, clothing.product.id, amount)
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.put(FUR_CLOTHING_COMPONENT_ID,this)
        return this
    }


    //Enum for the various pieces, requirements, etc.
    internal enum class FUR_CLOTHING(val requiredFur: Item, val price: Int, val product: Item, val textChildID: Int, val textContent: String){
        POLAR_KEBBIT_TOP(Item(10117,2),20, POLAR_CAMO_TOP,18,"Polar kebbit"),
        POLAR_KEBBIT_BOT(Item(10117,2),20, POLAR_CAMO_LEGS,18,"Polar kebbit"),
        COMMON_KEBBIT_TOP(Item(10121,2),20, WOOD_CAMO_TOP,19,"Common kebbit"),
        COMMON_KEBBIT_BOT(Item(10121,2),20, WOOD_CAMO_LEGS,19,"Common kebbit"),
        FELDIP_WEASEL_TOP(Item(10119,2),20, JUNGLE_CAMO_TOP,20,"Feldip weasel"),
        FELDIP_WEASEL_BOT(Item(10119,2),20, JUNGLE_CAMO_LEGS,20,"Feldip weasel"),
        DESERT_DEVIL_TOP(Item(10123,2),20, DESERT_CAMO_TOP,21,"Desert devil"),
        DESERT_DEVIL_BOT(Item(10123,2),20, DESERT_CAMO_LEGS, 21, "Desert devil"),
        LARUPIA_HEAD(Item(10095),500, LARUPIA_HAT,15,"Larupia"),
        LARUPIA_CHEST(Item(10095),500, LARUPIA_TOP,15,"Larupia"),
        LARUPIA_BOT(Item(10095),500, LARUPIA_LEGS,15,"Larupia"),
        GRAAHK_HEAD(Item(10099),750, GRAAHK_HEADDRESS,16,"Graahk"),
        GRAAHK_CHEST(Item(10099),750, GRAAHK_TOP,16,"Graahk"),
        GRAAHK_BOT(Item(10099),750, GRAAHK_LEGS,16,"Graahk"),
        KYATT_HEAD(Item(10039),1000, KYATT_HAT,17,"Kyatt"),
        KYATT_CHEST(Item(10039),1000, KYATT_TOP,17,"Kyatt"),
        KYATT_BOT(Item(10039),1000, KYATT_LEGS,17,"Kyatt"),
        GLOVES_SILENCE(Item(10115),600, GLOVES_OF_SILENCE,22,"Dark kebbit"),
        SPOT_CAPE(Item(10125),400, SPOTTED_CAPE,23,"Spotted kebbit"),
        DASH_CAPE(Item(10127),800, SPOTTIER_CAPE,24,"Dashing kebbit")
    }
}