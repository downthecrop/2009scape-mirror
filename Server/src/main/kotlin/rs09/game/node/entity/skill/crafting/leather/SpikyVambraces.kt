package rs09.game.node.entity.skill.crafting.leather

import api.Container
import api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import org.rs09.consts.Items
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles attaching Kebbit Claws to Vambraces
 * to create "Spiky Vambraces"
 * @author downthecrop
 */

@Initializable
class SpikyVambraces: UseWithHandler(Items.KEBBIT_CLAWS_10113) {

    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.LEATHER_VAMBRACES_1063, ITEM_TYPE, this)
        addHandler(Items.GREEN_DHIDE_VAMB_1065, ITEM_TYPE, this)
        addHandler(Items.BLUE_DHIDE_VAMB_2487, ITEM_TYPE, this)
        addHandler(Items.RED_DHIDE_VAMB_2489, ITEM_TYPE, this)
        addHandler(Items.BLACK_DHIDE_VAMB_2491, ITEM_TYPE, this)
        return this
    }
    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false
        val vamb = event.usedWith.id
        val player = event.player
        when(vamb){
            Items.LEATHER_VAMBRACES_1063 -> {
                craftVamb(player,vamb,Items.SPIKY_VAMBRACES_10077,"leather")
            }
            Items.GREEN_DHIDE_VAMB_1065 -> {
                craftVamb(player,vamb,Items.GREEN_SPIKY_VAMBS_10079,"green dragonhide")
            }
            Items.BLUE_DHIDE_VAMB_2487 -> {
                craftVamb(player,vamb,Items.BLUE_SPIKY_VAMBS_10081,"blue dragonhide")
            }
            Items.RED_DHIDE_VAMB_2489 -> {
                craftVamb(player,vamb,Items.RED_SPIKY_VAMBS_10083,"red dragonhide")
            }
            Items.BLACK_DHIDE_VAMB_2491 -> {
                craftVamb(player,vamb,Items.BLACK_SPIKY_VAMBS_10085,"black dragonhide")
            }
        }
        return true
    }

    private fun craftVamb(player: Player, vamb: Int, product: Int, vambLeather: String){
        if (player.skills.getLevel(Skills.CRAFTING) >= 32){
            if (removeItem(player,vamb,Container.INVENTORY) &&
                removeItem(player,Items.KEBBIT_CLAWS_10113,Container.INVENTORY)
            ) {
                addItem(player,product)
                player.skills.addExperience(Skills.CRAFTING,6.0)
                sendMessage(player, "You carefully attach the sharp claws to the $vambLeather vambraces.")
            }
        }
        else{
            sendMessage(player,"You need a crafting level of 32 to craft this.")
        }
    }
}