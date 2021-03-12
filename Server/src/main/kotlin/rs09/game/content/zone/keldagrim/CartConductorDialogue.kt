package rs09.game.content.zone.keldagrim

import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.Initializable

private const val ICE_MOUNTAIN_CONDUCTOR = 2180
private const val WHITE_WOLF_CONDUCTOR = 2181
private const val KELDAGRIM_CONDUCTOR = 2182

@Initializable
class CartConductorDialogue(player: Player? = null) : DialoguePlugin(player) {
    var visitedKeldagrim = false
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc("Sorry, but I can only take people","who have been there before.").also { stage = 1000 }

            //Start Ice Mountain (Dwarven Mines) Conductor Dialogue
            100 -> npc("Alright, that'll cost ye 150gp.").also { stage++ }
            101 -> options("Okay, sure.","No, thanks.").also { stage++ }
            102 -> when(buttonId){
                1 -> end().also { purchaseTrip(player,150) }
                2 -> end()
            }

            //Start White Wolf Mountain Conductor Dialogue
            200 -> npc("Alright, that'll cost ye 100gp.").also { stage++ }
            201 -> options("Okay, sure.","No, thanks.").also { stage++ }
            202 -> when(buttonId){
                1 -> end().also { purchaseTrip(player,100) }
                2 -> end()
            }

            //Start Keldagrim Conductor Dialogue
            300 -> npc("Alright, where would ye like to go?").also { stage++ }
            301 -> options("Grand Exchange","White Wolf Mountain","Ice Mountain").also { stage++ }
            302 -> when(buttonId){
                1 -> KeldagrimCartMethods.leaveKeldagrimTo(player, Location.create(3140, 3507, 0)).also{ end() }
                2 -> KeldagrimCartMethods.leaveKeldagrimTo(player,Location.create(2875, 9871, 0)).also{ end() }
                3 -> KeldagrimCartMethods.leaveKeldagrimTo(player,Location.create(2997, 9837, 0)).also{ end() }
            }
            1000 -> end()
        }
        return true
    }

    fun purchaseTrip(player: Player, cost: Int){
        val coins = Item(995,cost)
        if(player.inventory.containsItem(coins)){
            player.inventory.remove(coins)
            KeldagrimCartMethods.goToKeldagrim(player)
        } else {
            player.dialogueInterpreter.sendDialogue("You can not afford that.")
        }
    }

    override fun npc(vararg messages: String?): Component {
        return super.npc(FacialExpression.OLD_NORMAL, *messages)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC //get which conductor specifically is being used
        visitedKeldagrim = player.getAttribute("keldagrim-visited",false) //visited keldagrim?

        player("I'd like to use your cart, please.")
        when(npc.id){
            ICE_MOUNTAIN_CONDUCTOR -> stage = if(!visitedKeldagrim) 0 else 100
            WHITE_WOLF_CONDUCTOR -> stage = if(!visitedKeldagrim)   0 else 200
            KELDAGRIM_CONDUCTOR -> stage = 300
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return CartConductorDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(ICE_MOUNTAIN_CONDUCTOR, WHITE_WOLF_CONDUCTOR, KELDAGRIM_CONDUCTOR)
    }

}