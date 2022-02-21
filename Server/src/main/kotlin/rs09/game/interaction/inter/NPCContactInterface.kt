package rs09.game.interaction.inter

import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import core.tools.RandomFunction
import org.rs09.consts.NPCs
import rs09.game.content.dialogue.DialogueFile
import rs09.game.interaction.InterfaceListener

class NPCContactInterface : InterfaceListener() {
    val contactNPCs = arrayOf(NPCs.HONEST_JIMMY_4362, NPCs.BERT_3108, NPCs.ADVISOR_GHRIM_1375, NPCs.TURAEL_8273, NPCs.LANTHUS_1526, NPCs.SUMONA_7780, NPCs.MAZCHNA_8274, NPCs.DURADEL_8275, NPCs.VANNAKA_1597, NPCs.DARK_MAGE_2262, NPCs.CHAELDAR_1598, NPCs.CYRISUS_432, NPCs.LARRY_5424)
    val DialogueFiles = arrayOf<DialogueFile?>(
        /*TODO("Honest Jimmy"),
        TODO("Bert"),
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
    val INTER = 429
    override fun defineListeners() {

        //Remove a bunch of the buttons/heads so that people don't
        //waste runes on spells that aren't implemented
        //TODO: Re-enable NPCs as their respective content gets added
        onOpen(INTER){player, component ->
            //Honest Jimmy: Trouble Brewing
            player.packetDispatch.sendInterfaceConfig(429,10,true)
            player.packetDispatch.sendInterfaceConfig(429,38,true)

            //Bert the Sandman: Hand in the Sand quest
            player.packetDispatch.sendInterfaceConfig(429,11,true)
            player.packetDispatch.sendInterfaceConfig(429,39,true)

            //Advisor Ghrim: Kingdoms of Miscellania
            player.packetDispatch.sendInterfaceConfig(429,12,true)
            player.packetDispatch.sendInterfaceConfig(429,40,true)

            //Lanthus: Castle Wars
            player.packetDispatch.sendInterfaceConfig(429,17,true)
            player.packetDispatch.sendInterfaceConfig(429,43,true)

            //Sumona: Completion of Smoking Kills
            player.packetDispatch.sendInterfaceConfig(429,27,true)
            player.packetDispatch.sendInterfaceConfig(429,42,true)

            // Replace Murphy's icon with the Abyss's Dark Mage
            player.packetDispatch.sendNpcOnInterface(NPCs.DARK_MAGE_2262, 429, 30)
            player.packetDispatch.sendString("Dark mage", 429, 47)

            return@onOpen true
        }

        on(INTER){player, _, _, buttonID, _, _ ->
            var index = when(buttonID){
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
            if(index == -1) index = RandomFunction.random(contactNPCs.size)
            val dialogueFile = DialogueFiles.getOrNull(index)
            player.getAttribute<() -> Unit>("contact-caller")?.invoke()
            player.interfaceManager.close()
            if(dialogueFile != null){
                player.dialogueInterpreter.open(dialogueFile, NPC(contactNPCs[index]))
            } else {
                player.dialogueInterpreter.open(contactNPCs[index], NPC(contactNPCs[index], Location(0, 0)))
            }
            return@on true
        }

    }
}
