package core.api.utils

object CameraUtils {

}

/*
 * All camera shake types were found by facing the camera North using
 * these values:
 * Type [0-4] Jit: 0 Amp: 0 Freq: 128 Speed: 2
 *
 * **See Also:** [This forum post](https://forum.2009scape.org/viewtopic.php?t=173-in-game-camera-movement-documentation-server-sided)
 *
 * WARNING: Playing around with camera shake values may potentially trigger seizures for people with photosensitive epilepsy.
 * Please use care when discovering camera values.
 */
enum class CameraShakeType {
    TRUCK,      // camera movement from left to right
    PEDESTAL,   // camera movement vertically up to down, fixated on one location
    DOLLY,      // camera movement forwards to backwards
    PAN,        // camera movement horizontally, fixed on a certain point
    TILT        // camera movement vertically, fixed on a certain point
}