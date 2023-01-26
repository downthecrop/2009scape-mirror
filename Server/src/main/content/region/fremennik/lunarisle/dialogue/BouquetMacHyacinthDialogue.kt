package content.region.fremennik.lunarisle.dialogue

import core.api.teleport
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author qmqz
 */

@Initializable
class BouquetMacHyacinthDialogue(player: Player? = null) : core.game.dialogue.DialoguePlugin(player){

    private val Rellekka = Location.create(2663, 3644, 0)
    private var teled = false

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (!teled) {
            if (player.inventory.contains(Items.SEAL_OF_PASSAGE_9083, 1) || player.equipment.contains(Items.SEAL_OF_PASSAGE_9083, 1)) {
                player(core.game.dialogue.FacialExpression.FRIENDLY, "Hi! What are you up to?").also { stage = 0 }
            } else {
                player(core.game.dialogue.FacialExpression.FRIENDLY, "Hi, I...").also { stage = 5 }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(core.game.dialogue.FacialExpression.FRIENDLY,"Watering the pretty flowers, you want to help?").also { stage++ }
            1 -> playerl(core.game.dialogue.FacialExpression.HALF_WORRIED, "I don't have time to water flowers, I have people to save!").also { stage++ }
            2 -> npcl(core.game.dialogue.FacialExpression.NEUTRAL, "Pft, you should take time to enjoy the simple things.").also { stage++ }
            3 -> player(core.game.dialogue.FacialExpression.NEUTRAL, "I'm not a simple person.").also { stage++ }
            4 -> npc(core.game.dialogue.FacialExpression.LAUGH, "So it seems.").also { stage = 99 }

            5 -> npc(core.game.dialogue.FacialExpression.ANNOYED, "What are you doing here, Fremennik?!").also { stage++ }
            6 -> player(core.game.dialogue.FacialExpression.WORRIED, "I have a seal of pass...").also { stage++ }
            7 -> npc(core.game.dialogue.FacialExpression.ANNOYED, "No you do not! Begone!").also { stage++ }
            8 -> teleport(player, Rellekka, TeleportManager.TeleportType.LUNAR).also { wait1() }

            99 -> end()
        }
        return true
    }

    private fun wait1() {
            Executors.newSingleThreadScheduledExecutor().schedule({
                if (player.location.isInRegion(Rellekka.regionId)) {
                    playerl(
                        core.game.dialogue.FacialExpression.WORRIED,
                        "Ooops. Suppose I need a seal of passage when I'm walking around that island."
                    ).also { end() }
                } else {
                    wait1()
                }
            }, 1, TimeUnit.SECONDS)
    }

    override fun newInstance(player: Player?): core.game.dialogue.DialoguePlugin {
        return BouquetMacHyacinthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BOUQUET_MAC_HYACINTH_4526)
    }
}