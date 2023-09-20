package content.region.misc.keldagrim.dialogue

import core.api.*
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.Components
import org.rs09.consts.NPCs

@Initializable
class DwarvenBoatmanForthDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        // TODO: This should be replaced with the Giant Dwarf quest
        if (!getAttribute(player, "/save:keldagrim-visited", false)) {
            when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "Ho there, human!").also { stage++ }
                1 -> playerl(FacialExpression.FRIENDLY, "${player.username}.").also { stage++ }
                2 -> npcl(FacialExpression.OLD_HAPPY, "Ho there, ${player.username}! Want to take a ride with me?").also { stage++ }
                3 -> playerl(FacialExpression.FRIENDLY, "Where are you going? Across the river?").also { stage++ }
                4 -> npcl(FacialExpression.OLD_HAPPY, "No no, that's what the ferryman is for! I'm going to Keldagrim, my home!").also { stage++ }
                5 -> playerl(FacialExpression.FRIENDLY, "How much will that cost me then?").also { stage++ }
                6 -> npcl(FacialExpression.OLD_HAPPY, "For a human like you, I can do it for free!").also { stage++ }
                7 -> showTopics(
                        Topic(FacialExpression.FRIENDLY, "That's a deal!", 8),
                        Topic(FacialExpression.FRIENDLY, "I prefer the mines to the city.", 15),
                )
                8 -> npcl(FacialExpression.OLD_HAPPY, "Excellent! I'm just waiting for my ship to arrive and then we can go.").also { stage++ }
                9 -> npcl(FacialExpression.OLD_HAPPY, "Mind, this trip could take a few minutes! Are you sure you're ready to go as well?").also { stage++ }
                10 -> {
                    setComponentVisibility(player, 228, 6, true)
                    setComponentVisibility(player, 228, 9, false)
                    sendDialogueOptions(player, "(Not Done) Start The Giant Dwarf quest?", "Yes", "No").also { stage++ }
                }
                11 -> when (buttonId) {
                    1 -> playerl(FacialExpression.FRIENDLY, "Yes, I'm ready and don't mind it taking a few minutes.").also { stage = 12 }
                    2 -> playerl(FacialExpression.FRIENDLY, "No, I don't have time right now, I'll be back later.").also { stage = 14 }
                }
                12 -> npcl(FacialExpression.OLD_HAPPY, "Well, let's not waste any more time then!").also { stage++ }
                13 -> end().also {
                    submitWorldPulse(TravelForthPulse(player))
                }
                14 -> npcl(FacialExpression.OLD_HAPPY, "Sure, sure! Come back when you need to!").also {
                    stage = END_DIALOGUE
                }
                15 -> npcl(FacialExpression.OLD_HAPPY, "Well, suit yourself then!").also {
                    stage = END_DIALOGUE
                }
            }
        } else {
            when (stage) {
                START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "Hello again, ${player.username}! Want to go back to Keldagrim?").also { stage++ }
                1 -> showTopics(
                        Topic(FacialExpression.FRIENDLY, "Yes, please take me.", 2),
                        Topic(FacialExpression.FRIENDLY, "What, on your ship? No way.", 3),
                )
                2 -> end().also {
                    submitWorldPulse(TravelForthPulse(player))
                }
                3 -> npcl(FacialExpression.OLD_NORMAL, "Hey, it was only a little accident! Calm down!").also {
                    stage = END_DIALOGUE
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarvenBoatmanForthDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARVEN_BOATMAN_2205) //1844,1843
    }
}

class TravelForthPulse(val player: Player): Pulse(1){
    var counter = 0
    override fun pulse(): Boolean {
        when(counter++){
            0 -> lock(player, 10).also {
                openInterface(player, Components.FADE_TO_BLACK_120)
            }
            3 -> teleport(player, Location.create(2892, 10225, 0))
            4 -> {
                closeInterface(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
            }
            6 -> unlock(player).also {
                closeInterface(player)
                setAttribute(player, "/save:keldagrim-visited", true)
                return true
            }
        }
        return false
    }
}


@Initializable
class DwarvenBoatmanBackDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage){
            START_DIALOGUE -> npcl(FacialExpression.OLD_HAPPY, "Want me to take you back to the mines?").also { stage++ }
            1 -> showTopics(
                    Topic(FacialExpression.FRIENDLY, "Yes, please take me.", 2),
                    Topic(FacialExpression.FRIENDLY, "What, on your ship! No thanks!", 3),
            )
            2 -> end().also { submitWorldPulse(TravelBackPulse(player)) }
            3 -> npcl(FacialExpression.OLD_NORMAL, "Hey now, it was only a slight accident!").also { stage++ }
            4 -> playerl(FacialExpression.FRIENDLY, "A slight accident?? Have you any idea how much time I spent rebuilding that statue?").also { stage++ }
            5 -> npcl(FacialExpression.OLD_NORMAL, "Calm down, calm down! You got what you paid for the trip, didn't you?").also {
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DwarvenBoatmanBackDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.DWARVEN_BOATMAN_2206)
    }
}

class TravelBackPulse(val player: Player): Pulse(1){
    var counter = 0
    override fun pulse(): Boolean {
        when(counter++){
            0 -> lock(player, 10).also {
                openInterface(player, Components.FADE_TO_BLACK_120)
            }
            3 -> teleport(player, Location.create(2838, 10127, 0))
            4 -> {
                closeInterface(player)
                openInterface(player, Components.FADE_FROM_BLACK_170)
            }
            6 -> unlock(player).also {
                closeInterface(player)
                return true
            }
        }
        return false
    }
}
