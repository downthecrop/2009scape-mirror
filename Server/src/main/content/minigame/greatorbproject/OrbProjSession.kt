package content.minigame.greatorbproject

import content.minigame.greatorbproject.OrbProjOverlay.GOP_OVERLAY
import content.minigame.greatorbproject.OrbProjOverlay.updateRoundTimer
import content.minigame.greatorbproject.OrbProjUtils.ATTR_GOP_TEAM
import content.minigame.greatorbproject.OrbProjUtils.removeBarriers
import content.minigame.greatorbproject.OrbProjUtils.removeFromTeamAndTeleportGuild
import core.api.*
import core.game.bots.AIPlayer
import core.game.component.Component
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs09.consts.*

/**
 * Handles a session of Great Orb Project.
 * Session starts at RC guild and goes through all 8 altars.
 * Round-specific logic (spawning/moving/scoring orbs) is in OrbProjRound.
 * This class manages the overall round-to-round state, player status, and end of game logic.
 */

class OrbProjSession(val participants: MutableList<Player>) {

    companion object {
        // the currently running session. started by OrbProjLobby.startGame(), cleared by terminateSession()
        var active: OrbProjSession? = null
    }

    // watchdog session timeout: 3000 ticks (30 minutes)
    // a normal game takes about 20 minutes
    private var absoluteTimeoutTicks = 3000

    // round time is 200 ticks (2 minutes)
    val roundTime = 200

    // players are given 50 ticks (30 seconds) to move to the next location, otherwise they are force tele'd
    private val gracePeriod = 50

    // 5 ticks (~3 seconds) delay before orbs spawn
    private val spawnDelay = 5

    // other timing shit
    var ticksRemaining = roundTime
    private var forceStartTicks = 0
    private var delayTicks = 0

    // VERY IMPORTANT THIS IS THE INDEX THAT MOST OF THE GAME STATE LOOKS AT
    var currentAltarIndex = 0

    // tracks the winners
    private val roundWinners = Array(8) { Team.NONE }

    // the round score
    var greenScore = 0
    var yellowScore = 0

    // the currently running round
    var currentRound: OrbProjRound? = null

    // tracks which players are at the altar
    private val playersAtAltar = mutableSetOf<Player>()
    private var activePortal: NPC? = null

    // various game state trackers
    var isWaitingForNextRound = false
    private var isDelayingStart = false
    private var isGameOver = false

    // the entry and exit points for each altar
    val altars = listOf(
        Location(1696, 5473, 2), // guild start
        Location(2844, 4839, 0), // air
        Location(2786, 4846, 0), // mind
        Location(3484, 4842, 0), // water
        Location(2658, 4846, 0), // earth
        Location(2585, 4844, 0), // fire
        Location(2523, 4845, 0), // body
        Location(2271, 4848, 0), // chaos
        Location(2400, 4846, 0), // nature
    )

    // the 5x5 area around each altar you try and get the orb to
    val altarScoreZones = listOf(
        null, // rc guild (none)
        ZoneBorders(Location(2842, 4836, 0), Location(2846, 4832, 0)), // air
        ZoneBorders(Location(2784, 4843, 0), Location(2788, 4839, 0)), // mind
        ZoneBorders(Location(3482, 4838, 0), Location(3486, 4834, 0)), // water
        ZoneBorders(Location(2656, 4843, 0), Location(2660, 4839, 0)), // earth
        ZoneBorders(Location(2583, 4840, 0), Location(2587, 4836, 0)), // fire
        ZoneBorders(Location(2521, 4842, 0), Location(2525, 4838, 0)), // body
        ZoneBorders(Location(2269, 4844, 0), Location(2273, 4840, 0)), // chaos
        ZoneBorders(Location(2398, 4843, 0), Location(2402, 4839, 0)), // nature
    )

    // starts the game session
    fun start() {
        participants.forEach { player ->
            val team = getAttribute(player, ATTR_GOP_TEAM, Team.NONE)

            if (team == Team.YELLOW) {
                sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "What are you waiting for? The portal is open!")
            } else {
                sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "Stop dawdling and enter the portal to the Air Altar.")
            }
        }
        currentAltarIndex = 0
        isWaitingForNextRound = true
        forceStartTicks = gracePeriod
        spawnPortal() // portal at rc guild
        updateAllOverlays()
    }

    // this tick is incremented in OrbProjMinigame.kt
    // VERY IMPORTANT THIS IS LIKE MOST OF THE GAME STATE RIGHT HERE
    fun tick() {
        // decrement watchdog
        absoluteTimeoutTicks--

        if (absoluteTimeoutTicks <= 0  // each session has a 30min watchdog
            || !sessionContainsHumans  // if the last human leaves, kill the session
            || participants.size < 4   // also destroy session if there's not enough people.
        ) {
            terminateSession()
            return
        }

        // skip all the game processing if game is over
        if (isGameOver) return

        // check all players to make sure they are still here
        checkOutOfBounds()

        // don't start the round until all team members have arrived
        if (isWaitingForNextRound) {
            forceStartTicks--
            updateAllOverlays()
            startRound()
            return
        }

        // after all players have arrived, wait a few ticks before orbs appear
        if (isDelayingStart) {
            delayTicks--
            if (delayTicks <= 0) {
                isDelayingStart = false
                startNextRound()
            }
            return
        }

        // process the active round: orb movement and scoring
        currentRound?.tick()

        // round timer
        ticksRemaining--
        if (ticksRemaining <= 0) {
            prepareNextAltar()
        }

        // update overlay
        updateAllOverlays()
    }

    /* * * * * * * * * * * * * * * * * * * *
     *
     * HELPER FUNCTIONS FOR THE SESSION LOGIC
     *
     * * * * * * * * * * * * * * * * * * * */

    // begins a round, either when everyone arrives at current altar or the grace period timer runs out
    private fun startRound() {
        if (!isWaitingForNextRound) return
        if (playersAtAltar.size < participants.size && forceStartTicks > 0) return

        // If the timer ran out before everyone arrived, force teleport the stragglers
        if (forceStartTicks <= 0 && playersAtAltar.size < participants.size) {
            val destinationIndex = currentAltarIndex + 1
            val destinationLoc = altars[destinationIndex]

            participants.forEach { player ->
                if (!playersAtAltar.contains(player)) {
                    teleport(player, destinationLoc)
                    clearHintIcon(player)
                    playersAtAltar.add(player)
                }
            }
        }

        isWaitingForNextRound = false
        isDelayingStart = true
        delayTicks = spawnDelay

        // advance the index after everyone arrives
        currentAltarIndex++
        updateAllOverlays()
    }

    // checks for any non-clanker players
    private val sessionContainsHumans: Boolean
        get() = participants.any { !it.isArtificial }

    // awards player pess equal to their team's score minus half the other team's score, plus 1. (min qty. 1)
    private fun awardEss() {
        participants.forEach { player ->
            val team = getAttribute(player, ATTR_GOP_TEAM, Team.NONE)

            val ess = if (team == Team.GREEN) {
                (greenScore - (yellowScore / 2) + 1).coerceAtLeast(1)
            } else {
                (yellowScore - (greenScore / 2) + 1).coerceAtLeast(1)
            }

            // award ess. excess should be dropped. no soup for bots
            if (!player.isArtificial) {
                addItemOrDrop(player, Items.PURE_ESSENCE_7936, ess)
            }
        }
    }

    // awards player tokens. 100 per altar won, 10 per altar lost, 25 per altar tied. 200 tokens for the overall winner, or split (100 ea team) if overall was a tie.
    // some message feedback: https://www.youtube.com/watch?v=-V6_B9BsbNs
    private fun awardTokens() {
        var greenTokens = 0
        var yellowTokens = 0
        var greenRoundsWon = 0
        var yellowRoundsWon = 0

        // calculate tokens based on individual altar results
        roundWinners.forEach { winner ->
            when (winner) {
                Team.GREEN -> {
                    greenTokens += 100
                    yellowTokens += 10
                    greenRoundsWon++
                }
                Team.YELLOW -> {
                    yellowTokens += 100
                    greenTokens += 10
                    yellowRoundsWon++
                }
                else -> {
                    greenTokens += 25
                    yellowTokens += 25
                }
            }
        }

        // calculate the overall game winner bonus
        if (greenRoundsWon > yellowRoundsWon) {
            greenTokens += 200
        } else if (yellowRoundsWon > greenRoundsWon) {
            yellowTokens += 200
        } else {
            greenTokens += 100
            yellowTokens += 100
        }

        // add tokens
        participants.forEach { player ->
            val team = getAttribute(player, ATTR_GOP_TEAM, Team.NONE)
            val amount = if (team == Team.GREEN) greenTokens else yellowTokens
            addItemOrDrop(player, Items.RUNECRAFTING_GUILD_TOKEN_13650, amount)
            if (team == Team.GREEN) sendMessage(player, "Wizard Acantha gives you $amount tokens.")
            if (team == Team.YELLOW) sendMessage(player, "Wizard Vief gives you $amount tokens.")
            sendMessage(player, "Wizard Elriss will exchange these for rewards.")
        }
    }

    // updates the interface for all active participants
    fun updateAllOverlays() {
        // determine the state text. other text is set in OrbProjLobby during the lobby time.
        val stateText: String? = when {
            isWaitingForNextRound -> "Waiting for orbs to appear"
            else -> null // null hides the text child
        }

        // determine the team for each player, then update interface
        participants.forEach { player ->
            val hasUsedPortal = playersAtAltar.contains(player)
            val team = getAttribute(player, ATTR_GOP_TEAM, Team.NONE)

            // determine if the portal is currently open or if the player has gone through
            val isPortalOpen = isGameOver || (isWaitingForNextRound && !hasUsedPortal)

            // overlay graphic. the -1 fixes an issue where graphic jumped a round ahead (baka)
            val overlayGraphicIndex = currentAltarIndex - 1

            // update the actual interface
            OrbProjOverlay.updateInterface(
                player = player,
                team = team,
                portalOpen = isPortalOpen,
                greenScore = greenScore,
                yellowScore = yellowScore,
                stateText = stateText,
                currentAltarIndex = overlayGraphicIndex,
                roundWinners = roundWinners
            )

            // update the timer
            when {
                isWaitingForNextRound -> {
                    // show the inter-round timer
                    updateRoundTimer(player, team, forceStartTicks, gracePeriod)
                }

                isDelayingStart -> {
                    // show a frozen timer
                    updateRoundTimer(player, team, 0, 1)
                }

                !isGameOver -> {
                    // show the regular timer
                    updateRoundTimer(player, team, ticksRemaining, roundTime)
                }
            }
        }
    }

    // handle the transfer to the next altar.
    private fun prepareNextAltar() {
        // clear the current round's orbs, and any player-made barriers
        currentRound?.end()
        currentRound = null
        clearBarriers()

        // award the pure ess participation trophy to each player
        awardEss()

        // evaluate and save the winner.
        // note: currentAltarIndex is 1 for Air, 8 for Nature, so subtract 1 for the array index
        if (currentAltarIndex > 0) {
            val roundIndex = currentAltarIndex - 1
            if (greenScore > yellowScore) roundWinners[roundIndex] = Team.GREEN
            else if (yellowScore > greenScore) roundWinners[roundIndex] = Team.YELLOW
            else roundWinners[roundIndex] = Team.NONE

            // reset scores for the next round
            greenScore = 0
            yellowScore = 0
        }

        // if we just finished the nature altar, end the game
        if (currentAltarIndex == altars.size - 1) {
            endGame()
            return
        }

        // otherwise, spawn the portal at the altar we just finished
        spawnPortal()

        // put the game into the waiting state
        isWaitingForNextRound = true
        forceStartTicks = gracePeriod
        playersAtAltar.clear()

        updateAllOverlays()
    }

    // starts a new round
    private fun startNextRound() {
        ticksRemaining = roundTime
        currentRound = OrbProjRound(this).also { it.start() }

        // clear the previous portal if it exists
        activePortal?.clear()

        updateAllOverlays()
    }

    // clears all player-made barriers from the current round
    private fun clearBarriers() {
        participants.forEach { player ->
            removeBarriers(player)
        }
    }

    // the portal spawns at current location, and leads to the next location
    private fun spawnPortal() {
        // clear the previous portal if it exists
        activePortal?.clear()

        // spawn a new portal
        val portal = NPC.create(NPCs.PORTAL_8019, altars[currentAltarIndex])
        portal.init()
        activePortal = portal

        // register hint
        participants.forEach { player ->
            registerHintIcon(player, portal)
        }
    }

    // this is called in the interactionListener for the portal. Portal teleports to the next altar
    fun handlePortal(player: Player) {
        // only allow players who are part of this session to use the portal
        if (!participants.contains(player)) {
            sendMessage(player, "You are not part of this game.")
            return
        }

        clearHintIcon(player)

        // if we are at the nature altar, go back to guild and finish game
        if (currentAltarIndex == altars.size - 1) {
            teleport(player, altars[0], TeleportManager.TeleportType.RC_GUILD)
            sendMessage(player, "You enter the portal.")
            finishPlayer(player)
            return
        }

        // otherwise, the portal leads to the next altar in the sequence
        val destinationIndex = currentAltarIndex + 1
        teleport(player, altars[destinationIndex], TeleportManager.TeleportType.RC_GUILD)
        sendMessage(player, "You enter the portal.")

        // register that this player has made it to the new altar
        if (isWaitingForNextRound) {
            playersAtAltar.add(player)
            updateAllOverlays()
        }
    }

    // stops game processing, clears orbs, awards tokens, and spawns portal back to guild
    private fun endGame() {
        isGameOver = true
        currentRound?.end()
        currentRound = null
        clearBarriers()
        awardTokens()
        spawnPortal()
        updateAllOverlays()
    }

    // handles finishing the game for each player. procs on the portal back to rc guild.
    private fun finishPlayer(player: Player) {
        val team = getAttribute(player, ATTR_GOP_TEAM, Team.NONE)

        // fetch the wins. so fetch.
        var greenRoundsWon = 0
        var yellowRoundsWon = 0
        var tieRounds = 0
        roundWinners.forEach { winner ->
            when (winner) {
                Team.GREEN -> greenRoundsWon++
                Team.YELLOW -> yellowRoundsWon++
                else -> tieRounds++
            }
        }

        // some ending dialogue sources. The HUGE FAILURE one is from the wiki. They are from various eras but the best I can find. I have had to fill in some with my best guess based on tone of voice.
        // https://www.youtube.com/watch?v=lReO-E1XuOs, https://www.youtube.com/watch?v=4eCh2QsZlcY&t=47s, https://www.youtube.com/watch?v=A0l-SWStRDA, https://www.youtube.com/watch?v=t-yK3-TXQHw, https://www.youtube.com/watch?v=8LYS5Cc7PnA
        if (team == Team.GREEN) {
            if (greenRoundsWon == 8) { // big W
                sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "Victory! You saved every single altar from those horrid yellow orbs. If you can keep this up, I'll prove Vief wrong in no time.")
            } else if (greenRoundsWon == 0) { // big L
                sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "You FAILED! $greenRoundsWon altars? That's it? I'm making a note here: HUGE FAILURE.") // Portal meme from wiki trivia.
            } else if (greenRoundsWon == yellowRoundsWon) { // tie
                sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "A stalemate! You and Vief's followers both claimed $greenRoundsWon altars. You'll have to do better next time.")
            } else if (greenRoundsWon > yellowRoundsWon) { // win
                sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "Well done. We control $greenRoundsWon altars, although you did let Vief's followers get $yellowRoundsWon.")
            } else { // else greenRoundsWon < yellowRoundsWon, lose
                sendNPCDialogue(player, NPCs.WIZARD_ACANTHA_8031, "A poor performance. We control $greenRoundsWon altars, and you let Vief's followers get $yellowRoundsWon.")
            }
        } else {
            if (yellowRoundsWon == 8) { // big W
                sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "Perfect! We captured every single altar! At this rate, I'm well on track to be the next head of the guild.")
            } else if (yellowRoundsWon == 0) { // big L
                sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "$yellowRoundsWon altars? I'm making a note here: HUGE FAILURE.")
            } else if (yellowRoundsWon == greenRoundsWon) { // tie
                sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "A stalemate! You and Acantha's followers both claimed $yellowRoundsWon altars. You'll have to do better next time.")
            } else if (yellowRoundsWon > greenRoundsWon) { // win
                sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "Well done. We control $yellowRoundsWon altars, although you did let Acantha's followers get $greenRoundsWon.")
            } else { // else yellowRoundsWon < greenRoundsWon, lose
                sendNPCDialogue(player, NPCs.WIZARD_VIEF_8030, "Better luck next time. We only control $yellowRoundsWon altars, and Acantha's followers got $greenRoundsWon.")
            }
        }

        participants.remove(player)
        removeFromTeamAndTeleportGuild(player)
        clearHintIcon(player)
        // contentAPI doesn't work to close the interface
        player.interfaceManager.close(Component(Components.RCGUILD_OVERLAY_781))

        // if this was the very last player to leave, or if there are only bots left, destroy the session
        if (!sessionContainsHumans) {
            terminateSession()
        }
    }

    // removes a player from session
    fun removePlayer(player: Player) {
        if (participants.remove(player)) {
            removeBarriers(player)
            playersAtAltar.remove(player) // remove from arrival tracking
            removeFromTeamAndTeleportGuild(player) // remove game items
            clearHintIcon(player) // clear hint icon
            player.interfaceManager.close(Component(Components.RCGUILD_OVERLAY_781))

            // apply a penalty timer for abandoning (ignore bots)
            if (!player.isArtificial) {
                val penalty = if (isGameOver || player.isAdmin) {
                    // minimum 1 minute after the last round ended, or 1 min if player is admin
                    100
                } else {
                    // max 10 minutes, and reduces by 1 minute per altar
                    (10 - currentAltarIndex) * 100
                }
                registerTimer(player, OrbProjAbandonTimer(penalty))
            }

            // if we were waiting on this specific player, check if we can start now
            startRound()

            // if there are only bots left, kill session
            if (!sessionContainsHumans) {
                terminateSession()
            }
        }
    }

    // checks if a player has left the game area
    private fun checkOutOfBounds() {
        participants.toList().forEach { player ->
            // if player isn't anywhere inside the GOP regions, remove them
            if (!OrbProjUtils.validRegions.contains(player.location.regionId)) {
                removePlayer(player)
            }
        }
    }

    // destroys a session if the watchdog timer is ever hit. normally this should not happen as each game should get over faster.
    private fun terminateSession() {
        val players = participants.toList()

        // clears all the player shit
        players.forEach { player ->
            removeFromTeamAndTeleportGuild(player)
            clearHintIcon(player)
            player.interfaceManager.close(Component(Components.RCGUILD_OVERLAY_781))
            removeBarriers(player)

            // kill bots
            if (player is AIPlayer) {
                AIPlayer.deregister(player.uid)
            }

            // flag back to the bot script to stop
            OrbProjBot.gameTerminated = true
        }

        // clears all the game shit
        participants.clear()
        playersAtAltar.clear()
        currentRound?.end()
        currentRound = null
        activePortal?.clear()
        active = null
    }
}