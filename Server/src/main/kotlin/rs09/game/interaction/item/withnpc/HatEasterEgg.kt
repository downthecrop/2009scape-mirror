package rs09.game.interaction.item.withnpc

import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.item.Item
import core.game.world.update.flag.context.Graphics
import org.rs09.consts.Items
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InteractionListener

val graphics = 482
class HatEasterEgg :  InteractionListener(){

    val MACHINE = 20040
    val WIZ_HAT = Items.WIZARD_HAT_579
    val NEW_HAT = 14650

    override fun defineListeners() {
        onUseWith(SCENERY,WIZ_HAT,MACHINE){ player, used, with ->
            player.dialogueInterpreter.open(HatDialogue(), NPC(872))
            return@onUseWith true
        }
    }

}

class HatDialogue : DialogueFile(){
    override fun handle(componentID: Int, buttonID: Int) {
        when(stage++){
            0 -> {
                npc("WHAT HAVE YOU DONE?")
                player!!.graphics(Graphics(graphics))
            }
            1 -> player(FacialExpression.AFRAID,"What do you mean?!")
            2 -> npc("You've disjointed the fabric assimilation matrix!")
            3 -> player(FacialExpression.THINKING,"W-what...?")
            4 -> npc("You've put us at risk of ripping Gielinor apart!")
            5 -> player(FacialExpression.HALF_GUILTY,"I.. I just wanted a hat...")
            6 -> npc("Damn you and damn your hat! You could kill us all!")
            7 -> player("I... I'm sorry....")
            8 -> npc("*sigh* I've managed to stabilize the flux material inductors.")
            9 -> npc("You may just be safe, yet.")
            10 -> npc("Here, take your damn hat and get out of here.")
            11 -> {
                end()
                player!!.inventory.remove(Item(Items.WIZARD_HAT_579))
                player!!.inventory.add(Item(14650))
            }
        }
    }
}