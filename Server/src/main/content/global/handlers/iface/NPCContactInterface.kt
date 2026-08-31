package content.global.handlers.iface

import content.data.Quests
import content.region.kandarin.yanille.quest.thehandinthesand.dialogue.BertNPCContactDialogueFile
import core.api.isQuestComplete
import core.api.removeAttribute
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import org.rs09.consts.NPCs
import core.game.dialogue.DialogueFile
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player

class NPCContactInterface : InterfaceListener {
    val contactNPCs = arrayOf(
        NPCs.HONEST_JIMMY_4362,
        NPCs.BERT_3108,
        NPCs.ADVISOR_GHRIM_1375,
        NPCs.TURAEL_8273,
        NPCs.LANTHUS_1526,
        NPCs.SUMONA_7780,
        NPCs.MAZCHNA_8274,
        NPCs.DURADEL_8275,
        NPCs.VANNAKA_1597,
        NPCs.MURPHY_464,
        NPCs.CHAELDAR_1598,
        NPCs.CYRISUS_432,
        NPCs.LARRY_5424
    )
    val DialogueFiles = arrayOf<DialogueFile?>(
        /*TODO("Honest Jimmy"),
        TODO("ADVISOR GHRIM"),
        TODO("Turael"),
        TODO("LANTHUS"),
        TODO("Sumona"),
        TODO("Mazchna"),
        TODO("Duradel"),
        TODO("Vannaka"),
        TODO("Murphy"),
        TODO("Chaeldar"),
        TODO("Cyrisus"),
        TODO("Larry")*/
        )

    private fun canContact(player: Player, index: Int): Boolean {
        return when (contactNPCs[index]) {
            // Hidden until their required content is implemented.
            NPCs.HONEST_JIMMY_4362, // Trouble Brewing
            NPCs.ADVISOR_GHRIM_1375, // Managing Miscellania
            NPCs.LANTHUS_1526, // Castle Wars
            NPCs.SUMONA_7780, // Smoking Kills
            NPCs.MURPHY_464, // Hook up after !2361 is merged.
            NPCs.CYRISUS_432 -> false // Dream Mentor

            // Add NPC Contact unlock checks here.
            NPCs.BERT_3108 -> BertNPCContactDialogueFile.canContact(player)
            NPCs.CHAELDAR_1598 -> isQuestComplete(player, Quests.LOST_CITY)

            // TODO: Gate Larry after Penguin Hide and Seek intro dialogue is implemented.
            NPCs.LARRY_5424 -> true

            else -> true
        }
    }

    private fun getContactDialogue(index: Int): DialogueFile? {
        return when (contactNPCs[index]) {
            // Add custom NPC Contact dialogue files here.
            NPCs.BERT_3108 -> BertNPCContactDialogueFile()
            else -> DialogueFiles.getOrNull(index)
        }
    }

    val INTER = 429
    override fun defineInterfaceListeners() {

        //Remove a bunch of the buttons/heads so that people don't
        //waste runes on spells that aren't implemented
        //TODO: Re-enable NPCs as their respective content gets added
        onOpen(INTER){player, _ ->
            //Honest Jimmy: Trouble Brewing
            player.packetDispatch.sendInterfaceConfig(429,10,true)
            player.packetDispatch.sendInterfaceConfig(429,38,true)

            //Bert the Sandman: Hand in the Sand quest
            val showBert = BertNPCContactDialogueFile.canContact(player)
            player.packetDispatch.sendInterfaceConfig(429,11, !showBert)
            player.packetDispatch.sendInterfaceConfig(429,39, !showBert)

            //Advisor Ghrim: Kingdoms of Miscellania
            player.packetDispatch.sendInterfaceConfig(429,12,true)
            player.packetDispatch.sendInterfaceConfig(429,40,true)

            //Lanthus: Castle Wars
            player.packetDispatch.sendInterfaceConfig(429,17,true)
            player.packetDispatch.sendInterfaceConfig(429,43,true)

            //Sumona: Completion of Smoking Kills
            player.packetDispatch.sendInterfaceConfig(429,27,true)
            player.packetDispatch.sendInterfaceConfig(429,42,true)

            // Murphy: Fishing Trawler
            player.packetDispatch.sendInterfaceConfig(429, 30, true)
            player.packetDispatch.sendInterfaceConfig(429, 47, true)

            // Chaeldar has a duplicate/stale interface child 31
            val showChaeldar = canContact(player, 10)
            player.packetDispatch.sendInterfaceConfig(429, 29, !showChaeldar)
            player.packetDispatch.sendInterfaceConfig(429, 48, !showChaeldar)

            val showCyrisus = canContact(player, 11)
            player.packetDispatch.sendInterfaceConfig(429, 32, !showCyrisus)
            player.packetDispatch.sendInterfaceConfig(429, 33, !showCyrisus)

            return@onOpen true
        }

        onClose(INTER) { player, _ ->
            removeAttribute(player, "contact-caller")
            return@onClose true
        }

        on(INTER){player, _, _, buttonID, _, _ ->
            val index = when(buttonID) {
                10,38 -> 0
                11,39 -> 1
                12,40 -> 2
                13,41 -> 3
                17,43 -> 4
                27,42 -> 5
                18,44 -> 6
                23,45 -> 7
                28,46 -> 8
                30,47 -> 9
                29,48 -> 10
                32,33 -> 11
                34,49 -> 12
                else -> -1
            }
            if (index == -1) {
                // TODO: "Random will contact any of 26 other NPCs around RuneScape, and appears not to be "random" in that it cycles through 26 different NPCs.
                //  The spell may start going in order randomly or just jump around. Using Random requires completion of Dream Mentor."
                return@on true
            }

            if (!canContact(player, index)) {
                return@on true
            }

            val npcId = contactNPCs[index]
            val dialogueFile = getContactDialogue(index)
            val contactCaller = player.getAttribute<() -> Unit>("contact-caller")

            removeAttribute(player, "contact-caller")
            contactCaller?.invoke()
            player.interfaceManager.close()

            if(dialogueFile != null) {
                player.dialogueInterpreter.open(dialogueFile, NPC(npcId))
            } else {
                player.dialogueInterpreter.open(npcId, NPC(npcId, Location(0, 0)))
            }

            return@on true
        }

    }
}
