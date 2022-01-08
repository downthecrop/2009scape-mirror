package rs09.game.content.dialogue.region.lunarisle

import api.teleport
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
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
class SeleneDialog(player: Player? = null) : DialoguePlugin(player){

    private val Rellekka = Location.create(2663, 3644, 0)
    private var teled = false

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (!teled) {
            if (player.inventory.contains(Items.SEAL_OF_PASSAGE_9083, 1) || player.equipment.contains(Items.SEAL_OF_PASSAGE_9083, 1)) {
                player(FacialExpression.FRIENDLY, "Can you tell me a bit about your people?").also { stage = 0; }
            } else {
                player(FacialExpression.FRIENDLY, "Hi, I...").also { stage = 10 }
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            0 -> npc(FacialExpression.FRIENDLY,"Ok. Like what?").also { stage++ }
            1 -> player(FacialExpression.HALF_ASKING, "How about the values of the Moon clan!").also { stage++ }
            2 -> npcl(FacialExpression.NEUTRAL, "Let me see... We value knowledge of self because it is this that gives us our strength! It is most important!").also { stage++ }
            3 -> playerl(FacialExpression.FRIENDLY, "Well... I know things about myself. I know I like hot chocolate!").also { stage++ }
            4 -> npcl(FacialExpression.LAUGH, "I was meaning something a little deeper than that. We also like to see someone listen. You know how they say a wise man listens?").also { stage++ }
            5 -> player(FacialExpression.HALF_WORRIED, "....").also { stage++ }
            6 -> npc(FacialExpression.HALF_WORRIED, "Did you hear me?").also { stage++ }
            7 -> player(FacialExpression.HALF_WORRIED, ".... I'm listening.").also { stage++ }
            8 -> npc(FacialExpression.HALF_WORRIED, "Most wise.").also { stage++ }
            9 -> player(FacialExpression.HALF_WORRIED, "Huh?").also { stage = 99 }

            10 -> npc(FacialExpression.ANNOYED, "What are you doing here, Fremennik?!").also { stage++ }
            11 -> player(FacialExpression.WORRIED, "I have a seal of pass...").also { stage++ }
            12 -> npc(FacialExpression.ANNOYED, "No you do not! Begone!").also { stage++ }
            13 -> teleport(player, Rellekka, TeleportManager.TeleportType.LUNAR).also { wait1() }

            99 -> end()
        }
        return true
    }

    private fun wait1() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            if (player.location.isInRegion(Rellekka.regionId)) {
                playerl(FacialExpression.WORRIED, "Ooops. Suppose I need a seal of passage when I'm walking around that island.").also { end() }
            } else {
                wait1()
            }
        }, 1, TimeUnit.SECONDS)
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return SeleneDialog(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SELENE_4517)
    }
}