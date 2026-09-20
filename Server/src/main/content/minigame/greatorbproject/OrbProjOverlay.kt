package content.minigame.greatorbproject

import core.game.node.entity.player.Player
import core.api.*
import org.rs09.consts.Components

/**
 * Overlay for The Great Orb Project
 */

object OrbProjOverlay {

    // interface and child values for Great Orb Project minigame
    // this overlay is initialized with 731.cs2, but no varp triggers for updates
    const val GOP_OVERLAY = Components.RCGUILD_OVERLAY_781
    const val OVERLAY_PORTAL_OPEN = 1 // 'portal is open' text child.
    const val OVERLAY_ROUND_STATE_CONTAINER = 2 // container that holds the round state text
    const val OVERLAY_ROUND_STATE = 52 // the round state, e.g. "Waiting for other players" or "Waiting for portal to open" or "Waiting for orbs to appear". Child disappears once orbs spawn.

    // team ui containers
    const val GREEN_TEAM_IFACE = 13 // green team interface. contains sub-children for score and round %. hidden by default
    const val GREEN_TEAM_TIMER = 33 // green team timer
    const val YELLOW_TEAM_IFACE = 14 // yellow team interface. contains sub-children for score and round %. hidden by default
    const val YELLOW_TEAM_TIMER = 40 // yellow team timer

    // score texts
    const val GREEN_TEAM_GREEN_ORBS = 36 // the number of green orbs scored if the player is on the green team.
    const val GREEN_TEAM_YELLOW_ORBS = 37 // the number of yellow orbs scored if the player is on the green team.
    const val YELLOW_TEAM_YELLOW_ORBS = 43 // the number of yellow orbs scored if the player is on the yellow team.
    const val YELLOW_TEAM_GREEN_ORBS = 44 // the number of green orbs scored if the player is on the yellow team.

    // win models
    const val GREEN_WIN = 41479 // interface model for a green win
    const val YELLOW_WIN = 41480 // interface model for a yellow win
    const val TIE_WIN = -1 // nothing interesting happens.

    // round tracking
    // indices: 0=Air, 1=Mind, 2=Water, 3=Earth, 4=Fire, 5=Body, 6=Chaos, 7=Nature
    val roundChildren = intArrayOf(15, 16, 17, 18, 19, 20, 21, 22)
    val roundWinChildren = intArrayOf(23, 24, 25, 26, 27, 28, 29, 30)
    val altarModels = intArrayOf(
        41489, // Air
        41493, // Mind
        41495, // Water
        41485, // Earth
        41492, // Fire
        41490, // Body
        41487, // Chaos
        41484  // Nature
    )

    // Array of anims: 10139 (full) to 10170 (empty)
    val roundTimer = intArrayOf(
        10139, 10140, 10141, 10142, 10143, 10144, 10145, 10146, 10147, 10148,
        10149, 10150, 10151, 10152, 10153, 10154, 10155, 10156, 10157, 10158,
        10159, 10160, 10161, 10162, 10163, 10164, 10165, 10166, 10167, 10168,
        10169, 10170
    )

    /**
     * Updates the entire GOP overlay for a player.
     * @param player The player to update.
     * @param team The team the player is on.
     * @param portalOpen True if the portal is currently open.
     * @param greenScore Total green orbs scored this round.
     * @param yellowScore Total yellow orbs scored this round.
     * @param stateText The text to display in the middle (e.g. "Waiting for orbs..."). Pass null to hide it.
     * @param currentAltarIndex The index of the altar currently being played (0 for Air, 7 for Nature).
     * @param roundWinners A map or array of who won past rounds to display the bottom tracking bar.
     */
    fun updateInterface(
        player: Player,
        team: Team,
        portalOpen: Boolean,
        greenScore: Int,
        yellowScore: Int,
        stateText: String?,
        currentAltarIndex: Int,
        roundWinners: Array<Team>
    ) {
        // toggle 'portal is open' text
        setComponentVisibility(player, GOP_OVERLAY, OVERLAY_PORTAL_OPEN, !portalOpen)

        // show correct UI per team
        if (team == Team.GREEN) {
            setComponentVisibility(player, GOP_OVERLAY, GREEN_TEAM_IFACE, false)
            setComponentVisibility(player, GOP_OVERLAY, YELLOW_TEAM_IFACE, true)

            setInterfaceText(player, "$greenScore", GOP_OVERLAY, GREEN_TEAM_GREEN_ORBS)
            setInterfaceText(player, "$yellowScore", GOP_OVERLAY, GREEN_TEAM_YELLOW_ORBS)
        } else if (team == Team.YELLOW) {
            setComponentVisibility(player, GOP_OVERLAY, YELLOW_TEAM_IFACE, false)
            setComponentVisibility(player, GOP_OVERLAY, GREEN_TEAM_IFACE, true)

            setInterfaceText(player, "$greenScore", GOP_OVERLAY, YELLOW_TEAM_GREEN_ORBS)
            setInterfaceText(player, "$yellowScore", GOP_OVERLAY, YELLOW_TEAM_YELLOW_ORBS)
        }

        // update state text (e.g., "Waiting for orbs to appear")
        if (stateText != null) {
            setComponentVisibility(player, GOP_OVERLAY, OVERLAY_ROUND_STATE_CONTAINER, false)
            setInterfaceText(player, stateText, GOP_OVERLAY, OVERLAY_ROUND_STATE)
        } else {
            // hide the state text if null (a round is being played)
            setComponentVisibility(player, GOP_OVERLAY, OVERLAY_ROUND_STATE_CONTAINER, true)
        }

        // update the round history
        for (i in roundChildren.indices) {
            val altarChild = roundChildren[i]
            val winChild = roundWinChildren[i]
            val modelId = altarModels[i]

            // always show the altar model if we are on this round or past it
            if (i <= currentAltarIndex) {
                player.packetDispatch.sendModelOnInterface(modelId, GOP_OVERLAY, altarChild, 0)
                // fix rotation. 360 degrees = 2048. zoom is more like 'distance from'.
                player.packetDispatch.sendAngleOnInterface(GOP_OVERLAY, altarChild, 900, 512, 0)
            }

            // show the win overlay (Green or Yellow) if the round is completed
            when (roundWinners[i]) {
                Team.GREEN -> player.packetDispatch.sendModelOnInterface(GREEN_WIN, GOP_OVERLAY, winChild, 0)
                Team.YELLOW -> player.packetDispatch.sendModelOnInterface(YELLOW_WIN, GOP_OVERLAY, winChild, 0)
                else -> {
                    // if it's a tie, or the round hasn't finished yet, use model -1 (nothing).
                    player.packetDispatch.sendModelOnInterface(TIE_WIN, GOP_OVERLAY, winChild, 0)
                }
            }
        }
    }

    /**
     * Updates the timer dial.
     * Maps the ticks remaining to the 32 animation frames.
     */
    fun updateRoundTimer(player: Player, team: Team, ticksRemaining: Int, maxTicks: Int) {
        // determine which timer to update
        val timerChild = if (team == Team.GREEN) GREEN_TEAM_TIMER else YELLOW_TEAM_TIMER

        // coerce the ticks for data safety or something. gotta make sure it's zero
        val safeTicks = ticksRemaining.coerceIn(0, maxTicks)

        // calculate a percentage like it's really 2009 and you're back in middle school
        val progress = safeTicks.toDouble() / maxTicks.toDouble()

        // convert progress to the array index. 0 is full (10139), 31 is empty (10170).
        val index = ((1.0 - progress) * (roundTimer.size - 1)).toInt().coerceIn(0, roundTimer.size - 1)

        val animId = roundTimer[index]
        animateInterface(player, GOP_OVERLAY, timerChild, animId)
        // TODO: the timer should flash red near the end. This is a very low priority to do.
    }
}