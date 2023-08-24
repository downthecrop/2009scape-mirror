package core.game.node.entity.player.link.audio

/**
 * An audio object to play.
 * @author Zerken
 */
class Audio
    @JvmOverloads
    constructor(
        @JvmField
        val id: Int,
        @JvmField
        val delay: Int = 0,
        @JvmField
        val loops: Int = 1,
        @JvmField
        val radius: Int = defaultAudioRadius
    ) {
    companion object {
        /**
         * ----Audio Radius Constants----
         * Values which represent the tile distance/radius from which certain groups of audio can be heard from
         * Certain sounds can be heard from a further distance than others
         * This is a common place where audio distances can be adjusted if needed
         */
        // TODO: Add specific groups of audios to have the correct radius (some sounds should be heard from a farther radius than others)
            const val defaultAudioRadius = 8
        }
    }