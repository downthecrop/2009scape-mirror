package rs09.game.content.dialogue.region.varrock

import core.game.component.Component
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Dialogue for Asyff, the fancy dress shop owner
 * @author Ceikry
 */
@Initializable
class AsyffDialogue(player: Player? = null) : DialoguePlugin(player){
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> player(FacialExpression.ASKING,"Err...what are you saying exactly?").also { stage++ }
            1 -> npc(FacialExpression.LAUGH,"I'm just saying that perhaps you would","like to peruse my selection of garments.").also { stage++ }
            2 -> npc(FacialExpression.LAUGH,"Or, if that doesn't interest you, then maybe you have","something else to offer? I'm always on the look out","for interesting or unusual new materials.").also { stage++ }
            3 -> options("Okay, let's see what you've got then.","Can you make clothing suitable for hunting in?","I think I might just leave the perusing for now thanks.","What sort of unusual materials did you have in mind?").also { stage++ }
            4 -> when(buttonId){
                1 -> player("Okay, let's see what you've got then.").also { stage = 10 }
                2 -> player("Can you make clothing suitable for hunting?").also { stage = 20 }
                3 -> player("I think I might just leave the perusing for now, thanks.").also { stage = 30 }
                4 -> player("What sort of unusual materials did you have in mind?").also { stage = 40 }
            }

            //Okay, let's see what you've got then
            10 -> end().also { npc.openShop(player) }

            //Can you make clothing suitable for hunting
            20 -> end().also { player.interfaceManager.open(Component(477)) }//Open custom fur clothing interface

            //I think I might just leave the perusing for now, thanks.
            30 -> end()

            //What sort of unusual materials do you have in mind?
            40 -> npc("Well, some more colourful feathers might be useful.","For some surreal reason, all I normally seem to get offered","are large quantities of rather beaten up chicken feathers.").also { stage++ }
            41 -> npc("People must have some very strange pastimes around","these parts, that's all I can say.").also { stage++ }
            42 -> player("Ok, let's see what you've got then.").also { stage = 10 }
        }
        return true
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FacialExpression.HAPPY,"Now you look like someone who goes to","a lot of fancy dress parties.")
        stage = 0
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return AsyffDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(554)
    }
}