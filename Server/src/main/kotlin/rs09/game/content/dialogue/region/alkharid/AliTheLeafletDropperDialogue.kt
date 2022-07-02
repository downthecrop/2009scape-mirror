package rs09.game.content.dialogue.region.alkharid

import api.addItem
import core.game.content.dialogue.DialoguePlugin
import core.game.content.dialogue.FacialExpression
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import rs09.tools.END_DIALOGUE

/**
 * @author bushtail
 * Converts AliTheLeafletDropper.java with a Kotlin implementation
 */

@Initializable
class AliTheLeafletDropperDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun newInstance(player: Player?): DialoguePlugin {
        return AliTheLeafletDropperDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FacialExpression.CHILD_NORMAL, "I don't have the time to talk right now! Ali Morrisane is paying me to hand out these flyers.")
        stage = 100
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            100 -> options(
                "Who is Ali Morissane?",
                "What are the flyers for?",
                "What is there to do round here, boy?"
            ).also { stage++ }
            101 -> when(buttonId) {
                1 -> {
                    player(FacialExpression.ASKING, "Who is Ali Morissane?").also{ stage = 201 }
                }
                2 -> {
                    player(FacialExpression.ASKING, "What are the flyers for?").also { stage = 301 }
                }
                3 -> {
                    player(FacialExpression.ASKING, "What is there to do round here, boy?").also { stage = 401 }
                }
            }

            201 -> npcl(FacialExpression.CHILD_FRIENDLY, "Ali Morrisane is the greatest merchant in the east!").also { stage++ }
            202 -> player(FacialExpression.HALF_ASKING, "Were you paid to say that?").also { stage++ }
            203 -> npcl(FacialExpression.CHILD_LOUDLY_LAUGHING, "Of course I was! You can find him on the north edge of town.").also { stage = END_DIALOGUE }

            301 -> npcl(FacialExpression.CHILD_THINKING, "Well, Ali Morrisane isn't too popular with the other traders in Al Kharid, mainly because he's from Pollnivneach and they feel he has no business trading in their town.").also { stage++ }
            302 -> npcl(FacialExpression.CHILD_FRIENDLY, "I think they're just sour because he's better at making money than them.").also { stage++ }
            303 -> npcl(FacialExpression.CHILD_FRIENDLY, "The flyer advertises the different shops you can find in Al Kharid.").also { stage++ }
            304 -> npcl(FacialExpression.CHILD_THINKING, "It also entitles you to money off your next purchase in any of the shops listed on it. It's Ali's way of getting on the good side of the traders.").also { stage++ }
            305 -> player(FacialExpression.ASKING, "Which shops?").also {
                if(player.inventory.containItems(Items.AL_KHARID_FLYER_7922)) {
                    npcl(FacialExpression.CHILD_SUSPICIOUS, "Are you trying to be funny or has age turned your brain to mush? Look at the flyer you already have!").also { stage = END_DIALOGUE }
                } else {
                    if(addItem(player, Items.AL_KHARID_FLYER_7922)) {
                        npcl(FacialExpression.CHILD_FRIENDLY, "Here! Take one and let me get back to work.").also { stage = END_DIALOGUE }
                    } else {
                        end()
                    }
                }
            }

            401 -> npcl(FacialExpression.CHILD_NORMAL, "I'm very busy, so listen carefully! I shall say this only once.").also { stage++ }
            402 -> npcl(FacialExpression.CHILD_THINKING, "Apart from a busy and wonderous market place in Al Kharid to the south, there is the Duel Arena to the south-east where you can challenge other players to a fight.").also { stage++ }
            403 -> npcl(FacialExpression.CHILD_NORMAL, "If you're here to make money, there is a mine to the south.").also { stage++ }
            404 -> npcl(FacialExpression.CHILD_SUSPICIOUS, "Watch out for scorpions though, they'll take a pop at you if you go too near them. To avoid them just follow the western fence as you travel south.").also { stage++ }
            405 -> npcl(FacialExpression.CHILD_FRIENDLY, "If you're in the mood for a little rest and relaxation, there are a couple of nice fishing spots south of the town.").also { stage++ }
            406 -> player(FacialExpression.FRIENDLY, "Thanks for the help!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALI_THE_LEAFLET_DROPPER_3680)
    }
}