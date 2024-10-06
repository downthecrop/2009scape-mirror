package content.region.misthalin.varrock.handlers

import core.api.*
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import org.rs09.consts.Components
import org.rs09.consts.NPCs
import org.rs09.consts.Sounds

class MuseumInterfaceListener : InterfaceListener {
    override fun defineInterfaceListeners() {
        onOpen(Components.VM_MUSEUM_MAP_527) { player, _ ->
            showMapFloor(player, getAttribute(player, "iface:527:floor", "main"))
            removeAttribute(player, "iface:527:floor")
            return@onOpen true
        }

        on(Components.VM_MUSEUM_MAP_527) { player, _, _, buttonID, _, _ ->
            showMapFloor(player, when (buttonID) {
                in mapButtonsToBasement -> "basement"
                in mapButtonsToMainFloor -> "main"
                in mapButtonsToSecondFloor -> "second"
                in mapButtonsToTopFloor -> "top"
                else -> return@on true
            })
            return@on true
        }

        onOpen(NATURAL_HISTORY_EXAM_533) { player, component ->
            // The model for each display is confusing as hell. Some are objects and some are NPCs.
            val model = getScenery(1763, 4937, 0)?.definition?.modelIds?.first()
            player.packetDispatch.sendModelOnInterface(model!!, component.id, 3, 0)

            // Showing this child makes child 28 - 31 visible.
            setComponentVisibility(player, component.id, 27, false)

            // The case number to display.
            setInterfaceText(player, "1", component.id, 25)

            // The question text.
            setInterfaceText(player, "When will the Natural History Quiz be implemented?", component.id, 28)

            // The choices.
            setInterfaceText(player, "Never.", component.id, 29)
            setInterfaceText(player, "In 2 days.", component.id, 30)
            setInterfaceText(player, "After Barbarian Assault.", component.id, 31)
            return@onOpen true
        }

        on(NATURAL_HISTORY_EXAM_533) { player, component, opcode, buttonID, slot, itemID ->
            if (buttonID in 29..31) {
                closeInterface(player)
                setVarbit(player, 3637, 1, false)
                playAudio(player, Sounds.VM_GAIN_KUDOS_3653)
                sendNPCDialogue(player, NPCs.ORLANDO_SMITH_5965, "Nice job, mate. That looks about right.")
            }
            return@on true
        }
    }
    companion object {
        private const val NATURAL_HISTORY_EXAM_533 = 533

        private val mapButtonsToBasement = intArrayOf(41, 186)
        private val mapButtonsToMainFloor = intArrayOf(117, 120, 187, 188)
        private val mapButtonsToSecondFloor = intArrayOf(42, 44, 152, 153)
        private val mapButtonsToTopFloor = intArrayOf(42, 44, 118, 119)

        private fun showMapFloor(player: Player, floor: String) {
            when (floor) {
                "basement" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 2, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 7, false)
                }
                "main" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 3, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 7, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 2, false)
                }
                "second" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 2, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 5, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 3, false)
                }
                "top" -> {
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 3, true)
                    setComponentVisibility(player, Components.VM_MUSEUM_MAP_527, 5, false)
                }
            }
        }
    }
}