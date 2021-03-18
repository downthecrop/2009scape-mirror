package rs09.game.interaction.npc

import core.game.node.entity.npc.NPC
import rs09.game.content.activity.gnomecooking.*
import rs09.game.content.ame.RandomEvents
import rs09.game.interaction.InteractionListener

/**
 * Handles the NPC talk-to option.
 * @author Ceikry
 */
class NPCTalkListener : InteractionListener() {

    override fun defineListeners() {

        on(NPC,"talk-to","talk"){player,node ->
            val npc = node.asNpc()
            if(RandomEvents.randomIDs.contains(node.id)){
                if(player.antiMacroHandler.event == null || player.antiMacroHandler.event.id != node.id){
                    player.sendMessage("They aren't interested in talking to you.")
                } else {
                    player.antiMacroHandler.event.talkTo(node.asNpc())
                }
                return@on true
            }
            if (!npc.getAttribute("facing_booth", false)) {
                npc.faceLocation(player.location)
            }
            //I'm sorry for this but it was honestly the best way to do this
            if (player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_JOB_ORDINAL, -1) != -1) {
                val job = GnomeCookingJob.values()[player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_JOB_ORDINAL, -1)]
                if (node.getId() == job.npc_id && !player.getAttribute(GC_BASE_ATTRIBUTE + ":" + GC_JOB_COMPLETE, false)) {
                    player.dialogueInterpreter.open(GCCompletionDialogue(job))
                    return@on true
                }
            }
            return@on player.dialogueInterpreter.open(npc.id, npc)
        }

    }

    override fun defineDestinationOverrides() {
        setDest(NPC,"talk","talk-to"){node ->
            val npc = node as NPC
            if (npc.getAttribute("facing_booth", false)) {
                val offsetX = npc.direction.stepX shl 1
                val offsetY = npc.direction.stepY shl 1
                return@setDest npc.location.transform(offsetX, offsetY, 0)
            }
            return@setDest node.location
        }
    }
}