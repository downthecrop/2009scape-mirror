package content.region.asgarnia.falador.quest.recruitmentdrive

import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.DialoguePlugin
import core.game.dialogue.FacialExpression
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import org.rs09.consts.Components
import org.rs09.consts.NPCs

class LadyTableDialogue (player: Player? = null) : DialoguePlugin(player) {
    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        openDialogue(player, LadyTableDialogueFile(), npc)
        return true
    }
    override fun newInstance(player: Player): DialoguePlugin {
        return LadyTableDialogue(player)
    }
    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LADY_TABLE_2283)
    }
}

class LadyTableDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile(), InteractionListener {
    companion object {
        const val statueVarbit = 658
        const val attributeStatueStateNumber = "quest:recruitmentdrive-statuestatenumber"
        val statueArray = intArrayOf(0, 7308, 7307, 7306, 7305, 7304, 7303, 7312, 7313, 7314, 7311, 7310, 7309)
    }

    override fun defineListeners() {

        on(statueArray, IntType.SCENERY, "touch") { player, node ->
            if( node.id == statueArray[getAttribute(player, attributeStatueStateNumber, 0)]) {
                if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0) {
                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, 1)
                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, false, "Excellent work, @name.", "Please step through the portal to meet your next", "challenge.")
                    return@on true
                }
            } else {
                if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 0) {
                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, -1)
                    openDialogue(player, LadyTableDialogueFile(2), NPC(NPCs.LADY_TABLE_2283))
                    return@on true
                }
            }
            if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == 1) {
                sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, false, "Please step through the portal to meet your next", "challenge.")
            }
            if (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == -1) {
                openDialogue(player, LadyTableDialogueFile(2), NPC(NPCs.LADY_TABLE_2283))
            }
            return@on true
        }

    }
    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> dialogueNum == 1 }
                .endWith { _, player ->
                    submitWorldPulse(object : Pulse() {
                        var counter = 0
                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    lock(player, 15)
                                    setAttribute(player, attributeStatueStateNumber, (1..12).random())
                                    setVarbit(player, statueVarbit, getAttribute(player, attributeStatueStateNumber, 0))
                                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, true,"Welcome, @name.", "This room will test your observation skills.")
                                }
                                5 -> {
                                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, true, "Study the statues closely.", "There is one missing statue in this room.")
                                }
                                10 -> {
                                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, true, "We will also mix the order up a little, to make things", "interesting for you!")
                                }
                                15 -> {
                                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, true,"You have 10 seconds to memorise the statues... starting", "NOW!")
                                }
                                20 -> {
                                    closeDialogue(player)
                                }
                                31 -> { // From 15 -> 16 * 600ms = 10 seconds
                                    openOverlay(player,Components.FADE_TO_BLACK_120)
                                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, true,"We will now dim the lights and bring the missing statue", "back in.")
                                }
                                34 -> { // From 15 -> 16 * 600ms = 10 seconds
                                    setVarbit(player, statueVarbit, 0)
                                    openOverlay(player,Components.FADE_FROM_BLACK_170)
                                    sendNPCDialogueLines(player, NPCs.LADY_TABLE_2283, FacialExpression.NEUTRAL, true,"Please touch the statue you think has been added.")
                                    return true
                                }
                            }
                            return false
                        }
                    })
                }

        b.onPredicate { player -> dialogueNum == 2 || (getAttribute(player, RecruitmentDrive.attributeStagePassFailState, 0) == -1) }
                .betweenStage { _, player, _, _ ->
                    setAttribute(player, RecruitmentDrive.attributeStagePassFailState, -1)
                }
                .npc(FacialExpression.SAD, "No... I am very sorry.", "Apparently you are not up to the challenge.", "I will return you where you came from, better luck in the", "future.")
                .endWith { _, player ->
                    removeAttribute(player, attributeStatueStateNumber)
                    removeAttribute(player, RecruitmentDrive.attributeStagePassFailState)
                    RecruitmentDriveListeners.FailTestCutscene(player).start()
                }
    }
}