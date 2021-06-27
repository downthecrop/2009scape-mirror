package rs09.game.content.dialogue

import api.ContentAPI
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

@Initializable
class CaptainCainDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return CaptainCainDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.FRIENDLY, "Hello, there, adventurer. Say, you wouldn't happen to be interested in purchasing a Fighter Torso would you?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> options("Yes, please.","No, thanks.").also { stage++ }
            1 -> when(buttonId){
                1 -> playerl(FacialExpression.FRIENDLY, "Yes, please.").also { stage = 10 }
                2 -> playerl(FacialExpression.HALF_THINKING, "No, thanks.").also { stage = END_DIALOGUE }
            }

            10 -> npcl(FacialExpression.FRIENDLY, "Alright, then, that'll be 7,500,000 gold please.").also { stage++ }
            11 -> options("Here you go!","Nevermind.").also { stage++ }
            12 -> when(buttonId){
                1 -> if(ContentAPI.inInventory(player, 995, 7500000))
                    playerl(FacialExpression.FRIENDLY, "Here you go!").also { stage = 20 }
                else
                    playerl(FacialExpression.HALF_GUILTY, "Actually, I don't have that much.").also { stage = END_DIALOGUE }

                2 -> playerl(FacialExpression.FRIENDLY, "On second thought, nevermind.").also { stage = END_DIALOGUE }
            }

            20 -> {
                npcl(FacialExpression.FRIENDLY, "Thank you much, kind sir. And here's your torso.")
                if(ContentAPI.removeItem(player, Item(995,7500000), api.Container.INVENTORY)) {
                    ContentAPI.addItem(player, Items.FIGHTER_TORSO_10551, 1)
                }
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_CAIN_5030)
    }

}