package content.region.asgarnia.burthorpe.dialogue

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import org.rs09.consts.NPCs

/**
 * Burthorpe Busy Soldiers Messages
 * @author ovenbread
 *
 * https://www.youtube.com/watch?v=5RfEUmIdH10 for some busy interactions
 * Covers the Sergeant/TrainingSoldier/EatingSoldier/Archer/Guard cases.
 * These NPCs do not talk to you as they are busy.
 */
class BurthorpeBusySoldierListener: InteractionListener {
    override fun defineListeners() {
        // For sergeants.
        on(intArrayOf(NPCs.SERGEANT_1061, NPCs.SERGEANT_1062), IntType.NPC, "talk-to") { player, npc ->
            sendMessage(player, "The Sergeant is busy training the soldiers.")
            return@on true
        }

        // For soldiers training.
        on(intArrayOf(NPCs.SOLDIER_1063, NPCs.SOLDIER_1064), IntType.NPC, "talk-to") { player, npc ->
            sendMessage(player, "The soldier is busy training.")
            return@on true
        }

        // For soldiers sitting around the fire.
        on(intArrayOf(NPCs.SOLDIER_1066, NPCs.SOLDIER_1067, NPCs.SOLDIER_1068), IntType.NPC, "talk-to") { player, npc ->
            sendMessage(player, "The soldier is busy eating.")
            return@on true
        }

        // For archers at the castle.
        on(intArrayOf(NPCs.ARCHER_1073, NPCs.ARCHER_1074), IntType.NPC, "talk-to") { player, npc ->
            sendMessage(player, "The archer won't talk whilst on duty.")
            return@on true
        }

        // For soldiers at the castle.
        on(intArrayOf(NPCs.GUARD_1076, NPCs.GUARD_1077), IntType.NPC, "talk-to") { player, npc ->
            sendMessage(player, "The guard won't talk whilst on duty.")
            return@on true
        }
    }
}