package core.game.system.command.sets

import core.api.*
import core.api.utils.PlayerCamera
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable

/**
 * To view camera angles in game. Do note that the camera stuff is really hard to control.
 * The idea here is to be able to move your camera around and then copy down the values to use it directly in code.
 * The interface is borrowed and is the best I can come up with to allow movement in the main screen.
 * The icons do not reflect what they are supposed to do most of the time.
 *
 * There are 2 sets here. The camera position, and the square it is looking at.
 * The camera will have an x,y and height, and will look at a square at x,y and height too.
 * Starting off, the camera will be placed where your player is at 600 height and looks at y+10(north) at 300 height.
 * The Camera MUST BE HIGHER than the square it is looking at, or it will point at the ground (200 height buffer).
 * The toggle, will toggle between camera and the look-at controls.
 *
 * The printouts will follow Cam/Look : Value(Value % 64) <-- Value % 64 is for instance positioning
 *
 * Please note that this borrows interface iface 683 Catapult, you can steal this back when you implement the quest.
 */
@Initializable
class CameraViewerCommandSet  : CommandSet(Privilege.ADMIN), InterfaceListener {
    companion object {
        const val CATAPULT_INTERFACE = 683
        const val ATTRIBUTE_PLAYERCAMERA = "commandplayercamera"
        const val ATTRIBUTE_CAMERA_X = "commandcamx"
        const val ATTRIBUTE_CAMERA_Y = "commandcamy"
        const val ATTRIBUTE_CAMERA_H = "commandcamh"
        const val ATTRIBUTE_LOOKAT_X = "commandlookx"
        const val ATTRIBUTE_LOOKAT_Y = "commandlooky"
        const val ATTRIBUTE_LOOKAT_H = "commandlookh"
        const val ATTRIBUTE_MODE_ORIGIN = "commandmodeorigin"

        private fun updateInterfaceText(player: Player) {
            val cx = getAttribute(player, ATTRIBUTE_CAMERA_X, player.location.x)
            val cy = getAttribute(player, ATTRIBUTE_CAMERA_Y, player.location.y)
            val ch = getAttribute(player, ATTRIBUTE_CAMERA_H, 600)
            val lx = getAttribute(player, ATTRIBUTE_LOOKAT_X, player.location.x)
            val ly = getAttribute(player, ATTRIBUTE_LOOKAT_Y, player.location.y + 10)
            val lh = getAttribute(player, ATTRIBUTE_LOOKAT_H, 300)
            val o = getAttribute(player, ATTRIBUTE_MODE_ORIGIN, true)

            val c = if (o) "!" else ""
            val l = if (!o) "!" else ""

            setInterfaceText(player, "" +
                    "${c}CamX:$cx(${cx and 63}) " +
                    "${c}CamY:$cy(${cy and 63}) " +
                    "${c}CamZ:$ch " +
                    "${l}LookX:$lx(${lx and 63}) " +
                    "${l}LookY:$ly(${ly and 63}) " +
                    "${l}LookZ:$lh",
                CATAPULT_INTERFACE, 48)
        }

        private fun modifyAttributeAndMove(
            player: Player, playerCamera: PlayerCamera, isOrigin: Boolean,
            xDelta: Int = 0, yDelta: Int = 0, hDelta: Int = 0
        ) {
            val xAttr = if (isOrigin) ATTRIBUTE_CAMERA_X else ATTRIBUTE_LOOKAT_X
            val yAttr = if (isOrigin) ATTRIBUTE_CAMERA_Y else ATTRIBUTE_LOOKAT_Y
            val hAttr = if (isOrigin) ATTRIBUTE_CAMERA_H else ATTRIBUTE_LOOKAT_H
            val defaultH = if (isOrigin) 600 else 300

            if (xDelta != 0) setAttribute(player, xAttr, getAttribute(player, xAttr, player.location.x) + xDelta)
            if (yDelta != 0) setAttribute(player, yAttr, getAttribute(player, yAttr, player.location.y) + yDelta)
            if (hDelta != 0) setAttribute(player, hAttr, getAttribute(player, hAttr, defaultH) + hDelta)

            val x = getAttribute(player, xAttr, player.location.x)
            val y = getAttribute(player, yAttr, player.location.y)
            val h = getAttribute(player, hAttr, defaultH)

            if (isOrigin) {
                playerCamera.panTo(x, y, h, 100)
                sendGraphics(Graphics(80, h, 0), Location(x, y))
            } else {
                playerCamera.rotateTo(x, y, h, 100)
                sendGraphics(Graphics(80, h, 0), Location(x, y))
            }
            updateInterfaceText(player)
        }
    }
    override fun defineCommands() {
        define("camview", description = "Opens the in-game camera viewer."){ player, args ->

            // Bad number of args
            if(args.size > 2){
                reject(player,"Usage: ::camview")
                return@define
            }

            sendMessage(player, "=== CAMVIEW INSTRUCTIONS ===")
            sendMessage(player, "Click the left/right arrows for location.x, up/down arrows for location.y")
            sendMessage(player, "Note that the movement is NOT RELATIVE TO YOUR CAMERA e.g. Up isn't forward")
            sendMessage(player, "Click the clockwise/anticlockwise arrows to go down/up in height")
            sendMessage(player, "Click the flip icon to swap between camera and look-at control")

            player.interfaceManager.openSingleTab(Component(CATAPULT_INTERFACE))
            val playerCamera = PlayerCamera(player)
            setAttribute(player, ATTRIBUTE_PLAYERCAMERA, playerCamera)
            setAttribute(player, ATTRIBUTE_CAMERA_X, player.location.x)
            setAttribute(player, ATTRIBUTE_CAMERA_Y, player.location.y)
            setAttribute(player, ATTRIBUTE_CAMERA_H, 600)
            setAttribute(player, ATTRIBUTE_LOOKAT_X, player.location.x)
            setAttribute(player, ATTRIBUTE_LOOKAT_Y, player.location.y + 10)
            setAttribute(player, ATTRIBUTE_LOOKAT_H, 300)
            // Doing playerCamera.position is weird. It doesn't respect the height value.

            setComponentVisibility(player, CATAPULT_INTERFACE, 43, false)
            for (i in 44..47) {
                setComponentVisibility(player, CATAPULT_INTERFACE, i, true)
            }
            modifyAttributeAndMove(player, playerCamera, true)
            modifyAttributeAndMove(player, playerCamera, false)

            return@define
        }
    }

    override fun defineInterfaceListeners() {
        /*
        If and when the time comes that you implement the Catapult Construction Quest,
        you can override this or coexist with the command.
        Do whatever you want since god knows how long it took you to get to this point lmao.
        */
        on(CATAPULT_INTERFACE){ player, _, _, buttonID, _, _ ->
            val playerCamera = getAttribute(player, ATTRIBUTE_PLAYERCAMERA, PlayerCamera(player))
            val isOrigin = getAttribute(player, ATTRIBUTE_MODE_ORIGIN, true)

            when (buttonID) {
                34 -> modifyAttributeAndMove(player, playerCamera, isOrigin, hDelta = -100) // Decrease height
                35 -> modifyAttributeAndMove(player, playerCamera, isOrigin, hDelta = 100)  // Increase height
                36 -> { setAttribute(player, ATTRIBUTE_MODE_ORIGIN, !isOrigin); updateInterfaceText(player) } // Switch Control
                37 -> { playerCamera.reset(); player.interfaceManager.closeSingleTab() }         // Quit
                38 -> modifyAttributeAndMove(player, playerCamera, isOrigin, yDelta = 1)         // Up
                39 -> modifyAttributeAndMove(player, playerCamera, isOrigin, xDelta = 1)         // Right
                40 -> modifyAttributeAndMove(player, playerCamera, isOrigin, xDelta = -1)        // Left
                41 -> modifyAttributeAndMove(player, playerCamera, isOrigin, yDelta = -1)        // Down
            }
            return@on true
        }
    }
}
