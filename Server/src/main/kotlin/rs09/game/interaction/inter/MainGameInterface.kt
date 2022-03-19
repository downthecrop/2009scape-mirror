package rs09.game.interaction.inter

import api.*
import core.game.component.CloseEvent
import core.game.component.Component
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.combat.equipment.WeaponInterface.WeaponInterfaces
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import org.rs09.consts.Components
import rs09.game.interaction.InterfaceListener
import rs09.game.world.GameWorld.settings

class MainGameInterface : InterfaceListener() {
    val TOPLEVEL = Components.TOPLEVEL_548
    val TOPLEVEL_FS = Components.TOPLEVEL_FULLSCREEN_746
    val RUN_BUTTON = Components.TOPSTAT_RUN_750
    val FILTER_BUTTONS = Components.FILTERBUTTONS_751
    val REPORT_ABUSE = Components.SNAPSHOT_MAIN_553

    override fun defineListeners() {
        on(FILTER_BUTTONS){player, _, _, buttonID, _, _ ->
            if(buttonID == 27)
                openReport(player)
            return@on true
        }

        on(RUN_BUTTON){player, _, _, _, _, _ ->
            player.settings.toggleRun()
            return@on true
        }

        on(TOPLEVEL_FS){player, _, _, buttonID, _, _ ->
            when (buttonID) {
                12 -> setInterfaceText(player,
                        "When you have finished playing " + settings!!.name + ", always use the button below to logout safely. ",
                        182,
                        0
                )
                49 -> setInterfaceText(player,
                        "Friends List - " + settings!!.name + " " + settings!!.worldId,
                        550,
                        3
                )
                110 -> configureWorldMap(player)
            }
            return@on true
        }


        on(TOPLEVEL){player, _, _, buttonID, _, _ ->
            when (buttonID) {
                21 -> {
                    player.packetDispatch.sendString(
                        "Friends List -" + settings!!.name + " " + settings!!.worldId,
                        550,
                        3
                    )
                }
                38 -> {
                    if (player.getExtension<Any>(WeaponInterface::class.java) === WeaponInterfaces.STAFF) {
                        val c = Component(WeaponInterfaces.STAFF.interfaceId)
                        player.interfaceManager.openTab(0, c)
                        val inter = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
                        inter.updateInterface()
                    }
                }
                40 -> player.questRepository.syncronizeTab(player)
                41 -> player.inventory.refresh()
                66, 110 -> configureWorldMap(player)
                69 -> player.packetDispatch.sendString(
                    "When you have finished playing " + settings!!.name + ", always use the button below to logout safely. ",
                    182,
                    0
                )
            }
            return@on true
        }
    }

    fun openReport(player: Player) {
        player.interfaceManager.open(Component(REPORT_ABUSE)).closeEvent =
            CloseEvent { player1: Player, c: Component? ->
                player1.packetDispatch.sendRunScript(80, "")
                player1.packetDispatch.sendRunScript(137, "")
                true
            }
        player.packetDispatch.sendRunScript(508, "")
        if (player.details.rights !== Rights.REGULAR_PLAYER) {
            for (i in 0..17) {
                player.packetDispatch.sendInterfaceConfig(553, i, false)
            }
        }
    }

    private fun configureWorldMap(player: Player) {
        if (player.inCombat()) {
            player.packetDispatch.sendMessage("It wouldn't be very wise opening the world map during combat.")
            return
        }
        if (player.locks.isInteractionLocked || player.locks.isMovementLocked) {
            player.packetDispatch.sendMessage("You can't do this right now.")
            return
        }
        player.interfaceManager.openWindowsPane(Component(755))
        val posHash = player.location.z shl 28 or (player.location.x shl 14) or player.location.y
        player.packetDispatch.sendScriptConfigs(622, posHash, "", 0)
        player.packetDispatch.sendScriptConfigs(674, posHash, "", 0)
    }
}