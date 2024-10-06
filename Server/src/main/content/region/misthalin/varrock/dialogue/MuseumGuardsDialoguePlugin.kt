package content.region.misthalin.varrock.dialogue

import content.region.misthalin.varrock.handlers.MuseumInteractionListener.Companion.handleMuseumDoor
import core.api.forceWalk
import core.api.getScenery
import core.api.isQuestComplete
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs09.consts.NPCs

@Initializable
class DoorGuardDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        npcl(FacialExpression.NEUTRAL, "Hello there. Come to see the new museum?").also { stage = START_DIALOGUE }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> playerl(FacialExpression.NEUTRAL, "Yes, how do I get in?").also { stage++ }
            1 -> npcl(FacialExpression.NEUTRAL, "Well, the main entrance is 'round the front. Just head west then north slightly, you can't miss it!").also { stage++ }
            2 -> playerl(FacialExpression.NEUTRAL, "What about these doors?").also { stage++ }
            3 -> {
                if (isQuestComplete(player, "The Dig Site")) {
                    npcl(FacialExpression.NEUTRAL, "They're primarily for the workmen bringing finds from the Dig Site, but you can go through if you want.").also { stage++ }
                } else {
                    npcl(FacialExpression.NEUTRAL, "They're for the workmen bringing finds from the Dig Site; sorry, but you can't go through.").also { stage = END_DIALOGUE }
                }
            }
            4 -> playerl(FacialExpression.NEUTRAL, "Okay, thanks.").also { stage++ }
            5 -> {
                end()
                handleMuseumDoor(player, getScenery(3264, 3441, 0))
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return DoorGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUSEUM_GUARD_5943)
    }
}

@Initializable
class GateGuardDialogue(player: Player? = null) : DialoguePlugin(player) {
    override fun open(vararg args: Any?): Boolean {
        // Shows the player walking to this spot first https://www.youtube.com/watch?v=t-oeY3a-ZSA&t=53s
        if (player.location != Location(3261, 3447)) forceWalk(player, Location(3261, 3447), "smart")

        if (isQuestComplete(player, "The Dig Site")) {
            npcl(FacialExpression.NEUTRAL, "Welcome! Would you like to go into the Dig Site archaeology cleaning area?").also { stage = START_DIALOGUE }
        } else {
            npcl(FacialExpression.NEUTRAL, "You're not permitted in this area.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            START_DIALOGUE -> showTopics(
                Topic("Yes, I'll go in!", 1, true),
                Topic("No thanks, I'll take a look around out here.", END_DIALOGUE, true)
            )
            1 -> {
                end()
                handleMuseumDoor(player, getScenery(3261, 3446, 0))
            }
        }
        return true
    }

    override fun newInstance(player: Player?): DialoguePlugin {
        return GateGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MUSEUM_GUARD_5941)
    }
}