package content.minigame.barbassault

import core.api.Container
import core.api.addItem
import core.api.inInventory
import core.api.removeItem
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*

@Initializable
class CaptainCainDialogue(player: Player? = null) : DialoguePlugin(player) {
    companion object {
        const val TORSO_PRICE = 4_500_000
    }

    val sdf = SimpleDateFormat("ddMMyyyy")
    override fun newInstance(player: Player?): DialoguePlugin {
        return CaptainCainDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.FRIENDLY, "Hello, there, adventurer. Say, you wouldn't happen to be interested in purchasing a Fighter Torso would you?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val start = sdf.parse("27112020").toInstant()
        val now = Date().toInstant()
        val days = ChronoUnit.DAYS.between(start,now)
        when(stage){
            0 -> npcl(FacialExpression.ANNOYED,"I'm having to offer this service because it's been $days days since Ryan promised to give us barbarian assault.").also { stage++ }
            1 -> options("Yes, please.","No, thanks.").also { stage++ }
            2 -> when(buttonId){
                1 -> playerl(FacialExpression.FRIENDLY, "Yes, please.").also { stage = 10 }
                2 -> playerl(FacialExpression.HALF_THINKING, "No, thanks.").also { stage = END_DIALOGUE }
            }

            10 -> npcl(FacialExpression.FRIENDLY, "Alright, then, that'll be %,d gold please.".format(TORSO_PRICE)).also { stage++ }
            11 -> options("Here you go!","Nevermind.").also { stage++ }
            12 -> when(buttonId){
                1 -> if(inInventory(player, 995, TORSO_PRICE))
                    playerl(FacialExpression.FRIENDLY, "Here you go!").also { stage = 20 }
                else
                    playerl(FacialExpression.HALF_GUILTY, "Actually, I don't have that much.").also { stage = END_DIALOGUE }

                2 -> playerl(FacialExpression.FRIENDLY, "On second thought, never mind.").also { stage = END_DIALOGUE }
            }

            20 -> {
                npcl(FacialExpression.FRIENDLY, "Thank you much, kind sir. And here's your torso.")
                if(removeItem(player, Item(995, TORSO_PRICE), Container.INVENTORY)) {
                    addItem(player, Items.FIGHTER_TORSO_10551, 1)
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