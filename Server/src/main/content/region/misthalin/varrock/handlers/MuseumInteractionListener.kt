package content.region.misthalin.varrock.handlers

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs09.consts.Components
import org.rs09.consts.Items
import org.rs09.consts.NPCs
import org.rs09.consts.Scenery

class MuseumInteractionListener : InteractionListener {
    override fun defineListeners() {
        // Basement Stairs
        addClimbDest(Location(3255, 3451, 0), Location(1759, 4958, 0))
        addClimbDest(Location(1758, 4959, 0), Location(3258, 3452, 0))

        on(mapObject, IntType.SCENERY, "look-at", "take") { player, node ->
            if (getUsedOption(player) == "take") {
                if (!addItem(player, Items.MUSEUM_MAP_11184)) {
                    sendMessage(player, "You don't have enough space in your inventory.")
                }
            } else {
                when (node.id) {
                    Scenery.MAP_24390 -> setAttribute(player, "iface:527:floor", "main")
                    Scenery.MAP_24391 -> setAttribute(player, "iface:527:floor", "second")
                    Scenery.MAP_24392 -> setAttribute(player, "iface:527:floor", "top")
                }
                openInterface(player, Components.VM_MUSEUM_MAP_527)
            }
            return@on true
        }

        on(Items.MUSEUM_MAP_11184, IntType.ITEM, "look-at") { player, node ->
            openInterface(player, Components.VM_MUSEUM_MAP_527)
            return@on true
        }

        on(Scenery.INFORMATION_BOOTH_24452, IntType.SCENERY, "look-at") { player, node ->
            // TODO: I cannot find anything that shows what this does in 2009.
            sendMessage(player, "Nothing interesting happens.")
            return@on true
        }

        on(doorsToDigsite, IntType.SCENERY, "open") { player, node ->
            if (node.id == Scenery.GATE_24536) {
                if (player.location.y <= 3446) {
                    handleMuseumDoor(player, node.asScenery())
                } else {
                    openDialogue(player, NPCs.MUSEUM_GUARD_5941)
                }
                return@on true
            } else {
                if (player.location.y >= 3442) {
                    handleMuseumDoor(player, node.asScenery())
                } else {
                    openDialogue(player, NPCs.MUSEUM_GUARD_5943)
                }
            }
            return@on true
        }

        on(Scenery.TOOLS_24535, IntType.SCENERY, "take") { player, node ->
            sendDialogueOptions(
                player,
                "Which tool would you like?",
                "Trowel",
                "Rock pick",
                "Specimen brush",
                "Leather gloves",
                "Leather boots"
            )
            addDialogueAction(player) { _, button ->
                val item = when (button) {
                    2 -> Items.TROWEL_676
                    3 -> Items.ROCK_PICK_675
                    4 -> Items.SPECIMEN_BRUSH_670
                    5 -> Items.LEATHER_GLOVES_1059
                    6 -> Items.LEATHER_BOOTS_1061
                    else -> return@addDialogueAction
                }
                val name = item.asItem().name.lowercase()
                val word = if (name.startsWith("leather")) "pair of " else ""

                if (!addItem(player, item)) {
                    sendMessage(player, "You don't have enough space in your inventory.")
                } else {
                    sendItemDialogue(player, item, "You take a $word$name from the rack.")
                }
            }
            return@on true
        }

        on(naturalHistoryPlaques, IntType.SCENERY, "study") { player, node ->
            openInterface(player, 533)
            return@on true
        }
    }

    companion object {
        private val doorsToDigsite = intArrayOf(Scenery.GATE_24536, Scenery.DOOR_24565, Scenery.DOOR_24567)
        private val mapObject = intArrayOf(Scenery.MAP_24390, Scenery.MAP_24391, Scenery.MAP_24392)
        private val naturalHistoryPlaques = intArrayOf(
            Scenery.PLAQUE_24605, Scenery.PLAQUE_24606, Scenery.PLAQUE_24607, Scenery.PLAQUE_24608,
            Scenery.PLAQUE_24609, Scenery.PLAQUE_24610, Scenery.PLAQUE_24611, Scenery.PLAQUE_24612,
            Scenery.PLAQUE_24613, Scenery.PLAQUE_24614, Scenery.PLAQUE_24615, Scenery.PLAQUE_24616,
            Scenery.PLAQUE_24617, Scenery.PLAQUE_24618
        )

        fun handleMuseumDoor(player: Player, door: core.game.node.scenery.Scenery?) {
            val npc = if (door?.id == Scenery.GATE_24536) findLocalNPC(player, NPCs.MUSEUM_GUARD_5941) else findLocalNPC(player, NPCs.MUSEUM_GUARD_5943)
            val animation = if (DoorActionHandler.getEndLocation(player, door).y > player.location.y) Animation(6391) else Animation(6392)

            if (npc != null) {
                animate(npc,  animation)
                queueScript(player, animationDuration(animation)) { DoorActionHandler.handleAutowalkDoor(player, door) }
            } else {
                DoorActionHandler.handleAutowalkDoor(player, door)
            }
        }
    }
}