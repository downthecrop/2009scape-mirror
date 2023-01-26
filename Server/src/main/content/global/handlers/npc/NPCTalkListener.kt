package content.global.handlers.npc

import core.api.sendMessage
import content.region.kandarin.barcrawl.BarcrawlManager
import content.region.kandarin.barcrawl.BarcrawlType
import content.global.ame.RandomEvents
import content.minigame.gnomecooking.*
import core.game.interaction.InteractionListener
import core.game.interaction.IntType

/**
 * Handles the NPC talk-to option.
 * @author Ceikry
 */
class NPCTalkListener : InteractionListener {

    val barCrawlNPCs = intArrayOf(733,848,735,739,737,738,731,568,3217,736,734)

    override fun defineListeners() {
        on(barCrawlNPCs, IntType.NPC, "talk-to", "talk"){ player, node ->
            val type = BarcrawlType.forId(node.id)
            val instance = BarcrawlManager.getInstance(player)
            if (instance.isFinished || !instance.isStarted || instance.isCompleted(type!!.ordinal)) {
                player.dialogueInterpreter.open(node.id, node)
            } else {
                player.dialogueInterpreter.open("barcrawl dialogue", node.id, type)
            }
            return@on true
        }

        on(IntType.NPC,"talk-to","talk","talk to"){ player, node ->
            val npc = node.asNpc()
            if(RandomEvents.randomIDs.contains(node.id)){
                if(content.global.ame.RandomEventManager.getInstance(player)!!.event == null || content.global.ame.RandomEventManager.getInstance(player)!!.event!! != node.asNpc()){
                    player.sendMessage("They aren't interested in talking to you.")
                } else {
                    content.global.ame.RandomEventManager.getInstance(player)!!.event!!.talkTo(node.asNpc())
                }
                return@on true
            }
            if (!npc.getAttribute("facing_booth", false)) {
                npc.faceLocation(player.location)
            }
            // ---------------- THESE DIALOGUES ARE INAUTHENTIC BUT ARE COPING FOR A CORE ISSUE -----------------------
            if (player.properties.combatPulse.getVictim() == npc) {
                sendMessage(player, "I don't think they have any interest in talking to me right now!")
                return@on true
            }
            if (npc.inCombat()) {
                sendMessage(player, "They look a bit busy at the moment.")
                return@on true
            }
            //---------------------------------------------------------------------------------------------------------
            //I'm sorry for this but it was honestly the best way to do this
            if (player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1) != -1) {
                val job = GnomeCookingJob.values()[player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1)]
                if (node.getId() == job.npc_id && !player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_COMPLETE", false)) {
                    player.dialogueInterpreter.open(GCCompletionDialogue(job))
                    return@on true
                }
            }
            return@on player.dialogueInterpreter.open(npc.id, npc)
        }
    }
}