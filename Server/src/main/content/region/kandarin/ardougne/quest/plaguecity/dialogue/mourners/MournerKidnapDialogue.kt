package content.region.kandarin.ardougne.quest.plaguecity.dialogue.mourners

import content.data.Quests
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.dialogue.Topic
import core.game.global.action.DoorActionHandler
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs09.consts.Items
import org.rs09.consts.NPCs

/**
 * These are the mourners guarding the kidnap building
 */
@Initializable
class MournerKidnapDialogue(player: Player? = null) :DialoguePlugin(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, MournerKidnapDialogueFile(), npc)
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOURNER_3216)
    }
}

/**
 * Most of the work is in the dialogue file so the player can access this by trying to enter the room
 */
class MournerKidnapDialogueFile : DialogueFile(){

    companion object {
        const val KIDNAP = 10
        const val FEAR = 30
        const val CLEARANCE = 40

        var closeMourner : NPC? = null
        var farMourner : NPC? = null

        val eDoor: Location = Location.create(2540, 3273, 0)
        val wDoor: Location = Location.create(2533, 3273, 0)

        var east = false

    }
    override fun handle(componentID: Int, buttonID: Int) {
        // Figure out who we are. It needs to be this since we can enter this dialogue from a door
        // Do this for the first time regardless of where we are
        if (stage == 0) {
            RegionManager.getLocalNpcs(player!!, 2).forEach {
                if (it.id == NPCs.MOURNER_3216) {
                    closeMourner = it
                    resetFace(closeMourner!!)
                    face(closeMourner!!, player!!)
                    face(player!!, closeMourner!!)
                }
            }

            east = player!!.location.x > 2537
        }
        if (hasAnItem(player!!, Items.WARRANT_1503).exists()){
            when (stage){
                0 -> npcl(
                    FacialExpression.NEUTRAL,
                    " I'd stand away from there. That black cross means that house has been touched by the plague."
                ).also { stage++ }

                1 -> playerl(FacialExpression.FRIENDLY, " I have a warrant from Bravek to enter here.").also { stage++ }
                2 -> npcl(FacialExpression.HALF_WORRIED, " This is highly irregular. Please wait...").also { stage++ }
                3 -> {
                    // Look further for the other one
                    RegionManager.getLocalNpcs(player!!, 10).forEach {
                        if (it.id == NPCs.MOURNER_3216 && it != closeMourner){
                            farMourner = it
                            resetFace(farMourner!!)
                        }
                    }

                    // I don't know why the queue has to be on this mourner to get both talking
                    val mournerQueue = if (east) farMourner else closeMourner

                    queueScript(mournerQueue!!, 1, QueueStrength.WEAK) { animStage: Int ->
                        when(animStage){
                            0 -> {
                                end()
                                forceWalk(player!!, if (east) eDoor else wDoor, "smart")
                                face(closeMourner!!, farMourner!!)
                                // Legit typo
                                sendChat(closeMourner!!, "Hay... I got someone here with a warrant from Bravek, what should we do?")
                                // Immediately set the quest stage in case the player clicks again
                                if (getQuestStage(player!!, Quests.PLAGUE_CITY) < 17){
                                    setQuestStage(player!!, Quests.PLAGUE_CITY, 17)
                                }
                                return@queueScript delayScript(mournerQueue, 1)
                            }
                            3 -> {
                                face(farMourner!!, closeMourner!! )
                                sendChat(farMourner!!, "Well you can't let them in...")
                                return@queueScript delayScript(mournerQueue, 1)
                            }
                            5 -> {
                                resetFace(player!!)
                                return@queueScript delayScript(mournerQueue, 1)
                            }
                            6 -> {
                                // only walk the player if they have not walked themselves through
                                if (player!!.location.y > 3272)
                                    DoorActionHandler.handleAutowalkDoor(player, getScenery (if (east) eDoor else wDoor))
                                sendDialogue(player!!, "You wait until the mourner's back is turned and sneak into the building.").also { stage = END_DIALOGUE}
                                resetFace(closeMourner!!)
                                resetFace(farMourner!!)
                                return@queueScript delayScript(mournerQueue, 1)
                            }
                            10 -> {
                                // Face north again
                                face(closeMourner!!, Location.create(closeMourner!!.location.x, 3275, 0))
                                face(farMourner!!, Location.create(farMourner!!.location.x, 3275, 0))
                                return@queueScript stopExecuting(mournerQueue)
                            }
                        }
                        return@queueScript delayScript(mournerQueue, 1)
                    }

                }

            }
        }
        else {

            when (stage) {
                0 -> npcl(
                    FacialExpression.NEUTRAL,
                    " I'd stand away from there. That black cross means that house has been touched by the plague."
                ).also { stage = if (getQuestStage(player!!, Quests.PLAGUE_CITY) == 11) stage + 1 else END_DIALOGUE }

                1 -> showTopics(
                    Topic("But I think a kidnap victim is in here.", KIDNAP),
                    Topic("I fear not a mere plague.", FEAR),
                    Topic("Thanks for the warning.", END_DIALOGUE),
                )

                KIDNAP -> npcl(
                    FacialExpression.NEUTRAL,
                    "Sounds unlikely, even kidnappers wouldn't go in there. Even if someone is in there, they're probably dead by now."
                ).also { stage++ }

                KIDNAP + 1 -> showTopics(
                    Topic("Good point.", END_DIALOGUE),
                    Topic("I want to check anyway.", KIDNAP + 2)
                )

                KIDNAP + 2 -> npcl(FacialExpression.NEUTRAL, "You don't have clearance to go in there.").also {
                    stage = CLEARANCE
                }

                FEAR -> npcl(
                    FacialExpression.NEUTRAL,
                    " That's irrelevant. You don't have clearance to go in there."
                ).also { stage = CLEARANCE }

                CLEARANCE -> playerl(FacialExpression.ASKING, " How do I get clearance?").also { stage++ }
                CLEARANCE + 1 -> npcl(
                    FacialExpression.NEUTRAL,
                    " Well you'd need to apply to the head mourner, or I suppose Bravek the city warder."
                ).also { stage++ }

                CLEARANCE + 2 -> npcl(FacialExpression.NEUTRAL, " I wouldn't get your hopes up though.").also {
                    stage = END_DIALOGUE
                    setQuestStage(player!!, Quests.PLAGUE_CITY, 12)
                }
            }
        }
    }

}