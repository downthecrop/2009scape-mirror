package core.game.system.command.sets

import core.api.sendMessage
import core.api.utils.PlayerCamera
import core.game.system.command.Privilege
import core.game.world.map.RegionManager
import core.plugin.Initializable

/**
 * Camera command set
 *
 * @constructor Create empty Camera command set
 *
 * WARNING: Playing around with camera values may potentially trigger seizures for people with photosensitive epilepsy.
 * Please use care when discovering camera values.
 */
@Initializable
class CameraCommandSet : CommandSet(Privilege.ADMIN) {

    override fun defineCommands() {
        define("poscam", Privilege.ADMIN, "::poscam <lt>Region X<gt> <lt>Region Y<gt> [<lt>Height<gt>]", "Positions the camera to the given region-local coordinates.") { player, args ->
            val regionX = args[1].toIntOrNull() ?: return@define
            val regionY = args[2].toIntOrNull() ?: return@define
            var height = 300

            if (args.size > 3) {
                height = args[3].toIntOrNull() ?: return@define
            }

            val region = RegionManager.forId(player.location.regionId)
            val base = region.baseLocation

            val globalLoc = base.transform(regionX, regionY, 0)
            sendMessage(player, "<col=8e7cc3><shad=000000>CAMERA POSITION | loc:[$regionX, $regionY] settings:[height:$height]</shad></col>")
            PlayerCamera(player).setPosition(globalLoc.x, globalLoc.y, height)
        }

        define("movcam", Privilege.ADMIN, "::movcam <lt>Region X<gt> <lt>Region Y<gt> [<lt>Height<gt> <lt>Speed<gt>]", "Moves the camera to the given region-local coordinates.") { player, args ->
            val regionX = args[1].toIntOrNull() ?: return@define
            val regionY = args[2].toIntOrNull() ?: return@define
            var height = 300
            var speed = 100

            if (args.size > 3)
                height = args[3].toIntOrNull() ?: return@define

            if (args.size > 4)
                speed = args[4].toIntOrNull() ?: return@define

            val region = RegionManager.forId(player.location.regionId)
            val base = region.baseLocation

            val globalLoc = base.transform(regionX, regionY, 0)
            sendMessage(player, "<col=8e7cc3><shad=000000>CAMERA MOVE | loc:[$regionX, $regionY] settings:[height:$height, speed:$speed]</shad></col>")
            PlayerCamera(player).panTo(globalLoc.x, globalLoc.y, height, speed)
        }

        define("rotcam", Privilege.ADMIN, "::rotcam <lt>Region X<gt> <lt>Region Y<gt> [<lt>Height<gt> <lt>Speed<gt>]", "Rotates the camera to face the given region-local coordinates.") { player, args ->
            val regionX = args[1].toIntOrNull() ?: return@define
            val regionY = args[2].toIntOrNull() ?: return@define
            var height = 300
            var speed = 100

            if (args.size > 3) {
                height = args[3].toIntOrNull() ?: return@define
            }

            if (args.size > 4) {
                speed = args[4].toIntOrNull() ?: return@define
            }

            val region = RegionManager.forId(player.location.regionId)
            val base = region.baseLocation

            val globalLoc = base.transform(regionX, regionY, 0)
            sendMessage(player, "<col=8e7cc3><shad=000000>CAMERA ROTATE | loc:[$regionX, $regionY] settings:[height:$height, speed:$speed]</shad></col>")
            PlayerCamera(player).rotateTo(globalLoc.x, globalLoc.y, height, speed)
        }

        define("shakecam", Privilege.ADMIN, "::shakecam <lt>Camera Movement Type (0-4)<gt> [<lt>Jitter<gt> <lt>Amplitude<gt> <lt>Frequency<gt> <lt>Speed<gt>]",
            "Type (0-4) Jitter, Amplitude, Frequency (0-255)") {player, args ->
            val cameraMovementType = args[1].toIntOrNull() ?: return@define
            if (cameraMovementType < 0 || cameraMovementType > 4) {
                return@define
            }
            // Type [0-4] Jit: 0 Amp: 0 Freq: 128 Speed: 2
            var jitter = 0
            var amplitude = 0
            var frequency = 128
            var speed = 2

            if (args.size > 2) {
                jitter = args[2].toIntOrNull() ?: return@define
            }

            if (args.size > 3) {
                amplitude = args[3].toIntOrNull() ?: return@define
            }

            if (args.size > 4) {
                frequency = args[4].toIntOrNull() ?: return@define
            }

            if (args.size > 5) {
                speed = args[5].toIntOrNull() ?: return@define
            }

            sendMessage(player, "<col=8e7cc3><shad=000000>CAMERA SHAKE | type:$cameraMovementType settings:[jit:$jitter, amp:$amplitude, freq:$frequency, speed:$speed]</shad></col>")
            PlayerCamera(player).shake(cameraMovementType, jitter, amplitude, frequency, speed)
        }

        define("resetcam", Privilege.ADMIN, "::resetcam", "Resets the current camera position.") {player, args ->
            sendMessage(player, "<col=8e7cc3><shad=000000>CAMERA RESET</shad></col>")
            PlayerCamera(player).reset()
        }
    }
}