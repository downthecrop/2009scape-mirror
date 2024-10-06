package content.region.misthalin.varrock.handlers

import content.region.misthalin.varrock.dialogue.KnockAtBankDoor
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery
import org.rs09.consts.Sounds

class VarrockInteractionListener : InteractionListener {
    override fun defineListeners() {
        // Varrock Sewer Manhole
        on(VARROCK_MANHOLE, IntType.SCENERY, "open", "close") { player, node ->
            if (getUsedOption(player) == "open") {
                playAudio(player, Sounds.MANHOLE_OPEN_75)
                replaceScenery(node.asScenery(), Scenery.VARROCK_MANHOLE_OPEN_882, 100)
            } else {
                playAudio(player, Sounds.MANHOLE_CLOSE_74)
                replaceScenery(node.asScenery(), Scenery.VARROCK_MANHOLE_CLOSED_881, -1)
            }
            return@on true
        }

        // Phoenix Gang Hideout Plaque
        on(Scenery.PLAQUE_23636, IntType.SCENERY, "read") { player, _ ->
            openInterface(player, VTAM_IFACE)
            return@on true
        }

        // Varrock Census in the palace Library
        on(Scenery.VARROCK_CENSUS_37209, IntType.SCENERY, "read") { player, _ ->
            sendPlayerDialogue(player, "Hmm. The Varrock Census - year 160. That means it's nine years out of date.")
            addDialogueAction(player) { _, buttonID ->
                if (buttonID == 6) {
                    openInterface(player, VARROCK_CENSUS_IFACE)
                }
            }
            return@on true
        }

        // Broken Cart next to Rat Burgiss
        on(Scenery.BROKEN_CART_23055, IntType.SCENERY, "search") { player, node ->
            sendDialogue(player, "You search the cart but are surprised to find very little there. " +
                    "It's a little odd for a travelling trader not to have anything to trade.")
            return@on true
        }

        on(openOptionNodes, IntType.SCENERY, "open") { player, node ->
            when (node.id) {
                // Guidor's Bedroom Door
                Scenery.BEDROOM_DOOR_2032 -> {
                    openDialogue(player, NPCs.GUIDORS_WIFE_342, true, true)
                }

                // Guidor's Drawers
                Scenery.DRAWERS_17466 -> {
                    sendMessage(player, "The drawers are locked shut.")
                }

                // Brass Key Door to Edgeville Dungeon
                Scenery.DOOR_1804 -> {
                    if (inInventory(player, Items.BRASS_KEY_983)) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    } else {
                        sendMessage(player, "This door is locked.")
                    }
                }
            }
            return@on true
        }

        // Varrock West Bank Door
        on(Scenery.DOOR_24389, IntType.SCENERY, "knock-at") { player, node ->
            openDialogue(player, KnockAtBankDoor())
            return@on true
        }

        // TODO: Cooking Guild
        // TODO: Fix Achievements
    }
    companion object {
        private val VARROCK_MANHOLE = intArrayOf(Scenery.VARROCK_MANHOLE_CLOSED_881, Scenery.VARROCK_MANHOLE_OPEN_882)
        private val openOptionNodes = intArrayOf(Scenery.BEDROOM_DOOR_2032, Scenery.DRAWERS_17466, Scenery.DOOR_1804)
        private const val VTAM_IFACE = 531
        private const val VARROCK_CENSUS_IFACE = 794
    }
}