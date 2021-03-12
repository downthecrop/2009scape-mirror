package rs09.game.interaction

import core.cache.def.impl.ItemDefinition
import core.game.component.Component
import core.game.component.ComponentDefinition
import core.game.component.ComponentPlugin
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import rs09.game.system.SystemLogger

@Initializable
class FOGRewardsInterface : ComponentPlugin(){
    class ShopItem(val id: Int,val price: Int,val amount: Int)

    override fun handle(player: Player?, component: Component?, opcode: Int, button: Int, slot: Int, itemId: Int): Boolean {
        var choice = ShopItem(0, 0, 0)
        when(button){
            2 -> choice = Druidic_Mage_Top
            7 -> choice = Druidic_Mage_Bottom
            12 -> choice = Druidic_Mage_Hood
            19 -> choice = Combat_Robe_Top
            24 -> choice = Combat_Robe_Bottom
            29 -> choice = Combat_Robe_Hood
            36 -> choice = Battle_Robe_Top
            41 -> choice = Battle_Robe_Bottom
            46 -> choice = Battle_Robe_Hood
            53 -> choice = Green_Coif
            58 -> choice = Blue_Coif
            63 -> choice = Red_Coif
            68 -> choice = Black_Coif
            75 -> choice = Bronze_Gaunt
            80 -> choice = Iron_Gaunt
            85 -> choice = Steel_Gaunt
            90 -> choice = Black_Gaunt
            95 -> choice = Mithril_Gaunt
            100 -> choice = Adamant_Gaunt
            105 -> choice = Rune_Gaunt
            110 -> choice = Dragon_Gaunt
            117 -> choice = Addy_Spike
            122 -> choice = Addy_Beserk
            127 -> choice = Rune_Spike
            132 -> choice = Rune_Beserk
            139 -> choice = Irit_Gloves
            144 -> choice = Avantoe_Gloves
            149 -> choice = Kwuarm_Gloves
            154 -> choice = Cadantine_Gloves
            161 -> choice = Swordfish_Gloves
            166 -> choice = Shark_Gloves
            171 -> choice = Dragon_Gloves
            176 -> choice = Air_Gloves
            181 -> choice = Water_Gloves
            186 -> choice = Earth_Gloves
            else -> SystemLogger.logWarn("Unhandled button ID for FOG interface: $button").also { return true }
        }
        handleOpcode(choice,opcode,player!!)
        return true
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        ComponentDefinition.forId(732).plugin = this
        return this
    }

    private fun handleOpcode(item: ShopItem, opcode: Int, player: Player){
        when(opcode){
            155 -> player.sendMessage("${ItemDefinition.forId(item.id).name.replace("100","")}: costs ${item.price} tokens.")
            196 -> handleBuyOption(item,1,player)
            124 -> handleBuyOption(item,5,player)
            199 -> handleBuyOption(item,10,player)
            234 -> player.sendMessage(ItemDefinition.forId(item.id).examine.replace("100",""))
        }
    }

    private fun handleBuyOption(item: ShopItem, amount: Int, player: Player){
        val neededTokens = Item(12852,item.price * amount)
        if(player.inventory.containsItem(neededTokens)){
            if(player.inventory.hasSpaceFor(Item(item.id,amount))){
                player.inventory.remove(neededTokens)
                player.inventory.add(Item(item.id,amount))
            } else {
                player.sendMessage("You don't have enough space in your inventory.")
            }
        }
    }


        val Druidic_Mage_Top = (ShopItem(12894, 300, 1))
        val Druidic_Mage_Hood = (ShopItem(12887, 100, 1))
        val Druidic_Mage_Bottom = (ShopItem(12901, 200, 1))
        val Combat_Robe_Top = (ShopItem(12971, 150, 1))
        val Combat_Robe_Hood = (ShopItem(12964, 50, 1))
        val Combat_Robe_Bottom = (ShopItem(12978, 100, 1))
        val Battle_Robe_Hood = (ShopItem(12866, 250, 1))
        val Battle_Robe_Top = (ShopItem(12873, 1500, 1))
        val Battle_Robe_Bottom = (ShopItem(12880, 1000, 1))
        val Green_Coif  = (ShopItem(12936, 150, 1))
        val Blue_Coif = (ShopItem(12943, 200, 1))
        val Red_Coif = (ShopItem(12950, 300, 1))
        val Black_Coif = (ShopItem(12957, 500, 1))
        val Bronze_Gaunt = (ShopItem(12985, 15, 1))
        val Iron_Gaunt = (ShopItem(12988, 30, 1))
        val Steel_Gaunt = (ShopItem(12991, 50, 1))
        val Black_Gaunt = (ShopItem(12994, 75, 1))
        val Mithril_Gaunt = (ShopItem(12997, 100, 1))
        val Adamant_Gaunt = (ShopItem(13000, 150, 1))
        val Rune_Gaunt = (ShopItem(13003, 200, 1))
        val Dragon_Gaunt = (ShopItem(13006, 300, 1))
        val Addy_Spike = (ShopItem(12908, 50, 1))
        val Addy_Beserk = (ShopItem(12915, 100, 1))
        val Rune_Spike = (ShopItem(12922, 200, 1))
        val Rune_Beserk = (ShopItem(12929, 300, 1))
        val Air_Gloves = (ShopItem(12863, 75, 1))
        val Water_Gloves = (ShopItem(12864, 75, 1))
        val Earth_Gloves = (ShopItem(12865, 75, 1))
        val Irit_Gloves = (ShopItem(12856, 75, 1))
        val Avantoe_Gloves = (ShopItem(12857, 100, 1))
        val Kwuarm_Gloves = (ShopItem(12858, 200, 1))
        val Cadantine_Gloves = (ShopItem(12859, 200, 1))
        val Swordfish_Gloves = (ShopItem(12860, 200, 1))
        val Shark_Gloves = (ShopItem(12861, 200, 1))
        val Dragon_Gloves = (ShopItem(12862, 200, 1))
}