package content.minigame.greatorbproject

import content.global.skill.runecrafting.Altar
import core.api.*
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs09.consts.*

/**
 * Constants and helper functions used across Great Orb Project.
 * Lobby-specific functions are in OrbProjLobby.
 * Session-specific functions are in OrbProjSession.
 * Round-specific functions are in OrbProjRound.
 */

object OrbProjUtils {

    const val ATTR_GOP_TEAM = "gop_team"
    const val ATTR_ACTIVE_BARRIER = "gop_active_barrier"

    // region IDs for the guild and each altar, in the same order as OrbProjSession.altars
    val validRegions = intArrayOf(
        6741,  // RC Guild
        11339, // Air
        11083, // Mind
        13899, // Water
        10571, // Earth
        10315, // Fire
        10059, // Body
        9035,  // Chaos
        9547   // Nature
    )

    // green orbs
    val greenOrbs = intArrayOf(
        NPCs.GREEN_ORB_8026, // inactive orb
        NPCs.GREEN_ORB_8027, // attract orb
        NPCs.GREEN_ORB_8028  // repel orb
    )

    // yellow orbs
    val yellowOrbs = intArrayOf(
        NPCs.YELLOW_ORB_8022, // inactive orb
        NPCs.YELLOW_ORB_8023, // attract orb
        NPCs.YELLOW_ORB_8024  // repel orb
    )

    // animations for players and orbs
    const val USE_WAND = 10132
    const val BARRIER_UP = 10172
    const val BARRIER_DOWN = 10173

    // orbs fading in and out
    const val ORB_FADE_OUT = 10135
    const val ORB_FADE_IN = 10137

    // graphics for players and orbs
    const val GREEN_ATTRACT = 1753
    const val GREEN_REPEL = 1754
    const val YELLOW_ATTRACT = 1755
    const val YELLOW_REPEL = 1756
    const val GREEN_PROJ = 1757
    const val YELLOW_PROJ = 1758

    // the altars played
    @JvmField
    val orbProjAltars = arrayOf(
        Altar.AIR,
        Altar.MIND,
        Altar.WATER,
        Altar.EARTH,
        Altar.FIRE,
        Altar.BODY,
        Altar.CHAOS,
        Altar.NATURE
    )

    // the start/end location for the game
    val guildStartLoc = Location(1696, 5473, 2)

    // removes team items, clears team attributes, and moves player back to guild. Authentically, there should be a 10-minute penalty for team abandonment (not implemented).
    fun removeFromTeamAndTeleportGuild(player: Player) {
        // remove green team gear
        removeItem(player, Items.GREEN_ATTRACTOR_13645, container = Container.INVENTORY)
        removeItem(player, Items.GREEN_ATTRACTOR_13645, container = Container.EQUIPMENT)
        removeItem(player, Items.GREEN_REPELLER_13646, container = Container.INVENTORY)
        removeItem(player, Items.GREEN_REPELLER_13646, container = Container.EQUIPMENT)
        removeItem(player, Items.GREEN_BARRIER_GENERATOR_13647)
        removeItem(player, Items.RUNECRAFTER_HAT_13613, container = Container.EQUIPMENT)

        // remove yellow team gear
        removeItem(player, Items.YELLOW_ATTRACTOR_13643, container = Container.INVENTORY)
        removeItem(player, Items.YELLOW_ATTRACTOR_13643, container = Container.EQUIPMENT)
        removeItem(player, Items.YELLOW_REPELLER_13644, container = Container.INVENTORY)
        removeItem(player, Items.YELLOW_REPELLER_13644, container = Container.EQUIPMENT)
        removeItem(player, Items.YELLOW_BARRIER_GENERATOR_13648)
        removeItem(player, Items.RUNECRAFTER_HAT_13612, container = Container.EQUIPMENT)

        removeAttribute(player, ATTR_GOP_TEAM)

        // wiki says you are booted back to guild if you leave team.
        teleport(player, guildStartLoc)
    }

    // removes a barrier and the attribute on the player used to track if they have a barrier up
    fun removeBarriers(player: Player) {
        getAttribute<core.game.node.scenery.Scenery?>(player, ATTR_ACTIVE_BARRIER, null)?.let { barrier ->
            removeScenery(barrier)
            removeAttribute(player, ATTR_ACTIVE_BARRIER)
        }
    }
}

// teams (for orb proj, not the famous collaboration software version)
enum class Team { GREEN, YELLOW, NONE }